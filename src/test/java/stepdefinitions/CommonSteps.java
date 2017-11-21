package stepdefinitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import general.CommonHelpers;
import org.apache.http.client.fluent.Request;
import org.junit.Assert;

import java.io.InputStream;

public class CommonSteps {

    private World world;

    public CommonSteps(World world) {
        this.world = world;

    }

    @When("^I make a GET request to \"([^\"]*)\"$")
    public void i_make_a_GET_request_to(String endpoint) throws Throwable {
        world.httpResponse = Request.Get(world.baseUrl + endpoint)
                .execute()
                .returnResponse();
    }

    @Given("^I'm using the API on url \"([^\"]*)\"$")
    public void im_using_the_api_on_url(String url) throws Throwable {
        world.baseUrl = url;
    }

    @And("^The response body should read \"([^\"]*)\"$")
    public void TheResponseBodyShouldRead(String expectedBodyText) throws Throwable {
        expectedBodyText += "\n";
        InputStream content = world.httpResponse.getEntity().getContent();

        CommonHelpers commonHelpers = new CommonHelpers();
        String bodyText = commonHelpers.ConvertStreamToString(content);

        Assert.assertEquals(expectedBodyText, bodyText);
    }

    @And("^The response body should be empty$")
    public void theResponseBodyShouldBeEmpty() throws Throwable {
        InputStream content = world.httpResponse.getEntity().getContent();

        CommonHelpers commonHelpers = new CommonHelpers();
        String httpBody = commonHelpers.ConvertStreamToString(content);
        Assert.assertEquals("", httpBody);
    }
}
