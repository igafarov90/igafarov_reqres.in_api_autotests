package api;

import model.user.SuccessUserCreate;
import model.user.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static helpers.Holder.successUserCreate;
import static helpers.Specification.*;
import static io.restassured.RestAssured.given;
import static java.time.ZoneOffset.UTC;

public class TestData {

    public Integer id = 2,
            page = 1;
    public String name = "igafarov",
            job = "manual QA",
            updatedJob = "AQA",
            updatedName = "iegafarov";

    public String getDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(UTC));
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    public SuccessUserCreate createUser() {

        User user = new User(name, job);

        SuccessUserCreate response = given()
                .when()
                .spec(requestSpec())
                .body(user)
                .post("api/users")
                .then()
                .spec(responseSpecCreated201())
                .extract()
                .response()
                .as(SuccessUserCreate.class);
        return (response);
    }

    public int getUserIdStatusCode() {
        return
                given().when()
                        .spec(requestSpec())
                        .get("api/users/" + successUserCreate.getId())
                        .then().extract().statusCode();
    }
}
