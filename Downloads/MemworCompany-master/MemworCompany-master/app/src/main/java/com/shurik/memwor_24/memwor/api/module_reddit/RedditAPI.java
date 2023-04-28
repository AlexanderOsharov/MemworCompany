package com.shurik.memwor_24.memwor.api.module_reddit;

import com.shurik.memwor_24.memwor.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RedditAPI {
    private static final String ACCESS_TOKEN = Constants.ACCESS_TOKEN_REDDIT;

    public String getJsonContent(String subreddit) throws IOException {
        HttpURLConnection connection = getHttpURLConnection(subreddit);
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder jsonContent = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            jsonContent.append(line);
        }

        reader.close();
        connection.disconnect();

        return jsonContent.toString();
    }

    private HttpURLConnection getHttpURLConnection(String subreddit) throws IOException {
        URL url = new URL("https://www.reddit.com/r/" + subreddit + "/hot.json");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/3");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        return connection;
    }
}
