package framework.pageObjects;


import framework.utils.Utils;
import framework.utils.WebDriverUtils;
import framework.utils.datatypes.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class BasePage {

    protected static WebDriver driver;

    private static final String base_body_selector = ".v-generated-body";
    private static final String base_body_window = ".v-window-wrap";
    private static final String base_body_modal_ok_button = "//*[contains(@class,'v-window-offer-confirm-dialog')]//*[contains(text(),'Aceptar')]/ancestor::node()[3]";


    public BasePage(WebDriver driver) {
        BasePage.driver = driver;
        addWait(Wait.LONG_WAIT);
    }

    public void addWait(Wait wait){
        WebDriverUtils.addWait(this.driver, wait);
    }

    public WebElement findElement(final By locator){
        return WebDriverUtils.findElement(this.driver, locator);
    }

    protected void isModalOpen(){
        if (this.isWindowOpen()){
            Utils.waitUntilElementClickable(driver,this.base_body_modal_ok_button).click();
        }
    }

    protected boolean isWindowOpen(){
        return Utils.waitUntilLoadElement(driver,this.base_body_selector).getAttribute("class").contains("v-modal-window-open");
    }

    protected boolean isWindowWrapOpen(){
        return Utils.waitUntilLoadElement(driver,this.base_body_window).isDisplayed();
    }

}
