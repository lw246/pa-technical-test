package stepdefinitions;

import cucumber.api.PendingException;
import cucumber.api.java.en.*;
import general.CommonHelpers;
import general.PlaylistHelpers;
import gherkin.lexer.Pl;
import models.Playlist;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.junit.Before;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class PlaylistSteps {

    private World world;
    private PlaylistHelpers playlistHelpers;
    private CommonHelpers commonHelpers;
    private VideoSteps videoSteps;
    private CommonSteps commonSteps;

    public PlaylistSteps(World world) {
        this.world = world;
        playlistHelpers = new PlaylistHelpers(world.baseUrl);
        commonHelpers = new CommonHelpers();
        videoSteps = new VideoSteps(world);
        commonSteps = new CommonSteps(world);
    }

    @And("^I have no playlists in the database$")
    public void iHaveNoPlaylistsInTheDatabase() throws Throwable {
        playlistHelpers.ClearOutPlaylistDatabase();
    }

    @And("^I have a single playlist without videos in the database$")
    public void iHaveASinglePlaylistInTheDatabase() throws Throwable {
        String testData = "{\"desc\": \"My first playlist.\",\"title\": \"My List\"}";
        commonHelpers.AddTestData(world.baseUrl + "/Playlist/", testData);
    }

    @Then("^The \"([^\"]*)\" property should be present in all the playlist JSON objects returned$")
    public void thePropertyShouldBePresentInAllThePlaylistJSONObjectsReturned(String propertyName) throws Throwable {
        ArrayList<Playlist> playlists = playlistHelpers.BuildPlaylistArrayFromHttpResponse(world.httpResponse);
        boolean propertyPresent = false;

        // This is probably overly complicated, however I'm also using this as a learning experience as it's fun!
        for (Playlist playlist: playlists){
            propertyPresent = playlistHelpers.PropertyInPlaylist(propertyName, playlist);
        }
        assertTrue(propertyPresent);
    }

    @And("^I have multiple playlists with videos in the database$")
    public void iHaveMultiplePlaylistsWithVideosInTheDatabase() throws Throwable {
        throw new PendingException();
        // Add videos into the database
//        VideoSteps videoSteps = new VideoSteps(world);
//        videoSteps.IHaveMultipleVideosInTheDatabase();
//
//        // Create playlists
//        iHaveMultiplePlaylistsInTheDatabase();

        // Assign videos to playlists
    }

    @Then("^A playlist JSON Array should be returned with (\\d+) results$")
    public void aPlaylistJSONArrayShouldBeReturnedWithResults(int expectedNumberOfPlaylists) throws Throwable {
        ArrayList<Playlist> playlists = playlistHelpers.BuildPlaylistArrayFromHttpResponse(world.httpResponse);
        assertEquals(expectedNumberOfPlaylists, playlists.size());
    }

    @And("^I have the playlists in file \"([^\"]*)\" in the database$")
    public void iHaveThePlaylistsInFileDatabase(String fileName) throws Throwable {
        playlistHelpers.ClearOutPlaylistDatabase();
        String testData = commonHelpers.GetTestDataFromResource(fileName);
        commonHelpers.AddTestData(world.baseUrl + "/playlist/", testData);
    }

    @And("^The playlists have (\\d+) videos each$")
    public void thePlaylistsHaveVideos(int numberOfVideos) throws Throwable {
        commonSteps.i_make_a_GET_request_to("/playlist");
        ArrayList<Playlist> playlists = playlistHelpers.BuildPlaylistArrayFromHttpResponse(world.httpResponse);

        // Assign videos to playlists
        for (Playlist playlist : playlists)
            playlistHelpers.AddVideosToPlaylist(numberOfVideos, playlist._id);
    }

    @And("^The playlists Id should match that which was passed in the request$")
    public void thePlaylistsIdShouldMatchThatWhichWasPassedInTheRequest() throws Throwable {
        Playlist playlist = playlistHelpers.BuildPlaylistFromHttpResponse(world.httpResponse);
        assertEquals(world.playlistId, playlist._id);
    }

    @And("^I have acquired the Id for a playlist$")
    public void iHaveAcquiredTheIdForAPlaylist() throws Throwable {
        HttpResponse httpResponse = Request.Get(world.baseUrl + "/playlist")
                .execute()
                .returnResponse();
        ArrayList<Playlist> playlists = playlistHelpers.BuildPlaylistArrayFromHttpResponse(httpResponse);
        world.playlistId = playlists.get(0)._id;
    }

    @When("^I make a GET request to \"([^\"]*)\"/PlaylistId$")
    public void iMakeAGETRequestToPlaylistId(String method) throws Throwable {
        world.httpResponse = Request.Get(world.baseUrl + method + "/" + world.playlistId)
                .execute()
                .returnResponse();
    }

    @And("^A single playlist JSON object should be returned$")
    public void aSinglePlaylistJSONObjectShouldBeReturned() throws Throwable {
        Playlist video = playlistHelpers.BuildPlaylistFromHttpResponse(world.httpResponse);
        assertNotNull(video);
    }

    @Then("^The \"([^\"]*)\" property should be present in the JSON object returned$")
    public void thePropertyShouldBePresentInTheJSONObjectReturned(String propertyName) throws Throwable {
        Playlist playlist = playlistHelpers.BuildPlaylistFromHttpResponse(world.httpResponse);

        assertTrue(playlistHelpers.PropertyInPlaylist(propertyName, playlist));
    }

    @Then("^The video object in the playlist JSON should contain (\\d+) videos$")
    public void theVideoObjectInThePlaylistJSONShouldContainVideos(int expectNumberOfVideos) throws Throwable {
        Playlist playlist = playlistHelpers.BuildPlaylistFromHttpResponse(world.httpResponse);

        assertEquals(expectNumberOfVideos, playlist.videos.size());
    }

    @When("^I make a PATCH \"([^\"]*)\" request to the \"([^\"]*)\"/PlaylistId passing the song Id in the body$")
    public void iMakeAPATCHRequestToThePlaylistIdPassingTheSongIdInTheBody(String patchKeyWord, String method) throws Throwable {
        ArrayList<String> songIds = new ArrayList<>();
        songIds.add(world.songId);
        String patchJson = playlistHelpers.BuildPlaylistPatchJsonFromVideoIdList(songIds, patchKeyWord);

        world.httpResponse = Request.Patch(world.baseUrl + method + "/" + world.playlistId)
                .addHeader("Content-Type", "application/json")
                .bodyString(patchJson, ContentType.APPLICATION_JSON)
                .execute()
                .returnResponse();
    }

    @And("^The video id should be the same as the video Id posted$")
    public void theVideoIdShouldBeTheSameAsTheVideoIdPosted() throws Throwable {
        Playlist playlist = playlistHelpers.BuildPlaylistFromHttpResponse(world.httpResponse);
        assertEquals(world.songId, playlist.videos.get(0)._id);
    }

    @And("^I have acquired the Id for a video in the playlist$")
    public void iHaveAcquiredTheIdForAVideoInThePlaylist() throws Throwable {
        iMakeAGETRequestToPlaylistId("/playlist");
        Playlist playlist = playlistHelpers.BuildPlaylistFromHttpResponse(world.httpResponse);
        world.songId = playlist.videos.get(0)._id;
    }

    @When("^I make a DELETE request to \"([^\"]*)\"/PlaylistId$")
    public void iMakeADELETERequestToPlaylistId(String method) throws Throwable {
        world.httpResponse = Request.Delete(world.baseUrl + method + "/" + world.playlistId)
                .execute()
                .returnResponse();
    }

    @When("^I make a POST to the \"([^\"]*)\" method with playlist details in the body$")
    public void iMakeAPOSTToTheMethodWithAPlaylistsInTheBody(String method, List<Playlist> playlists) throws Throwable {
        String playlistJson = playlistHelpers.CreateJsonFromPlaylistArray(playlists);

        world.httpResponse = Request.Post(world.baseUrl + method)
                .addHeader("Content-Type", "application/json")
                .bodyString(playlistJson, ContentType.APPLICATION_JSON)
                .execute()
                .returnResponse();
    }

    @And("^The \"([^\"]*)\" property in the playlist JSON response should be \"([^\"]*)\"$")
    public void thePropertyInThePlaylistJsonResponseShouldBe(String propertyName, String expectedValue) throws Throwable {
        ArrayList<Playlist> playlists = playlistHelpers.BuildPlaylistArrayFromHttpResponse(world.httpResponse);
        Playlist playlist = playlists.get(0);

        Class<?> c = playlist.getClass();
        Field f = c.getDeclaredField(propertyName);
        f.setAccessible(true);
        assertEquals(expectedValue, f.get(playlist));
    }

    @And("^The Playlist JSON response should contain a playlist with the \"([^\"]*)\" property set to \"([^\"]*)\"$")
    public void thePlaylistJSONResponseShouldContainAPlaylistWithThePropertySetTo(String propertyName, String expectedValue) throws Throwable {
        ArrayList<Playlist> playlists = playlistHelpers.BuildPlaylistArrayFromHttpResponse(world.httpResponse);
        boolean propertyValueMatch = false;

        for (Playlist playlist : playlists) {
            Class<?> c = playlist.getClass();
            Field f = c.getDeclaredField(propertyName);
            f.setAccessible(true);
            if (expectedValue.equals(f.get(playlist))) {
                propertyValueMatch = true;
            }
        }
        assertTrue(propertyValueMatch);
    }

    @When("^I make a PATCH \"([^\"]*)\" request to the \"([^\"]*)\"/PlaylistId passing videoId of \"([^\"]*)\"$")
    public void iMakeAPATCHRequestToThePlaylistIdPassingVideoIdOf(String patchKeyword, String method, String videoId) throws Throwable {
        ArrayList<String> videoIds = new ArrayList<>();
        videoIds.add(videoId);

        String patchJson = playlistHelpers.BuildPlaylistPatchJsonFromVideoIdList(videoIds, patchKeyword);

        world.httpResponse = Request.Patch(world.baseUrl + method + "/" + world.playlistId)
                .addHeader("Content-Type", "application/json")
                .bodyString(patchJson, ContentType.APPLICATION_JSON)
                .execute()
                .returnResponse();
    }

    @When("^I make a patch request passing in (\\d+) video Ids$")
    public void iMakeAPatchRequestPassingInVideoIds(int numberOfVideos) throws Throwable {
        playlistHelpers.AddVideosToPlaylist(numberOfVideos, world.playlistId);
    }
}
