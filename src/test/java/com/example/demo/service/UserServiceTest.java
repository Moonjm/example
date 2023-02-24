package com.example.demo.service;

import com.example.demo.domain.Gender;
import com.example.demo.domain.User;
import com.example.demo.dto.UserDto.InsertUserRequest;
import com.example.demo.dto.UserDto.UserResponse;
import com.example.demo.exception.ImageExtensionException;
import com.example.demo.exception.OptionalObjectNullException;
import com.example.demo.model.CommonSuccessResponse;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Nested
    @DisplayName("사용자 등록")
    class InsertUserTest {

        @Spy
        @InjectMocks
        UserService userService;
        @Mock
        UserRepository userRepository;

        @Test
        @DisplayName("성공")
        void success1() {
            // given
            InsertUserRequest dto = new InsertUserRequest();
            dto.setName("이름");
            dto.setAge(10);
            dto.setDisease("N");
            dto.setGender(Gender.MALE);

            User user = dto.toEntity();
            given(userRepository.save(any())).willReturn(user);

            // when
            CommonSuccessResponse<String> response = (CommonSuccessResponse<String>) userService.insertUser(dto);

            // then
            assertThat(response.getResultCode()).isEqualTo(1);
            assertThat(response.getResultData()).isEqualTo(user.getToken());
            then(userService).should().insertUser(dto);
            then(userRepository).should().save(any());
        }

        @Test
        @DisplayName("실패 - 이름 누락")
        void fail1() {
            // given
            InsertUserRequest dto = new InsertUserRequest();
            dto.setGender(Gender.MALE);
            dto.setAge(10);
            dto.setDisease("Y");

            // when
            assertThatThrownBy(() -> userService.insertUser(dto)).isInstanceOf(InvalidParameterException.class);

            // then
            then(userService).should().insertUser(dto);
            then(userRepository).should(never()).save(any());
        }

        @Test
        @DisplayName("실패 - 나이 누락")
        void fail2() {
            // given
            InsertUserRequest dto = new InsertUserRequest();
            dto.setGender(Gender.MALE);
            dto.setName("이름");
            dto.setDisease("Y");

            // when
            assertThatThrownBy(() -> userService.insertUser(dto)).isInstanceOf(InvalidParameterException.class);

            // then
            then(userService).should().insertUser(dto);
            then(userRepository).should(never()).save(any());
        }

        @Test
        @DisplayName("실패 - 성별 누락")
        void fail3() {
            // given
            InsertUserRequest dto = new InsertUserRequest();
            dto.setName("이름");
            dto.setAge(10);
            dto.setDisease("Y");

            // when
            assertThatThrownBy(() -> userService.insertUser(dto)).isInstanceOf(InvalidParameterException.class);

            // then
            then(userService).should().insertUser(dto);
            then(userRepository).should(never()).save(any());
        }

        @Test
        @DisplayName("실패 - 질병 유무 누락")
        void fail4() {
            // given
            InsertUserRequest dto = new InsertUserRequest();
            dto.setName("이름");
            dto.setAge(10);
            dto.setGender(Gender.MALE);

            // when
            assertThatThrownBy(() -> userService.insertUser(dto)).isInstanceOf(InvalidParameterException.class);

            // then
            then(userService).should().insertUser(dto);
            then(userRepository).should(never()).save(any());
        }
    }

    @Nested
    @DisplayName("사용자 상세 조회")
    class GetUserDetailTest {

        @Spy
        @InjectMocks
        UserService userService;
        @Mock
        UserRepository userRepository;

        @Test
        @DisplayName("성공")
        void success1() {
            // given
            String userId = "id";

            User user = User.builder()
                    .name("이름")
                    .disease("Y")
                    .age(20)
                    .gender(Gender.MALE)
                    .build();
            user.updateImgPath("imagePath");

            given(userRepository.findByTokenAndImgPathIsNotNull(any())).willReturn(Optional.of(user));

            // when
            CommonSuccessResponse<UserResponse> response = (CommonSuccessResponse<UserResponse>) userService.getUserDetail(userId);

            // then
            assertThat(response.getResultCode()).isEqualTo(1);
            assertThat(response.getResultData().getUserId()).isEqualTo(user.getToken());
            assertThat(response.getResultData().getName()).isEqualTo(user.getName());
            assertThat(response.getResultData().getAge()).isEqualTo(user.getAge());
            assertThat(response.getResultData().getGender()).isEqualTo(user.getGender());
            assertThat(response.getResultData().getImgPath()).isEqualTo(user.getImgPath());
            then(userService).should().getUserDetail(userId);
            then(userRepository).should().findByTokenAndImgPathIsNotNull(any());
        }

        @Test
        @DisplayName("실패 - 사용자 아이디 오류")
        void fail1() {
            // given
            String userId = "";

            given(userRepository.findByTokenAndImgPathIsNotNull(any())).willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> userService.getUserDetail(userId)).isInstanceOf(OptionalObjectNullException.class);

            // then
            then(userService).should().getUserDetail(userId);
            then(userRepository).should().findByTokenAndImgPathIsNotNull(any());
        }
    }

    @Nested
    @DisplayName("사용자 이미지 등록")
    class InsertUserImageTest {

        @Spy
        @InjectMocks
        UserService userService;
        @Mock
        UserRepository userRepository;

        @Test
        @DisplayName("성공")
        void success1() {
            // given
            String userId = "";

            MockMultipartFile file = new MockMultipartFile("image", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test".getBytes(StandardCharsets.UTF_8));

            User user = User.builder()
                    .name("이름")
                    .disease("Y")
                    .age(20)
                    .gender(Gender.MALE)
                    .build();

            given(userRepository.findByToken(any())).willReturn(Optional.of(user));

            // when
            CommonSuccessResponse<String> response = (CommonSuccessResponse<String>) userService.insertUserImage(userId, file);

            // then
            assertThat(response.getResultCode()).isEqualTo(1);
            then(userService).should().insertUserImage(userId, file);
            then(userRepository).should().findByToken(any());
        }

        @Test
        @DisplayName("실패 - 사용자 아이디 오류")
        void fail1() {
            // given
            String userId = "";

            MultipartFile file = mock(MultipartFile.class);
            given(userRepository.findByToken(any())).willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> userService.insertUserImage(userId, file)).isInstanceOf(OptionalObjectNullException.class);

            // then
            then(userService).should().insertUserImage(userId, file);
            then(userRepository).should().findByToken(any());
        }

        @Test
        @DisplayName("실패 - 이미지 확장자 오류")
        void fail2() {
            // given
            String userId = "";

            MockMultipartFile file = new MockMultipartFile("image", "test.gif", MediaType.IMAGE_GIF_VALUE, "test".getBytes(StandardCharsets.UTF_8));

            User user = User.builder()
                    .name("이름")
                    .disease("Y")
                    .age(20)
                    .gender(Gender.MALE)
                    .build();

            given(userRepository.findByToken(any())).willReturn(Optional.of(user));

            // when
            assertThatThrownBy(() -> userService.insertUserImage(userId, file)).isInstanceOf(ImageExtensionException.class)
                    .hasMessageContaining("파일 확장자 오류, JPG, PNG만 가능");

            // then
            then(userService).should().insertUserImage(userId, file);
            then(userRepository).should().findByToken(any());
        }
    }

    @Nested
    @DisplayName("사용자 삭제")
    class DeleteUserTest {

        @Spy
        @InjectMocks
        UserService userService;
        @Mock
        UserRepository userRepository;

        @Test
        @DisplayName("성공")
        void success1() {
            // given
            String userId = "";

            User user = User.builder()
                    .name("이름")
                    .disease("Y")
                    .age(20)
                    .gender(Gender.MALE)
                    .build();

            given(userRepository.findByToken(any())).willReturn(Optional.of(user));

            // when
            CommonSuccessResponse<String> response = (CommonSuccessResponse<String>) userService.deleteUser(userId);

            // then
            assertThat(response.getResultCode()).isEqualTo(1);
            then(userService).should().deleteUser(userId);
            then(userRepository).should().findByToken(any());
            then(userRepository).should().delete(any());
        }

        @Test
        @DisplayName("실패 - 사용자 아이디 오류")
        void fail1() {
            // given
            String userId = "";

            given(userRepository.findByToken(any())).willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> userService.deleteUser(userId)).isInstanceOf(OptionalObjectNullException.class);

            // then
            then(userService).should().deleteUser(userId);
            then(userRepository).should().findByToken(any());
            then(userRepository).should(never()).delete(any());
        }
    }
}