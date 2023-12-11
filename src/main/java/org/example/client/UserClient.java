package org.example.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.example.model.user.User;
import org.example.model.user.UserWithoutEmail;
import org.example.model.user.UserWithoutName;
import org.example.model.user.UserWithoutPassword;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.example.generator.MyConstant.*;

public class UserClient extends BasePage {

    @Step("Регистрация пользователя")
    public Response registerUser(User user) {
        BasePage.installSpec(BasePage.requestSpec());
        return given()
                .body(user)
                .when()
                .post(ROOT + REGISTER);
    }

    @Step("Удаление пользователя")
    public Response deleteUser(User user, String accessToken) {
        BasePage.installSpec(BasePage.requestSpec());
        return given()
                .header(HEADER_AUTHORIZATION, accessToken)
                .body(user)
                .delete(ROOT + USER);
    }

    @Step("Авторизация пользователя")
    public Response loginUser(User user) {
        BasePage.installSpec(BasePage.requestSpec());
        return given()
                .body(user)
                .post(ROOT + LOGIN);
    }

    @Step("Получение информации о пользователе")
    public Response getUserInfo(String accessToken) {
        BasePage.installSpec(BasePage.requestSpec());
        return given()
                .headers(HEADER_AUTHORIZATION, accessToken)
                .get(ROOT + USER);
    }

    @Step("Регистрация пользователя без поля name")
    public ValidatableResponse registerUserWithoutName(UserWithoutName userWithoutName) {
        BasePage.installSpec(BasePage.requestSpec());
        return given()
                .body(userWithoutName)
                .post(ROOT + REGISTER)
                .then();
    }

    @Step("Авторизация пользователя без поля name")
    public ValidatableResponse loginUserWithoutName(UserWithoutName userWithoutName) {
        BasePage.installSpec(BasePage.requestSpec());
        return given()
                .body(userWithoutName)
                .post(ROOT + LOGIN)
                .then();
    }

    @Step("Авторизация пользователя без поля password")
    public ValidatableResponse loginUserWithoutPassword(UserWithoutPassword userWithoutPassword) {
        BasePage.installSpec(BasePage.requestSpec());
        return given()
                .body(userWithoutPassword)
                .post(ROOT + LOGIN)
                .then();
    }
    @Step("Регистрация пользователя без поля password")
    public ValidatableResponse registerUserWithoutPassword(UserWithoutPassword userWithoutPassword) {
        BasePage.installSpec(BasePage.requestSpec());
        return given()
                .body(userWithoutPassword)
                .post(ROOT + REGISTER)
                .then();
    }

    @Step("Авторизация пользователя без поля email")
    public ValidatableResponse loginUserWithoutEmail(UserWithoutEmail userWithoutEmail) {
        BasePage.installSpec(BasePage.requestSpec());
        return given()
                .body(userWithoutEmail)
                .post(ROOT + LOGIN)
                .then();
    }

    @Step("Регистрация пользователя без поля email")
    public ValidatableResponse registerUserWithoutEmail(UserWithoutEmail userWithoutEmail) {
        BasePage.installSpec(BasePage.requestSpec());
        return given()
                .body(userWithoutEmail)
                .post(ROOT + REGISTER)
                .then();
    }

    @Step("Изменение пользователя c авторизацией")
    public Response updateUser(Map<String, String> updateData, String accessToken) {
        BasePage.installSpec(BasePage.requestSpec());
        return given()
                .header(HEADER_AUTHORIZATION, accessToken)
                .body(updateData)
                .patch(ROOT + USER);
    }

    @Step("Изменение пользователя без авторизации")
    public Response updateUserWithoutLogin(Map<String, String> updateData) {
        BasePage.installSpec(BasePage.requestSpec());
        return given()
                .body(updateData)
                .patch(ROOT + USER);
    }
}
