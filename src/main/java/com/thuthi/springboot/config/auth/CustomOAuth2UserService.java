package com.thuthi.springboot.config.auth;

import com.thuthi.springboot.config.auth.dto.OAuthAttributes;
import com.thuthi.springboot.config.auth.dto.SessionUser;
import com.thuthi.springboot.domain.user.User;
import com.thuthi.springboot.domain.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        /*
         * registrationId: 현재 로그인 진행중인 서비스를 구분하는 코드.
         * 예를 들어, google로그인과 naver로그인 2개를 지원할 때 현재 어느 서비스로 접근했는지 구분하기 위해 사용 됨.
         */
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        /*
         * userNameAttributeName: OAuth2 로그인 진행 시 키가 되는 필드값을 의미함. 즉, PK와 같은 의미.
         */
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
                .getUserNameAttributeName();

        /*
         * OAuthAttributes: OAuth2UserService를 통해 가져온 OAuth2User의 attribute들을 담는 클래스.
         * 서비스에 무관하게 관리하기 위해 정의됨.
         */
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        /*
         * SessionUser: 인증된 사용자의 정보를 담는 클래스
         * User는 엔티티 그 자체이기 때문에 직렬화를 하게 될 경우, 자식들 까지 한꺼번에 직렬화 하느라 쿼리가 나갈 가능성이 있다.
         * 따라서 직렬화 기능을 가진 dto를 하나 더 추가하는게 유지보수성에 좋다.
         */
        User user = saveOrUpdate(attributes);
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    /*
     * 로그인 한 사용자의 정보(이름, 프로필 사진)이 변경되었을 때 자동으로 update해주기 위해 사용
     */
    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
