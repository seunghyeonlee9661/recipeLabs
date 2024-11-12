package com.example.recipeLabs.recipe.service;

import com.example.recipeLabs.global.security.UserDetailsImpl;
import com.example.recipeLabs.global.service.ImageService;
import com.example.recipeLabs.global.service.ImageTransformService;
import com.example.recipeLabs.recipe.dto.*;
import com.example.recipeLabs.recipe.entity.Recipe;
import com.example.recipeLabs.recipe.entity.RecipeStep;
import com.example.recipeLabs.recipe.repository.RecipeLikeRepository;
import com.example.recipeLabs.recipe.repository.RecipeRepository;
import com.example.recipeLabs.recipe.repository.RecipeStepRepository;
import com.example.recipeLabs.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeLikeRepository recipeLikeRepository;
    private final RecipeStepRepository recipeStepRepository;
    private final ImageService imageService;
    private final ImageTransformService imageTransformService;

    /* 레시피 작성 */
    @Transactional
    public ResponseEntity<String> createRecipe(UserDetailsImpl userDetails) {
        // user 정보 가져오기 (id)
        User user = userDetails.getUser();
        Recipe recipe = recipeRepository.save(new Recipe(user));
        return ResponseEntity.status(HttpStatus.CREATED).body(recipe.getId().toString());
    }

    /* 레시피 목록 조회 */
    @Transactional
    public ResponseEntity<Page<RecipeSimpleResponseDTO>> findRecipePage(int page) {
        Pageable pageable = PageRequest.of(page, 100);
        Page<Recipe> recipePage = recipeRepository.findAll(pageable);
        return ResponseEntity.ok(recipePage.map(RecipeSimpleResponseDTO::new));
    }

    /* 레시피 id로 조회 */
    @Transactional
    public ResponseEntity<RecipeResponseDTO> findRecipe(Long recipeId, UserDetailsImpl userDetails) {
        // 레시피 정보 가져오기
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다."));
        // 사용자 정보와 레시피 좋아요 상태 확인
        boolean isLiked = Optional.ofNullable(userDetails)
                .map(UserDetailsImpl::getUser)
                .map(user -> recipeLikeRepository.existsByUserAndRecipe(user, recipe))
                .orElse(false);

        return ResponseEntity.ok(new RecipeResponseDTO(recipe, isLiked));
    }

    /* 레시피 수정 - 이미지 */
    @Transactional
    public ResponseEntity<String> updateRecipeImage(Long recipeId, MultipartFile image,UserDetailsImpl userDetails) throws IOException {
        // 레시피 확인
        Recipe recipe = validateRecipeOwner(recipeId,userDetails);
        // 기존 이미지 제거
        if(recipe.getImage() != null) imageService.deleteFileByUrl(recipe.getImage());
        // 새로운 이미지 webp 변환
        File webPFile = imageTransformService.convertToWebP(image);
        // 이미지 저장 후 url 반환
        String imageUrl = imageService.uploadFile(webPFile);
        // 데이터 변환
        recipe.updateImage(imageUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(recipe.getId().toString());
    }

    /* 레시피 수정 - 내용 */
    @Transactional
    public ResponseEntity<String> updateRecipeContent(Long recipeId, RecipeUpdateRequestDTO requestDTO, UserDetailsImpl userDetails){
        // 레시피 확인
        Recipe recipe = validateRecipeOwner(recipeId,userDetails);
        // 업데이트
        recipe.updateContent(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(recipe.getId().toString());
    }

    /* 레시피 삭제 */
    @Transactional
    public ResponseEntity<String> deleteRecipe(Long recipeId, UserDetailsImpl userDetails) {
        // 레시피 확인
        Recipe recipe = validateRecipeOwner(recipeId,userDetails);
        // 기존 이미지 제거
        if(recipe.getImage() != null) imageService.deleteFileByUrl(recipe.getImage());
        // 데이터 삭제
        recipeRepository.delete(recipe);
        return ResponseEntity.ok("레시피가 삭제되었습니다.");
    }

    /* 레시피 작성 완료 설정 */
    @Transactional
    public ResponseEntity<String> completeRecipe(Long recipeId, UserDetailsImpl userDetails){
        // 레시피 확인
        Recipe recipe = validateRecipeOwner(recipeId,userDetails);
        // 업데이트
        recipe.setIsComplete(true);
        return ResponseEntity.status(HttpStatus.CREATED).body(recipe.getId().toString());
    }

    /*_________________레시피 단계 기능___________________________*/

    /* 레시피 단계 추가 */
    @Transactional
    public ResponseEntity<String> createRecipeStep(Long recipeId, UserDetailsImpl userDetails) {
        // 레시피 확인
        Recipe recipe = validateRecipeOwner(recipeId,userDetails);
        // 레시피 현 단계 개수 찾기
        int stepOrder = recipe.getRecipeSteps().size();
        // 새로운 스탭 추가
        RecipeStep recipeStep = new RecipeStep(recipe,stepOrder);
        recipeStepRepository.save(recipeStep);
        return ResponseEntity.status(HttpStatus.CREATED).body("새로운 레시피 단계가 추가되었습니다.");
    }

    /* 레시피 단계 수정 - 내용 */
    @Transactional
    public ResponseEntity<String> updateRecipeStepContent(Long recipeStepId, RecipeStepCreateRequestDTO requestDTO, UserDetailsImpl userDetails){
        // 레시피 단계 확인
        RecipeStep recipeStep = validateRecipeStepOwner(recipeStepId,userDetails);
        // 업데이트
        recipeStep.updateContent(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("레시피 단계 내용 수정");
    }

    /* 레시피 단계 수정 - 이미지 */
    @Transactional
    public ResponseEntity<String> updateRecipeStepImage(Long recipeStepId, MultipartFile image,UserDetailsImpl userDetails) throws IOException {
        // 레시피 단계 확인
        RecipeStep recipeStep = validateRecipeStepOwner(recipeStepId,userDetails);
        // 기존 이미지 제거
        if(recipeStep.getImage() != null) imageService.deleteFileByUrl(recipeStep.getImage());
        // 새로운 이미지 webp 변환
        File webPFile = imageTransformService.convertToWebP(image);
        // 이미지 저장 후 url 반환
        String imageUrl = imageService.uploadFile(webPFile);
        // 데이터 이미지 url 수정
        recipeStep.updateImage(imageUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body("레시피 단계 이미지 수정");
    }

    /* 레시피 단계 삭제 */
    @Transactional
    public ResponseEntity<String> deleteRecipeStep(Long recipeStepId, UserDetailsImpl userDetails) {
        // 레시피 단계 확인
        RecipeStep recipeStep = validateRecipeStepOwner(recipeStepId,userDetails);
        // 기존 이미지 제거
        if(recipeStep.getImage() != null) imageService.deleteFileByUrl(recipeStep.getImage());
        // 데이터 삭제
        recipeStepRepository.delete(recipeStep);
        return ResponseEntity.ok("레시피 단계가 삭제되었습니다.");
    }

    /* 레시피 단계 순서 변경 */
    @Transactional
    public ResponseEntity<String> updateRecipeStepOrder(Long recipeId, int currentOrder, int newOrder,UserDetailsImpl userDetails) {
        // 레시피 확인
        Recipe recipe = validateRecipeOwner(recipeId,userDetails);
        // 레시피 단계 확인
        RecipeStep recipeStep = recipeStepRepository.findByRecipeAndStepOrder(recipe,currentOrder).orElseThrow(() -> new IllegalArgumentException("레시피 단계를 찾을 수 없습니다."));
        // 레시피 단계 데이터
        List<RecipeStep> steps = recipe.getRecipeSteps();
        // 유효한 순서인지 확인
        if (currentOrder < 0 || newOrder < 0 || currentOrder >= steps.size() || newOrder >= steps.size()) return ResponseEntity.badRequest().body("유효하지 않은 순서입니다.");
        // Recipe 엔티티의 순서 변경 메서드 호출
        recipe.updateStepOrder(currentOrder, newOrder);
        // 타겟 단계의 순서를 새로운 순서로 설정
        recipeStep.setStepOrder(newOrder);
        // 변경된 단계를 저장
        recipeRepository.save(recipe);
        return ResponseEntity.ok("단계 순서가 업데이트되었습니다.");
    }

    // 레시피 작성자 검증 메서드
    private Recipe validateRecipeOwner(Long recipeId, UserDetailsImpl userDetails) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다."));
        if (!recipe.getUser().getId().equals(userDetails.getUser().getId())) throw new IllegalArgumentException("레시피 작성자가 아니면 편집할 수 없습니다.");
        return recipe;
    }

    // 레시피 단계 검증 메서드
    private RecipeStep validateRecipeStepOwner(Long recipeStepId, UserDetailsImpl userDetails) {
        RecipeStep recipeStep = recipeStepRepository.findById(recipeStepId).orElseThrow(() -> new IllegalArgumentException("레시피 단계를 찾을 수 없습니다."));
        if (!recipeStep.getRecipe().getUser().getId().equals(userDetails.getUser().getId())) throw new IllegalArgumentException("레시피 작성자가 아니면 편집할 수 없습니다.");
        return recipeStep;
    }
}
