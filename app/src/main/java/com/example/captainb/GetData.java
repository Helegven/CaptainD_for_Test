package com.example.captainb;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class GetData {

    public static HashMap testMac (){
        HashMap<String, List<Integer>> map = new HashMap<String, List<Integer>>();
        map.put("Серега", Arrays.asList(21));
        map.put("Николай", Arrays.asList(22,33,45,15));
        map.put("Иван Петрович", Arrays.asList(48));

        return map;
    }
    private static final String TAG = "GetData";
    @NonNull
    public static String getContent(String path, String phrase, String user_id) throws IOException {
        BufferedReader reader=null;
        InputStream stream = null;
        HttpsURLConnection connection = null;
        try {
            URL url= new URL(path);
            connection =(HttpsURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setReadTimeout(1000);
            connection.connect();

            String jsonInputString = "{\"user_id\":\"" + user_id + "\", \"text\":\"" + phrase + "\"}";

            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            stream = connection.getInputStream();
            reader= new BufferedReader(new InputStreamReader(stream, "utf-8"));
            StringBuilder response=new StringBuilder();
            String line;
            while ((line=reader.readLine()) != null) {
                response.append(line.trim());
            }
            String js = response.toString();

            JSONObject jObj = new JSONObject(js);
            Log.d(TAG, "getInputStream: " + js);
            String answer = jObj.getJSONObject("response").getString("text");;

            return(answer);

        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
//            throw new RuntimeException(e);
            return("error");

        } finally {
            if (reader != null) {
                reader.close();
            }
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

}
