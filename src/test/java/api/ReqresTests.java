package api;

import lombok.SneakyThrows;
import model.user.SuccessUserCreate;
import model.user.SuccessUserUpdate;
import model.user.User;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static helpers.Specification.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static helpers.Holder.*;



public class ReqresTests extends TestBase {

    TestData testData = new TestData();

    @DisplayName("Тестирование запроса Get List Users c queryParams page")
    @Test
    void getListUsersTest() {

        step("Отправка запроса с query page " + testData.page + " и проверка тела ответа по json schema", () ->
                given(requestSpec())
                        .when()
                        .queryParam("page", testData.page)
                        .get("api/users")
                        .then()
                        .spec(responseSpecOk200())
                        .assertThat()
                        .body(matchesJsonSchemaInClasspath("schemas/list_users_schema.json")))
                .body("page", equalTo(testData.page));

    }

    @DisplayName("Тестирование запроса Get получить пользователя по его id")
    @Test
    @SneakyThrows
    void getSingleUserByIdTest() {

        String expectedResult = Files.readString(Paths.get("src/test/resources/schemas/single_user_response_body.json"));

        String responseBody = step("Отправка запроса get users by id " + testData.id, () ->
                given(requestSpec())
                        .when()
                        .get("api/users/" + testData.id)
                        .then()
                        .spec(responseSpecOk200())
                        .extract().asString());

        step("Сверка тела ответа с ожидаемым результатом", () ->
                assertEquals(expectedResult, responseBody));

    }

    @DisplayName("Тестирование запроса Post c проверкой key/value")
    @Test
    void createNewUserTest() {

        User user = new User(testData.name, testData.job);

        SuccessUserCreate response = step("Отправка запроса post /users " + user, () ->
                given(requestSpec())
                        .when()
                        .body(user)
                        .post("api/users")
                        .then()
                        .spec(responseSpecCreated201())
                        .body(matchesJsonSchemaInClasspath("schemas/create_user_schema.json"))
                        .extract().response().as(SuccessUserCreate.class));

        step("Проверка, что id not null", () ->
                assertThat(response.getId(), notNullValue()));

        step("Проверка, что name равен " + testData.name, () ->
                assertEquals(response.getName(), testData.name));

        step("Проверка, что job равен " + testData.job, () ->
                assertEquals(response.getJob(), testData.job));

        step("Проверка, что gateCreated равна " + testData.getDate(), () ->
                assertThat(response.getCreatedAt(), startsWith(testData.getDate())));
    }


    @Test
    @DisplayName("Тестирование запроса Put c обновлением данных Users по полю job")
    public void modifyUser() {

        step("Создание user", () ->
                successUserCreate = testData.createUser());

        User userUpdate = new User(testData.updatedName, testData.updatedJob);

        SuccessUserUpdate response = step("Отправка запроса put /users " + userUpdate, () ->
                given(requestSpec())
                        .when()
                        .body(userUpdate)
                        .put("/api/users/" + (successUserCreate.getId()))
                        .then()
                        .spec(responseSpecOk200())
                        .body(matchesJsonSchemaInClasspath("schemas/update_user_schema.json"))
                        .extract()
                        .as(SuccessUserUpdate.class));

        step("Проверка, что name равен " + testData.updatedName, () ->
                assertEquals(response.getName(), testData.updatedName));

        step("Проверка, что job равен " + testData.updatedJob, () ->
                assertEquals(response.getJob(), testData.updatedJob));

        step("Проверка, что updatedAT равна " + testData.getDate(), () ->
                assertThat(response.getUpdatedAt(), startsWith(testData.getDate())));
    }

    @Test
    @DisplayName("Тестирование запроса Delete c удалением пользователя")
    public void deleteUser() {

        step("Создание user", () ->
                successUserCreate = testData.createUser());

        step("Удаление user " + successUserCreate.getId(), () ->
                given(requestSpec())
                        .when()
                        .delete("/api/users/" + successUserCreate.getId())
                        .then()
                        .spec(responseSpecNoContent204()));

        step("Проверка, что user с id " + successUserCreate.getId() + " не существует", () ->
                assertThat(testData.getUserIdResponseStatusCode(), equalTo(HttpStatus.SC_NOT_FOUND)));

    }
}
