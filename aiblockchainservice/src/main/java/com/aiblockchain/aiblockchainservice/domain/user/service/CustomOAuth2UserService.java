package com.aiblockchain.aiblockchainservice.domain.user.service;

import com.aiblockchain.aiblockchainservice.domain.user.entity.Role;
import com.aiblockchain.aiblockchainservice.domain.user.entity.User;
import com.aiblockchain.aiblockchainservice.domain.user.repository.UserRepository;
import com.aiblockchain.aiblockchainservice.global.auth.dto.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. 구글 서버에서 유저 정보 가져오기
        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("getAttributes : {}", oAuth2User.getAttributes());

        // 2. provider 확인 (google)
        String provider = userRequest.getClientRegistration().getRegistrationId();

        // 3. 정보 추출
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String providerId = oAuth2User.getName(); // google의 sub
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        if (email == null) {
            throw new OAuth2AuthenticationException("이메일 정보가 없습니다.");
        }

        // 4. 회원가입 또는 업데이트 (Login or Register)
        User user = saveOrUpdate(email, name, provider, providerId);

        // 5. UserDetails 객체 반환 (SecurityContext에 저장될 객체)
        return new CustomOAuth2User(user, attributes);
    }

    private User saveOrUpdate(String email, String name, String provider, String providerId) {
        // 이메일로 유저 찾기
        User user = userRepository.findByEmail(email)
                .map(entity -> {
                    // 이미 가입된 유저라면 이름만 업데이트
                    entity.updateName(name);
                    return entity;
                })
                .orElse(User.builder()
                        .email(email)
                        .name(name)
                        .role(Role.USER) // 기본 권한 USER
                        .provider(provider)
                        .providerId(providerId)
                        .build());

        return userRepository.save(user);
    }
}