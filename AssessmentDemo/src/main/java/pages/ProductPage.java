package pages;

import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ProductPage {
    WebDriver driver;

    @FindBy(className = "inventory_item_price")
    private List<WebElement> productPrices;

    @FindBy(xpath = "//button[contains(text(),'Add to cart')]")
    private List<WebElement> addToCartButtons;

    @FindBy(xpath = "//a[@class='shopping_cart_link']")
    private WebElement cartButton;

    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(className = "inventory_item_name")
    private List<WebElement> productNames;

    @FindBy(className = "inventory_item_name")
    private List<WebElement> cartItemNames;

    @FindBy(className = "inventory_item_price")
    private List<WebElement> cartItemPrices;

    private int highestPriceIndex = -1; 

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Gets the name of the highest-priced item before adding to cart.
     */
    public String getHighestPriceItemName() {
        if (highestPriceIndex == -1) {
            findHighestPriceItemIndex();
        }
        return productNames.get(highestPriceIndex).getText();
    }

    /**
     * Gets the price of the highest-priced item before adding to cart.
     */
    public String getHighestPriceItemPrice() {
        if (highestPriceIndex == -1) {
            findHighestPriceItemIndex();
        }
        return productPrices.get(highestPriceIndex).getText();
    }

    /**
     * Selects and adds the highest-priced item to the cart.
     */
    public void addHighestPriceItemToCart() {
        if (productPrices.isEmpty() || addToCartButtons.isEmpty()) {
            throw new RuntimeException("No products found!");
        }

        findHighestPriceItemIndex();

        if (highestPriceIndex != -1) {
            System.out.println("Adding highest-priced item: " + getHighestPriceItemName() + " (" + getHighestPriceItemPrice() + ")");
            addToCartButtons.get(highestPriceIndex).click();
        } else {
            throw new RuntimeException(" Could not determine the highest-priced item.");
        }
    }

    /**
     * Finds the index of the highest-priced item.
     */
    private void findHighestPriceItemIndex() {
        double highestPrice = 0.0;
        highestPriceIndex = -1;

        for (int i = 0; i < productPrices.size(); i++) {
            String priceText = productPrices.get(i).getText().replace("$", "").trim();
            double price = Double.parseDouble(priceText);

            if (price > highestPrice) {
                highestPrice = price;
                highestPriceIndex = i;
            }
        }

        if (highestPriceIndex == -1) {
            throw new RuntimeException(" No valid highest-priced item found.");
        }
    }

    /**
     * Navigates to the shopping cart page using JavaScript Executor.
     */
    public void goToCart() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", cartButton);
    }

    /**
     * Checks if an item has been successfully added to the cart.
     */
    public boolean isItemAddedToCart() {
        return cartBadge.isDisplayed();
    }

    /**
     * Verifies that the highest-priced item is present in the cart.
     */
    public boolean verifyHighestPricedItemInCart(String expectedItemName, String expectedItemPrice) {
        for (int i = 0; i < cartItemNames.size(); i++) {
            String cartItemText = cartItemNames.get(i).getText().trim();
            String cartItemPrice = cartItemPrices.get(i).getText().trim();

            if (cartItemText.equals(expectedItemName) && cartItemPrice.equals(expectedItemPrice)) {
                return true;  // return true if found the correct item in the cart
            }
        }
        return false;  // return as false if the item not found in the cart
    }
}