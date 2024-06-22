package frontend;

import org.junit.jupiter.api.Tag;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.Alert;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.nio.file.Paths;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = org.example.Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("frontend")
public class LoginTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        // Set the path to the chromedriver executable
        System.setProperty("webdriver.chrome.driver", "drivers/Chrome/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-web-security"); // Disable web security
        options.addArguments("--allow-running-insecure-content"); // Allow running insecure content
        options.addArguments("--disable-features=IsolateOrigins,site-per-process"); // Disable site isolation
        driver = new ChromeDriver(options);
    }

    @Test
    public void testLoginInvalidAccount() {
        // Construct the file URL for the local HTML file
        String fileUrl = Paths.get("src/main/java/org/example/frontend/Login/Login.html").toUri().toString();
        // Open the local HTML file
        driver.get(fileUrl);

        // Find the username field and enter the username
        WebElement usernameField = driver.findElement(By.id("login-username"));
        usernameField.sendKeys("invalidUsername");

        // Find the password field and enter the password
        WebElement passwordField = driver.findElement(By.id("login-password"));
        passwordField.sendKeys("invalidPassword");

        // Find the login button and click it
        WebElement loginButton = driver.findElement(By.id("subBtn"));
        loginButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        // Verify the alert text
        String alertText = alert.getText();
        assertEquals("Invalid login credentials. Please try again.", alertText);
        // Accept the alert to close it
        alert.accept();
    }

    @Test
    public void testLoginValidStudentAccount() {
        // Construct the file URL for the local HTML file
        String fileUrl = Paths.get("src/main/java/org/example/frontend/Login/Login.html").toUri().toString();
        // Open the local HTML file
        driver.get(fileUrl);

        // Find the username field and enter the username
        WebElement usernameField = driver.findElement(By.id("login-username"));
        usernameField.sendKeys("braduvasile@tudelft.nl");

        // Find the password field and enter the password
        WebElement passwordField = driver.findElement(By.id("login-password"));
        passwordField.sendKeys("text");

        // Find the login button and click it
        WebElement loginButton = driver.findElement(By.id("subBtn"));
        loginButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlToBe("src/main/java/org/example/frontend/Student/Student.html"));
        // Verify the login was successful by checking the URL or an element on the page
        String expectedUrl = "src/main/java/org/example/frontend/Student/Student.html";
        assertEquals(expectedUrl, driver.getCurrentUrl());
    }

    @Test
    public void testLoginValidSupervisorAccount() {
        // Construct the file URL for the local HTML file
        String fileUrl = Paths.get("src/main/java/org/example/frontend/Login/Login.html").toUri().toString();
        // Open the local HTML file
        driver.get(fileUrl);

        // Find the username field and enter the username
        WebElement usernameField = driver.findElement(By.id("login-username"));
        usernameField.sendKeys("braduvasile@tudelft.nl");

        // Find the password field and enter the password
        WebElement passwordField = driver.findElement(By.id("login-password"));
        passwordField.sendKeys("text");

        // Find the login button and click it
        WebElement loginButton = driver.findElement(By.id("subBtn"));
        loginButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlToBe("src/main/java/org/example/frontend/Dashboard/Dashboard.html"));
        // Verify the login was successful by checking the URL or an element on the page
        String expectedUrl = "src/main/java/org/example/frontend/Dashboard/Dashboard.html";
        assertEquals(expectedUrl, driver.getCurrentUrl());
    }


    @AfterEach
    public void tearDown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }
}
