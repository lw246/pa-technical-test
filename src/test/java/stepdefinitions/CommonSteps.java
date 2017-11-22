package stepdefinitions;

import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.*;
import general.CommonHelpers;
import general.PlaylistHelpers;
import general.VideoHelpers;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.io.InputStream;
import static org.junit.Assert.*;

public class CommonSteps {

    private World world;

    public CommonSteps(World world) {
        this.world = world;
    }

    @When("^I make a GET request to \"([^\"]*)\"$")
    public void i_make_a_GET_request_to(String method) throws Throwable {
        world.httpResponse = Request.Get(world.baseUrl + method)
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

        assertEquals(expectedBodyText, bodyText);
    }

    @And("^The response body should be empty$")
    public void theResponseBodyShouldBeEmpty() throws Throwable {
        InputStream content = world.httpResponse.getEntity().getContent();

        CommonHelpers commonHelpers = new CommonHelpers();
        String httpBody = commonHelpers.ConvertStreamToString(content);
        assertEquals("", httpBody);
    }

    @Then("^The response code should be (\\d+)$")
    public void the_response_code_should_be(int expectedResponseCode) throws Throwable {
        int statusCode = world.httpResponse.getStatusLine().getStatusCode();
        assertEquals(expectedResponseCode , statusCode);
    }

    @And("^The response should have no content$")
    public void theResponseShouldHaveNoContent() throws Throwable {
        assertEquals(world.httpResponse.getStatusLine().getReasonPhrase(), "No Content");
    }

    @When("^I make a DELETE request to \"([^\"]*)\"$")
    public void iMakeADELETERequestTo(String method) throws Throwable {
        world.httpResponse = Request.Delete(world.baseUrl + method)
                .execute()
                .returnResponse();
    }
}
