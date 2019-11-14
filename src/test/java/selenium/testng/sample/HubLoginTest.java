package selenium.testng.sample;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.By.cssSelector;
import static org.testng.Assert.assertEquals;

public class HubLoginTest {

    private static final String HUB_URL = "https://snapengage-qa.appspot.com/signin?to=hub";
    private static final String USERNAME = "pedroalmodovar@test.com";
    private static final String PASSWORD = "1q2w3e";

    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30,TimeUnit.SECONDS);
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void shouldLoadHubPage() throws Exception {
        // open hub page
        driver.get(HUB_URL);

        // enter email
        WebElement emailBox = driver.findElement(By.name("email"));
        emailBox.sendKeys(USERNAME);

        // enter password
        WebElement passwordBox = driver.findElement(By.name("password"));
        passwordBox.sendKeys(PASSWORD);

        // click on sign in button
        WebElement signInButton = driver.findElement(By.name("Submit"));
        signInButton.click();

        // wait until the page loads
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("intro-box")));

        // assert page elements
        assertElementText(cssSelector("#intro-box > div.indexcss__StyledGreeting-sc-1lqr3q4-2.jXcJqq"), "Good night,\nPedro Almodovar!");
        assertElementText(cssSelector("div[data-testid='accordion-title-recent']"), "RECENT");
        assertElementText(cssSelector("div[data-testid='accordion-title-team-chat']"), "TEAM CHAT");
        assertElementText(cssSelector("div[data-testid='accordion-title-1:1']"), "1:1");

        // take screen shot
        File screenShot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenShot, new File("./src/test/resources/screenshots/screenshot.png"));
    }

    private void assertElementText(By selector, String expectedValue) {
        WebElement element = driver.findElement(selector);
        assertEquals(expectedValue, element.getText());
    }
}

