package helpers;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomApiListener.withCustomTemplates;
import static io.restassured.http.ContentType.JSON;

public class Specification {
    public static RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .addFilter(withCustomTemplates())
                .setRelaxedHTTPSValidation()
                .setContentType(JSON)
                .setAccept(JSON)
                .build();
    }

    public static ResponseSpecification responseSpec() {
        return new ResponseSpecBuilder()
                .expectContentType(JSON)
                .build();
    }

}
