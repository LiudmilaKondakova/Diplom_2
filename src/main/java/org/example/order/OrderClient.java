package org.example.order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.client.BasePage;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.example.generator.MyConstant.*;

public class OrderClient extends BasePage {

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutLogin(Map<String, String[]> ingredients) {
        BasePage.installSpec(BasePage.requestSpec());
        return given()
                .body(ingredients)
                .post(ORDERS);
    }

    @Step("Создание заказа с авотризацией")
    public Response createOrderWithLogin(Map<String, String[]> ingredients, String accessToken) {
        BasePage.installSpec(BasePage.requestSpec());
        return given()
                .header(HEADER_AUTHORIZATION, accessToken)
                .body(ingredients)
                .post(ORDERS);
    }

    @Step("Получение всех заказов")
    public Response getAllOrders() {
        BasePage.installSpec(BasePage.requestSpec());
        return given()
                .get(ORDERS + ALL);
    }

    @Step("Получение заказов конкретного пользователя с авторизацией")
    public Response getOrdersWithAuth(String accessToken) {
        BasePage.installSpec(BasePage.requestSpec());
        return given()
                .header(HEADER_AUTHORIZATION, accessToken)
                .get(ORDERS);
    }

    @Step("Получение заказов конкретного пользователя без авторизации")
    public Response getOrdersWithoutAuth() {
        BasePage.installSpec(BasePage.requestSpec());
        return given()
                .get(ORDERS);
    }
}
