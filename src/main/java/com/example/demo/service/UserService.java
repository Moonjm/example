package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserDto.InsertUserRequest;
import com.example.demo.exception.OptionalObjectNullException;
import com.example.demo.model.BasicResponse;
import com.example.demo.model.CommonSuccessResponse;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.CustomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    @Transactional
    public void init() {
        jdbcTemplate.execute("INSERT INTO USERS (token, name, age, gender, disease, img_path)\n" +
                "values ('user_70T0yV9lJKdXTGU', '주디', 24, 'FEMALE', 'N', '/image/sample.jpg')");
    }

    public BasicResponse getUserDetail(String userId) {
        User user = userRepository.findByTokenAndImgPathIsNotNull(userId).orElseThrow(OptionalObjectNullException::new);
        return new CommonSuccessResponse<>(new UserDto.UserResponse(user));
    }

    @Transactional
    public BasicResponse insertUser(InsertUserRequest dto) {
        User saveUser = userRepository.save(dto.toEntity());
        return new CommonSuccessResponse<>(saveUser.getToken());
    }

    @Transactional
    public BasicResponse insertUserImage(String userId, MultipartFile image) {
        User user = userRepository.findByToken(userId).orElseThrow(OptionalObjectNullException::new);

        if (StringUtils.hasText(user.getImgPath())) {    // 이미지가 존재하는 경우
            try {
                File file = new File(CustomUtil.getRootPath() + user.getImgPath());
                Files.delete(file.getAbsoluteFile().toPath());
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        } else {

        }

        return new CommonSuccessResponse<>("SUCCESS");
    }
}
