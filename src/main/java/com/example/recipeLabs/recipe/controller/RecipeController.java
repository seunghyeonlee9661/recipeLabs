package com.example.recipeLabs.recipe.controller;

import com.example.recipeLabs.global.security.UserDetailsImpl;
import com.example.recipeLabs.recipe.dto.RecipeResponseDTO;
import com.example.recipeLabs.recipe.dto.RecipeSimpleResponseDTO;
import com.example.recipeLabs.recipe.dto.RecipeStepCreateRequestDTO;
import com.example.recipeLabs.recipe.dto.RecipeUpdateRequestDTO;
import com.example.recipeLabs.recipe.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "레시피 작성", description = "새로운 레시피를 작성하고 레시피 ID를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "레시피 작성 성공, 생성된 레시피 ID 반환", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<String> createRecipe(
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return recipeService.createRecipe(userDetails);
    }

    /* 레시피 목록 요청 */
    @GetMapping("")
    @Operation(summary = "레시피 목록 요청", description = "레시피 목록을 페이지 단위로 요청합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "레시피 목록 조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Page<RecipeSimpleResponseDTO>> findRecipePage(
            @RequestParam(value = "page", defaultValue = "0") @Parameter(description = "조회할 페이지 번호 (기본값: 0)") int page){
        return recipeService.findRecipePage(page);
    }

    /* TODO : 레시피 검색에 대한 기능 구현 */
    /* 레시피 정보 요청 */
    @GetMapping("/{recipeId}")
    @Operation(summary = "레시피 정보 요청", description = "레시피 ID로 해당 레시피의 상세 정보를 요청합니다. 또한, 사용자의 좋아요 및 즐겨찾기 상태도 포함됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "레시피 정보 조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "레시피를 찾을 수 없음", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<RecipeResponseDTO> findRecipeDetail(
            @PathVariable @Parameter(description = "레시피의 ID") Long recipeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return recipeService.findRecipe(recipeId,userDetails);
    }

    /* 레시피 사진 업데이트 */
    @PutMapping("/{recipeId}/image")
    @Operation(summary = "레시피 사진 업데이트", description = "레시피 ID에 해당하는 레시피의 이미지를 업데이트합니다. 새 이미지는 WebP 형식으로 변환하여 저장됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "레시피 이미지 업데이트 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효하지 않은 레시피 ID", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "레시피를 찾을 수 없음", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<String> updateRecipeImage(
            @PathVariable @Parameter(description = "레시피의 ID") Long recipeId,
            @RequestPart(value = "image", required = true) @Parameter(description = "레시피 단계 이미지 파일 (필수 항목)") MultipartFile image,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return recipeService.updateRecipeImage(recipeId,image,userDetails);
    }

    /* 레시피 내용 업데이트 */
    @PutMapping("/{recipeId}/content")
    @Operation(summary = "레시피 내용 업데이트", description = "레시피 ID에 해당하는 레시피의 내용을 업데이트합니다. 내용은 RecipeUpdateRequestDTO를 통해 전달됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "레시피 내용 업데이트 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효하지 않은 레시피 ID", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "레시피를 찾을 수 없음", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<String> updateRecipeContent(
            @PathVariable @Parameter(description = "레시피의 ID") Long recipeId,
            @RequestBody RecipeUpdateRequestDTO recipeUpdateRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return recipeService.updateRecipeContent(recipeId, recipeUpdateRequestDTO, userDetails);
    }

    /* 레시피 삭제 */
    @DeleteMapping("/{recipeId}")
    @Operation(summary = "레시피 삭제", description = "레시피 ID에 해당하는 레시피를 삭제합니다. 삭제 시 레시피의 이미지도 함께 제거됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "레시피 삭제 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효하지 않은 레시피 ID", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "레시피를 찾을 수 없음", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<String> deleteRecipe(
            @PathVariable @Parameter(description = "레시피의 ID") Long recipeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return recipeService.deleteRecipe(recipeId,userDetails);
    }

    /* 레시피 작성 완료 */
    @PutMapping("/{recipeId}")
    @Operation(summary = "레시피 작성 완료 설정", description = "레시피 작성이 완료되었음을 설정하는 API입니다. 레시피 상태를 '완료'로 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "레시피 작성 완료 설정 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효하지 않은 레시피 ID", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "레시피를 찾을 수 없음", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<String> completeRecipe(
            @PathVariable @Parameter(description = "레시피의 ID") Long recipeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return recipeService.completeRecipe(recipeId,userDetails);
    }

    /* 레시피 좋아요 설정 */
    @PutMapping("/{recipeId}/like")
    @Operation(summary = "레시피 좋아요 설정", description = "레시피에 대해 좋아요를 설정하거나 해제하는 API입니다. 좋아요가 있으면 삭제되고, 없으면 추가됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요 설정 변경 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효하지 않은 레시피 ID", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "레시피를 찾을 수 없음", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<String> setLike(
            @PathVariable @Parameter(description = "레시피의 ID") Long recipeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return recipeService.setLike(recipeId,userDetails);
    }

    /* 레시피 즐겨찾기 설정 */
    @PutMapping("/{recipeId}/favorite")
    @Operation(summary = "레시피 즐겨찾기 설정", description = "레시피에 대해 즐겨찾기를 설정하거나 해제하는 API입니다. 즐겨찾기가 있으면 삭제되고, 없으면 추가됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "즐겨찾기 설정 변경 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효하지 않은 레시피 ID", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "레시피를 찾을 수 없음", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<String> setFavorite(
            @PathVariable @Parameter(description = "레시피의 ID") Long recipeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return recipeService.setFavorite(recipeId,userDetails);
    }

    /*_____________________레시피 단계__________________*/

    /* 레시피 단계 추가*/
    @PostMapping("/{recipeId}/step")
    @Operation(summary = "레시피 단계 추가", description = "레시피에 새로운 단계를 추가하는 API입니다. 단계 순서는 기존 단계 수에 따라 자동으로 설정됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "새로운 레시피 단계 추가 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효하지 않은 레시피 ID", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "레시피를 찾을 수 없음", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<String> createRecipeStep(
            @PathVariable @Parameter(description = "레시피의 ID") Long recipeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return recipeService.createRecipeStep(recipeId, userDetails);
    }

    /* 레시피 단계 내용 수정*/
    @PutMapping("/step/{recipeStepId}/content")
    @Operation(summary = "레시피 단계 내용 수정", description = "레시피 단계의 내용을 수정하는 API입니다. 해당 단계의 내용은 요청 본문으로 전달된 데이터로 업데이트됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "레시피 단계 내용 수정 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효하지 않은 레시피 단계 ID", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "레시피 단계를 찾을 수 없음", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<String> createRecipeStep(
            @PathVariable @Parameter(description = "레시피의 단계 ID") Long recipeStepId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody RecipeStepCreateRequestDTO recipeStepCreateRequestDTO){
        return recipeService.updateRecipeStepContent(recipeStepId,recipeStepCreateRequestDTO,userDetails);
    }

    /* 레시피 단계 이미지 수정*/
    @PutMapping("/step/{recipeStepId}/image")
    @Operation(summary = "레시피 단계 이미지 수정", description = "레시피 단계의 이미지를 수정하는 API입니다. 요청된 이미지는 WebP 형식으로 변환되어 저장됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "레시피 단계 이미지 수정 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효하지 않은 레시피 단계 ID", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "레시피 단계를 찾을 수 없음", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<String> createRecipeStep(
            @PathVariable @Parameter(description = "레시피의 단계 ID") Long recipeStepId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart(value = "image", required = true) MultipartFile image) throws IOException {
        return recipeService.updateRecipeStepImage(recipeStepId, image, userDetails);
    }

    /* 레시피 단계 삭제 */
    @DeleteMapping("/step/{recipeStepId}")
    @Operation(summary = "레시피 단계 삭제", description = "레시피 단계와 그에 연결된 이미지를 삭제하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "레시피 단계 삭제 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효하지 않은 레시피 단계 ID", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "레시피 단계를 찾을 수 없음", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<String> deleteRecipeStep(
            @PathVariable @Parameter(description = "레시피의 단계 ID") Long recipeStepId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return recipeService.deleteRecipeStep(recipeStepId, userDetails);
    }

    /* 레시피 단계 순서 변경 */
    @PutMapping("/{recipeId}/steps/{currentOrder}/order")
    @Operation(summary = "레시피 단계 순서 변경", description = "레시피 단계의 순서를 변경하는 API입니다. 현재 순서와 새로운 순서를 입력하여 레시피 단계의 순서를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "레시피 단계 순서 변경 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청 (잘못된 순서 입력)", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "레시피 또는 레시피 단계가 존재하지 않음", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<String> updateRecipeStepOrder(
            @PathVariable @Parameter(description = "레시피의 ID") Long recipeId,
            @PathVariable @Parameter(description = "현재 레시피 단계의 순서 (변경 전)") int currentOrder,
            @RequestParam @Parameter(description = "새로운 순서 값") int newOrder,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return recipeService.updateRecipeStepOrder(recipeId, currentOrder, newOrder,userDetails);
    }
}