import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import utilities.PropertiesLoader;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

public class BaseTest {


    public String key;
    public String token;
    public String expectedBoardName;
    public String expectedBoardID;


    @BeforeSuite
    @Parameters({"env"})
    public void setUp(String environment) throws IOException {
        // public void setUp() throws IOException {
        PropertiesLoader.LoadProperties();

        if (environment.equalsIgnoreCase("testenv")) {
            System.out.println("*****" + environment);
            RestAssured.baseURI = PropertiesLoader.prop.getProperty("url");
            key = PropertiesLoader.prop.getProperty("key");
            token = PropertiesLoader.prop.getProperty("token");

        } else {
            RestAssured.baseURI = PropertiesLoader.prop.getProperty("mockurl");
            key = PropertiesLoader.prop.getProperty("mockkey");
            token = PropertiesLoader.prop.getProperty("mocktoken");
        }
        expectedBoardID = createNewBoard();
    }


    public String createNewBoard() {
        expectedBoardName = "VapasiBoard";

        JSONObject requestParams = new JSONObject();

        requestParams.put("key", key);
        requestParams.put("token", token);
        requestParams.put("name", expectedBoardName);


        Response response =
                given().log().ifValidationFails()
                        //.body(requestParams.toJSONString())
                        .queryParam("key", key)
                        .queryParam("token", token)
                        .queryParam("name", expectedBoardName)
                        .contentType(ContentType.JSON)
                        .when()
                        .post("1/boards")
                        .then()
                        .contentType(ContentType.JSON)
                        .and()
                        .statusCode(200)
                        .log().all()
                        .and()
                        .body("name", equalTo(expectedBoardName))
                        .body(containsString("name"))
                        .log().ifValidationFails()
                        .extract().response();

        return response.jsonPath().getMap("$").get("id").toString();
    }


    public void deleteBoard() {
        given().log().ifValidationFails()
                .queryParam("key", key)
                .queryParam("token", token)
                .contentType(ContentType.JSON)
                .when()
                .delete("1/boards/" + expectedBoardID)
                .then()
                .contentType(ContentType.JSON)
                .and()
                .statusCode(200)
                .and()
                .body("_value", equalTo(null))
                .body(containsString("_value"))
                .extract()
                .response().then().log().ifValidationFails();

    }


    @AfterSuite
    public void tearDown() {
        deleteBoard();
        RestAssured.reset();
    }

}
