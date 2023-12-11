import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.example.client.BasePage;
import org.example.client.UserClient;
import org.example.generator.IngredientsGenerator;
import org.example.generator.UserGenerator;
import org.example.model.ingredient.Ingredient;
import org.example.model.order.OrderAfterCreate;
import org.example.model.user.User;
import org.example.order.OrderClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.example.generator.MyConstant.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

@Slf4j
public class CreateOrderWithLoginTest {
    protected final UserClient userClient = new UserClient();
    protected final UserGenerator userGenerator = new UserGenerator();
    private final IngredientsGenerator ingredientsGenerator = new IngredientsGenerator();
    private final OrderClient orderClient = new OrderClient();
    private OrderAfterCreate orderAfterCreate;
    protected User user;
    protected String accessToken;

    @Before
    public void createUser() {
        BasePage.installSpec(BasePage.requestSpec());
        user = userGenerator.createUser();
        log.info("Попытка создания пользователя: {}", user);
        Response response = userClient.registerUser(user);
        accessToken = response.body().path(ACCESS_TOKEN);
        log.info("Получен ответ от сервера: {}", response.body().asString());
        Response responseToken = userClient.getUserInfo(accessToken);
    }

    @After
    public void deleteUser() {
        if (user != null) {
            userClient.deleteUser(user, accessToken);
        }
    }

    private List<String> getActual(OrderAfterCreate orderAfterCreate) {
        List<String> actual = new ArrayList<>();
        for (Ingredient ingredient : orderAfterCreate.getIngredients()) {
            actual.add(ingredient.get_id());
        }
        return actual;
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void createCorrectOrderWithLoginTest() {
        Map<String, String[]> ingredientsMap = ingredientsGenerator.getCorrectIngredients();
        Response response = orderClient.createOrderWithLogin(ingredientsMap, accessToken);
        orderAfterCreate = response.body().jsonPath().getObject(ORDER_MESSAGE, OrderAfterCreate.class);
        List<String> expected = new ArrayList<>(Arrays.asList(ingredientsMap.get(INGREDIENT_MESSAGE)));
        List<String> actual = getActual(orderAfterCreate);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией, без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        Map<String, String[]> ingredientsMap = ingredientsGenerator.getEmptyIngredients();
        log.info("Список ингредиентов: {}", ingredientsMap);
        Response response = orderClient.createOrderWithLogin(ingredientsMap, accessToken);
        log.info("Получен ответ от сервера: {}", response.body().asString());
        response.then()
                .statusCode(400)
                .and().body(SUCCESS, equalTo(false))
                .and().body(MESSAGE, equalTo(TEXT_MESSAGE_INGREDIENT_IDS_MUST_BE_PROVIDED));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией, с неверным хешем ингредиентов")
    public void createOrderWithIncorrectIngredientsTest() {
        Map<String, String[]> ingredientsMap = ingredientsGenerator.getIncorrectIngredients();
        log.info("Список ингредиентов: {}", ingredientsMap);
        Response response = orderClient.createOrderWithLogin(ingredientsMap, accessToken);
        log.info("Получен ответ от сервера: {}", response.body().asString());
        response.then()
                .statusCode(500);
    }
}
