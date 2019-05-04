import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import utilities.PropertiesLoader;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class BaseTest {


    public String key;
    public String token;
    public String expectedBoardName;
    public String expectedBoardID;


    @BeforeSuite
    public void setUp() throws IOException {
        PropertiesLoader.LoadProperties();
        RestAssured.baseURI = PropertiesLoader.prop.getProperty("url");
        key = PropertiesLoader.prop.getProperty("key");
        token = PropertiesLoader.prop.getProperty("token");
        expectedBoardID = createNewBoard();
    }


    public String createNewBoard() {
        expectedBoardName = "Vapasi Board 23";

        JSONObject requestParams = new JSONObject();

        requestParams.put("key", key);
        requestParams.put("token", token);
        requestParams.put("name", expectedBoardName);


        Response response =
                given().log().ifValidationFails()
                        .body(requestParams.toJSONString())
//                        .queryParam("key", key)
//                        .queryParam("token", token)
//                        .queryParam("name", expectedBoardName)
                        .contentType(ContentType.JSON)
                        .when()
                        .post("1/boards")
                        .then()
                        .contentType(ContentType.JSON)
                        .and()
                        .statusCode(200)
                        .and()
                        .body("name", equalTo(expectedBoardName))
                        .log().ifValidationFails()
                        .extract().response();

        return response.jsonPath().getMap("$").get("id").toString();
    }


    public void deleteBoard() {
        given().log().ifValidationFails()
                .queryParam("key", key)
                .queryParam("token", token)
                .when()
                .delete("1/boards/" + expectedBoardID)
                .then()
                .contentType(ContentType.JSON)
                .and()
                .statusCode(200)
                .and()
                .body("_value", equalTo(null))
                .extract()
                .response().then().log().ifValidationFails();

    }


    @AfterSuite
    public void tearDown() {
        deleteBoard();
        RestAssured.reset();
    }

}
