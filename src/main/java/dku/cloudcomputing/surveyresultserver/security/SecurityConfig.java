package dku.cloudcomputing.surveyresultserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import dku.cloudcomputing.surveyresultserver.controller.dto.StatusResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticator jwtAuthenticator;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(v -> v.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(v -> {
                    v.requestMatchers(HttpMethod.POST, "/surveys/*/results").permitAll();
                    v.requestMatchers(HttpMethod.GET, "/surveys/*/results").authenticated();
                    v.anyRequest().permitAll();
                })
                .addFilterBefore(new JwtAuthenticationFilter(jwtAuthenticator), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(v -> {
                            v.accessDeniedHandler((request, response, accessDeniedException) -> {
                                response.setStatus(403);
                                response.setCharacterEncoding("utf-8");
                                response.getWriter()
                                        .write(objectMapper.writeValueAsString(new StatusResponseDto("fail")));
                            });
                            v.authenticationEntryPoint((request, response, authException) -> {
                                response.setStatus(401);
                                response.setCharacterEncoding("utf-8");
                                response.getWriter()
                                        .write(objectMapper.writeValueAsString(new StatusResponseDto("fail")));
                            });
                        }
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}