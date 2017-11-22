package general;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.Video;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import models.Playlist;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PlaylistHelpers {

    private String baseUrl;
    private VideoHelpers videoHelpers;

    public PlaylistHelpers(String baseUrl) {
        this.baseUrl = baseUrl;
        videoHelpers = new VideoHelpers(baseUrl);
    }

    public void ClearOutPlaylistDatabase() throws IOException {
        List<Playlist> playlists = GetPlaylistsFromApi();

        for (Playlist playlist : playlists) {
            Request.Delete(baseUrl + "/Playlist/" + playlist._id)
                    .execute();
        }
    }

    private ArrayList<Playlist> GetPlaylistsFromApi() throws IOException {
        String data;

        data = Request.Get(baseUrl + "/Playlist/")
                      .execute()
                      .returnContent()
                      .asString();

        return MapHttpBodyToPlaylistArray(data);
    }

    private ArrayList<Playlist> MapHttpBodyToPlaylistArray(String data) {
        if (data == null || data.isEmpty()) return new ArrayList<>();

        Gson gson = new Gson();
        Type playlistListType = new TypeToken<ArrayList<Playlist>>(){}.getType();
        return gson.fromJson(data, playlistListType);
    }

    public ArrayList<Playlist> BuildPlaylistArrayFromHttpResponse(HttpResponse httpResponse) throws IOException {
        CommonHelpers commonHelpers = new CommonHelpers();
        InputStream content = httpResponse.getEntity().getContent();
        String httpBody = commonHelpers.ConvertStreamToString(content);
        return MapHttpBodyToPlaylistArray(httpBody);
    }

    public boolean PropertyInPlaylist(String propertyName, Playlist playlist) throws NoSuchFieldException, IllegalAccessException {
        try{
            Class<?> c = playlist.getClass();
            Field f = c.getDeclaredField(propertyName);
            f.setAccessible(true);
            if (f.get(playlist) != null)
            {
                return true;
            };
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
        return false;
    }

    public void AddVideosToPlaylist(Integer numberOfVideosToAdd, String playlistId) throws IOException {
        List<Video> videos = videoHelpers.GetVideoArrayFromApi();

        if (numberOfVideosToAdd > videos.size()) {
            throw new IndexOutOfBoundsException(String.format("You requested to add %d videos to the playlist, there are only %d videos available to add",
                    numberOfVideosToAdd, videos.size()));
        }
        List<Video> videosToAdd = videos.subList(0, numberOfVideosToAdd);

        String playlistPatchJson = BuildPlaylistPatchJsonFromVideoArray(videosToAdd);

        Request.Patch(baseUrl + "/playlist/" + playlistId)
                .addHeader("Content-Type", "application/json")
                .bodyString(playlistPatchJson, ContentType.APPLICATION_JSON)
                .execute()
                .returnResponse();
    }

    private String BuildPlaylistPatchJsonFromVideoArray(List<Video> videosToAdd) {
        StringBuilder jsonBody = new StringBuilder("{\"videos\": [");

        Integer i = 0;
        for (Video video : videosToAdd) {
            jsonBody.append(String.format("{\"%s\": \"add\"}", video._id));

            if (i != videosToAdd.size()-1) {
                jsonBody.append(",");
            }
            i += 1;
        }
        jsonBody.append("]}");
        return jsonBody.toString();
    }

    public String BuildPlaylistPatchJsonFromVideoIdList(List<String> videoIds, String patchKeyWord) {
        StringBuilder jsonBody = new StringBuilder("{\"videos\": [");

        Integer i = 0;
        for (String videoId : videoIds) {
            jsonBody.append(String.format("{\"%s\": \"%s\"}", videoId, patchKeyWord));

            if (i != videoIds.size()-1) {
                jsonBody.append(",");
            }
            i += 1;
        }
        jsonBody.append("]}");
        return jsonBody.toString();
    }

    public Playlist BuildPlaylistFromHttpResponse(HttpResponse httpResponse) throws IOException {
        CommonHelpers commonHelpers = new CommonHelpers();
        InputStream content = httpResponse.getEntity().getContent();
        String httpBody = commonHelpers.ConvertStreamToString(content);
        return MapHttpBodyToPlaylist(httpBody);
    }

    private Playlist MapHttpBodyToPlaylist(String httpBody) {
        Gson gson = new Gson();
        return gson.fromJson(httpBody, Playlist.class);
    }

    public String CreateJsonFromPlaylistArray(List<Playlist> playlists) {
        Gson gson = new Gson();
        return gson.toJson(playlists);
    }
}
