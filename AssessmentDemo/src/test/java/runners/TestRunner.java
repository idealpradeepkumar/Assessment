package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"stepDefinitions", "hooks"},
        plugin = {"pretty", "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm", 
        		"html:allure-results/result.html", 
        		"json:allure-results/result.json"}
	)

public class TestRunner extends AbstractTestNGCucumberTests {
	
}
