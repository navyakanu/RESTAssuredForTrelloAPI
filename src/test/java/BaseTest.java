import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import utilities.PropertiesLoader;

import java.io.IOException;

import static io.restassured.RestAssured.given;

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
        createNewBoard();
    }


    public void createNewBoard() {
        expectedBoardName = "Vapasi Board 23";

        JSONObject requestParams = new JSONObject();
        requestParams.put("key", key);
        requestParams.put("token", token);
        requestParams.put("name", expectedBoardName);


        Response response = given()
                .body(requestParams.toJSONString())
                .queryParam("key", key)
                .queryParam("token", token)
                .queryParam("name", expectedBoardName)
                .when()
                .post("1/boards/")
                .then()
                .contentType(ContentType.JSON)
                .extract()
                .response();

        expectedBoardID = response.jsonPath().getMap("$").get("id").toString();
        System.out.println(expectedBoardID);


    }


    public void deleteBoard() {
        given()
                .when()
                .queryParam("key", key)
                .queryParam("token", token)
                .delete("1/boards/" + expectedBoardID)

                .then()
                .contentType(ContentType.JSON)
                .extract()
                .response().then().log().all();

    }


    @AfterSuite
    public void tearDown() {
        deleteBoard();
        RestAssured.reset();
    }

}
