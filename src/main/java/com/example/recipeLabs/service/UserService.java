package com.example.recipeLabs.service;

import com.example.recipeLabs.dto.UserCreateRequestDTO;
import com.example.recipeLabs.entity.User;
import com.example.recipeLabs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    /* 회원가입 - 메일 전송 */
    @Transactional
    public ResponseEntity<String> createUser(UserCreateRequestDTO userCreateRequestDTO){
        // 이메일 중복 검사
        if (userRepository.findByEmail(userCreateRequestDTO.getEmail()).isPresent()) throw new IllegalArgumentException("중복된 Email 입니다.");
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
        if(user.getEmailVerificationCode().equals(code)){
            user.setEmailVerified();
            return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
        }else{
            return ResponseEntity.status(HttpStatus.CREATED).body("올바르지 않는 코드입니다.");
        }
    }
}
