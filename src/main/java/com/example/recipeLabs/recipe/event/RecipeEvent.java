package com.example.recipeLabs.recipe.event;

import com.example.recipeLabs.recipe.entity.Recipe;
import com.example.recipeLabs.user.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RecipeEvent extends ApplicationEvent {
    private final User user;     // 레시피 작성자
    private final Recipe recipe; // 후기가 추가된 레시피

    public RecipeEvent(Object source, User user, Recipe recipe) {
        super(source); // 이벤트 소스를 전달 (보통 null 또는 호출 주체)
        this.user = user;
        this.recipe = recipe;
    }
}
