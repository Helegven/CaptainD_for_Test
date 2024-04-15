package com.example.captainb;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import static android.Manifest.permission.RECORD_AUDIO;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import java.util.ArrayList;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private SpeechRecognizer speechRecognizer;
    private Intent intentRecognizer;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

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
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String spokenText = "";

                if (matches != null){
                    spokenText = matches.get(0);
                    textView.setText(spokenText);
                }
                else {
                    textView.setText("Not set");
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
                                                });



//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
    public void StartButton(View view){
        Log.d(TAG, "StartButton: ");
        try {
            URL url = new URL("https://algame9-vps.roborumba.com/vector_search");
            new GetData().execute(url);

        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        textView.setText("Listening..");
        speechRecognizer.startListening(intentRecognizer);
    }

    public void StopButton(View view){
        speechRecognizer.stopListening();

    }
}