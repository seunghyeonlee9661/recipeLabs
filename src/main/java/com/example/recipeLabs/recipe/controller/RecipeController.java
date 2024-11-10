package com.example.recipeLabs.recipe.controller;

import com.example.recipeLabs.global.security.UserDetailsImpl;
import com.example.recipeLabs.recipe.dto.RecipeResponseDTO;
import com.example.recipeLabs.recipe.dto.RecipeSimpleResponseDTO;
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
    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponseDTO> findRecipeDetail(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return recipeService.findRecipe(id,userDetails);
    }

    /* 레시피 사진 업데이트 */
    @PutMapping("/{id}/image")
    public ResponseEntity<String> updateRecipeImage(
            @PathVariable Long id,
            @RequestPart(value = "images", required = true) MultipartFile image,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return recipeService.updateRecipeImage(id,image,userDetails);
    }

    /* 레시피 내용 업데이트 */
    @PutMapping("/{id}/content")
    public ResponseEntity<String> updateRecipeContent(
            @PathVariable Long id,
            @RequestBody RecipeUpdateRequestDTO recipeUpdateRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return recipeService.updateRecipeContent(id, recipeUpdateRequestDTO, userDetails);
    }

    /* 레시피 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecipe(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return recipeService.deleteRecipe(id,userDetails);
    }

    /*__________________________________________*/



}
