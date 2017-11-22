package stepdefinitions;

import cucumber.api.PendingException;
import cucumber.api.java.en.*;
import general.CommonHelpers;
import general.VideoHelpers;
import models.Video;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.*;


public class VideoSteps {

    private VideoHelpers videoHelpers;
    private World world;
    private CommonHelpers commonHelpers;

    public VideoSteps(World world) {
        this.world = world;
        videoHelpers = new VideoHelpers(world.baseUrl);
        commonHelpers = new CommonHelpers();
    }

    @Given("^I have no videos in the database$")
    public void i_have_no_videos_in_the_database() throws Throwable {
        videoHelpers.ClearOutVideoDatabase();
    }

    @And("^I have a single video in the database$")
    public void IHaveASingleVideoInTheDatabase() throws Throwable {
        videoHelpers.ClearOutVideoDatabase();
        String testData = "[{\"artist\": \"Forest Swords\",\"song\": \"War It\",\"publishDate\": \"2017-09-01\"}]";
        commonHelpers.AddTestData(world.baseUrl + "/video/", testData);
    }

    @Then("^A JSON Array should be returned with a single result$")
    public void AJSONArrayShouldBeReturnedWithASingleResult() throws Throwable {
        ArrayList<Video> videos = videoHelpers.BuildVideoArrayFromHttpResponse(world.httpResponse);
        assertEquals(1, videos.size());
    }

    @Then("^A JSON Array should be returned with multiple results$")
    public void AJSONArrayShouldBeReturnedWithMultipleResults() throws Throwable {
        ArrayList<Video> videos = videoHelpers.BuildVideoArrayFromHttpResponse(world.httpResponse);
        assertTrue(videos.size() > 1);
    }

    @And("^I have a single video in the database with an artist named \"([^\"]*)\"$")
    public void IHaveASingleVideoInTheDatabaseWithAnArtistNamed(String artist) throws Throwable {
        String testData = "[{\"artist\": "+ artist + ",\"song\": \"War It\",\"publishDate\": \"2017-09-01\"}]";
        commonHelpers.AddTestData(world.baseUrl + "/video/", testData);
    }

    @And("^I have acquired the Id for a song$")
    public void IHaveAcquiredTheIdForASong() throws Throwable {
        HttpResponse allSongsResponse = Request.Get(world.baseUrl + "/video")
                                                .execute()
                                                .returnResponse();
        ArrayList<Video> videos = videoHelpers.BuildVideoArrayFromHttpResponse(allSongsResponse);
        world.songId = videos.get(0)._id;
    }

    @When("^I make a GET request to \"([^\"]*)\"/SongId$")
    public void IMakeAGETRequestToSongId(String method) throws Throwable {
        world.httpResponse = Request.Get(world.baseUrl + method + "/" + world.songId)
                .execute()
                .returnResponse();
    }

    @And("^The Songs ID should match that which was passed in the request$")
    public void theSongsIDShouldMatchThatWhichWasPassedInTheRequest() throws Throwable {
        Video video = videoHelpers.BuildVideoFromHttpResponse(world.httpResponse);
        assertEquals(world.songId, video._id);
    }

    @Then("^A single JSON object should be returned$")
    public void ASingleJSONObjectShouldBeReturned() throws Throwable {
        Video video = videoHelpers.BuildVideoFromHttpResponse(world.httpResponse);
        assertNotNull(video);
    }

    @Then("^The \"([^\"]*)\" property should be present in all the video JSON objects returned$")
    public void thePropertyShouldBePresentInTheVideoJSONObjectsReturned(String propertyName) throws Throwable {
        ArrayList<Video> videos = videoHelpers.BuildVideoArrayFromHttpResponse(world.httpResponse);
        boolean propertyPresent = false;

        for (Video video: videos){
            propertyPresent = videoHelpers.PropertyInVideo(propertyName, video);
        }
        assertTrue(propertyPresent);
    }

    @And("^The \"([^\"]*)\" property should be present in the video JSON object returned$")
    public void thePropertyShouldBePresentInTheJSONObjectReturned(String propertyName) throws Throwable {
        Video video = videoHelpers.BuildVideoFromHttpResponse(world.httpResponse);

        assertTrue(videoHelpers.PropertyInVideo(propertyName, video));
    }

    @When("^I make a POST to the \"([^\"]*)\" method with a single song in the body$")
    public void iMakeAPOSTToTheMethodWithASingleSongInTheBody(String method) throws Throwable {
        String singleSong = "{\"artist\": \"Bon Iver\", \"song\": \"8 (circle)\", \"publishDate\": \"2016-01-01\"}";

        world.httpResponse = Request.Post(world.baseUrl + method)
                .addHeader("Content-Type", "application/json")
                .bodyString(singleSong, ContentType.APPLICATION_JSON)
                .execute()
                .returnResponse();
    }

    @When("^I make a POST to the \"([^\"]*)\" method with the song details in the body$")
    public void IMakeAPOSTToTheMethodWithTheSongDetailsInTheBody(String method, List<Video> video) throws Throwable {
      String songs = videoHelpers.CreateJsonFromVideoArray(video);

        world.httpResponse = Request.Post(world.baseUrl + method)
                .addHeader("Content-Type", "application/json")
                .bodyString(songs, ContentType.APPLICATION_JSON)
                .execute()
                .returnResponse();
    }

    @And("^The returned Video JSON should contain a song with song property set to \"([^\"]*)\"$")
    public void TheReturnedJSONShouldContainASongWithSongPropertySetTo(String songTitle) throws Throwable {
        List<Video> videos = videoHelpers.BuildVideoArrayFromHttpResponse(world.httpResponse);
        boolean songInList = false;

        for(Video v : videos) {
            if (v.song.equals(songTitle)){
                songInList = true;
            }
        }
        assertTrue(songInList);
    }

    @When("^I make a POST to the \"([^\"]*)\" method passing the \"([^\"]*)\" property as an \"([^\"]*)\"$")
    public void IMakeAPOSTToTheMethodPassingThePropertyAsAn(String method, String property, String dataType,
                                                                     Map<String, String> tableData) throws Throwable {
        String song = tableData.get("Song");
        String artist = tableData.get("Artist");
        String publishDate = tableData.get("PublishDate");

        String jsonBody = videoHelpers.CreateInvalidVideoJson(song, artist, publishDate, property);

        world.httpResponse = Request.Post(world.baseUrl + method)
                .addHeader("Content-Type", "application/json")
                .bodyString(jsonBody, ContentType.APPLICATION_JSON)
                .execute()
                .returnResponse();
    }

    @When("^I make a PATCH request to the \"([^\"]*)\"/SongId with the below data$")
    public void IMakeAPATCHRequestToTheSongIdWithTheBelowData(String endpoint, List<Video> video) throws Throwable {
        String jsonBody = videoHelpers.CreateJsonFromVideoArray(video);

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

    @And("^The \"([^\"]*)\" property in the video JSON response should be \"([^\"]*)\"$")
    public void thePropertyInTheVideoJsonResponseShouldBe(String propertyName, String expectedValue) throws Throwable {
        ArrayList<Video> videos = videoHelpers.BuildVideoArrayFromHttpResponse(world.httpResponse);
        Video video = videos.get(0);

        Class<?> c = video.getClass();
        Field f = c.getDeclaredField(propertyName);
        f.setAccessible(true);
        assertEquals(expectedValue, f.get(video));
    }

    @And("^I have videos in file \"([^\"]*)\" in the database$")
    public void iHaveVideosInFileInTheDatabase(String fileName) throws Throwable {
        String testData = commonHelpers.GetTestDataFromResource(fileName);
        commonHelpers.AddTestData(world.baseUrl + "/video/", testData);
    }
}
