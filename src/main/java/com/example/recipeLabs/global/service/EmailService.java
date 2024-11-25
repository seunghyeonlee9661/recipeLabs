package com.example.recipeLabs.global.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/*
작성자 : 이승현
사용자 비밀번호 찾기를 위해 사용하는 SMTP 서비스
*/
@Service
@RequiredArgsConstructor
public class EmailService {

    private final RedisService redisService;
    private final JavaMailSender emailSender;
    private final String senderEmail = "RecipeLabs@gmail.com"; // 발송자 이메일 주소
    private final String senderName = "RecipeLabs"; // 발송자 이름

    // 메일 발송 기능
    public boolean sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setFrom(String.format("%s <%s>", senderName, senderEmail)); // 발송자 이름과 이메일 주소 설정
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message); // 이메일 전송
            return true; // 전송 성공
        } catch (Exception e) { // 이메일 전송 실패 시 예외 처리
            e.printStackTrace(); // 또는 로깅 처리
            return false; // 전송 실패
        }
    }
}
