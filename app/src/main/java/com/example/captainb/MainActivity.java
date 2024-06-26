package com.example.captainb;

import android.graphics.drawable.AnimationDrawable;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.EditText;
import android.widget.ImageView;

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
    private AnimationDrawable isAnimation;
    private ImageView img;


    // A boolean variable to keep track of the animation
    // Статус который отслеживает, работает анимация или нет.
    private boolean isStart = false;

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

        img = findViewById(R.id.img);
        img.setImageResource(R.drawable.animation_button_on);
        isAnimation = (AnimationDrawable)img.getDrawable();

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

                            String http_content = GetData.getContent("https://algame9-vps.roborumba.com/hook_app/", ask_text, user_id);
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
        img.setVisibility(View.VISIBLE);
        isAnimation.start();
        isStart = true;
    }

    public void StopListen(View view){
        speechRecognizer.stopListening();
//        textView.setText("Стоп");
        Toast.makeText(MainActivity.this, "Всё, не слышу", Toast.LENGTH_SHORT).show();
        img.setVisibility(View.GONE);
        isAnimation.stop();
        isStart = false;
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

    public void sendMessage(View view) {
        EditText editText = findViewById(R.id.editMessage);
        String userMessage = String.valueOf(editText.getText());
        String uuid = uuidFactory.getUUID(this);


        textView.setText(userMessage + uuid);
        Runnable runnable = new Runnable() {
            public void run() {
                try{

                    String http_content = GetData.getContent("https://algame9-vps.roborumba.com/hook_app/", userMessage, uuid);
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