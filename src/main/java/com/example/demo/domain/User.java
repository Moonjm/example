package com.example.demo.domain;

import com.example.demo.util.TokenGenerator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    private static final String USER_PREFIX = "user_";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sn;
    private String token;
    private String name;
    private int age;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String disease;
    private String imgPath;

    @Builder
    public User(String name, int age, Gender gender, String disease, String imgPath) {
        this.token = TokenGenerator.randomCharacterWithPrefix(USER_PREFIX);
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.disease = disease;
        this.imgPath = imgPath;
    }

    public void updateImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
