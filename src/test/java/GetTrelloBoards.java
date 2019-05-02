import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class GetTrelloBoards extends BaseTest {

    Integer expectedSize = 4;
    String expectedID = "59b0dbe5a0f94ee47e731f3c";
    String expectedName = "Automation";
    String expectedURL = "https://trello.com/b/XG9QBwf1/automation";


    int expectedBoardHeadings = 5;


    @Test
    public void testCreateNewCard() {
        String expectedCardName = "CardCreatedNowInAutomation";
        String expectedCardDescription = "Cardwhichiscreatednow";
        String expectedIDBoard = "5ccae85e74a0ac794efe2377";
        String expectedIDList = "5ccae85e74a0ac794efe2378";

        JSONObject requestParams = new JSONObject();
        requestParams.put("key", key);
        requestParams.put("token", token);
        requestParams.put("name", expectedCardName);
        requestParams.put("desc", expectedCardDescription);
        requestParams.put("idList", expectedIDList);
        requestParams.put("idBoard", expectedIDBoard);

        Response response = given()
                .body(requestParams.toJSONString())
                .queryParam("key", key)
                .queryParam("token", token)
                .queryParam("name", expectedCardName)
                .queryParam("desc", expectedCardDescription)
                .queryParam("idList", expectedIDList)
                .queryParam("idBoard", expectedIDBoard)
                .when()
                .post("1/cards")
                .then()
                .contentType(ContentType.JSON)
                .extract()
                .response();

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(expectedCardName, response.jsonPath().getMap("$").get("name").toString());
        Assert.assertEquals(expectedCardDescription, response.jsonPath().getMap("$").get("desc").toString());
    }


    @Test
    public void testAllBoardDetails() {
        Response response = given()
                .queryParam("key", key)
                .queryParam("token", token)
                .when()
                .get("1/members/me/boards")
                .then()
                .contentType(ContentType.JSON)
                .extract()
                .response();

        response.prettyPrint();

        List<Map<String, ?>> listMap = response.jsonPath().getList("$");
        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(4, response.jsonPath().getList("$").size());
        Assert.assertTrue(checkAutomationExists(listMap) == 1);
    }


    @Test
    public void testGetNameAndURLFROMID() {
        Response response = given()
                .queryParam("fields", "name,url")
                .queryParam("key", key)
                .queryParam("token", token)
                .when()
                .get("1/boards/" + expectedID)
                .then()
                .contentType(ContentType.JSON)
                .extract()
                .response();
        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(expectedID, response.jsonPath().getMap("$").get("id").toString());
        Assert.assertEquals(expectedName, response.jsonPath().getMap("$").get("name").toString());
        Assert.assertEquals(expectedURL, response.jsonPath().getMap("$").get("url").toString());

    }


    private int checkAutomationExists(List<Map<String, ?>> listMap) {
        int flag = 0;
        for (int i = 0; i < listMap.size(); i++) {
            if (listMap.get(i).get("name").toString().contains(expectedName)) {
                flag++;
            }
        }
        return flag;
    }


    @Test
    public void testAllAvailableBoards() {
        Response response = given()
                .queryParam("fields", "name,url")
                .queryParam("key", key)
                .queryParam("token", token)
                .when()
                .get("1/boards/" + expectedID + "/lists")
                .then()
                .contentType(ContentType.JSON)
                .extract()
                .response();

        Assert.assertEquals(response.statusCode(), 200);
        Integer actualLength = response.jsonPath().getList("$").size();
        Assert.assertTrue(expectedBoardHeadings == actualLength);
    }


}



