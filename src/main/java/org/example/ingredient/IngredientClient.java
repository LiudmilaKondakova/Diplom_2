package org.example.ingredient;

import io.restassured.response.Response;
import org.example.client.BasePage;

import static io.restassured.RestAssured.given;
import static org.example.generator.MyConstant.INGREDIENTS;

public class IngredientClient extends BasePage {

    public Response getAllIngredients() {
        BasePage.installSpec(BasePage.requestSpec());
        return given()
                .get(INGREDIENTS);
    }
}
