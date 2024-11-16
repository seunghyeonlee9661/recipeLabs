package com.example.recipeLabs.user.controller;

import com.example.recipeLabs.recipe.dto.RecipeSimpleResponseDTO;
import com.example.recipeLabs.user.dto.*;
import com.example.recipeLabs.global.security.UserDetailsImpl;
import com.example.recipeLabs.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@Tag(name = "사용자 API", description = "사용자 관련 API")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자 로그인 요청, 로그인 후 JWT 토큰을 쿠키에 저장")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    public ResponseEntity<String> login(@RequestBody UserLoginRequestDTO request) {
        // 실제 로그인 처리는 시큐리티 필터에서 처리
        return ResponseEntity.ok("로그인 요청이 성공적으로 처리되었습니다.");
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "사용자 로그아웃 요청, JWT 토큰을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    public ResponseEntity<String> logout() {
        // 실제 로그아웃 처리는 시큐리티에서 진행
        return ResponseEntity.ok("로그아웃 요청이 성공적으로 처리되었습니다.");
    }

    // 사용자 회원가입 - 이메일 발송
    @PostMapping("")
    @Operation(summary = "회원가입", description = "새로운 사용자를 생성하고 이메일 인증을 위해 메일을 전송합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 완료 - 이메일 인증 필요", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "중복된 이메일이거나 비밀번호 확인 불일치", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "메일 전송 실패 - 계정이 삭제되었습니다", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<String> createUser(
            @Valid @RequestBody UserCreateRequestDTO requestDTO) {
        return userService.createUser(requestDTO);
    }

    // 사용자 회원가입 - 이메일 링크 승인
    @GetMapping("/verify")
    @Operation(summary = "회원가입 - 이메일 인증", description = "이메일을 통해 발송된 코드를 입력받아 사용자의 계정을 활성화합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 인증 완료",content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 사용자",content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "200", description = "이미 완료된 이메일 인증",content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "201", description = "올바르지 않은 인증 코드",content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<String> verifyUser(
            @RequestParam @Parameter(description = "인증할 사용자 ID") Long userId,
            @RequestParam @Parameter(description = "사용자가 받은 인증 코드") String code) {
        return userService.verifyUser(userId, code);
    }

    // 사용자 탈퇴
    @DeleteMapping("")
    @Operation(summary = "회원 탈퇴", description = "인증된 사용자를 삭제하고 관련 쿠키 및 데이터를 제거합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 탈퇴 완료", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패 또는 유효하지 않은 인증 정보", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인한 탈퇴 실패", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<String>  removeUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            HttpServletResponse res) throws IOException {
        return userService.removeUser(userDetails,res);
    }

    // 사용자 정보 요청
    @GetMapping("")
    @Operation(summary = "사용자 정보 조회", description = "인증된 사용자의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공", content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패 또는 유효하지 않은 인증 정보", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<UserResponseDTO> findUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(new UserResponseDTO(userDetails.getUser()));
    }

    // 사용자 정보 수정
    @PutMapping("/info")
    @Operation(summary = "사용자 정보 수정", description = "인증된 사용자의 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 정보가 변경되었습니다.", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패 또는 유효하지 않은 인증 정보", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인한 수정 실패", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<String>  updateUserInfo(
            @RequestBody UserUpdateRequestDTO requestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return userService.updateUserInfo(requestDTO,userDetails);
    }

    // 사용자 이미지 수정
    @PutMapping("/image")
    @Operation(summary = "사용자 이미지 수정", description = "인증된 사용자의 프로필 이미지를 수정합니다. 기존 이미지는 삭제되고 새 이미지는 WebP 형식으로 변환하여 저장됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "사용자 이미지 수정 완료", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 이미지 파일 형식", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패 또는 유효하지 않은 인증 정보", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인한 이미지 수정 실패", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<String>  updateUserImage(
            @RequestPart(value = "image", required = true) @Parameter(description = "레시피 단계 이미지 파일 (필수 항목)") MultipartFile image,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return userService.updateUserImage(image,userDetails);
    }

    // 사용자 비밀번호 수정
    @PutMapping("/password")
    @Operation(summary = "사용자 비밀번호 수정", description = "사용자의 비밀번호를 수정합니다. 기존 비밀번호와 새로운 비밀번호를 입력받습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 수정 완료", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "비밀번호 확인 불일치 또는 잘못된 요청", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패 또는 유효하지 않은 인증 정보", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인한 비밀번호 수정 실패", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<String>  sendUserPasswordUpdate(
            @RequestBody @Valid UserPasswordUpdateRequestDTO requestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.updateUserPassword(requestDTO,userDetails);
    }

    // 사용자 비밀번호 초기화 - 이메일 발송
    @PostMapping("/reset")
    @Operation(summary = "사용자 비밀번호 초기화 이메일 발송", description = "사용자에게 비밀번호 초기화 이메일을 발송합니다. 이메일에 포함된 링크를 통해 비밀번호를 리셋할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "비밀번호 초기화 메일 발송 완료", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "이메일에 해당하는 LOCAL 계정 사용자가 존재하지 않음", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "메일 전송 실패", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<String> sendUserPasswordResetEmail(
            @RequestParam("email") @Parameter(description = "사용자의 이메일 주소") String email) {
        return userService.sendUserPasswordResetEmail(email);
    }

    // 사용자 비밀번호 초기화 - 승인
    @PutMapping("/reset")
    @Operation(summary = "사용자 비밀번호 초기화 승인", description = "사용자가 비밀번호 초기화 요청을 승인하고 새 비밀번호를 설정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호가 성공적으로 변경되었습니다.", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않거나 만료된 코드 / 새 비밀번호 확인 불일치", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "이메일에 해당하는 LOCAL 계정 사용자가 존재하지 않음", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<String>  resetUserPassword(
            @Valid @RequestBody UserPasswordResetRequestDTO requestDTO) {
        return userService.resetUserPassword(requestDTO);
    }

    //______________________________사용자 관련 정보 요청_________________________________

    // 사용자 레시피 정보 요청
    @GetMapping("/recipes")
    @Operation(summary = "사용자의 작성 레시피 정보 요청", description = "사용자가 작성한 레시피 정보를 페이지네이션하여 요청합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 레시피 정보 반환", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 페이지 번호", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Page<RecipeSimpleResponseDTO>> findUserRecipes(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(value = "page", defaultValue = "0") @Parameter(description = "조회할 페이지 번호 (기본값: 0)") int page){
        return userService.findUserRecipes(userDetails,page);
    }

    // 사용자 즐겨찾기 정보 요청
    @GetMapping("/favorites")
    @Operation(summary = "사용자의 즐겨찾기 레시피 정보 요청", description = "사용자가 즐겨찾기한 레시피 정보를 페이지네이션하여 요청합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 즐겨찾기 레시피 정보 반환", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 페이지 번호", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Page<RecipeSimpleResponseDTO>> findUserFavorites(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(value = "page", defaultValue = "0") @Parameter(description = "조회할 페이지 번호 (기본값: 0)") int page){
        return userService.findUserFavorites(userDetails,page);
    }

    //______________________________사용자 팔로우_________________________________

    // 사용자 팔로우 조회
    @GetMapping("/follows")
    @Operation(summary = "사용자의 작성 레시피 정보 요청", description = "사용자가 팔로우한 다른 사용자의 목록을 요청합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자가 팔로우한 다른 사용자 목록 반환", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 사용자 정보", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Page<UserSimpleResponseDTO>> findUserFollow(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(value = "page", defaultValue = "0") @Parameter(description = "조회할 페이지 번호 (기본값: 0)") int page){
        return userService.findUserFollow(userDetails,page);
    }

    // 사용자 팔로우 설정
    @PostMapping("/follows/{userId}")
    @Operation(summary = "사용자 팔로우 설정", description = "사용자가 다른 사용자에 대해 팔로우 설정을 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자의 팔로우 설정 변경 결과를 반환", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 사용자 정보", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<String> setUserFollow(
            @PathVariable @Parameter(description = "다른 사용자 ID") Long userId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.setUserFollow(userId, userDetails);
    }

    // 사용자 팔로워 조회
    @GetMapping("/followers")
    @Operation(summary = "사용자의 작성 레시피 정보 요청", description = "사용자를 팔로우한 다른 사용자의 목록을 요청합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자를 팔로우한 다른 사용자 목록 반환", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 사용자 정보", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Page<UserSimpleResponseDTO>> findUserFollower(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(value = "page", defaultValue = "0") @Parameter(description = "조회할 페이지 번호 (기본값: 0)") int page){
        return userService.findUserFollower(userDetails,page);
    }

    //______________________________다른 사용자 정보_________________________________

    // 사용자 팔로우 조회
    @GetMapping("/{userId}")
    @Operation(summary = "다른 사용자 정보 호출", description = "다른 사용자의 정보를 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "다른 사용자 정보 반환", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 사용자 정보", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<UserResponseDTO> findOtherUser(
            @PathVariable @Parameter(description = "다른 사용자 ID") Long userId){
        return userService.findOtherUser(userId);
    }
}
