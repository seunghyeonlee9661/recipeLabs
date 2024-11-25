package com.example.recipeLabs.user.event;
import com.example.recipeLabs.user.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class FollowEvent extends ApplicationEvent {
    private final User user;     // 레시피 작성자
    private final User target; // 후기가 추가된 레시피

    public FollowEvent(Object source, User user, User target) {
        super(source); // 이벤트 소스를 전달 (보통 null 또는 호출 주체)
        this.user = user;
        this.target = target;
    }
}
