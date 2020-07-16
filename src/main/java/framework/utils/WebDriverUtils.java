package framework.utils;

import framework.utils.datatypes.Wait;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.google.common.io.Files;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static framework.utils.Constants.*;

public class WebDriverUtils {

    public static boolean isElementPresent(WebDriver driver, final By Locator) {
        boolean oAux = true;

        try {
            driver.findElement(Locator);

        } catch (NoSuchElementException e) {
            oAux = false;
        }

        return oAux;
    }
	
	public static void waitTime(WebDriver driver, int time){
        driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
    }

    public static void addWait(WebDriver driver, Wait wait){
        switch (wait){
            case LONG_WAIT:
                waitTime(driver, LONG_TIMEOUT);
                break;
            case MEDIUM_WAIT:
                waitTime(driver, TIMEOUT);
                break;
            case SHORT_WAIT:
                waitTime(driver, SHORT_TIMEOUT);
                break;
            default:
                waitTime(driver, USER_TIMEOUT);
                break;
        }
    }

    public static WebElement findElement(WebDriver driver, final By locator){
        WebElement element = null;
        try{
            element = driver.findElement(locator);
        }
        catch (NoSuchElementException e){
            System.err.print(e.getMessage());
        }
        return element;
    }

    public static List<WebElement> findElements(WebDriver driver, final By locator){
        List<WebElement> elements = null;
        try{
            elements = driver.findElements(locator);
        }
        catch (NoSuchElementException e){
            System.err.print(e.getMessage());
        }
        return elements;
    }

    public static void scrollToElement(WebDriver driver, WebElement element) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].scrollIntoView({ block: 'start', behavior: 'smooth' });", element);
    }

    public static void setInputValueByID(WebDriver driver, WebElement element, String value) {

        String strJS = "document.getElementById('"+element.getAttribute("id")+"').value = '"+value+"'";
        ((JavascriptExecutor)driver).executeScript(strJS);
        Utils.wait(Constants.USER_TIMEOUT);
    }

    public static void htmlDragAndDrop(WebDriver driver,WebElement elementDrag,WebElement elementDrop) {
        String strJS = "var DndSimulatorDataTransfer=function(){this.data={}};DndSimulatorDataTransfer.prototype.dropEffect=\"move\",DndSimulatorDataTransfer.prototype.effectAllowed=\"all\",DndSimulatorDataTransfer.prototype.files=[],DndSimulatorDataTransfer.prototype.items=[],DndSimulatorDataTransfer.prototype.types=[],DndSimulatorDataTransfer.prototype.clearData=function(t){if(t){delete this.data[t];var e=this.types.indexOf(t);delete this.types[e],delete this.data[e]}else this.data={}},DndSimulatorDataTransfer.prototype.setData=function(t,e){this.data[t]=e,this.items.push(e),this.types.push(t)},DndSimulatorDataTransfer.prototype.getData=function(t){return t in this.data?this.data[t]:\"\"},DndSimulatorDataTransfer.prototype.setDragImage=function(t,e,a){},DndSimulator={simulate:function(t,e){\"string\"==typeof t&&(t=document.querySelector(t)),\"string\"==typeof e&&(e=document.querySelector(e));var a=t.getBoundingClientRect(),n=e.getBoundingClientRect(),r=this.createEvent(\"mousedown\",{clientX:a.left,clientY:a.top});t.dispatchEvent(r);var i=this.createEvent(\"dragstart\",{clientX:a.left,clientY:a.top,dataTransfer:new DndSimulatorDataTransfer});t.dispatchEvent(i);var s=this.createEvent(\"drag\",{clientX:a.left,clientY:a.top});t.dispatchEvent(s);var o=this.createEvent(\"dragenter\",{clientX:n.left,clientY:n.top,dataTransfer:i.dataTransfer});e.dispatchEvent(o);var l=this.createEvent(\"dragover\",{clientX:n.left,clientY:n.top,dataTransfer:i.dataTransfer});e.dispatchEvent(l);var c=this.createEvent(\"drop\",{clientX:n.left,clientY:n.top,dataTransfer:i.dataTransfer});e.dispatchEvent(c);var d=this.createEvent(\"dragend\",{clientX:n.left,clientY:n.top,dataTransfer:i.dataTransfer});t.dispatchEvent(d);var f=this.createEvent(\"mouseup\",{clientX:n.left,clientY:n.top});e.dispatchEvent(f)},createEvent:function(t,e){var a=document.createEvent(\"CustomEvent\");a.initCustomEvent(t,!0,!0,null),a.view=window,a.detail=0,a.ctlrKey=!1,a.altKey=!1,a.shiftKey=!1,a.metaKey=!1,a.button=0,a.relatedTarget=null,e.clientX&&e.clientY&&(a.screenX=window.screenX+e.clientX,a.screenY=window.screenY+e.clientY);for(var n in e)a[n]=e[n];return a}};";
        ((JavascriptExecutor)driver).executeScript(strJS+"DndSimulator.simulate(arguments[0],arguments[1]);\n",elementDrag,elementDrop);
        Utils.wait(Constants.USER_TIMEOUT);
    }


    public static void printScreen(WebDriver driver, ExtentTest rep_logger, String screenPath) {
        File sourcePath = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File screenshootsPath = new File(screenPath);

        if (!screenshootsPath.exists()) {
            screenshootsPath.mkdir();
        }
        File destinationPath = new File(screenPath +Utils.now() + ".png");
        try {
            Files.copy(sourcePath, destinationPath);
            rep_logger.addScreenCaptureFromPath(destinationPath.getAbsolutePath());
        } catch (Exception e1) {
            rep_logger.log(Status.INFO,"printScreen() - "+e1.getMessage());
        }

    }

    public static boolean isChecked(WebElement check) {
        return check.getAttribute("checked") != null;
    }

    public static void checkElement(WebElement check) {
        boolean isChecked = isChecked(check);
        if (isChecked) return;
        check.click();
    }

    public static void clear(WebElement element){
        element.clear();
    }

    public static void sendText(WebElement element, String text){
        clear(element);
        element.sendKeys(text);
    }

    public static void doubleClick(WebDriver driver,WebElement element){
        Actions actions = new Actions(driver);
        actions.doubleClick(element).perform();
    }
}
