package general;

import com.google.gson.reflect.TypeToken;

import models.Video;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import com.google.gson.*;
import org.apache.http.entity.ContentType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;


public class VideoHelpers {
    private String BaseUrl = "http://turing.niallbunting.com:3006/api";

    public void ClearOutSongDatabase() throws IOException {
        List<Video> videos = GetVideoList();

        for (Video video : videos) {
            Request.Delete(BaseUrl + "/Video/" + video._id)
            .execute();
        }
    }

    public void AddTestData(String testData) throws IOException {
        Request.Post("http://turing.niallbunting.com:3006/api/video/")
                .addHeader("Content-Type", "application/json")
                .bodyString(testData, ContentType.APPLICATION_JSON)
                .execute();
    }
    
    private ArrayList<Video> GetVideoList() throws IOException {
        String data;

        data = Request.Get(BaseUrl + "/Video/")
                .execute()
                .returnContent()
                .asString();

        return CreateVideoListFromJsonArray(data);
    }

    public String GetTestDataFromResource(String fileName) {
        StringBuilder result = new StringBuilder("");

        ClassLoader classLoader = getClass().getClassLoader();

        File file = new File(classLoader.getResource(fileName).getFile());

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    public String ConvertStreamToString(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line)
                             .append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return stringBuilder.toString();
    }

    public ArrayList<Video> BuildVideoListFromHttpResponse(HttpResponse httpResponse) throws IOException {
        InputStream content = httpResponse.getEntity().getContent();
        String httpBody = ConvertStreamToString(content);
        return CreateVideoListFromJsonArray(httpBody);
    }

    private ArrayList<Video> CreateVideoListFromJsonArray(String data) {
        if (data == null || data.isEmpty()) return new ArrayList<>();

        Gson gson = new Gson();
        Type videoListType = new TypeToken<ArrayList<Video>>(){}.getType();
        return gson.fromJson(data, videoListType);
    }

    public Video BuildVideoObjectFromHTTPResponse(HttpResponse httpResponse) throws IOException {
        InputStream content = httpResponse.getEntity().getContent();
        String httpBody = ConvertStreamToString(content);
        return MapJsonVideoObject(httpBody);
    }

    private Video MapJsonVideoObject(String data) {
        if (data == null || data.isEmpty()) return null;

        Gson gson = new Gson();
        return gson.fromJson(data, Video.class);
    }

    public String CreateVideoJsonFromClass(List<Video> videos){
        Gson gson = new Gson();
        return gson.toJson(videos);
    }

    public String CreateVideoJsonFromClass(Video video){
        Gson gson = new Gson();
        return gson.toJson(video);
    }

    /**
     * Builds an invalid Video JSON POST object
     * Current only supports passing integers
     * @param song Song title for the video
     * @param artist Artist for the video
     * @param publishDate Publish date for the Video
     * @param property The property to pass as an invalid data type
     * @param dataType The data type to pass as
     * @return A Json string for POST
     */
    public String CreateInvalidVideoJson(String song, String artist, String publishDate, String property, String dataType) {

        if (!property.equals("song")
                && !property.equals("artist")
                && !property.equals("publishDate")
                || !dataType.equals("integer")) {
            throw new NotImplementedException();
        }

        if (property.equals("song") && dataType.equals("integer")) {
            artist = "\"" + artist + "\"";
            publishDate = "\"" + publishDate + "\"";
        }

        if (property.equals("artist") && dataType.equals("integer")) {
            song = "\"" + song + "\"";
            publishDate = "\"" + publishDate + "\"";
        }

        if (property.equals("publishDate") && dataType.equals("integer")) {
            artist = "\"" + artist + "\"";
            song = "\"" + song + "\"";
        }

        return "{\"song\": " + song + "," +
                "\"artist\": " + artist + "," +
                "\"publishDate\": " + publishDate;
    }
}


