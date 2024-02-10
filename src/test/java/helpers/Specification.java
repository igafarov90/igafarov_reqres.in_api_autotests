package helpers;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
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

    public static ResponseSpecification responseSpecOk200() {
        return new ResponseSpecBuilder()
                .expectContentType(JSON)
                .expectStatusCode(HttpStatus.SC_OK)
                .build();
    }
    public static ResponseSpecification responseSpecCreated201() {
        return new ResponseSpecBuilder()
                .expectContentType(JSON)
                .expectStatusCode(HttpStatus.SC_CREATED)
                .build();
    }

    public static ResponseSpecification responseSpecNoContent204() {
        return new ResponseSpecBuilder()
                .expectStatusCode(HttpStatus.SC_NO_CONTENT)
                .build();
    }
}
