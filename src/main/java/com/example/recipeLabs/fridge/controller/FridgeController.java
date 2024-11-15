package com.example.recipeLabs.fridge.controller;

import com.example.recipeLabs.fridge.dto.FridgeItemRequestDTO;
import com.example.recipeLabs.fridge.dto.FridgeItemResponseDTO;
import com.example.recipeLabs.fridge.service.FridgeService;
import com.example.recipeLabs.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "냉장고 API", description = "냉장고 관련 API")
@RequestMapping("/fridges")
@RequiredArgsConstructor
public class FridgeController {
    private final FridgeService fridgeService;

    // 냉장고 아이템 목록
    @GetMapping("")
    public ResponseEntity<Page<FridgeItemResponseDTO>> findItems(
            @RequestParam(value = "page", defaultValue = "0") @Parameter(description = "조회할 페이지 번호 (기본값: 0)") int page,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return fridgeService.findItems(page,userDetails);
    }

    // 냉장고 아이템 추가
    @PostMapping("")
    public ResponseEntity<String> createItem(
            @RequestBody FridgeItemRequestDTO fridgeItemRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return fridgeService.createItem(fridgeItemRequestDTO, userDetails);
    }

    /* TODO 검색 필터링용! */
    // 냉장고 아이템 목록
    @PutMapping("/{itemId}")
    public ResponseEntity<String> updateItems(
            @PathVariable @Parameter(description = "아이템 ID") Long itemId,
            @RequestBody FridgeItemRequestDTO fridgeItemRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return fridgeService.updateItems(itemId,fridgeItemRequestDTO,userDetails);
    }

    // 냉장고 아이템 제거
    @DeleteMapping("/{itemId}")
    public ResponseEntity<String> deleteItem(
            @PathVariable @Parameter(description = "아이템 ID") Long itemId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return fridgeService.deleteItem(itemId, userDetails);
    }
}
