package general;

import com.google.gson.reflect.TypeToken;
import com.google.gson.*;
import com.sun.media.sound.InvalidDataException;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import models.Video;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class VideoHelpers {
    
    private String BaseUrl;
    private CommonHelpers commonHelpers;

    public VideoHelpers(String baseUrl) {
        this.BaseUrl = baseUrl;
        commonHelpers = new CommonHelpers();
    }

    public boolean propertyInVideo(String propertyName, Video video) throws NoSuchFieldException, IllegalAccessException {
        try{
            Class<?> c = video.getClass();
            Field f = c.getDeclaredField(propertyName);
            f.setAccessible(true);
            if (f.get(video) != null)
            {
                return true;
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
        return false;
    }

    public String createJsonFromVideoArray(List<Video> videos){
        Gson gson = new Gson();
        return gson.toJson(videos);
    }

    public String createInvalidVideoJson(String song, String artist, String publishDate, String property) {
        if (!property.equals("song")
                && !property.equals("artist")
                && !property.equals("publishDate")) {
            throw new NotImplementedException();
        }

        if (property.equals("song")) {
            artist = "\"" + artist + "\"";
            publishDate = "\"" + publishDate + "\"";
        }

        if (property.equals("artist")) {
            song = "\"" + song + "\"";
            publishDate = "\"" + publishDate + "\"";
        }

        if (property.equals("publishDate")) {
            artist = "\"" + artist + "\"";
            song = "\"" + song + "\"";
        }

        return "{\"song\": " + song + "," +
                "\"artist\": " + artist + "," +
                "\"publishDate\": " + publishDate;
    }

    public void clearOutVideoDatabase() throws IOException {
        List<Video> videos = getVideoArrayFromApi();

        for (Video video : videos) {
            Request.Delete(BaseUrl + "/Video/" + video._id)
                    .execute();
        }
    }

    public Video buildVideoFromHttpResponse(HttpResponse httpResponse) throws IOException {
        CommonHelpers commonHelpers = new CommonHelpers();
        InputStream content = httpResponse.getEntity().getContent();
        String httpBody = commonHelpers.convertStreamToString(content);
        return mapHttpBodyToVideo(httpBody);
    }

    public ArrayList<Video> buildVideoArrayFromHttpResponse(HttpResponse httpResponse) throws IOException {
        InputStream content = httpResponse.getEntity().getContent();
        String httpBody = commonHelpers.convertStreamToString(content);
        return mapHttpBodyStringToVideoArray(httpBody);
    }

    ArrayList<Video> getVideoArrayFromApi() throws IOException {
        String data;

        data = Request.Get(BaseUrl + "/Video/")
                .execute()
                .returnContent()
                .asString();

        return mapHttpBodyStringToVideoArray(data);
    }

    private Video mapHttpBodyToVideo(String data) throws InvalidDataException {
        if (data == null || data.isEmpty()) {
            throw new InvalidDataException("No JSON to map");
        }

        Gson gson = new Gson();
        return gson.fromJson(data, Video.class);
    }

    private ArrayList<Video> mapHttpBodyStringToVideoArray(String data) throws InvalidDataException {
        if (data == null || data.isEmpty()) {
            throw new InvalidDataException("No Json data to map");
        }

        Gson gson = new Gson();
        Type videoListType = new TypeToken<ArrayList<Video>>(){}.getType();
        return gson.fromJson(data, videoListType);
    }
}


