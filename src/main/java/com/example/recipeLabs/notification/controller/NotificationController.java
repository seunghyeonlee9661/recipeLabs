package com.example.recipeLabs.notification.controller;

import com.example.recipeLabs.global.security.UserDetailsImpl;
import com.example.recipeLabs.notification.dto.NotificationResposeDTO;
import com.example.recipeLabs.notification.service.NotificationService;
import com.example.recipeLabs.recipe.dto.RecipeSimpleResponseDTO;
import com.example.recipeLabs.user.dto.*;
import com.example.recipeLabs.user.repository.UserRepository;
import com.example.recipeLabs.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "사용자 API", description = "사용자 관련 API")
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("")
    @Operation(summary = "사용자 알림 전체", description = "사용자가 받은 모든 알림 목록을 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "알림 목록 반환"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    public ResponseEntity<Page<NotificationResposeDTO>> findNotifications(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(value = "page", defaultValue = "0") @Parameter(description = "조회할 페이지 번호 (기본값: 0)") int page) {
        return notificationService.findNotifications(userDetails,page);
    }

    @PutMapping("/{notificationId}")
    @Operation(summary = "알림 읽음 설정", description = "알림에 대해 읽음으로 상태를 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "알림 변경 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    public ResponseEntity<String> setNotificationsRead(
            @PathVariable @Parameter(description = "레시피의 단계 ID") Long notificationId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return notificationService.setNotificationsRead(userDetails,notificationId);
    }

    @GetMapping("/unread")
    @Operation(summary = "사용자 읽지 않은 알림", description = "사용자가 읽지 않은 알림 목록을 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "알림 목록 반환"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    public ResponseEntity<List<NotificationResposeDTO>> findNotificationsUnread(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return notificationService.findNotificationsUnread(userDetails);
    }
}
