package framework.tests;

import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import framework.utils.Utils;
import framework.utils.GetProperties;
import framework.utils.WebDriverUtils;
import framework.utils.datatypes.BrowserType;
import framework.utils.datatypes.Wait;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.lang.reflect.Method;

public class BaseTest {

    private static final Logger logger = LogManager.getLogger(BaseTest.class);
    private final String screenPath = System.getProperty("user.dir") + "/test-report/img/";

    @BeforeSuite
    public void setContext(ITestContext context){

        //Propertie Config
        GetProperties properties = new GetProperties("config");

        context.setAttribute("properties",properties);

        //Report Config
        ExtentHtmlReporter htmlReports = new  ExtentHtmlReporter(System.getProperty("user.dir") + "/test-report/report.html");
        htmlReports.setAppendExisting(true);
        htmlReports.config().setDocumentTitle("Automation Results");
        htmlReports.config().setReportName("Regression Testing");
        htmlReports.config().setTheme(Theme.STANDARD);
        htmlReports.config().setTestViewChartLocation(ChartLocation.BOTTOM);
        htmlReports.config().setEncoding("UTF-8");
        ExtentReports extent = new ExtentReports();
        extent.attachReporter(htmlReports);

        context.setAttribute("extent",extent);

    }

    @Parameters("headlessM")
    @BeforeMethod
    public void setUp(ITestContext context,Method method, String headlessM) {

        if(method.getName().contains("ui_")){

            GetProperties properties = ((GetProperties) context.getAttribute("properties"));

            String browser = properties.getString("BROWSER").toUpperCase();
            String headless = properties.getString("HEADLESS").toUpperCase();

            BrowserType browserType = BrowserType.valueOf(browser);
            FirefoxOptions ff_options;
            ChromeOptions chOptions;
            WebDriver driver = null;

            switch (browserType) {

                case FIREFOX:

                    if(System.getProperty("os.name").toLowerCase().contains("win")){
                        logger.info("Firefox Driver on Windows");
                        System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "/driver/win/firefox/geckodriver.exe");
                    }else{
                        logger.info("Firefox Driver on Linux");
                        System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "/driver/linux/firefox/geckodriver");
                    }
                    ff_options = new FirefoxOptions();

                    if (headless.equalsIgnoreCase("y") || headlessM.equalsIgnoreCase("y")){
                        ff_options.addArguments("--headless");
                        logger.info("headless mode on");
                    }

                    driver = new FirefoxDriver(ff_options);
                    driver.manage().window().maximize();

                break;

                case CHROME:

                    if(System.getProperty("os.name").toLowerCase().contains("win")){
                        logger.info("Chrome Driver on Windows");
                        System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir") + "/driver/win/chrome/chromedriver.exe");
                    }else{
                        logger.info("Chrome Driver on Linux");
                        System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "/driver/linux/chrome/chromedriver");
                    }

                    chOptions = new ChromeOptions();

                    chOptions.addArguments("--disable-extensions");
                    chOptions.addArguments("--start-maximized");
                    chOptions.addArguments("--disable-popup-blocking");
                    chOptions.addArguments("--disable-infobars");

                    if (headless.equalsIgnoreCase("y") || headlessM.equalsIgnoreCase("y")){
                        chOptions.addArguments("--headless");
                        logger.info("headless mode on");
                    }

                driver = new ChromeDriver(chOptions);

                break;

            }

            context.setAttribute("driver",driver);
            logger.info("Starting UI Test");
            navigateLogin(driver,properties);
        }else{
            logger.info("Starting API Test");
        }

    }

    @AfterMethod
    public void checkResults(ITestContext context,
                             ITestResult testResults,
                             Method method){

        ExtentReports extent = ((ExtentReports) context.getAttribute("extent"));
        ExtentTest rep_logger = ((ExtentTest) context.getAttribute("rep_logger"));

        if(method.getName().contains("ui_")){

            WebDriver driver = ((WebDriver) context.getAttribute("driver"));

            if(testResults.getStatus()==ITestResult.SUCCESS){
                Utils.logMsg(rep_logger,logger,Status.PASS,"Test Success");
            }else{
                Utils.logMsg(rep_logger,logger,Status.FAIL,"Test Failed");
                WebDriverUtils.printScreen(driver,rep_logger,screenPath);
                rep_logger.log(Status.FAIL, testResults.getThrowable());
            }
            driver.quit();

        }else{
            if(testResults.getStatus()==ITestResult.SUCCESS){
                Utils.logMsg(rep_logger,logger,Status.PASS,"Test Success");
            }else{
                Utils.logMsg(rep_logger,logger,Status.FAIL,"Test Failed");
                rep_logger.log(Status.FAIL, testResults.getThrowable());
            }
        }

        extent.flush();

    }

    @AfterTest
    public void tearDown(){}

    private void navigateLogin(WebDriver driver,GetProperties properties) {

        //Defining initial wait times for all elements
        WebDriverUtils.addWait(driver, Wait.MEDIUM_WAIT);

        //Setting base url
        String BASE_URL = properties.getString("BASE_URL");

        //calling url
        driver.get(BASE_URL);
    }


}
