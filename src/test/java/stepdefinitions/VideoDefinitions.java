package stepdefinitions;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import general.CommonHelpers;
import general.VideoHelpers;
import models.Video;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class VideoDefinitions{

    private VideoHelpers videoHelpers;
    private World world;

    public VideoDefinitions(World world) {
        this.world = world;
        this.videoHelpers = new VideoHelpers(world.baseUrl);
    }

    @Before
    public void ClearTestData() throws IOException {
        videoHelpers.ClearOutVideoDatabase();
    }

    @Given("^I have no videos in the database$")
    public void i_have_no_videos_in_the_database() throws Throwable {
        videoHelpers.ClearOutVideoDatabase();
    }

    @Then("^An Empty JSON array is returned$")
    public void an_Empty_JSON_array_is_returned() throws Throwable {
        List<Video> videoList = videoHelpers.BuildVideoListFromHttpResponse(world.httpResponse);
        Assert.assertTrue(videoList.isEmpty());
    }

    @Then("^The response code should be (\\d+)$")
    public void the_response_code_should_be(int expectedResponseCode) throws Throwable {
        int statusCode = world.httpResponse.getStatusLine().getStatusCode();
        Assert.assertEquals(expectedResponseCode , statusCode);
    }

    @And("^I have a single video in the database$")
    public void IHaveASingleVideoInTheDatabase() throws Throwable {
        String testData = "[{\"artist\": \"Forest Swords\",\"song\": \"War It\",\"publishDate\": \"2017-09-01\"}]";
        videoHelpers.AddTestData(testData);
    }

    @Then("^A JSON Array should be returned with a single result$")
    public void AJSONArrayShouldBeReturnedWithASingleResult() throws Throwable {
        ArrayList<Video> videos = videoHelpers.BuildVideoListFromHttpResponse(world.httpResponse);
        Assert.assertEquals(1, videos.size());
    }

    @And("^I have multiple videos in the database$")
    public void IHaveMultipleVideosInTheDatabase() throws Throwable {
        CommonHelpers commonHelpers = new CommonHelpers();
        String testData = commonHelpers.GetTestDataFromResource("VideosTestData.json");
        videoHelpers.AddTestData(testData);
    }

    @Then("^A JSON Array should be returned with multiple results$")
    public void AJSONArrayShouldBeReturnedWithMultipleResults() throws Throwable {
        ArrayList<Video> videos = videoHelpers.BuildVideoListFromHttpResponse(world.httpResponse);
        Assert.assertTrue(videos.size() > 1);
    }

    @And("^I have a single video in the database with an artist named \"([^\"]*)\"$")
    public void IHaveASingleVideoInTheDatabaseWithAnArtistNamed(String artist) throws Throwable {
        String testData = "[{\"artist\": "+ artist + ",\"song\": \"War It\",\"publishDate\": \"2017-09-01\"}]";
        videoHelpers.AddTestData(testData);
    }

    @And("^I have acquired the Id for a song$")
    public void IHaveAcquiredTheIdForASong() throws Throwable {
        HttpResponse allSongsResponse = Request.Get(world.baseUrl + "/video")
                                                .execute()
                                                .returnResponse();
        ArrayList<Video> videos = videoHelpers.BuildVideoListFromHttpResponse(allSongsResponse);
        world.songId = videos.get(0)._id;
    }

    @When("^I make a GET request to \"([^\"]*)\"/SongId$")
    public void IMakeAGETRequestToSongId(String endpoint) throws Throwable {
        world.httpResponse = Request.Get(world.baseUrl + endpoint + "/" + world.songId)
                .execute()
                .returnResponse();
    }

    @And("^The Songs ID should match that which was passed in the request$")
    public void theSongsIDShouldMatchThatWhichWasPassedInTheRequest() throws Throwable {
        Video video = videoHelpers.BuildVideoObjectFromHTTPResponse(world.httpResponse);
        Assert.assertEquals(world.songId, video._id);
    }

    @Then("^A single JSON object should be returned$")
    public void ASingleJSONObjectShouldBeReturned() throws Throwable {
        Video video = videoHelpers.BuildVideoObjectFromHTTPResponse(world.httpResponse);
        Assert.assertNotNull(video);
    }

    @Then("^The \"([^\"]*)\" property should be present in all the JSON objects returned$")
    public void thePropertyShouldBePresentInTheJSONObjectsReturned(String propertyName) throws Throwable {
        ArrayList<Video> videos = videoHelpers.BuildVideoListFromHttpResponse(world.httpResponse);

        // This is probably overly complicated, however I'm also using this as a learning experience as it's fun!
        for (Video video: videos){
            Class<?> c = video.getClass();
            Field f = c.getDeclaredField(propertyName);
            f.setAccessible(true);
            Assert.assertNotNull(f.get(video));
        }
    }

    @And("^The \"([^\"]*)\" property should be present in the JSON object returned$")
    public void thePropertyShouldBePresentInTheJSONObjectReturned(String propertyName) throws Throwable {
        Video video = videoHelpers.BuildVideoObjectFromHTTPResponse(world.httpResponse);

        Class<?> c = video.getClass();
        Field f = c.getDeclaredField(propertyName);
        f.setAccessible(true);
        Assert.assertNotNull(f.get(video));
    }

    @When("^I make a POST to the \"([^\"]*)\" endpoint with a single song in the body$")
    public void iMakeAPOSTToTheEndpointWithASingleSongInTheBody(String endpoint) throws Throwable {
        String singleSong = "{\"artist\": \"Bon Iver\", \"song\": \"8 (circle)\", \"publishDate\": \"2016-01-01\"}";

        world.httpResponse = Request.Post(world.baseUrl + endpoint)
                .addHeader("Content-Type", "application/json")
                .bodyString(singleSong, ContentType.APPLICATION_JSON)
                .execute()
                .returnResponse();
    }

    @When("^I make a POST to the \"([^\"]*)\" endpoint with the song details in the body$")
    public void IMakeAPOSTToTheEndpointWithTheSongDetailsInTheBody(String endpoint, List<Video> video) throws Throwable {
      String songs = videoHelpers.CreateVideoJsonFromClass(video);

        world.httpResponse = Request.Post(world.baseUrl + endpoint)
                .addHeader("Content-Type", "application/json")
                .bodyString(songs, ContentType.APPLICATION_JSON)
                .execute()
                .returnResponse();
    }

    @And("^The returned JSON should contain a song with song property set to \"([^\"]*)\"$")
    public void TheReturnedJSONShouldContainASongWithSongPropertySetTo(String songTitle) throws Throwable {
        // TODO Investigate using assertThat as it sounds useful - Right now I can't get it to play

        List<Video> videos = videoHelpers.BuildVideoListFromHttpResponse(world.httpResponse);

        // TODO Query why v.song == songTitle doesn't work
        boolean songInList = false;

        for(Video v : videos) {
            if (v.song.equals(songTitle)){
                songInList = true;
            }
        }
        Assert.assertTrue(songInList);
    }

    @When("^I make a POST to the \"([^\"]*)\" endpoint passing the \"([^\"]*)\" property as an \"([^\"]*)\"$")
    public void IMakeAPOSTToTheEndpointPassingThePropertyAsAn(String endpoint, String property, String dataType,
                                                                     Map<String, String> tableData) throws Throwable {
        String song = tableData.get("Song");
        String artist = tableData.get("Artist");
        String publishDate = tableData.get("PublishDate");

        String jsonBody = videoHelpers.CreateInvalidVideoJson(song, artist, publishDate, property, dataType);

        world.httpResponse = Request.Post(world.baseUrl + endpoint)
                            .addHeader("Content-Type", "application/json")
                            .execute()
                            .returnResponse();
    }

    @When("^I make a PATCH request to the \"([^\"]*)\"/SongId with the below data$")
    public void IMakeAPATCHRequestToTheSongIdWithTheBelowData(String endpoint, List<Video> video) throws Throwable {
        String jsonBody = videoHelpers.CreateVideoJsonFromClass(video);

        world.httpResponse = Request.Patch(world.baseUrl + endpoint + "/" + world.songId)
                              .addHeader("Content-Type", "application/json")
                              .bodyString(jsonBody, ContentType.APPLICATION_JSON)
                              .execute()
                              .returnResponse();
    }

    @When("^I make a DELETE request to \"([^\"]*)\"/SongId$")
    public void iMakeADELETERequestToSongId(String endpoint) throws Throwable {
        world.httpResponse = Request.Delete(world.baseUrl + endpoint + "/" + world.songId)
                              .execute()
                              .returnResponse();
    }

    @After
    public void RemoveTestData() throws IOException {
        videoHelpers.ClearOutVideoDatabase();
    }
}
