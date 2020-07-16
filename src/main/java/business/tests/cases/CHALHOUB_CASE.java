package business.tests.cases;

import business.pages.TrelloPage;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import framework.tests.BaseTest;
import framework.utils.Utils;
import com.aventstack.extentreports.Status;
import io.restassured.http.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.w3c.dom.DOMException;
import java.util.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;


public class CHALHOUB_CASE extends BaseTest {

    private static final Logger logger = LogManager.getLogger(CHALHOUB_CASE.class);
    private final String scenariesFile = System.getProperty("user.dir") + "/scenaries/DATA_CASES.json";

    @DataProvider(name = "data_scenaries")
    public Iterator<Object[]> escsArray() {
        Object[][] escenarios;
        escenarios = Utils.getEscenariosJson(scenariesFile);
        Assert.assertNotNull(escenarios,"Fail Scenaries Data");
        return Arrays.asList(escenarios).iterator();
    }

    public void dataResetBefore(Map data) throws Exception{

        //Check Boards Qty
        if(this.getQtyBoards(data) > 0 ){
            //Get Boards IDs
            List<String> idBoardsList = given().
                    pathParam("key",data.get("KEY").toString()).
                    pathParam("token",data.get("TOKEN").toString()).
                    baseUri(data.get("BASE_URI").toString()).
                    when().
                    get("members/me/boards?key={key}&token={token}").
                    then().
                    assertThat().
                    statusCode(200).and().contentType(ContentType.JSON).
                    and().
                    extract().
                    jsonPath().getList("id");

            //Delete each Board
            for(String item : idBoardsList){
                    given().
                        pathParam("boardID",item).
                        pathParam("key",data.get("KEY").toString()).
                        pathParam("token",data.get("TOKEN").toString()).
                        baseUri(data.get("BASE_URI").toString()).
                        when().
                        contentType(ContentType.JSON).
                        delete("boards/{boardID}?key={key}&token={token}").
                    then().
                        assertThat().statusCode(200);
            }
        }

        //Create default Board
        given().
                baseUri(data.get("BASE_URI").toString()).
                pathParam("name", "Welcome To Trello").
                pathParam("key", data.get("KEY").toString()).
                pathParam("token", data.get("TOKEN").toString()).
                contentType(ContentType.JSON).
                post("boards/?name={name}&defaultLabels=true&defaultLists=true&keepFromSource=none&prefs_permissionLevel=private&prefs_voting=disabled&prefs_comments=members&prefs_invitations=members&prefs_selfJoin=true&prefs_cardCovers=true&prefs_background=blue&prefs_cardAging=regular&key={key}&token={token}").
        then().
                assertThat().statusCode(200);

    }

    @Test(dataProvider = "data_scenaries",priority = 1)
    public void ui_challenge01(ITestContext context,Map data){

        WebDriver driver = ((WebDriver) context.getAttribute("driver"));
        ExtentReports extent = (ExtentReports) context.getAttribute("extent");

        ExtentTest rep_logger = extent.createTest(data.get("caseID").toString()+"_1");

        try {

            //Reset Data
            this.dataResetBefore(data);

            //Validate HomePage
            TrelloPage trelloPage = PageFactory.initElements(driver, TrelloPage.class);
            Assert.assertTrue(trelloPage.validateScreen("home","Log In"),"Trello HomePage Not Loaded");
            Utils.logMsg(rep_logger,logger,Status.PASS,"Trello HomePage Loaded");

            //LoginPage
            trelloPage.signIn(data.get("USER").toString(),data.get("PASS").toString());

            //Validate User
            Assert.assertTrue(trelloPage.validateScreen("user",data.get("FullName").toString()),"Trello User Not Autenticated");
            Utils.logMsg(rep_logger,logger,Status.PASS,"Trello User Autenticated");

            //Go Wellcome Board
            trelloPage.goBoard("welcome");
            Assert.assertTrue(trelloPage.validateScreen("board","Welcome To Trello"),"Trello Board Not Loaded");
            Utils.logMsg(rep_logger,logger,Status.PASS,"Trello Board Loaded");

            //Add card on Board
            trelloPage.addCard("New Card");

            //Check New Card
            Assert.assertTrue(trelloPage.validateScreen("newCard"),"Trello Card Not Added");
            Utils.logMsg(rep_logger,logger,Status.PASS,"Trello Card Added");

        }catch (TimeoutException e){
            rep_logger.log(Status.FAIL,"TimeoutException: "+e.getMessage());
            Assert.fail("TimeoutException: "+e.getMessage());
        }catch (DOMException e){
            rep_logger.log(Status.FAIL,"DOMException: "+e.getMessage());
            Assert.fail("DOMException: "+e.getMessage());
        }catch (NoSuchElementException e) {
            rep_logger.log(Status.FAIL,"NoSuchElementException: "+e.getMessage());
            Assert.fail("No se encontro Elemento: "+e.getMessage());
        }catch (Exception e) {
            rep_logger.log(Status.FAIL,"Exception: "+e.getMessage());
            Assert.fail("Exception: "+e.getMessage());
        }finally {
            context.setAttribute("rep_logger",rep_logger);
        }

    }

    @Test(dataProvider = "data_scenaries",priority = 2)
    public void api_challenge02(ITestContext context,Map data){



        ExtentReports extent = (ExtentReports) context.getAttribute("extent");
        ExtentTest rep_logger = extent.createTest(data.get("caseID").toString()+"_2");

        try {

            //Reset Data
            this.dataResetBefore(data);

            //Get Board ID
            String boardID = given().
                    pathParam("key",data.get("KEY").toString()).
                    pathParam("token",data.get("TOKEN").toString()).
                    baseUri(data.get("BASE_URI").toString()).
                    when().
                    get("members/me/boards?key={key}&token={token}").
                    then().
                    assertThat().
                    statusCode(200).and().contentType(ContentType.JSON).
                    and().
                    extract().
                    path("[0].'id'");

            Assert.assertNotNull(boardID,"Board ID is null");
            Utils.logMsg(rep_logger,logger,Status.PASS,"Board ID: "+boardID);

            //Get List ID
            String listID = given().
                    baseUri(data.get("BASE_URI").toString()).
                    pathParam("boardID",boardID).
                    pathParam("key",data.get("KEY").toString()).
                    pathParam("token",data.get("TOKEN").toString()).
                    when().
                    get("boards/{boardID}/lists?cards=all&card_fields=all&filter=open&fields=all&key={key}&token={token}").
                    then().
                    assertThat().
                    statusCode(200).and().contentType(ContentType.JSON).
                    and().
                    extract().
                    jsonPath().getList("findAll{it.name=='To Do'}.id").get(0).toString();

            Assert.assertNotNull(listID,"List ID is null");
            Utils.logMsg(rep_logger,logger,Status.PASS,"List ID: "+listID);

            //Get List of Cards
            int qtyCards = this.getQtyCardsOnList(listID,data);

            //Create New Card
            given().
                    baseUri(data.get("BASE_URI").toString()).
                    pathParam("name", "New Card").
                    pathParam("listID", listID).
                    pathParam("key", data.get("KEY").toString()).
                    pathParam("token", data.get("TOKEN").toString()).
                    contentType(ContentType.JSON).
                    post("cards?name={name}&pos=bottom&idList={listID}&keepFromSource=all&key={key}&token={token}").
                    then().
                    assertThat().statusCode(200);

            //Check New Card
            int qtyFinalCards = this.getQtyCardsOnList(listID,data);
            assertThat(qtyFinalCards, greaterThan(qtyCards));
            Utils.logMsg(rep_logger,logger,Status.PASS,"New Card Added! ");

        }catch (TimeoutException e){
            rep_logger.log(Status.FAIL,"TimeoutException: "+e.getMessage());
            Assert.fail("TimeoutException: "+e.getMessage());
        }catch (DOMException e){
            rep_logger.log(Status.FAIL,"DOMException: "+e.getMessage());
            Assert.fail("DOMException: "+e.getMessage());
        }catch (NoSuchElementException e) {
            rep_logger.log(Status.FAIL,"NoSuchElementException: "+e.getMessage());
            Assert.fail("No se encontro Elemento: "+e.getMessage());
        }catch (Exception e) {
            rep_logger.log(Status.FAIL,"Exception: "+e.getMessage());
            Assert.fail("Exception: "+e.getMessage());
        }finally {
            context.setAttribute("rep_logger",rep_logger);
        }

    }

    private int getQtyCardsOnList(String listID, Map data){

        return given().
                baseUri(data.get("BASE_URI").toString()).
                pathParam("listID",listID).
                pathParam("key",data.get("KEY").toString()).
                pathParam("token",data.get("TOKEN").toString()).
                when().
                get("lists/{listID}/cards?fields=id&key={key}&token={token}").
                then().
                assertThat().
                statusCode(200).and().contentType(ContentType.JSON).
                and().
                extract().
                jsonPath().getList("findAll{it.id}").size();
    }

    private int getQtyBoards(Map data){

        return given().
                pathParam("key",data.get("KEY").toString()).
                pathParam("token",data.get("TOKEN").toString()).
                baseUri(data.get("BASE_URI").toString()).
                when().
                get("members/me/boards?key={key}&token={token}").
                then().
                assertThat().
                statusCode(200).and().contentType(ContentType.JSON).
                and().
                extract().
                jsonPath().getList("id").size();
    }

}
