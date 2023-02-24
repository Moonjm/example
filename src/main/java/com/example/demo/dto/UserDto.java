package com.example.demo.dto;

import com.example.demo.domain.Gender;
import com.example.demo.domain.User;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.security.InvalidParameterException;

public class UserDto {

    @Data
    public static class InsertUserRequest {
        private String name;
        private int age = 0;
        private Gender gender = null;
        private String disease;

        public User toEntity() {
            if (!StringUtils.hasText(name) || !StringUtils.hasText(disease) || !"YN".contains(disease) || age < 1 || gender == null) {
                throw new InvalidParameterException();
            }
            return User.builder()
                    .name(name)
                    .age(age)
                    .gender(gender)
                    .disease(disease)
                    .build();
        }
    }

    @Data
    public static class UserResponse {
        private String userId;
        private String name;
        private int age;
        private Gender gender;
        private String disease;
        private String imgPath;

        public UserResponse(User user) {
            this.userId = user.getToken();
            this.name = user.getName();
            this.age = user.getAge();
            this.gender = user.getGender();
            this.disease = user.getDisease();
            this.imgPath = user.getImgPath();
        }
    }
}
