package com.example.recipeLabs.recipe.controller;

import com.example.recipeLabs.global.security.UserDetailsImpl;
import com.example.recipeLabs.recipe.dto.RecipeSimpleResponseDTO;
import com.example.recipeLabs.user.dto.UserCreateRequestDTO;
import com.example.recipeLabs.user.dto.UserResponseDTO;
import com.example.recipeLabs.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    /* 사용자 레시피 정보 요청 */
    @GetMapping("users/recipes")
    public ResponseEntity<Page<RecipeSimpleResponseDTO>> findUsersRecipes(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                          @RequestParam(value= "page", required = false, defaultValue="0") int page){
        return userService.findUserRecipes(userDetails,page);
    }
}
