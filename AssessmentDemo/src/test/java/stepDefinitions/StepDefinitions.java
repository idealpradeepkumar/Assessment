package stepDefinitions;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import hooks.Hooks;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.LoginPage;
import pages.ProductPage;

public class StepDefinitions {
    WebDriver driver;
    LoginPage loginPage;
    ProductPage productPage;

    private String selectedItemName;
    private String selectedItemPrice;

    public StepDefinitions() {
        this.driver = Hooks.getDriver();
        loginPage = new LoginPage(driver);
        productPage = new ProductPage(driver);
    }

    @Given("User navigates to SauceDemo login page")
    public void navigateToLoginPage() {
        driver.get("https://www.saucedemo.com/");
    }

    @When("User logs in with username {string} and password {string}")
    public void login(String user, String pass) {
        loginPage.login(user, pass);
    }

    @When("User selects the highest price item")
    public void selectHighestPriceItem() throws InterruptedException {
        Thread.sleep(5000);
        
        // Get the highest-priced item details before adding
        selectedItemName = productPage.getHighestPriceItemName();
        selectedItemPrice = productPage.getHighestPriceItemPrice();
        System.out.println("selectedItemName : "+ selectedItemName);
        System.out.println("selectedItemPrice : " + selectedItemPrice);
        productPage.addHighestPriceItemToCart();
    }

    @Then("The item should be added to the cart successfully")
    public void verifyItemAdded() throws InterruptedException {
        Thread.sleep(3000);
        productPage.goToCart();
        Thread.sleep(2000);

        boolean isAdded = productPage.isItemAddedToCart();
        Assert.assertTrue(isAdded, "The item was NOT added to the cart!");

        // Verify the exact item in the cart
        boolean isCorrectItemAdded = productPage.verifyHighestPricedItemInCart(selectedItemName, selectedItemPrice);
        Assert.assertTrue(isCorrectItemAdded, "The wrong item was added to the cart!");

        System.out.println(" The highest-priced item '" + selectedItemName + "' (" + selectedItemPrice + ") was successfully added to the cart!");
    }
}