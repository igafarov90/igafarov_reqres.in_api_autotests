import com.fasterxml.jackson.databind.ObjectMapper;
import config.ApiConfig;
import helpers.TestData;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;


public class TestBase {

    TestData testData = new TestData();
    ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    public static void setUp() {

        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();


    }

}
