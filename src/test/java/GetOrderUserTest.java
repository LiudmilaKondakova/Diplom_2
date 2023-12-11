import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.example.client.BasePage;
import org.example.client.UserClient;
import org.example.generator.IngredientsGenerator;
import org.example.generator.UserGenerator;
import org.example.model.user.User;
import org.example.order.OrderClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.example.generator.MyConstant.ACCESS_TOKEN;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@Slf4j
public class GetOrderUserTest {
    protected final UserClient userClient = new UserClient();
    protected final UserGenerator userGenerator = new UserGenerator();
    private final IngredientsGenerator ingredientsGenerator = new IngredientsGenerator();
    private final OrderClient orderClient = new OrderClient();
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

    @Test
    public void getOrdersWithAuthTest() {
        orderClient.createOrderWithLogin(ingredientsGenerator.getCorrectIngredients(), accessToken);
        Response response = orderClient.getOrdersWithAuth(accessToken);
        response.then()
                .statusCode(200)
                .and().body("success", equalTo(true))
                .and().body("orders", notNullValue());
    }

    @Test
    public void getOrdersWithoutAuthTest() {
        Response response = orderClient.getOrdersWithoutAuth();
        response.then()
                .statusCode(401)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"));
    }

    @Test
    public void getAllOrdersTest() {
        Response response = orderClient.getAllOrders();
        response.then()
                .statusCode(200)
                .and().body("success", equalTo(true))
                .and().body("orders", notNullValue());
    }
}
