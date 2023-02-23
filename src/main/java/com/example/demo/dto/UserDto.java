package com.example.demo.dto;

import com.example.demo.domain.Gender;
import com.example.demo.domain.User;
import lombok.Data;

public class UserDto {

    @Data
    public static class InsertUserRequest {
        private String name;
        private int age;
        private Gender gender;
        private String disease;

        public User toEntity() {
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
