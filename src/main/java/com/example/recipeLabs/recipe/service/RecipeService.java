package com.example.recipeLabs.recipe.service;

import com.example.recipeLabs.global.security.UserDetailsImpl;
import com.example.recipeLabs.global.service.ImageService;
import com.example.recipeLabs.global.service.ImageTransformService;
import com.example.recipeLabs.recipe.dto.RecipeResponseDTO;
import com.example.recipeLabs.recipe.dto.RecipeSimpleResponseDTO;
import com.example.recipeLabs.recipe.dto.RecipeUpdateRequestDTO;
import com.example.recipeLabs.recipe.entity.Recipe;
import com.example.recipeLabs.recipe.repository.RecipeLikeRepository;
import com.example.recipeLabs.recipe.repository.RecipeRepository;
import com.example.recipeLabs.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeLikeRepository recipeLikeRepository;
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

    /* 레시피 id로 조회 */
    @Transactional
    public ResponseEntity<Page<RecipeSimpleResponseDTO>> findRecipePage(int page) {
        Pageable pageable = PageRequest.of(page, 100);
        Page<Recipe> recipePage = recipeRepository.findAll(pageable);
        return ResponseEntity.ok(recipePage.map(RecipeSimpleResponseDTO::new));
    }

    /* 레시피 id로 조회 */
    @Transactional
    public ResponseEntity<RecipeResponseDTO> findRecipe(Long id, UserDetailsImpl userDetails) {
        // 레시피 정보 가져오기
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다."));

        // 사용자 정보와 레시피 좋아요 상태 확인
        boolean isLiked = Optional.ofNullable(userDetails)
                .map(UserDetailsImpl::getUser)
                .map(user -> recipeLikeRepository.existsByUserAndRecipe(user, recipe))
                .orElse(false);

        return ResponseEntity.ok(new RecipeResponseDTO(recipe, isLiked));
    }

    /* 레시피 삭제 */
    @Transactional
    public ResponseEntity<String> deleteRecipe(Long id, UserDetailsImpl userDetails) {
        // 레시피 정보 가져오기
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다."));
        // 작성자 확인
        if (!recipe.getUser().getId().equals(userDetails.getUser().getId())) return ResponseEntity.badRequest().body("작성자가 아니면 편집할 수 없습니다.");
        // 기존 이미지 제거
        if(recipe.getImage() != null) imageService.deleteFileByUrl(recipe.getImage());
        // 데이터 삭제
        recipeRepository.delete(recipe);
        return ResponseEntity.ok("레시피가 삭제되었습니다.");
    }

    /* 레시피 수정 - 이미지 */
    @Transactional
    public ResponseEntity<String> updateRecipeImage(Long id, MultipartFile image,UserDetailsImpl userDetails) throws IOException {
        // 레시피 확인
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다."));
        // 작성자 확인
        if (!recipe.getUser().getId().equals(userDetails.getUser().getId())) return ResponseEntity.badRequest().body("작성자가 아니면 편집할 수 없습니다.");
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
    public ResponseEntity<String> updateRecipeContent(Long id, RecipeUpdateRequestDTO requestDTO, UserDetailsImpl userDetails){
        // 레시피 확인
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다."));
        // 작성자 확인
        if (!recipe.getUser().getId().equals(userDetails.getUser().getId())) return ResponseEntity.badRequest().body("작성자가 아니면 편집할 수 없습니다.");
        // 업데이트
        recipe.updateContent(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(recipe.getId().toString());
    }
}
