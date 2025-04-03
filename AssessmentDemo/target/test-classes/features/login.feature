Feature: Login and add highest priced item to cart

  Scenario Outline: User adds the highest price item to cart
    Given User navigates to SauceDemo login page
    When User logs in with username "<username>" and password "<password>"
    And User selects the highest price item
    Then The item should be added to the cart successfully

    Examples:
      | username      | password      |
      | standard_user | secret_sauce  |