import io.qameta.allure.Owner;
import lombok.SneakyThrows;
import model.ListUsersModel;
import model.SuccessUserCreateModel;
import model.SuccessUserUpdate;
import model.UserModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static helpers.Specification.requestSpec;
import static helpers.Specification.responseSpec;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.apache.http.HttpStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Owner("Ilgiz Gafarov")
public class UserApiTest extends TestBase {


    @DisplayName("Тестирование запроса Get List Users ?page=2")
    @Test
    void getListUsersTest() {

        step("Отправка запроса с query page " + testData.page + " и проверка тела ответа по json schema", () ->
                given(requestSpec())
                        .when()
                        .queryParam("page", testData.page)
                        .get("/users")
                        .then()
                        .spec(responseSpec())
                        .assertThat()
                        .statusCode(SC_OK)
                        .body(matchesJsonSchemaInClasspath("schemas/schema_list_users.json"))
                        .body("page", equalTo(testData.page)));
    }

    @DisplayName("Тестирование запроса Get получить пользователя по его id")
    @Test
    @SneakyThrows
    void getSingleUserByIdTest() {

        ListUsersModel response = step("Отправка запроса get users by id " + testData.id, () ->
                given(requestSpec())
                        .when()
                        .get("/users/" + testData.id)
                        .then()
                        .spec(responseSpec())
                        .assertThat()
                        .statusCode(SC_OK)
                        .body(matchesJsonSchemaInClasspath("schemas/schema_single_users.json"))
                        .extract().as(ListUsersModel.class));

        String actualJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
        ListUsersModel expectedModel = mapper.readValue(
                new File("src/test/resources/json_template/single_user_response_body.json"),
                ListUsersModel.class);
        String expectedJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expectedModel);

        step("Сверка тела ответа с ожидаемым результатом", () ->
                assertThat(expectedJson.equals(actualJson)));
    }

    @DisplayName("Тестирование запроса Post c проверкой key/value")
    @Test
    void createNewUserTest() {

        UserModel userCreateBody = new UserModel(testData.name, testData.job);

        SuccessUserCreateModel response = step("Отправка запроса post /users", () ->
                given(requestSpec())
                        .when()
                        .body(userCreateBody)
                        .post("users")
                        .then()
                        .spec(responseSpec())
                        .statusCode(SC_CREATED)
                        .body(matchesJsonSchemaInClasspath("schemas/schema_create_user.json"))
                        .extract().response().as(SuccessUserCreateModel.class));

        step("Проверка, что id not null", () ->
                assertThat(response.getId()).isNotNull());

        step("Проверка, что name равен " + testData.name, () ->
                assertThat(response.getName().equals(testData.name)));

        step("Проверка, что job равен " + testData.job, () ->
                assertThat(response.getJob().equals(testData.job)));

        step("Проверка, что dateCreated равна " + testData.getDate(), () ->
                assertThat(response.getCreatedAt().startsWith(testData.getDate())));
    }


    @Test
    @DisplayName("Тестирование запроса Patch c обновлением данных Users по полю job/name")
    public void updateUserTest() {

        UserModel userUpdateBody = new UserModel(testData.updatedName, testData.updatedJob);

        SuccessUserUpdate response = step("Отправка запроса put /users/" + testData.id, () ->
                given(requestSpec())
                        .when()
                        .body(userUpdateBody)
                        .patch("/users/" + testData.id)
                        .then()
                        .spec(responseSpec())
                        .statusCode(SC_OK)
                        .body(matchesJsonSchemaInClasspath("schemas/schema_update_user.json"))
                        .extract()
                        .as(SuccessUserUpdate.class));

        step("Проверка, что name равен " + testData.updatedName, () ->
                assertThat(response.getName().equals(testData.updatedName)));

        step("Проверка, что job равен " + testData.updatedJob, () ->
                assertThat(response.getJob().equals(testData.updatedJob)));

        step("Проверка, что updatedAT равна " + testData.getDate(), () ->
                assertThat(response.getUpdatedAt()).startsWith(testData.getDate()));
    }

    @Test
    @DisplayName("Тестирование запроса Delete удаление пользователя")
    public void deleteUserTest() {

        step("Отправка запроса delete/user/" + testData.id, () ->
                given(requestSpec())
                        .when()
                        .delete("/users/" + testData.id)
                        .then()
                        .assertThat()
                        .statusCode(SC_NO_CONTENT));
    }


}
