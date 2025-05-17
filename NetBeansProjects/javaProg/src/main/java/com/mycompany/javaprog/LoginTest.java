/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javaprog;

/**
 *
 * @author mbare
 */
public class LoginTest {
      public static void main(String[] args) {
        // Path to ChromeDriver
        System.setProperty("webdriver.chrome.driver", "C:/path/to/chromedriver.exe");

        // Launch Chrome browser
        WebDriver driver = new ChromeDriver();

        // Step 1: Open the login page
        driver.get("https://the-internet.herokuapp.com/login");

        // Step 2: Enter username
        WebElement username = driver.findElement(By.id("username"));
        username.sendKeys("tomsmith");

        // Step 3: Enter password
        WebElement password = driver.findElement(By.id("password"));
        password.sendKeys("SuperSecretPassword!");

        // Step 4: Click login button
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        loginButton.click();

        // Step 5: Check for success message
        WebElement message = driver.findElement(By.id("flash"));
        if (message.getText().contains("You logged into a secure area!")) {
            System.out.println(" Login Test Passed!");
        } else {
            System.out.println(" Login Test Failed. ");
        }

        // Close the browser
        driver.quit();
    }
    
    
    
}
