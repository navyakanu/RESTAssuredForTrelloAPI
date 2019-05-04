import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.*;

public class GetTrelloBoards extends BaseTest {

    String expectedURL = "https://trello.com/b/Mb65TdAb/vapasi-board-23";
    String listName = "TO DO THIS";
    String listID;

    int expectedBoardHeadings = 5; //need to see how can we parameterise


    @BeforeMethod
    public void setUpClass() {
        createANewList(listName);
    }


    public void createANewList(String listName) {

        JSONObject requestParams = new JSONObject();
        requestParams.put("name", listName);
        requestParams.put("idBoard", expectedBoardID);
        requestParams.put("key", key);
        requestParams.put("token", token);

        listID = given()
                .body(requestParams.toJSONString())
                .contentType(ContentType.JSON)
                .when()
                .post("1/lists")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .extract()
                .response().jsonPath().getMap("$").get("id").toString();

    }


    @Test
    public void testCreateNewCard() {
        String expectedCardName = "CardCreatedNowInAutomation";
        String expectedCardDescription = "Cardwhichiscreatednow";


        JSONObject requestParams = new JSONObject();
        requestParams.put("key", key);
        requestParams.put("token", token);
        requestParams.put("name", expectedCardName);
        requestParams.put("desc", expectedCardDescription);
        requestParams.put("idList", listID);
        requestParams.put("idBoard", expectedBoardID);

        Response response = given().log().ifValidationFails()
                .body(requestParams.toJSONString())
                .when()
                .contentType(ContentType.JSON)
                .post("1/cards")
                .then()
                .contentType(ContentType.JSON)
                .and()
                .statusCode(200)    //Status code validation
                .and()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("createcard.json")) //Schema validator
                .and()
                .body("name", equalTo(expectedCardName))        //Response data validation
                .body("desc", equalTo(expectedCardDescription))
                .extract()
                .response();


        //Testng asserts not required when you can see the same in rest assured
        // Assert.assertEquals(response.statusCode(), 200);
        // Assert.assertEquals(expectedCardName, response.jsonPath().getMap("$").get("name").toString());
        // Assert.assertEquals(expectedCardDescription, response.jsonPath().getMap("$").get("desc").toString());
    }


    @Test
    public void testGetAllBoardDetailsForAMember() {
        Response response = given()
                .queryParam("key", key)
                .queryParam("token", token)
                .contentType(ContentType.JSON)
                .when()
                    .get("1/members/me/boards")
                .then()
                    .contentType(ContentType.JSON)
                .and()
                    .statusCode(200)
                .extract()
                .response();

        List<Map<String, ?>> listMap = response.jsonPath().getList("$");

       // Assert.assertEquals(4, response.jsonPath().getList("$").size());
        Assert.assertEquals(checkAutomationExists(listMap),1);
    }


    @Test
    public void testGetNameFromID() {
        Response response = given()
                .pathParam("idBoard", expectedBoardID)
                .queryParam("fields", "name,url")
                .queryParam("key", key)
                .queryParam("token", token)
                .when()
                .get("1/boards/{idBoard}")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("id",equalTo(expectedBoardID))
                .body("name",equalTo(expectedBoardName))
                .extract()
                .response();
    }


    private int checkAutomationExists(List<Map<String, ?>> listMap) {
        int flag = 0;
        for (int i = 0; i < listMap.size(); i++) {
            if (listMap.get(i).get("name").toString().contains(expectedBoardName) && listMap.get(i).get("id").equals(expectedBoardID)) {
                flag++;
            }
        }
        return flag;
    }


    @Test
    public void testlistsInBoard() {

        Response response =
                given()
                    .queryParam("fields", "name,url")
                    .queryParam("key", key)
                    .queryParam("token", token)
                    .contentType(ContentType.JSON)
                .when()
                    .get("1/boards/" + expectedBoardID + "/lists")
                .then()
                    .contentType(ContentType.JSON)
                        .statusCode(200)
                .extract()
                .response();

        Integer actualLength = response.jsonPath().getList("$").size();
//        Assert.assertTrue(expectedBoardHeadings == actualLength);
    }


}





