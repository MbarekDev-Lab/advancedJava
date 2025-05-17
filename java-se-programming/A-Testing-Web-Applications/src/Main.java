import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\mbare\\Downloads\\chromedriver.exe");

        driver.get("https://www.https://www.beautywelt.de");

        System.setProperty("webdriver.chrome.driver", "C:/path/to/chromedriver.exe");

            WebElement username = driver.findElement(By.id("username"));
            username.sendKeys("userName");

            WebElement password = driver.findElement(By.id("password"));
            password.sendKeys("SecretPassword!");

            WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
            loginButton.click();

            //to Check for success message
            WebElement message = driver.findElement(By.id("flash"));

            if (message.getText().contains("You logged into a secure area!")) {
                System.out.println("Login Test success!");
            } else {
                System.out.println("Login Test Failed.");
            }

        SeleniumHelper helper = new SeleniumHelper(driver);
            driver.quit();


        // Find elements
        WebElement searchInput = helper.findElement("id", "search-input");
        WebElement myButton = helper.findElement("css", ".my-button");
        List<WebElement> productItems = helper.findElements("class", "product-item");

        // Perform actions
        helper.sendKeys(searchInput, "product name");
        helper.click(myButton);

        // Get information
        String pageTitle = helper.getTitle();
        String buttonText = helper.getText(myButton);
        String productHref = helper.getAttribute(productItems.getFirst(), "href");

        // Check element state
        boolean isButtonEnabled = helper.isEnabled(myButton);

        // Navigate
        helper.navigateTo("https://www.example.com");

    }

}