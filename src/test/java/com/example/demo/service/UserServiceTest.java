package com.example.demo.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Nested
    @DisplayName("사용자 상세 조회")
    class GetUserDetailTest {
        @Test
        @DisplayName("성공")
        void success1() {

        }
    }
}