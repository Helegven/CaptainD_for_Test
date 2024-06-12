package com.example.captainb;

import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.ImageButton;
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
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity  {

    private SpeechRecognizer speechRecognizer;
    private Intent intentRecognizer;
    private TextView textView;
    private TextView textViewUser;
    private static final String TAG = "MainActivity";
    private AnimationDrawable isAnimation;
    private ImageView img;
    private Button micButton;


    // A boolean variable to keep track of the animation
    // Статус который отслеживает, работает анимация или нет.
    private boolean isStart = false;

    UuidFactory uuidFactory = new UuidFactory();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Два вызова снизу отвечает за запуск фоновой музки при старте приложения.
        MediaPlayer them = MediaPlayer.create(this, R.raw.them01);
        them.start();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //Поле ответов капитана
        textView = findViewById(R.id.textView);

        //Пользовательское окно вывода информации
        textViewUser = findViewById(R.id.textViewUser);

        //Отвечает за микрофон
        micButton = findViewById(R.id.micButton);

        ActivityCompat.requestPermissions(this, new String[]{RECORD_AUDIO}, PackageManager.PERMISSION_GRANTED);

        intentRecognizer = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intentRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.EXTRA_LANGUAGE_DETECTION_ALLOWED_LANGUAGES);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        // Анимация "Спинера" , при нажатой клавеше микрофона.
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
//                textView.setText("Слушаем...");

                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String spokenText = "";
                String user_id = "";
//                uuidFactory.getUUID(this);

                if (matches != null){
                    spokenText = matches.get(0);

                    String ask_text = "— " + spokenText;
                    textViewUser.setText(ask_text);

                    Runnable runnable = new Runnable() {
                        public void run() {
                            try{

                            String http_content = GetData.getContent("https://algame9-vps.roborumba.com/hook_app/", ask_text, user_id);
                            String answer_text = ask_text + "\n" + "— " + http_content;

                            textView.post(new Runnable() {
                                public void run() {
                                    textView.setText(answer_text);
                                    micButton.setVisibility(View.VISIBLE);
//                                    Toast.makeText(MainActivity.this, choiseButton1.getText(), Toast.LENGTH_SHORT).show();

                                }
                            });

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
//        Toast.makeText(MainActivity.this, choiseButton1.getText(), Toast.LENGTH_SHORT).show();
        speechRecognizer.startListening(intentRecognizer);
        micButton.setVisibility(View.GONE);
        img.setVisibility(View.VISIBLE);
        isAnimation.start();
        isStart = true;
    }

    public void StopListen(View view){
        speechRecognizer.stopListening();
        Toast.makeText(MainActivity.this, "Всё, не слышу", Toast.LENGTH_SHORT).show();
        img.setVisibility(View.GONE);
        micButton.setVisibility(View.VISIBLE);
        isAnimation.stop();
        isStart = false;
    }

    public void HelpButton(View view){
        onHelpClicked();
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

//                            String textForButton = GetData.testMac().get("Серега").toString();
//                            choiseButton1.setText(textForButton);
                        }
                    });
//                    ArrayList listForButton = GetData.testMac().get("Николай");
//                    for (int i = 0; i < array.length; i++) {
//                        // Код, который будет выполнен для каждого элемента
//                        System.out.println(array[i]);
//                    }
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
    public void onHelpClicked(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Правила игры")
                .setMessage("Тут примерный текст")
                .setCancelable(true)
                .setPositiveButton("Полный вперёд!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "Крысы сухопутной ответ!", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
        AlertDialog window = builder.create();
        window.show();
    };
    public void EventButton(View view){
        Button but = (Button) view;
        if(but.getText() ==  "Как играть?"){
            System.out.println(but);
            onHelpClicked();
        };
    }
}