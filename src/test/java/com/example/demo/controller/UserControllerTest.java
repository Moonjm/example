package com.example.demo.controller;

import com.example.demo.domain.Gender;
import com.example.demo.dto.UserDto.InsertUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.ContentModifyingOperationPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class UserControllerTest {

    @Autowired
    private WebApplicationContext ctx;
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void mockMvcSetup(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .apply(documentationConfiguration(restDocumentation))
                .addFilters(new CharacterEncodingFilter("UTF-8", true)) // ?????? ??????
                .alwaysDo(print()).build();
    }

    @Test
    @DisplayName("????????? ????????? ??????")
    void getUserImageTest() {
        try {
            this.mockMvc.perform(
                            RestDocumentationRequestBuilders.get("/v1/users/{userId}/image", "user_70T0yV9lJKdXTGU")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andDo(document("get-user-image",
                            customPreProcessorRequest(),
                            Preprocessors.preprocessResponse(new ContentModifyingOperationPreprocessor((originalContent, contentType) -> "<< IMAGE >>".getBytes(StandardCharsets.UTF_8))),
                            pathParameters(
                                    parameterWithName("userId").attributes(new Attributes.Attribute("type", "String")).description("????????? ?????????")
                            )
                    ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("????????? ??????")
    void getUsersTest() {
        try {
            this.mockMvc.perform(
                            RestDocumentationRequestBuilders.get("/v1/users/{userId}", "user_70T0yV9lJKdXTGU")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultCode").value(1))
                    .andDo(document("get-user-detail",
                            customPreProcessorRequest(),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("userId").attributes(new Attributes.Attribute("type", "String")).description("????????? ?????????")
                            ),
                            responseFields(
                                    fieldWithPath("resultCode").type(JsonFieldType.NUMBER).description("????????????"),
                                    fieldWithPath("resultData.userId").type(JsonFieldType.STRING).description("??????????????????"),
                                    fieldWithPath("resultData.name").type(JsonFieldType.STRING).description("??????"),
                                    fieldWithPath("resultData.age").type(JsonFieldType.NUMBER).description("??????"),
                                    fieldWithPath("resultData.gender").type(JsonFieldType.STRING).description("??????"),
                                    fieldWithPath("resultData.disease").type(JsonFieldType.STRING).description("????????????"),
                                    fieldWithPath("resultData.imgPath").type(JsonFieldType.STRING).description("????????? ??????")
                            )
                    ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("????????? ??????")
    void insertUserTest() {
        InsertUserRequest dto = new InsertUserRequest();
        dto.setName("??????");
        dto.setAge(20);
        dto.setGender(Gender.MALE);
        dto.setDisease("N");

        try {
            String content = objectMapper.writeValueAsString(dto);
            this.mockMvc.perform(
                            post("/v1/users")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .characterEncoding("UTF-8")
                                    .content(content)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultCode").value(1))
                    .andDo(document("insert-user",
                            customPreProcessorRequest(),
                            preprocessResponse(prettyPrint()),
                            requestFields(
                                    fieldWithPath("name").type(JsonFieldType.STRING).description("??????"),
                                    fieldWithPath("age").type(JsonFieldType.NUMBER).description("??????"),
                                    fieldWithPath("gender").type(JsonFieldType.STRING).description("??????"),
                                    fieldWithPath("disease").type(JsonFieldType.STRING).description("????????????")
                            ),
                            responseFields(
                                    fieldWithPath("resultCode").type(JsonFieldType.NUMBER).description("????????????"),
                                    fieldWithPath("resultData").type(JsonFieldType.STRING).description("????????? ????????? ?????????")
                            )
                    ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("????????? ????????? ??????")
    void insertUserImageTest() {
        try {
            MockMultipartFile file = new MockMultipartFile("image", "???????????????.jpg", MediaType.IMAGE_JPEG_VALUE, "???????????????".getBytes(StandardCharsets.UTF_8));
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("image", "");
            this.mockMvc.perform(
                            RestDocumentationRequestBuilders.multipart("/v1/users/{userId}/image", "user_70T0yV9lJKdXTGU")
                                    .file(file)
                                    .params(params)
                                    .contentType("multipart/form-data")
                                    .accept(MediaType.APPLICATION_JSON)
                                    .characterEncoding("UTF-8")
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultCode").value(1))
                    .andDo(document("insert-user-image",
                            customPreProcessorRequest(),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("userId").attributes(new Attributes.Attribute("type", "String")).description("????????? ?????????")
                            ),
                            requestParameters(
                                    parameterWithName("image").attributes(new Attributes.Attribute("type", "File")).description("?????????")
                            ),
                            responseFields(
                                    fieldWithPath("resultCode").type(JsonFieldType.NUMBER).description("????????????"),
                                    fieldWithPath("resultData").type(JsonFieldType.STRING).description("???????????????")
                            )
                    ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("????????? ??????")
    void deleteUserTest() {
        try {
            this.mockMvc.perform(
                            RestDocumentationRequestBuilders.delete("/v1/users/{userId}", "user_70T0yV9lJKdXTGU")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultCode").value(1))
                    .andDo(document("delete-user",
                            customPreProcessorRequest(),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("userId").attributes(new Attributes.Attribute("type", "String")).description("????????? ?????????")
                            ),
                            responseFields(
                                    fieldWithPath("resultCode").type(JsonFieldType.NUMBER).description("????????????"),
                                    fieldWithPath("resultData").type(JsonFieldType.STRING).description("???????????????")
                            )
                    ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OperationRequestPreprocessor customPreProcessorRequest() {
        return preprocessRequest(
                modifyUris()
                        .host("localhost")
                        .port(8080),
                prettyPrint());
    }

}