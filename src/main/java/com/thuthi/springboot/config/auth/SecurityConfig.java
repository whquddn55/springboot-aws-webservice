package com.thuthi.springboot.config.auth;

import com.thuthi.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                /* Rest Api 기반이므로 stateless임. 따라서 csrf설정이 필요하지 않음. */
                .csrf().disable()
                /* HTML삽입 취약점 방어로, iframe, object등에 삽입해서 제어하거나 클릭하는 공격을 방지하는 옵션을 삭제.
                * h2 console을 spring을 통해서 확인할 때 사용함. */
                .headers().frameOptions().disable()
                .and()
                /* url별 권한 사용 */
                    .authorizeHttpRequests()
                    .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll()
                    .requestMatchers("/api/v1/**").hasRole(Role.USER.name())
                    .anyRequest().authenticated()
                .and()
                    .logout()
                    .logoutSuccessUrl("/")
                .and()
                    .oauth2Login()
                    .userInfoEndpoint()
                    .userService(customOAuth2UserService);
        return http.build();
    }
}
