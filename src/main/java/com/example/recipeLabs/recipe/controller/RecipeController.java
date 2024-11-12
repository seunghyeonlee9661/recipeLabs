package com.example.recipeLabs.recipe.controller;

import com.example.recipeLabs.global.security.UserDetailsImpl;
import com.example.recipeLabs.recipe.dto.RecipeResponseDTO;
import com.example.recipeLabs.recipe.dto.RecipeSimpleResponseDTO;
import com.example.recipeLabs.recipe.dto.RecipeStepCreateRequestDTO;
import com.example.recipeLabs.recipe.dto.RecipeUpdateRequestDTO;
import com.example.recipeLabs.recipe.service.RecipeService;
import com.example.recipeLabs.user.dto.UserResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@Tag(name = "레시피 API", description = "레시피 관련 API")
@RequestMapping("/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    /* 레시피 작성 */
    @PostMapping("")
    public ResponseEntity<String> createRecipe(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return recipeService.createRecipe(userDetails);
    }

    /* 레시피 목록 요청 */
    @GetMapping("")
    public ResponseEntity<Page<RecipeSimpleResponseDTO>> findRecipePage(
            @RequestParam(value = "page", defaultValue = "0") int page){
        return recipeService.findRecipePage(page);
    }

    /* 레시피 정보 요청 */
    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeResponseDTO> findRecipeDetail(
            @PathVariable Long recipeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return recipeService.findRecipe(recipeId,userDetails);
    }

    /* 레시피 사진 업데이트 */
    @PutMapping("/{recipeId}/image")
    public ResponseEntity<String> updateRecipeImage(
            @PathVariable Long recipeId,
            @RequestPart(value = "image", required = true) MultipartFile image,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return recipeService.updateRecipeImage(recipeId,image,userDetails);
    }

    /* 레시피 내용 업데이트 */
    @PutMapping("/{recipeId}/content")
    public ResponseEntity<String> updateRecipeContent(
            @PathVariable Long recipeId,
            @RequestBody RecipeUpdateRequestDTO recipeUpdateRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return recipeService.updateRecipeContent(recipeId, recipeUpdateRequestDTO, userDetails);
    }

    /* 레시피 삭제 */
    @DeleteMapping("/{recipeId}")
    public ResponseEntity<String> deleteRecipe(
            @PathVariable Long recipeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return recipeService.deleteRecipe(recipeId,userDetails);
    }

    /*_____________________레시피 단계__________________*/
    /* 레시피 단계 추가*/
    @PostMapping("/{recipeId}/step")
    public ResponseEntity<String> createRecipeStep(
            @PathVariable Long recipeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return recipeService.createRecipeStep(recipeId, userDetails);
    }

    /* 레시피 단계 내용 수정*/
    @PutMapping("/step/{recipeStepId}/content")
    public ResponseEntity<String> createRecipeStep(
            @PathVariable Long recipeStepId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody RecipeStepCreateRequestDTO recipeStepCreateRequestDTO){
        return recipeService.updateRecipeStepContent(recipeStepId,recipeStepCreateRequestDTO,userDetails);
    }

    /* 레시피 단계 이미지 수정*/
    @PutMapping("/step/{recipeStepId}/image")
    public ResponseEntity<String> createRecipeStep(
            @PathVariable Long recipeStepId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart(value = "image", required = true) MultipartFile image) throws IOException {
        return recipeService.updateRecipeStepImage(recipeStepId, image, userDetails);
    }

    /* 레시피 단계 삭제 */
    @DeleteMapping("/step/{recipeStepId}")
    public ResponseEntity<String> deleteRecipeStep(
            @PathVariable Long recipeStepId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return recipeService.deleteRecipeStep(recipeStepId, userDetails);
    }

    /* 레시피 단계 순서 변경 */
    @PutMapping("/{recipeId}/steps/{currentOrder}/order")
    public ResponseEntity<String> updateRecipeStepOrder(
            @PathVariable Long recipeId,
            @PathVariable int currentOrder,
            @RequestParam int newOrder,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return recipeService.updateRecipeStepOrder(recipeId, currentOrder, newOrder,userDetails);
    }
}
