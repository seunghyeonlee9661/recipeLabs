package com.example.recipeLabs.controller;

import com.example.recipeLabs.dto.UserCreateRequestDTO;
import com.example.recipeLabs.dto.UserResponseDTO;
import com.example.recipeLabs.security.UserDetailsImpl;
import com.example.recipeLabs.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    /* 사용자 회원가입 */
    @PostMapping("")
    public ResponseEntity<String> createUser( @Valid @RequestBody UserCreateRequestDTO requestDTO) {
        return userService.createUser(requestDTO);
    }

    /* 사용자 회원가입 */
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


//    /* 사용자 정보 요청 */
//    @GetMapping("")
//    public ResponseEntity<UserResponseDTO> findUser(@AuthenticationPrincipal UserDetailsImpl userDetails){
//        return ResponseEntity.ok(new UserResponseDTO(userDetails.getUser()));
//    }
//
//    // FIXME : 데이터 DTO로 처리할 수 있도록 개선 필요
//    /* 사용자 정보 수정 */
//    @PutMapping("/users")
//    public ResponseEntity<String>  updateUserInfo(
//            @RequestParam("nickname") String nickname,
//            @RequestParam("deleteImage") boolean deleteImage,
//            @RequestParam("file") MultipartFile file, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
//        return userService.updateUserInfo(userDetails,nickname,deleteImage,file);
//    }
//
//    /* 사용자 비밀번호 수정 */
//    @PutMapping("/users/password")
//    public ResponseEntity<String>  updateUserPassword(@RequestBody @Valid UserPasswordUpdateRequestDTO userPasswordUpdateRequestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return userService.updateUserPassword(userDetails,userPasswordUpdateRequestDTO);
//    }
//    /* 사용자 정보 삭제 */
//    @DeleteMapping("/users")
//    public ResponseEntity<String>  removeUser(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse res) throws IOException {
//        return userService.removeUser(userDetails,res);
//    }

//    /* 사용자 레시피 정보 요청 */
//    @GetMapping("")
//    public ResponseEntity<UserResponseDTO> findUser(@AuthenticationPrincipal UserDetailsImpl userDetails){
//        return ResponseEntity.ok(new UserResponseDTO(userDetails.getUser()));
//    }


}
