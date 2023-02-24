package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserDto.InsertUserRequest;
import com.example.demo.exception.ImageExtensionException;
import com.example.demo.exception.OptionalObjectNullException;
import com.example.demo.model.BasicResponse;
import com.example.demo.model.CommonSuccessResponse;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.CustomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final List<String> IMAGE_EXT_ARR = Arrays.asList("JPG", "JPEG", "PNG");

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

        if (!IMAGE_EXT_ARR.contains(CustomUtil.getExtension(image.getOriginalFilename()).toUpperCase())) {
            throw new ImageExtensionException("파일 확장자 오류, JPG, PNG만 가능");
        }
        removeImage(user);
        String randName = CustomUtil.getRandName(Objects.requireNonNull(image.getOriginalFilename()));
        String imgPath = "/image/" + randName;
        File saveFile = new File(CustomUtil.getRootPath() + imgPath);
        try {
            FileCopyUtils.copy(image.getInputStream(), new FileOutputStream(saveFile));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        user.updateImgPath(imgPath);

        return new CommonSuccessResponse<>("SUCCESS");
    }

    public ResponseEntity<byte[]> getUserImage(String userId) {
        User user = userRepository.findByToken(userId).orElseThrow(OptionalObjectNullException::new);

        String imgPath = user.getImgPath();
        if (!StringUtils.hasText(imgPath)) {
            throw new InvalidParameterException();
        }

        MediaType mediaType = MediaType.IMAGE_JPEG;
        if (CustomUtil.getExtension(imgPath).toUpperCase().equals("PNG")) {
            mediaType = MediaType.IMAGE_PNG;
        }
        HttpHeaders headers = new HttpHeaders();
        try {
            byte[] media = Files.readAllBytes(new File(CustomUtil.getRootPath() + imgPath).toPath());
            headers.setContentType(mediaType);
            return new ResponseEntity<>(media, headers, HttpStatus.OK);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public BasicResponse deleteUser(String userId) {
        User user = userRepository.findByToken(userId).orElseThrow(OptionalObjectNullException::new);
        removeImage(user);
        userRepository.delete(user);
        return new CommonSuccessResponse<>("SUCCESS");
    }

    private void removeImage(User user) {
        if (StringUtils.hasText(user.getImgPath())) {    // 이미지가 존재하는 경우 삭제
            try {
                File file = new File(CustomUtil.getRootPath() + user.getImgPath());
                Files.delete(file.getAbsoluteFile().toPath());
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }
}
