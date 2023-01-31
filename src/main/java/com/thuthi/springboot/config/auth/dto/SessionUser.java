package com.thuthi.springboot.config.auth.dto;

import com.thuthi.springboot.domain.user.User;
import java.io.Serializable;
import lombok.Getter;

/**
 * 인증된 사용자의 정보를 담는 클래스
 */
@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
