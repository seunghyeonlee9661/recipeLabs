package com.example.recipeLabs.user.service;

import com.example.recipeLabs.global.service.ImageTransformService;
import com.example.recipeLabs.global.service.RedisService;
import com.example.recipeLabs.recipe.dto.RecipeSimpleResponseDTO;
import com.example.recipeLabs.user.dto.UserCreateRequestDTO;
import com.example.recipeLabs.recipe.entity.Recipe;
import com.example.recipeLabs.user.dto.UserPasswordResetRequestDTO;
import com.example.recipeLabs.user.dto.UserPasswordUpdateRequestDTO;
import com.example.recipeLabs.user.dto.UserUpdateRequestDTO;
import com.example.recipeLabs.user.entity.User;
import com.example.recipeLabs.global.enums.Provider;
import com.example.recipeLabs.recipe.repository.RecipeRepository;
import com.example.recipeLabs.user.repository.UserRepository;
import com.example.recipeLabs.global.security.JwtUtil;
import com.example.recipeLabs.global.security.UserDetailsImpl;
import com.example.recipeLabs.global.service.EmailService;
import com.example.recipeLabs.global.service.ImageService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ImageService imageService;
    private final ImageTransformService imageTransformService;
    private final RedisService redisService;
    private final JwtUtil jwtUtil;

    /* 회원가입 - 메일 전송 */
    @Transactional
    public ResponseEntity<String> createUser(UserCreateRequestDTO userCreateRequestDTO){
        // 이메일 중복 검사
        if (userRepository.findByEmailAndProvider(userCreateRequestDTO.getEmail(),Provider.LOCAL).isPresent()) throw new IllegalArgumentException("중복된 Email 입니다.");
        // 비밀번호 일치 검사
        if (!userCreateRequestDTO.getPassword().equals(userCreateRequestDTO.getPasswordCheck())) throw new IllegalArgumentException("비밀번호 확인이 일치하지 않습니다.");
        // 사용자 메일 인증 코드
        String code = UUID.randomUUID().toString();
        // 사용자 정보 생성
        User user = new User(userCreateRequestDTO,passwordEncoder.encode(userCreateRequestDTO.getPassword()),code);
        userRepository.save(user);
        // 사용자 id 확인
        Long userId = user.getId();

        String subject = "RecipeLabs 회원가입 인증 메일 발송"; // 제목
        String text = "인증 링크 - http://dltmdgus9661.iptime.org:8087/users/verify?userId=" + userId + "&code=" + code; // 인증 링크 // TODO:인증 링크 경로 수정 필요
        // 인증 메일 발송
        if(emailService.sendEmail(user.getEmail(),subject,text)){
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 완료 - 이메일 인증을 해주세요");
        }else{
            // 메일 전송 실패의 경우 계정 삭제
            userRepository.delete(user);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("메일 전송에 실패했습니다");
        }
    }

    /* 회원가입 - 인증 완료 */
    @Transactional
    public ResponseEntity<String> verifyUser(Long userId,String code){
        User user = userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자 입니다."));
        if(user.isEmailVerified()) return ResponseEntity.ok("이미 완료된 이메일 입니다.");
        if(user.getEmailVerificationCode().equals(code)){
            user.setEmailVerified();
            return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
        }else{
            return ResponseEntity.status(HttpStatus.CREATED).body("올바르지 않는 코드입니다.");
        }
    }

    /* 회원 탈퇴*/
    @Transactional
    public ResponseEntity<String> removeUser(UserDetailsImpl userDetails, HttpServletResponse res) {
        User user = userDetails.getUser();
        // 사용자가 기본 계정인 경우
        if(user.getProvider().equals(Provider.LOCAL) && user.getProfileImage() != null){// 사용자가 기본 계정인 경우
            imageService.deleteFileByUrl(user.getProfileImage()); //이미지 삭제
        }
        // 사용자 삭제
        userRepository.delete(user);
        // 쿠키 삭제
        jwtUtil.removeJwtCookie(res);
        return ResponseEntity.ok().body("User deleted successfully");
    }

    /* 사용자의 게시물 검색 기능 */
    public ResponseEntity<Page<RecipeSimpleResponseDTO>> findUserRecipes(UserDetailsImpl userDetails, int page){
        Pageable pageable = PageRequest.of(page, 8, Sort.by(Sort.Order.desc("id")));
        Page<Recipe> recipePage  = recipeRepository.findByUserId(userDetails.getUser().getId(),pageable);
        return ResponseEntity.ok(recipePage.map(RecipeSimpleResponseDTO::new));
    }

    /* 회원 정보 수정*/
    @Transactional
    public ResponseEntity<String> updateUserInfo(UserUpdateRequestDTO requestDTO, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        user.updateInfo(requestDTO);
        userRepository.save(user);
        return ResponseEntity.ok().body("사용자 정보가 변경되었습니다.");
    }

    /* 회원 이미지 수정*/
    @Transactional
    public ResponseEntity<String> updateUserImage(MultipartFile image, UserDetailsImpl userDetails) throws IOException {
        User user = userDetails.getUser();
        // 기존 이미지 제거
        if(user.getProfileImage() != null) imageService.deleteFileByUrl(user.getProfileImage());
        // 새로운 이미지 webp 변환
        File webPFile = imageTransformService.convertToWebP(image);
        // 이미지 저장 후 url 반환
        String imageUrl = imageService.uploadFile(webPFile);
        //변경 내용 적용
        user.updateImage(imageUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body("사용자 이미지가 변경되었습니다.");
    }

    /* 회원 비밀번호 수정*/
    @Transactional
    public ResponseEntity<String> updateUserPassword(UserPasswordUpdateRequestDTO requestDTO, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        // 새 비밀번호 확인
        if(!requestDTO.getNewPassword().equals(requestDTO.getNewPasswordCheck()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("새 비밀번호 확인이 일치하지 않습니다.");
        // 현재 비밀번호 일치 확인
        if(!passwordEncoder.matches(requestDTO.getCurrentPassword(),user.getPassword()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("현재 비밀번호가 올바르지 않습니다.");
        // 비밀번호 변경
        user.updatePassword(passwordEncoder.encode(requestDTO.getNewPassword()));
        return ResponseEntity.ok().body("사용자 비밀번호가 변경되었습니다.");
    }

    /* 비밀번호 초기화 메일 전송 */
    @Transactional
    public ResponseEntity<String> sendUserPasswordResetEmail(String email){
        User user = userRepository.findByEmailAndProvider(email, Provider.LOCAL).orElseThrow(() -> new IllegalArgumentException("이메일에 해당하는 LOCAL 계정 사용자가 존재하지 않습니다."));
        // 비밀번호 리셋 토큰 생성
        String resetCode = UUID.randomUUID().toString();
        String subject = "RecipeLabs 비밀번호 리셋 메일 발송"; // 제목
        String text = "리셋 링크 - http://dltmdgus9661.iptime.org:8087/users/reset?code=" + resetCode; // 인증 링크 // TODO:인증 링크 경로 수정 필요
        // 인증 메일 발송
        if(emailService.sendEmail(user.getEmail(),subject,text)){
            // 메일 발송 성공시 Redis에 인증 코드와 메일을 저장 / 5분
            redisService.save(RedisService.RESET_CODE_PREFIX,resetCode,email,RedisService.RESET_CODE_DURATION);
            return ResponseEntity.status(HttpStatus.CREATED).body("메일 발송 완료");
        }else{
            // 메일 전송 실패
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("메일 전송에 실패했습니다");
        }
    }

    /* 비밀번호 초기화 승인 */
    @Transactional
    public ResponseEntity<String> resetUserPassword(UserPasswordResetRequestDTO requestDTO){
        // redis에서 리셋 코드로 사용자 이메일 확인
        String email = redisService.get(RedisService.RESET_CODE_PREFIX,requestDTO.getResetCode());
        
        // 리셋 코드가 만료 or 올바르지 않은 경우
        if (email == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않거나 만료된 코드입니다.");
        }
        // 사용자 정보 확인
        User user = userRepository.findByEmailAndProvider(email, Provider.LOCAL).orElseThrow(() -> new IllegalArgumentException("이메일에 해당하는 LOCAL 계정 사용자가 존재하지 않습니다."));
        // 새 비밀번호 일치 확인
        if(!requestDTO.getNewPassword().equals(requestDTO.getNewPasswordCheck())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("새 비밀번호 확인이 일치하지 않습니다.");
        // 비밀번호 수정
        user.updatePassword(passwordEncoder.encode(requestDTO.getNewPassword()));
        return ResponseEntity.ok().body("사용자 비밀번호가 변경되었습니다.");
    }
}
