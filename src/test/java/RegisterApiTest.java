import config.ApiConfig;
import io.qameta.allure.Owner;
import model.RegisterModel;
import model.SuccessRegisterModel;
import model.UnsuccessfulRegisterModel;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static helpers.Specification.requestSpec;
import static helpers.Specification.responseSpec;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;

@Owner("Ilgiz Gafarov")
public class RegisterApiTest extends TestBase {
    private static ApiConfig config = ConfigFactory.create(ApiConfig.class);

    @Test
    @DisplayName("Тестирование запроса Post регистрация пользователя")
    void successfulRegisterUserTest() {

        RegisterModel registerBody = new RegisterModel(config.password(), config.email());

        SuccessRegisterModel response = step("Запрос на регистрацию существующего пользователя", () ->
                given(requestSpec())
                        .when()
                        .body(registerBody)
                        .post("/register/")
                        .then()
                        .spec(responseSpec())
                        .statusCode(SC_OK)
                        .extract().as(SuccessRegisterModel.class));

        step("Проверка Id", () ->
                assertThat(response.getId()).isNotNull());

        step("Проверка token", () ->
                assertThat(response.getToken()).isNotNull());
    }

    @Test
    @DisplayName("Тестирование запроса Post регистрация пользователя с незаполненными email/password")
    void unsuccessfulRegisterUserTest() {

        UnsuccessfulRegisterModel response = step("Передача запроса на регистрацию с незаполненными email/password", () ->
                given(requestSpec())
                        .when()
                        .post("/register")
                        .then()
                        .spec(responseSpec())
                        .extract().as(UnsuccessfulRegisterModel.class));

        step("Проверка ответа", () ->
                assertThat(response.getError()).isEqualTo(testData.errorRegister));
    }

}
