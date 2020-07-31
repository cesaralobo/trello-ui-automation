package framework.utils;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.google.gson.*;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class Utils {

  private static final String loader = ".v-loading-indicator";

  public static String applyDefaultIfMissing(String variable, String defaultValue) {
    if (variable == null) {
      variable = defaultValue;
      System.out.println("Default " + defaultValue + " execution was applied since was not provided");
    }

    return variable;
  }

  public static String today() {
    Date today = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    return dateFormat.format(today);
  }

  public static String now() {
    Date today = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
    return dateFormat.format(today);
  }

  public static String nowWS() {
    Date today = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    return dateFormat.format(today);
  }

  private static void waitForPageLoadComplete(WebDriver driver) throws TimeoutException{
    WebDriverWait waitPageLoad = new WebDriverWait(driver,Constants.PAGELOAD_TIMEOUT);
    waitPageLoad.until(driver1 -> String.valueOf(((JavascriptExecutor) driver1).executeScript("return document.readyState")).equals("complete"));
    waitPageLoad.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(By.tagName("html"))));
  }

  public static WebElement waitUntilLoadElement(WebDriver driver, String locator,int... timeoutArgs) throws TimeoutException{

    WebElement element;

    Utils.waitForPageLoadComplete(driver);

    int timeout = Constants.MEDIUM_TIMEOUT;

    if(timeoutArgs.length > 0){
      timeout =  timeoutArgs[0];
    }

    WebDriverWait waitLoaderVisible = new WebDriverWait(driver,timeout);

    element = waitLoaderVisible.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(Utils.getLocatorBy(locator))));

    return element;

  }

  public static WebElement waitUntilElementVisible(WebDriver driver, String locator,int... timeoutArgs) throws TimeoutException{

    WebElement element;

    Utils.waitForPageLoadComplete(driver);

    int timeout = Constants.PAGELOAD_TIMEOUT;

    if(timeoutArgs.length > 0){
      timeout =  timeoutArgs[0];
    }

    WebDriverWait waitLoaderVisible = new WebDriverWait(driver,timeout);

    waitLoaderVisible.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(Utils.getLocatorBy(locator))));
    element = waitLoaderVisible.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(Utils.getLocatorBy(locator))));

    return element;

  }

  public static List<WebElement> waitUntilLoadElements(WebDriver driver, String locator, int... timeoutArgs) throws TimeoutException{

    List<WebElement> elements;

    Utils.waitForPageLoadComplete(driver);

    int timeout = Constants.PAGELOAD_TIMEOUT;

    if(timeoutArgs.length > 0){
      timeout =  timeoutArgs[0];
    }

    WebDriverWait waitLoaderVisible = new WebDriverWait(driver,timeout);

    elements =waitLoaderVisible.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfAllElementsLocatedBy(Utils.getLocatorBy(locator))));

    return elements;

  }

  public static WebElement waitUntilElementClickable(WebDriver driver, String locator,int... timeoutArgs) throws TimeoutException{

    WebElement element;

    Utils.waitForPageLoadComplete(driver);

    int timeout = Constants.PAGELOAD_TIMEOUT;

    if(timeoutArgs.length > 0){
      timeout =  timeoutArgs[0];
    }

    WebDriverWait waitLoaderVisible = new WebDriverWait(driver,timeout);
    element = waitLoaderVisible.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(Utils.getLocatorBy(locator))));

    return element;

  }

  public static void logMsg(ExtentTest rep_logger, Logger logger,Status status, String msj) {
    rep_logger.log(status,msj);
    logger.info(msj);
  }

  public static void wait(int time) {
    try {
      Thread.sleep(time * 1000);
    } catch (Exception e) {

    }

  }

  private static By getLocatorBy(String locator) {

    By byLocator = null;
    if (locator.substring(0,2).equalsIgnoreCase("//")){
      byLocator = By.xpath(locator);
    }else{
      byLocator = By.cssSelector(locator);
    }

    return byLocator;

  }

  public static NodeList getNodeList(String xmlStr, String xpathStr) throws XPathExpressionException{

    //Create XPath
    XPathFactory xpathfactory = XPathFactory.newInstance();
    XPath xpath = xpathfactory.newXPath();
    xpath.setNamespaceContext(new MDMNamespacesContext());

    return (NodeList) xpath.evaluate(xpathStr, new InputSource( new StringReader( xmlStr )), XPathConstants.NODESET);

  }

  public static String getXMLTextElementByXpath(String xmlStr, String xpathStr) throws XPathExpressionException{

    //Create XPath
    XPathFactory xpathfactory = XPathFactory.newInstance();
    XPath xpath = xpathfactory.newXPath();
    xpath.setNamespaceContext(new MDMNamespacesContext());

    return (String) xpath.evaluate(xpathStr, new InputSource( new StringReader( xmlStr )), XPathConstants.STRING);

  }

  public static Boolean evaluateByXpath(String xmlStr, String xpathStr) throws XPathExpressionException{

    //Create XPath
    XPathFactory xpathfactory = XPathFactory.newInstance();
    XPath xpath = xpathfactory.newXPath();
    xpath.setNamespaceContext(new MDMNamespacesContext());

    return (Boolean) xpath.evaluate(xpathStr, new InputSource( new StringReader( xmlStr )), XPathConstants.BOOLEAN);

  }

  public static <T> Object[][]  getEscenariosJson(String jsonFile){

    Object[][] objArr = null;
    Gson gson = new GsonBuilder().create();

    String jsonStr = null;

    try {
      jsonStr = new String(Files.readAllBytes(Paths.get(jsonFile)));
    } catch (IOException e) {
      e.printStackTrace();
    }

    JsonElement element = gson.fromJson (jsonStr, JsonElement.class);

    if (element.isJsonObject()){
      if (element.isJsonObject()) {
        JsonObject mainObject = element.getAsJsonObject();
        JsonArray jsonArray = mainObject.getAsJsonArray("Cases");
        if (jsonArray.isJsonArray()){

          objArr = new Object[jsonArray.size()][1];
          Iterator jsonArrIT = jsonArray.iterator();

          while (jsonArrIT.hasNext()){
            for (int i = 0; i < objArr.length; ++i) {
              for(int j = 0; j < objArr[i].length; ++j) {
                JsonElement jsonElement = (JsonElement)jsonArrIT.next();
                objArr[i][j] = gson.fromJson(jsonElement, Map.class);
              }
            }
          }
        }
      }
    }
    return objArr;

  }

}

