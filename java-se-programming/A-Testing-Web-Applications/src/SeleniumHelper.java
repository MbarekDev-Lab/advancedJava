import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SeleniumHelper {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public SeleniumHelper(WebDriver driver) {
        this.driver = driver;
        Duration defaultTimeout = Duration.ofSeconds(10); // You can change the default timeout here
        this.wait = new WebDriverWait(driver, defaultTimeout);
    }

    /**
     * Finds a single WebElement using the specified locator.
     *
     * @param locatorType The type of locator to use (e.g., "id", "name", "xpath").
     * @param locatorValue The value of the locator (e.g., "myElementId").
     * @return The WebElement found, or null if not found.
     */
    public WebElement findElement(String locatorType, String locatorValue) {
        By by = getBy(locatorType, locatorValue);
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (Exception e) {
            System.err.println("Error finding element by " + locatorType + ": " + locatorValue);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Finds a list of WebElements using the specified locator.
     *
     * @param locatorType The type of locator to use (e.g., "class", "css", "xpath").
     * @param locatorValue The value of the locator.
     * @return A List of WebElements found, or an empty list if none are found.
     */
    public List<WebElement> findElements(String locatorType, String locatorValue) {
        By by = getBy(locatorType, locatorValue);
        try {
            return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
        } catch (Exception e) {
            System.err.println("Error finding elements by " + locatorType + ": " + locatorValue);
            e.printStackTrace();
            return List.of(); // Return an empty list
        }
    }

    /**
     * Clicks on a WebElement.
     *
     * @param element The WebElement to click.
     * @return True if the click was successful, false otherwise.
     */
    public boolean click(WebElement element) {
        if (element == null) {
            System.err.println("Cannot click: Element is null");
            return false;
        }
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
            return true;
        } catch (Exception e) {
            System.err.println("Error clicking element");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Enters text into an input field.
     *
     * @param element The WebElement (input field).
     * @param text The text to enter.
     * @return True if the text was entered successfully, false otherwise.
     */
    public boolean sendKeys(WebElement element, String text) {
        if (element == null) {
            System.err.println("Cannot send keys: Element is null");
            return false;
        }
        try {
            wait.until(ExpectedConditions.visibilityOf(element)).sendKeys(text);
            return true;
        } catch (Exception e) {
            System.err.println("Error sending keys to element");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets the text content of a WebElement.
     *
     * @param element The WebElement.
     * @return The text content, or null if the element is null.
     */
    public String getText(WebElement element) {
        if (element == null) {
            System.err.println("Cannot get text: Element is null");
            return null;
        }
        try {
            return wait.until(ExpectedConditions.visibilityOf(element)).getText();
        } catch (Exception e) {
            System.err.println("Error getting text from element");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the value of an attribute of a WebElement.
     *
     * @param element The WebElement.
     * @param attributeName The name of the attribute.
     * @return The attribute value, or null if the element or attribute is not found.
     */
    public String getAttribute(WebElement element, String attributeName) {
        if (element == null) {
            System.err.println("Cannot get attribute: Element is null");
            return null;
        }
        try {
            return wait.until(ExpectedConditions.visibilityOf(element)).getAttribute(attributeName);
        } catch (Exception e) {
            System.err.println("Error getting attribute from element");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Submits a form.
     *
     * @param element An element within the form.
     * @return True if the form was submitted successfully, false otherwise.
     */
    public boolean submit(WebElement element) {
        if (element == null) {
            System.err.println("Cannot submit: Element is null");
            return false;
        }
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).submit();
            return true;
        } catch (Exception e) {
            System.err.println("Error submitting form");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if a WebElement is selected.
     *
     * @param element The WebElement.
     * @return True if selected, false otherwise, or false if the element is null.
     */
    public boolean isSelected(WebElement element) {
        if (element == null) {
            System.err.println("Cannot check selection: Element is null");
            return false;
        }
        try {
            return wait.until(ExpectedConditions.visibilityOf(element)).isSelected();
        } catch (Exception e) {
            System.err.println("Error checking if element is selected");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if a WebElement is displayed.
     *
     * @param element The WebElement.
     * @return True if displayed, false otherwise, or false if the element is null.
     */
    public boolean isDisplayed(WebElement element) {
        if (element == null) {
            System.err.println("Cannot check display: Element is null");
            return false;
        }
        try {
            return wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
        } catch (Exception e) {
            System.err.println("Error checking if element is displayed");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if a WebElement is enabled.
     *
     * @param element The WebElement.
     * @return True if enabled, false otherwise, or false if the element is null.
     */
    public boolean isEnabled(WebElement element) {
        if (element == null) {
            System.err.println("Cannot check enabled: Element is null");
            return false;
        }
        try {
            return wait.until(ExpectedConditions.visibilityOf(element)).isEnabled();
        } catch (Exception e) {
            System.err.println("Error checking if element is enabled");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Navigates to a URL.
     *
     * @param url The URL to navigate to.
     */
    public void navigateTo(String url) {
        driver.navigate().to(url);
    }

    /**
     * Gets the title of the current page.
     *
     * @return The page title, or null if there's an error.
     */
    public String getTitle() {
        try {
            return driver.getTitle();
        } catch (Exception e) {
            System.err.println("Error getting page title");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates a By locator based on the locator type and value.
     *
     * @param locatorType The type of locator ("id", "name", "class", etc.).
     * @param locatorValue The value of the locator.
     * @return The By object, or null if the locator type is invalid.
     */
    private By getBy(String locatorType, String locatorValue) {
        return switch (locatorType.toLowerCase()) {
            case "id" -> By.id(locatorValue);
            case "name" -> By.name(locatorValue);
            case "classname", "class" -> By.className(locatorValue);
            case "tagname", "tag" -> By.tagName(locatorValue);
            case "linktext", "link" -> By.linkText(locatorValue);
            case "partiallinktext", "partiallink" -> By.partialLinkText(locatorValue);
            case "cssselector", "css" -> By.cssSelector(locatorValue);
            case "xpath" -> By.xpath(locatorValue);
            default -> {
                System.err.println("Invalid locator type: " + locatorType);
                yield null;
            }
        };
    }
}