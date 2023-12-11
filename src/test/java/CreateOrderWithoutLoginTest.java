import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.example.generator.IngredientsGenerator;
import org.example.order.OrderClient;
import org.junit.Test;

import java.util.Map;

import static org.example.generator.MyConstant.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@Slf4j
public class CreateOrderWithoutLoginTest {
    private final OrderClient orderClient = new OrderClient();
    private final IngredientsGenerator ingredientsGenerator = new IngredientsGenerator();

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createCorrectOrderWithoutLogin() {
        Map<String, String[]> ingredientsMap = ingredientsGenerator.getCorrectIngredients();
        log.info("Список ингредиентов: {}", ingredientsMap);

        Response response = orderClient.createOrderWithoutLogin(ingredientsMap);
        log.info("Получен ответ от сервера: {}", response.body().asString());

        response.then()
                .statusCode(200)
                .and().body(SUCCESS, equalTo(true))
                .and().body(ORDER_MESSAGE, notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без авторизации, без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        Map<String, String[]> ingredientsMap = ingredientsGenerator.getEmptyIngredients();
        log.info("Список ингредиентов: {}", ingredientsMap);
        Response response = orderClient.createOrderWithoutLogin(ingredientsMap);
        log.info("Получен ответ от сервера: {}", response.body().asString());
        response.then()
                .statusCode(400)
                .and().body(SUCCESS, equalTo(false))
                .and().body(MESSAGE, equalTo(TEXT_MESSAGE_INGREDIENT_IDS_MUST_BE_PROVIDED));
    }

    @Test
    @DisplayName("Создание заказа без авторизации, с некорректным хэшем ингредиентов")
    public void createOrderWithIncorrectIngredientsTest() {
        Map<String, String[]> ingredientsMap = ingredientsGenerator.getIncorrectIngredients();
        log.info("Список ингредиентов: {}", ingredientsMap);
        Response response = orderClient.createOrderWithoutLogin(ingredientsMap);
        log.info("Получен ответ от сервера: {}", response.body().asString());
        response.then()
                .statusCode(500);
    }
}
