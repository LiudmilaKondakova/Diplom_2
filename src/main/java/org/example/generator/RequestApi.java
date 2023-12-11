package org.example.generator;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.client.BasePage;

import static io.restassured.RestAssured.given;

public class RequestApi {

    @Step("Post запрос")
    public static Response post(Object user, String endPoint){
        BasePage.installSpec(BasePage.requestSpec());
        return given()
                .body(user)
                .when()
                .post(endPoint);
    }

    @Step("Put запрос")
    public static Response put(String endPoint) {
        BasePage.installSpec(BasePage.requestSpec());
        return given()
                .put(endPoint);
    }

    @Step("Delete запрос")
    public static Response delete(String endPoint){
        BasePage.installSpec(BasePage.requestSpec());
        return given()
                .delete(endPoint);
    }

    @Step("Get запрос")
    public static Response get(String endPoint) {
        BasePage.installSpec(BasePage.requestSpec());
        return given()
                .get(endPoint);
    }
}
