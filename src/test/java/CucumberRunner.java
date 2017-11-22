import cucumber.api.CucumberOptions;
import cucumber.api.junit.*;
import org.junit.runner.RunWith;
@RunWith(Cucumber.class)
@CucumberOptions(features = {"src/test/feature-files"},
        format = {"pretty",
                "json:target/cucumber.json",
                "html:target/site/cucumber-pretty"},
        tags = {"~@ignore"})

public class CucumberRunner {
}
