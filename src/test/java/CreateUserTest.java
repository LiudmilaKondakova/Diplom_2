import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.example.client.BasePage;
import org.example.client.UserClient;
import org.example.generator.UserGenerator;
import org.example.model.user.User;
import org.junit.After;
import org.junit.Test;

import static org.example.generator.MyConstant.*;
import static org.hamcrest.CoreMatchers.equalTo;


@Slf4j
public class CreateUserTest {
    private final UserClient userClient = new UserClient();
    private final UserGenerator userGenerator = new UserGenerator();
    protected User user;
    protected String accessToken;

    @After
    public void deleteUser() {
        if (user != null) {
            userClient.deleteUser(user, accessToken);
        }
    }

    @Test
    public void registerUniqueUserTest() {
        BasePage.installSpec(BasePage.requestSpec());
        user = userGenerator.createUser();
        log.info("Попытка создания пользователя: {}", user);
        Response response = userClient.registerUser(user);
        accessToken = response.body().path(ACCESS_TOKEN);
        log.info("Получен ответ от сервера: {}", response.body().asString());
        response.then()
                .statusCode(200)
                .and().body(SUCCESS, equalTo(true));
    }
    @Test
    public void registerUserWithExistingEmailTest() {
        BasePage.installSpec(BasePage.requestSpec());
        user = userGenerator.createUser();
        log.info("Попытка создания пользователя: {}", user);
        Response response = userClient.registerUser(user);
        accessToken = response.body().path(ACCESS_TOKEN);
        log.info("Получен ответ от сервера: {}", response.body().asString());
        response.then()
                .statusCode(200)
                .and().body(SUCCESS, equalTo(true));

        Response responseExist = userClient.registerUser(user);
        log.info("Попытка создания существующего пользователя: {}", user);
        log.info("Получен ответ от сервера: {}", responseExist.body().asString());
        responseExist.then()
                .statusCode(403)
                .and().body(SUCCESS, equalTo(false))
                .and().body(MESSAGE, equalTo(TEXT_MESSAGE_EXISTS));
    }
}
