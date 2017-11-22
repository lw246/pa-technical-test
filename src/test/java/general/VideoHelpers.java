package general;

import com.google.gson.reflect.TypeToken;

import com.sun.media.sound.InvalidDataException;
import models.Video;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import com.google.gson.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;


public class VideoHelpers {
    
    private String BaseUrl;
    private CommonHelpers commonHelpers;
    
    public VideoHelpers(String baseUrl) {
        this.BaseUrl = baseUrl;
        commonHelpers = new CommonHelpers();
    }

    public void ClearOutVideoDatabase() throws IOException {
        List<Video> videos = GetVideoArrayFromApi();

        for (Video video : videos) {
            Request.Delete(BaseUrl + "/Video/" + video._id)
            .execute();
        }
    }

    public ArrayList<Video> GetVideoArrayFromApi() throws IOException {
        String data;

        data = Request.Get(BaseUrl + "/Video/")
                .execute()
                .returnContent()
                .asString();

        return MapHttpBodyStringToVideoArray(data);
    }

    public ArrayList<Video> BuildVideoArrayFromHttpResponse(HttpResponse httpResponse) throws IOException {
        InputStream content = httpResponse.getEntity().getContent();
        String httpBody = commonHelpers.ConvertStreamToString(content);
        return MapHttpBodyStringToVideoArray(httpBody);
    }

    private ArrayList<Video> MapHttpBodyStringToVideoArray(String data) throws InvalidDataException {
        if (data == null || data.isEmpty()) {
            throw new InvalidDataException("No Json data to map");
        }

        Gson gson = new Gson();
        Type videoListType = new TypeToken<ArrayList<Video>>(){}.getType();
        return gson.fromJson(data, videoListType);
    }

    public Video BuildVideoFromHttpResponse(HttpResponse httpResponse) throws IOException {
        CommonHelpers commonHelpers = new CommonHelpers();
        InputStream content = httpResponse.getEntity().getContent();
        String httpBody = commonHelpers.ConvertStreamToString(content);
        return MapHttpBodyToVideo(httpBody);
    }

    private Video MapHttpBodyToVideo(String data) throws InvalidDataException {
        if (data == null || data.isEmpty()) {
            throw new InvalidDataException("No JSON to map");
        }

        Gson gson = new Gson();
        return gson.fromJson(data, Video.class);
    }

    public String CreateJsonFromVideoArray(List<Video> videos){
        Gson gson = new Gson();
        return gson.toJson(videos);
    }

    public String CreateInvalidVideoJson(String song, String artist, String publishDate, String property) {
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

    public boolean PropertyInVideo(String propertyName, Video video) throws NoSuchFieldException, IllegalAccessException {
        try{
            Class<?> c = video.getClass();
            Field f = c.getDeclaredField(propertyName);
            f.setAccessible(true);
            if (f.get(video) != null)
            {
                return true;
            };
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
        return false;
    }
}


