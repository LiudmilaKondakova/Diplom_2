import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.model.StatusDetails;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.example.client.BasePage;
import org.example.client.UserClient;
import org.example.generator.UserGenerator;
import org.example.model.user.User;
import org.example.model.user.UserWithoutEmail;
import org.example.model.user.UserWithoutName;
import org.example.model.user.UserWithoutPassword;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.example.generator.MyConstant.*;
import static org.hamcrest.CoreMatchers.equalTo;

@Slf4j
public class LoginUserTest {
    protected final UserClient userClient = new UserClient();
    protected final UserGenerator userGenerator = new UserGenerator();
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
    public void loginCorrectUserTest() {
        userClient.loginUser(user)
                .then()
                .statusCode(200)
                .and().body(SUCCESS, equalTo(true));
    }

    @Test
    public void loginWithNameNullTest() {
        user.setName(null);
        userClient.loginUser(user)
                .then()
                .statusCode(200)
                .and().body(SUCCESS, equalTo(true));
    }

    @Test
    public void loginWithoutNameTest() {
        UserWithoutName userWithoutName = new UserWithoutName(user.getEmail(), user.getPassword());
        userClient.loginUserWithoutName(userWithoutName)
                .statusCode(200)
                .and().body(SUCCESS, equalTo(true));
    }

    @Test
    public void loginWithPasswordNullTest() {
        user.setPassword(null);
        userClient.loginUser(user)
                .then()
                .statusCode(401)
                .and().body(SUCCESS, equalTo(false));
    }

    @Test
    public void loginWithIncorrectPasswordTest() {
        user.setPassword(INCORRECT_PASSWORD);
        userClient.loginUser(user)
                .then()
                .statusCode(401)
                .and().body(SUCCESS, equalTo(false));
    }

    @Test
    public void loginWithoutPasswordTest() {
        UserWithoutPassword userWithoutPassword = new UserWithoutPassword(user.getEmail(), user.getName());
        userClient.loginUserWithoutPassword(userWithoutPassword)
                .statusCode(401)
                .and().body(SUCCESS, equalTo(false));
    }

    @Test
    public void loginWithEmailNullTest() {
        user.setEmail(null);
        userClient.loginUser(user)
                .then()
                .statusCode(401)
                .and().body(SUCCESS, equalTo(false));
    }

    @Test
    public void loginWithEmailIncorrectTest() {
        user.setEmail(INCORRECT_EMAIL);
        userClient.loginUser(user)
                .then()
                .statusCode(401)
                .and().body(SUCCESS, equalTo(false));
    }

    @Test
    public void loginWithoutEmailTest() {
        UserWithoutEmail userWithoutEmail = new UserWithoutEmail(user.getPassword(), user.getName());
        userClient.loginUserWithoutEmail(userWithoutEmail)
                .statusCode(401)
                .and().body(SUCCESS, equalTo(false));
    }
}
