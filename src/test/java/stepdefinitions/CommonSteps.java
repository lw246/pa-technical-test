package stepdefinitions;

import cucumber.api.java.en.*;
import general.CommonHelpers;
import java.io.InputStream;
import org.apache.http.client.fluent.Request;

import static org.junit.Assert.*;

public class CommonSteps {

    private World world;

    public CommonSteps(World world) {
        this.world = world;
    }

    @Given("^I'm using the API on url \"([^\"]*)\"$")
    public void imUsingTheApiOnUrl(String url) throws Throwable {
        world.baseUrl = url;
    }

    @And("^The response body should read \"([^\"]*)\"$")
    public void theResponseBodyShouldRead(String expectedBodyText) throws Throwable {
        expectedBodyText += "\n";
        InputStream content = world.httpResponse.getEntity().getContent();

        CommonHelpers commonHelpers = new CommonHelpers();
        String bodyText = commonHelpers.convertStreamToString(content);

        assertEquals(expectedBodyText, bodyText);
    }

    @And("^The response body should be empty$")
    public void theResponseBodyShouldBeEmpty() throws Throwable {
        InputStream content = world.httpResponse.getEntity().getContent();

        CommonHelpers commonHelpers = new CommonHelpers();
        String httpBody = commonHelpers.convertStreamToString(content);
        assertEquals("", httpBody);
    }

    @And("^The response should have no content$")
    public void theResponseShouldHaveNoContent() throws Throwable {
        assertEquals(world.httpResponse.getStatusLine().getReasonPhrase(), "No Content");
    }

    @When("^I make a GET request to \"([^\"]*)\"$")
    public void iMakeAGetRequestTo(String method) throws Throwable {
        world.httpResponse = Request.Get(world.baseUrl + method)
                .execute()
                .returnResponse();
    }

    @When("^I make a DELETE request to \"([^\"]*)\"$")
    public void iMakeADeleteRequestTo(String method) throws Throwable {
        world.httpResponse = Request.Delete(world.baseUrl + method)
                .execute()
                .returnResponse();
    }

    @Then("^The response code should be (\\d+)$")
    public void theResponseCodeShouldBe(int expectedResponseCode) throws Throwable {
        int statusCode = world.httpResponse.getStatusLine().getStatusCode();
        assertEquals(expectedResponseCode , statusCode);
    }
}
