import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.client.BasePage;
import org.example.client.UserClient;
import org.example.generator.UserGenerator;
import org.example.model.user.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.HashMap;
import java.util.Map;

import static org.example.generator.MyConstant.*;
import static org.hamcrest.CoreMatchers.equalTo;

@Slf4j
@RunWith(Parameterized.class)
public class UpdateUserTest {
    private final UserGenerator userGenerator = new UserGenerator();
    private final UserClient userClient = new UserClient();
    protected User user;
    protected String accessToken;
    private final String email;
    private final String password;
    private final String name;
    private final String testName;

    public UpdateUserTest(String email, String password, String name, String testName) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.testName = testName;
    }

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

    private static String createUniqueEmail() {
        return String.format("%s@%s.ru", RandomStringUtils.randomAlphabetic(5),
                RandomStringUtils.randomAlphabetic(3));
    }

    private static String createPassword() {
        return RandomStringUtils.randomAlphanumeric(8);
    }

    private static String createName() {
        return RandomStringUtils.randomAlphabetic(6);
    }

    @Parameterized.Parameters(name = "{index} : update {3}")
    public static Object[][] getParameters() {
        return new Object[][]{
                {createUniqueEmail(), createPassword(), createName(), "all fields"},
                {createUniqueEmail(), MY, MY, "email"},
                {MY, createPassword(),MY, "password"},
                {MY, MY, createName(), "name"},
                {createUniqueEmail(), NULL, NULL, "email without another fields"},
                {NULL, createPassword(), NULL, "password without another fields"},
                {NULL, NULL, createName(), "name without another fields"},
                {createUniqueEmail(), createPassword(), NULL, "email and password without name"},
                {NULL, createPassword(), createName(), "password and name without email"},
                {createUniqueEmail(), NULL, createName(), "email and name without password"}
        };
    }

    @Test
    public void updateLoginUserTest() {
        Map<String, String> updateData = createMap(email, password, name);
        log.info("Обновляемые данные: {}", updateData);
        Response response = userClient.updateUser(updateData, accessToken);
        log.info("Получен ответ от сервера {}", response.body().asString());
        response.then().statusCode(200)
                .and().body(SUCCESS, equalTo(true));
    }

    @Test
    public void updateWithoutLoginTest() {
        Map<String, String> updateData = createMap(email, password, name);
        log.info("Обновляемые данные: {}", updateData);
        Response response = userClient.updateUserWithoutLogin(updateData);
        log.info("Получен ответ от сервера {}", response.body().asString());
        response.then()
                .statusCode(401)
                .and().body(SUCCESS, equalTo(false))
                .and().body(MESSAGE, equalTo(MESSAGE_YOU_SHOULD_BE_AUTHORISED));
    }

    private Map<String, String> createMap(String email, String password, String name) {
        Map<String, String> updateData = new HashMap<>();
        if (email.contains(MY)) {
            updateData.put("email", user.getEmail());
        } else if (!email.contains(NULL)) {
            updateData.put("email", email);
        }
        if (password.contains(MY)) {
            updateData.put("password", user.getPassword());
        } else if (!password.contains(NULL)) {
            updateData.put("password", password);
        }
        if (name.contains(MY)) {
            updateData.put("name", user.getName());
        } else if (!name.contains(NULL)) {
            updateData.put("name", name);
        }
        return updateData;
    }
}
