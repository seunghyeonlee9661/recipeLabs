package com.example.recipeLabs.user.controller;

import com.example.recipeLabs.recipe.dto.RecipeSimpleResponseDTO;
import com.example.recipeLabs.user.dto.*;
import com.example.recipeLabs.global.security.UserDetailsImpl;
import com.example.recipeLabs.user.service.UserService;
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

@RequiredArgsConstructor
@RestController
@Tag(name = "사용자 API", description = "사용자 관련 API")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /* 사용자 회원가입 - 이메일 발송 */
    @PostMapping("")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserCreateRequestDTO requestDTO) {
        return userService.createUser(requestDTO);
    }

    /* 사용자 회원가입 - 이메일 링크 승인*/
    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam Long userId, @RequestParam String code) {
        return userService.verifyUser(userId, code);
    }

    /* 사용자 탈퇴 */
    @DeleteMapping("")
    public ResponseEntity<String>  removeUser(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse res) throws IOException {
        return userService.removeUser(userDetails,res);
    }

        /* 사용자 정보 요청 */
    @GetMapping("")
    public ResponseEntity<UserResponseDTO> findUser(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(new UserResponseDTO(userDetails.getUser()));
    }

    /* 사용자 정보 수정 */
    @PutMapping("/info")
    public ResponseEntity<String>  updateUserInfo(
            @RequestBody UserUpdateRequestDTO requestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return userService.updateUserInfo(requestDTO,userDetails);
    }

    /* 사용자 이미지 수정 */
    @PutMapping("/image")
    public ResponseEntity<String>  updateUserImage(
            @RequestParam("image") MultipartFile image,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return userService.updateUserImage(image,userDetails);
    }

    /* 사용자 비밀번호 수정 */
    @PutMapping("/password")
    public ResponseEntity<String>  sendUserPasswordUpdate(
            @RequestBody @Valid UserPasswordUpdateRequestDTO requestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.updateUserPassword(requestDTO,userDetails);
    }

    /* 사용자 비밀번호 초기화 - 이메일 발송 */
    @PostMapping("/reset")
    public ResponseEntity<String> sendUserPasswordResetEmail(
            @RequestParam("email") String email) {
        return userService.sendUserPasswordResetEmail(email);
    }

    /* 사용자 비밀번호 초기화 - 승인 */
    @PutMapping("/reset")
    public ResponseEntity<String>  resetUserPassword(
            @Valid @RequestBody UserPasswordResetRequestDTO requestDTO) {
        return userService.resetUserPassword(requestDTO);
    }

    /*______________________________-사용자 관련 정보 요청_________________________________ */

    /* 사용자 레시피 정보 요청 */
    @GetMapping("/recipes")
    public ResponseEntity<Page<RecipeSimpleResponseDTO>> findUsersRecipes(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(value= "page", required = false, defaultValue="0") int page){
        return userService.findUserRecipes(userDetails,page);
    }
}
