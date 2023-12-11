import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.client.BasePage;
import org.example.client.UserClient;
import org.example.generator.UserGenerator;
import org.example.model.user.User;
import org.example.model.user.UserWithoutEmail;
import org.example.model.user.UserWithoutName;
import org.example.model.user.UserWithoutPassword;
import org.junit.Test;

import static org.example.generator.MyConstant.*;
import static org.hamcrest.CoreMatchers.equalTo;

@Slf4j
public class CreateUserInvalidTest {
    private final UserClient userClient = new UserClient();
    private final UserGenerator userGenerator = new UserGenerator();

    @Test
    public void registerUserWithoutNameTest() {
        BasePage.installSpec(BasePage.requestSpec());
        UserWithoutName userWithoutName = userGenerator.createUserWithoutName();
        ValidatableResponse response = userClient.registerUserWithoutName(userWithoutName);
        response.statusCode(403)
                .and().body(SUCCESS, equalTo(false))
                .and().body(MESSAGE, equalTo(TEXT_MESSAGE_REQUIRED_FIELDS));
    }

    @Test
    public void registerUserWithNameNullTest() {
        User user = userGenerator.createUserWithNameNull();
        Response response = userClient.registerUser(user);
        log.info("Получен ответ от сервера: {}", response.body().asString());
        response.then()
                .statusCode(403)
                .and().body(SUCCESS, equalTo(false))
                .and().body(MESSAGE, equalTo(TEXT_MESSAGE_REQUIRED_FIELDS));
    }

    @Test
    public void registerUserWithoutPasswordTest() {
        UserWithoutPassword userWithoutPassword = userGenerator.createUserWithoutPassword();
        ValidatableResponse response = userClient.registerUserWithoutPassword(userWithoutPassword);
        response.statusCode(403)
                .and().body(SUCCESS, equalTo(false))
                .and().body(MESSAGE, equalTo(TEXT_MESSAGE_REQUIRED_FIELDS));
    }

    @Test
    public void registerUserWithPasswordNullTest() {
        User user = userGenerator.createUserWithPasswordNull();
        Response response = userClient.registerUser(user);
        response.then()
                .statusCode(403)
                .and().body(SUCCESS, equalTo(false))
                .and().body(MESSAGE, equalTo(TEXT_MESSAGE_REQUIRED_FIELDS));
    }

    @Test
    public void registerUserWithoutEmailTest() {
        UserWithoutEmail userWithoutEmail = userGenerator.createUserWithoutEmail();
        ValidatableResponse response = userClient.registerUserWithoutEmail(userWithoutEmail);
        response.statusCode(403)
                .and().body(SUCCESS, equalTo(false))
                .and().body(MESSAGE, equalTo(TEXT_MESSAGE_REQUIRED_FIELDS));
    }

    @Test
    public void registerUserWithEmailNullTest() {
        User user = userGenerator.createUserWithEmailNull();
        Response response = userClient.registerUser(user);
        response.then()
                .statusCode(403)
                .and().body(SUCCESS, equalTo(false))
                .and().body(MESSAGE, equalTo(TEXT_MESSAGE_REQUIRED_FIELDS));
    }
}
