package com.example.captainb;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import static android.Manifest.permission.RECORD_AUDIO;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import android.util.Log;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private SpeechRecognizer speechRecognizer;
    private Intent intentRecognizer;
    private TextView textView;
    private static final String TAG = "MainActivity";

    UuidFactory uuidFactory = new UuidFactory();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        ToggleButton customButton = findViewById(R.id.toggle);

        ActivityCompat.requestPermissions(this, new String[]{RECORD_AUDIO}, PackageManager.PERMISSION_GRANTED);

        intentRecognizer = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intentRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.EXTRA_LANGUAGE_DETECTION_ALLOWED_LANGUAGES);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        speechRecognizer.setRecognitionListener(new RecognitionListener(){
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }
            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                textView.setText("Слушаем...");

                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String spokenText = "";
                String user_id = "";
//                uuidFactory.getUUID(this);

                if (matches != null){
                    spokenText = matches.get(0);

                    String ask_text = "— " + spokenText;
                    textView.setText(ask_text);

                    Runnable runnable = new Runnable() {
                        public void run() {
                            try{

                            String http_content = getContent("https://algame9-vps.roborumba.com/hook_app/", ask_text, user_id);
                            String answer_text = ask_text + "\n" + "— " + http_content;

                            textView.post(new Runnable() {
                                public void run() {
                                    textView.setText(answer_text);
                                    customButton.setChecked(false);
                                }
                            });
//
                            }catch (IOException ex){
                                textView.post(new Runnable() {
                                    public void run() {
                                        textView.setText("Ошибка IOException: " + ex.getMessage());
                                        Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    };

                    Thread thread = new Thread(runnable);
                    thread.start();
                }
                else {
                    textView.setText("Текст не распознан");
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
                                                });

    }
    public void StartListen(View view){
        Log.d(TAG, "StartButton: ");
        Toast.makeText(MainActivity.this, "Слушаем..", Toast.LENGTH_SHORT).show();
//        textView.setText("Слушаем..");
        speechRecognizer.startListening(intentRecognizer);
    }

    public void StopListen(View view){
        speechRecognizer.stopListening();
//        textView.setText("Стоп");
        Toast.makeText(MainActivity.this, "Всё, не слышу", Toast.LENGTH_SHORT).show();
    }

    public void onToggleClicked(View view) {

        // включена ли кнопка
        boolean on = ((ToggleButton) view).isChecked();
        if (on) {
            StartListen(view);
        } else {
            StopListen(view);
        }
    }

    @NonNull
    private String getContent(String path, String phrase, String user_id) throws IOException {
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

    public void sendMessage(View view) {
        EditText editText = findViewById(R.id.editMessage);
        String userMessage = String.valueOf(editText.getText());
        String uuid = uuidFactory.getUUID(this);


        textView.setText(userMessage + uuid);
        Runnable runnable = new Runnable() {
            public void run() {
                try{

                    String http_content = getContent("https://algame9-vps.roborumba.com/hook_app/", userMessage, uuid);
                    String answer_text = userMessage + "\n" + uuid + "\n" + "— " + http_content;

                    textView.post(new Runnable() {
                        public void run() {
                            textView.setText(answer_text);
                        }
                    });
//
                }catch (IOException ex){
                    textView.post(new Runnable() {
                        public void run() {
                            textView.setText("Ошибка IOException: " + ex.getMessage());
                            Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }
}