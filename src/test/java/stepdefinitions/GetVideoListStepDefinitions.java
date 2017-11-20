package stepdefinitions;
import cucumber.api.PendingException;
import cucumber.api.java.en.*;
import models.Video;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.junit.Assert;
import general.*;
import org.junit.Before;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class GetVideoListStepDefinitions {

    private String baseUrl;
    private HttpResponse httpResponse;
    private VideoHelpers videoHelpers = new VideoHelpers();
    private String songId;

    @Before
    public void ClearTestData() throws IOException {
        videoHelpers.ClearOutSongDatabase();
    }

    @Given("^I have no videos in the database$")
    public void i_have_no_videos_in_the_database() throws Throwable {
        videoHelpers.ClearOutSongDatabase();
    }

    @Given("^I'm using the API on url \"([^\"]*)\"$")
    public void im_using_the_api_on_url(String url) throws Throwable {
        baseUrl = url;
    }

    @When("^I make a GET request to \"([^\"]*)\"$")
    public void i_make_a_GET_request_to(String endpoint) throws Throwable {
        httpResponse = Request.Get(baseUrl + endpoint)
                .execute()
                .returnResponse();
    }

    @Then("^An Empty JSON array is returned$")
    public void an_Empty_JSON_array_is_returned() throws Throwable {
        List<Video> videoList = videoHelpers.BuildVideoListFromHttpResponse(httpResponse);
        Assert.assertTrue(videoList.isEmpty());
    }

    @Then("^The response code should be (\\d+)$")
    public void the_response_code_should_be(int expectedResponseCode) throws Throwable {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        Assert.assertEquals(expectedResponseCode , statusCode);
    }

    @And("^I have a single video in the database$")
    public void IHaveASingleVideoInTheDatabase() throws Throwable {
        String testData = "[{\"artist\": \"Forest Swords\",\"song\": \"War It\",\"publishDate\": \"2017-09-01\"}]";
        videoHelpers.AddTestData(testData);
    }

    @Then("^A JSON Array should be returned with a single result$")
    public void AJSONArrayShouldBeReturnedWithASingleResult() throws Throwable {
        ArrayList<Video> videos = videoHelpers.BuildVideoListFromHttpResponse(httpResponse);
        Assert.assertEquals(1, videos.size());
    }

    @And("^I have multiple videos in the database$")
    public void IHaveMultipleVideosInTheDatabase() throws Throwable {
        String testData = videoHelpers.GetTestDataFromResource("SongData.json");
        videoHelpers.AddTestData(testData);
    }

    @Then("^A JSON Array should be returned with multiple results$")
    public void AJSONArrayShouldBeReturnedWithMultipleResults() throws Throwable {
        ArrayList<Video> videos = videoHelpers.BuildVideoListFromHttpResponse(httpResponse);
        Assert.assertTrue(videos.size() > 1);
    }

    @And("^I have a single video in the database with an artist named \"([^\"]*)\"$")
    public void IHaveASingleVideoInTheDatabaseWithAnArtistNamed(String artist) throws Throwable {
        String testData = "[{\"artist\": "+ artist + ",\"song\": \"War It\",\"publishDate\": \"2017-09-01\"}]";
        videoHelpers.AddTestData(testData);
    }


    @And("^I have acquired the Id for a song$")
    public void IHaveAcquiredTheIdForASong() throws Throwable {
        HttpResponse allSongsResponse = Request.Get(baseUrl + "/video")
                                                .execute()
                                                .returnResponse();
        ArrayList<Video> videos = videoHelpers.BuildVideoListFromHttpResponse(allSongsResponse);
        songId = videos.get(0).Id;
    }


    @When("^I make a GET request to \"([^\"]*)\"/SongId$")
    public void IMakeAGETRequestToSongId(String endpoint) throws Throwable {
        httpResponse = Request.Get(baseUrl + endpoint + "/" + songId)
                .execute()
                .returnResponse();
    }

    @And("^The Songs ID should match that which was passed in the request$")
    public void theSongsIDShouldMatchThatWhichWasPassedInTheRequest() throws Throwable {
        Video video = videoHelpers.BuildVideoObjectFromHTTPResponse(httpResponse);
        Assert.assertEquals(songId, video.Id);
    }

    @Then("^A single JSON object should be returned$")
    public void ASingleJSONObjectShouldBeReturned() throws Throwable {
        Video video = videoHelpers.BuildVideoObjectFromHTTPResponse(httpResponse);
        Assert.assertNotNull(video);
    }

    @Then("^The \"([^\"]*)\" property should be present in all the JSON objects returned$")
    public void thePropertyShouldBePresentInTheJSONObjectsReturned(String propertyName) throws Throwable {
        ArrayList<Video> videos = videoHelpers.BuildVideoListFromHttpResponse(httpResponse);

        String classProperty = videoHelpers.GetVideoClassPropertyFromJsonProperty(propertyName);
        // This is probably overly complicated, however I'm also using this as a learning experience as it's fun!
        for (Video video: videos){
            Class<?> c = video.getClass();
            Field f = c.getDeclaredField(classProperty);
            f.setAccessible(true);
            Assert.assertNotNull(f.get(video));
        }
    }

    @And("^The response body should be empty$")
    public void theResponseBodyShouldBeEmpty() throws Throwable {
        InputStream content = httpResponse.getEntity().getContent();
        String httpBody = videoHelpers.ConvertStreamToString(content);
        Assert.assertEquals("", httpBody);

    }

    @And("^The \"([^\"]*)\" property should be present in the JSON object returned$")
    public void thePropertyShouldBePresentInTheJSONObjectReturned(String propertyName) throws Throwable {
        Video video = videoHelpers.BuildVideoObjectFromHTTPResponse(httpResponse);
        String classProperty = videoHelpers.GetVideoClassPropertyFromJsonProperty(propertyName);

        Class<?> c = video.getClass();
        Field f = c.getDeclaredField(classProperty);
        f.setAccessible(true);
        Assert.assertNotNull(f.get(video));
    }
}
