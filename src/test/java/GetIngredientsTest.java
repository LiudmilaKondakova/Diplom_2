import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.example.ingredient.IngredientClient;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

@Slf4j
public class GetIngredientsTest {
    private final IngredientClient ingredientClient = new IngredientClient();

    @Test
    @DisplayName("Получение списка ингредиентов")
    public void getAllIngredientsTest() {
        Response response = ingredientClient.getAllIngredients();
        log.info("Получен ответ от сервера: {}", response.body().asString());
        response.then().statusCode(200)
                .and().body("success", equalTo(true));
    }
}
