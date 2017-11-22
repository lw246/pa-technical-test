package general;

import java.io.*;
import java.util.Scanner;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

public class CommonHelpers {

    public String convertStreamToString(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder("");

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

    public String getTestDataFromResource(String fileName) {
        StringBuilder result = new StringBuilder("");

        ClassLoader classLoader = getClass().getClassLoader();

        File file;
        try {
            file = new File(classLoader.getResource(fileName).getFile());
        } catch (NullPointerException e) {
            throw new NullPointerException(String.format("Invalid filename %s", fileName));
        }

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

    public void addTestData(String url, String testData) throws IOException {
        Request.Post(url)
                .addHeader("Content-Type", "application/json")
                .bodyString(testData, ContentType.APPLICATION_JSON)
                .execute();
    }

}
