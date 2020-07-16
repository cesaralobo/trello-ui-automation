package business.pages;

import framework.pageObjects.BasePage;
import framework.utils.Constants;
import framework.utils.Utils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TrelloPage extends BasePage {

  private static final String menu_login_item = ".btn-link[href*=\"login\"]";
  private static final String trello_email_input = "#user";
  private static final String trello_pass_input = "#password";
  private static final String trello_login_button = "#login";
  private static final String trello_body_screen_att = "body[data-analytics-screen]";
  private static final String trello_home_screen = "a[href*=\"login\"]";
  private static final String trello_login_screen = "#login-form label[for=\"user\"]";
  private static final String user_page_logout_button = "button[data-test-id='header-member-menu-button']";
  private static final String user_page_wellcome_board_link = "//*[contains(@class,\"mod-no-sidebar\")][contains(.,\"Personal Boards\")]//a[contains(@class,\"board-tile\")][contains(@href,\"welcome-to-trello\")]";
  private static final String user_page_wellcome_board = ".mod-board-name .board-header-btn-text";
  private static final String user_page_board_add_another_card_link = "//*[contains(@class,\"js-list-content\")][contains(.,\"To Do\")]//*[contains(@class,\"js-add-another-card\")]";
  private static final String user_page_board_add_a_card_link = "//*[contains(@class,\"js-list-content\")][contains(.,\"To Do\")]//*[contains(@class,\"js-add-a-card\")]";
  private static final String user_page_board_add_card_textarea = "//*[contains(@class,\"js-list-content\")][contains(.,\"To Do\")]//*[contains(@class,\"list-card-composer-textarea\")]";
  private static final String user_page_board_add_card_button = "//*[contains(@class,\"js-list-content\")][contains(.,\"To Do\")]//*[contains(@class,\"confirm\")]";
  private static final String user_page_board_new_card_list = ".card-short-id";
  private int user_page_current_qty_cards = 0;

  public TrelloPage(WebDriver driver) {
    super(driver);
  }


  public boolean validateScreen(String screen, String... value){

    boolean result = false;

    switch (screen){
      case "home":
        result = Utils.waitUntilLoadElement(driver,this.trello_home_screen).getText().contains(value[0]);
        break;

      case "login":
        result = Utils.waitUntilLoadElement(driver,this.trello_login_screen).getText().contains(value[0]);
        break;

      case "user":
        result = Utils.waitUntilLoadElement(driver,this.user_page_logout_button).getAttribute("title").contains(value[0]);
        break;

      case "board":
        result = Utils.waitUntilLoadElement(driver,this.user_page_wellcome_board).getText().contains(value[0]);
        break;

      case "newCard":

        driver.navigate().refresh();
        int new_card_list_size = Utils.waitUntilLoadElements(driver,this.user_page_board_new_card_list).size();

        result = new_card_list_size > this.user_page_current_qty_cards;
        break;
    }

    return result;
  }

  public void goBoard(String boardName){

    if (boardName.equalsIgnoreCase("welcome")) {
      Utils.waitUntilElementClickable(driver,this.user_page_wellcome_board_link).click();
      Utils.wait(Constants.USER_TIMEOUT);
    }

  }

  public void addCard(String text){

    if(Utils.waitUntilLoadElement(driver,this.user_page_board_add_a_card_link).getAttribute("class").contains("hide")){
      this.user_page_current_qty_cards = Utils.waitUntilLoadElements(driver,this.user_page_board_new_card_list).size();
      Utils.waitUntilElementClickable(driver,this.user_page_board_add_another_card_link).click();
      Utils.wait(Constants.USER_TIMEOUT);
    }else{
      Utils.waitUntilElementClickable(driver,this.user_page_board_add_a_card_link).click();
      Utils.wait(Constants.USER_TIMEOUT);
    }

    Utils.waitUntilLoadElement(driver,this.user_page_board_add_card_textarea).sendKeys(text);
    Utils.wait(Constants.USER_TIMEOUT);

    Utils.waitUntilElementClickable(driver,this.user_page_board_add_card_button).click();
    Utils.wait(Constants.USER_TIMEOUT);

  }

  public void signIn(String user, String password) throws Exception{

    Utils.waitUntilElementClickable(driver,this.menu_login_item).click();
    Utils.wait(Constants.USER_TIMEOUT);

    if(this.validateScreen("login","Email")){

      WebElement email_input = Utils.waitUntilLoadElement(driver, this.trello_email_input);
      email_input.sendKeys(user);
      Utils.wait(Constants.USER_TIMEOUT);

      WebElement pass_input = Utils.waitUntilLoadElement(driver, this.trello_pass_input);
      pass_input.sendKeys(password);
      Utils.wait(Constants.USER_TIMEOUT);

      Utils.waitUntilElementClickable(driver,this.trello_login_button).click();
      Utils.wait(Constants.USER_TIMEOUT);

    }else{
      throw new Exception("Login page not loaded");
    }

  }

}
