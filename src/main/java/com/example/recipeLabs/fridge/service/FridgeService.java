package com.example.recipeLabs.fridge.service;

import com.example.recipeLabs.fridge.dto.FridgeItemRequestDTO;
import com.example.recipeLabs.fridge.dto.FridgeItemResponseDTO;
import com.example.recipeLabs.fridge.entity.FridgeItem;
import com.example.recipeLabs.fridge.repository.FridgeItemRepository;
import com.example.recipeLabs.global.security.UserDetailsImpl;
import com.example.recipeLabs.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FridgeService {

    private final FridgeItemRepository fridgeItemRepository;

    @Transactional
    public ResponseEntity<String> createItem(FridgeItemRequestDTO requestDTO, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        FridgeItem fridgeItem = new FridgeItem(requestDTO, user);
        fridgeItemRepository.save(fridgeItem);
        return ResponseEntity.status(HttpStatus.OK).body("냉장고 아이템이 추가되었습니다.");
    }

    @Transactional
    public ResponseEntity<Page<FridgeItemResponseDTO>> findItems(int page, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Pageable pageable = PageRequest.of(page, 100);
        Page<FridgeItem> recipePage = fridgeItemRepository.findAllByUser(user, pageable);
        return ResponseEntity.ok(recipePage.map(FridgeItemResponseDTO::new));
    }

    @Transactional
    public ResponseEntity<String> updateItems(Long itemId,FridgeItemRequestDTO fridgeItemRequestDTO, UserDetailsImpl userDetails) {
        FridgeItem fridgeItem = fridgeItemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("아이템를 찾을 수 없습니다."));
        if (!fridgeItem.getUser().getId().equals(userDetails.getUser().getId())) throw new IllegalArgumentException("사용자의 아이템이 아닙니다.");
        // 데이터 수정
        fridgeItem.update(fridgeItemRequestDTO);
        fridgeItemRepository.save(fridgeItem);
        return ResponseEntity.ok("냉장고 아이템이 수정되었습니다.");
    }

    @Transactional
    public ResponseEntity<String> deleteItem(Long itemId, UserDetailsImpl userDetails) {
        FridgeItem fridgeItem = fridgeItemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("아이템를 찾을 수 없습니다."));
        if (!fridgeItem.getUser().getId().equals(userDetails.getUser().getId())) throw new IllegalArgumentException("사용자의 아이템이 아닙니다.");
        // 데이터 삭제
        fridgeItemRepository.delete(fridgeItem);
        return ResponseEntity.ok("냉장고 아이템이 삭제되었습니다.");
    }
}
