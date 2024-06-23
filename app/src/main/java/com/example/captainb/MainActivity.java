package com.example.captainb;

import static android.Manifest.permission.RECORD_AUDIO;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity  {

    private SpeechRecognizer speechRecognizer;
    private Intent intentRecognizer;
    private ListView textView;
    private ListView textViewUser;
    public ListView list_of_massages;
    private static final String TAG = "MainActivity";
    private AnimationDrawable isAnimation;
    private ImageView img;
    private Button micButton;
    public Button choiseButton1, choiseButton2, choiseButton3, choiseButton4;
//    public MessageActivity messageActivity;

    ArrayList<Message> messages = new ArrayList<Message>();
    CustomAdapter customAdapter;

    // A boolean variable to keep track of the animation
    // Статус который отслеживает, работает анимация или нет.
    private boolean isStart = false;

    UuidFactory uuidFactory = new UuidFactory();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Два вызова снизу отвечает за запуск фоновой музки при старте приложения.
        MediaPlayer them = MediaPlayer.create(this, R.raw.them01);
        them.start();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //Поле ответов капитана
        textView = findViewById(R.id.list_of_massages);

        //Пользовательское окно вывода информации
        textViewUser = findViewById(R.id.list_of_massages);

        //Отвечает за активацию микрофона
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

        // Создаём адаптер
//        fillData();
        customAdapter = new CustomAdapter(this, messages);

        // настраиваем список
        ListView list_of_massages = (ListView) findViewById(R.id.list_of_massages);
        list_of_massages.setAdapter(customAdapter);

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
                String user_id = "";
//                uuidFactory.getUUID(this);

                if (matches != null){
                    spokenText = matches.get(0);
                    String ask_text = "— " + spokenText;
//                    textViewUser.setText(ask_text);
//                    messageActivity.addNewMessage(new Message(true, ask_text));
                    messages.add(new Message(true, ask_text));
                    Runnable runnable = new Runnable() {
                        public void run() {
                            try{

                            String http_content = GetData.getContent("https://algame9-vps.roborumba.com/hook_app/", ask_text, user_id);
                            String answer_text = ask_text + "\n" + "— " + http_content;

                            textView.post(new Runnable() {
                                public void run() {
//                                    messageActivity.addNewMessage(new Message(false, answer_text));
//                                    messages.add(new Message("Product ", answer_text, false));
                                    messages.add(new Message(false, answer_text));
//                                    textView.setText(answer_text);
                                    micButton.setVisibility(View.VISIBLE);
                                }
                            });

                            }catch (IOException ex){
                                textView.post(new Runnable() {
                                    public void run() {
//                                        messages.add(new Message("Product ", "Ошибка IOException: " + ex.getMessage(), false));
//                                        textView.setText("Ошибка IOException: " + ex.getMessage());
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
//                    textView.setText("Текст не распознан");
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

//    public void sendMessage(View view) {
//        EditText editText = findViewById(R.id.editMessage);
//        String userMessage = String.valueOf(editText.getText());
////        String uuid = uuidFactory.getUUID(this);
//        String uuid ="";
//
////        textViewUser.setText(userMessage + uuid);
//        Runnable runnable = new Runnable() {
//            public void run() {
//                try{
//                    String http_content = GetData.getContent("https://algame9-vps.roborumba.com/hook_app/", userMessage, uuid);
//                    String answer_text = userMessage + "\n" + "— " + http_content;
//                    textView.post(new Runnable() {
//                        public void run() {
////                            textView.setText(answer_text);
//                        }
//                    });
//                }catch (IOException ex){
//                    textView.post(new Runnable() {
//                        public void run() {
////                            textView.setText("Ошибка IOException: " + ex.getMessage());
//                            Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }
//        };
//
//        Thread thread = new Thread(runnable);
//        thread.start();
//    }
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
    // Класс для маршрутизации клавиш choiseButton
    public void EventButton(View view){
        Button but = (Button) view;
        if (ConnectingAPI(but.getText()) == true){
            but.setVisibility(View.GONE);
        }
    };
    // Отправляет запрос в АПИ согласно текстовом полю - дальше следует добавить user_id
    public boolean ConnectingAPI(CharSequence str){
        String text = (String) str;
        String user_id = "";
        if (str != null) {
            String spokenText = text;
            String ask_text = "— " + spokenText;

//            textViewUser.setText(ask_text);
//            messageActivity.addNewMessage(new Message(true, ask_text));
            messages.add(new Message(true, ask_text));
            Runnable runnable = new Runnable() {
                public void run() {
                    try {
                        String http_content = GetData.getContent("https://algame9-vps.roborumba.com/hook_app/", ask_text, user_id);
                        String answer_text = "— " + http_content;
                        textView.post(new Runnable() {
                            public void run() {
//                                messageActivity.addNewMessage(new Message(false, answer_text));
                                messages.add(new Message(false, answer_text));

//                              messages.add(new Message("Product ", answer_text, false));
//                              textView.setText(answer_text);
                                customAdapter.notifyDataSetChanged();
                            }

                        });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    };
                };

            };
        Thread thread = new Thread(runnable);
        thread.start();
        };
        return true;
    };
    //Метод для установки новвых параметров для choiseButton
    public void SetNewChoise(String[] ButtonsText ){
        choiseButton1.setText(ButtonsText[0]);
        choiseButton2.setText(ButtonsText[1]);
        choiseButton3.setText(ButtonsText[2]);
        choiseButton4.setText(ButtonsText[3]);
    };
//    public void sendMessage(View v) {
//        String newMessage = this.text.getText().toString().trim();
//        if (newMessage.length() > 0) {
//            this.text.setText("");
//            this.addNewMessage(new Message(newMessage, true));
//            (new SendMessage((SendMessage)null)).execute(new Void[0]);
//        }
//
//    }
//
//    void addNewMessage(Message m) {
//        this.messages.add(m);
//        this.customAdapter.notifyDataSetChanged();
//        getListView();
//    }
//
//    private class SendMessage extends AsyncTask <Void, String, String> {
//        private SendMessage() {
//        }
//
//        protected String doInBackground(Void... params) {
//            try {
//                Thread.sleep(2000L);
//            } catch (InterruptedException var5) {
//                var5.printStackTrace();
//            }
//            try {
//                Thread.sleep(2000L);
//            } catch (InterruptedException var4) {
//                var4.printStackTrace();
//            }
//            try {
//                Thread.sleep(3000L);
//            } catch (InterruptedException var3) {
//                var3.printStackTrace();
//            }
//
//            return messages.toString();
//        }
//
//        public void onProgressUpdate(String... v) {
//            if (((Message)MainActivity.this.messages.get(MainActivity.this.messages.size() - 1)).isStatusMessage) {
//                ((Message)MainActivity.this.messages.get(MainActivity.this.messages.size() - 1)).setMessage(v[0]);
//                MainActivity.this.adapter.notifyDataSetChanged();
//                MainActivity.this.getListView().setSelection(MainActivity.this.messages.size() - 1);
//            } else {
//                MainActivity.this.addNewMessage(new Message(true, v[0]));
//            }
//
//        }
//
//        protected void onPostExecute(String text) {
//            if (((Message)MainActivity.this.messages.get(MainActivity.this.messages.size() - 1)).isStatusMessage) {
//                MainActivity.this.messages.remove(MainActivity.this.messages.size() - 1);
//            }
//
//            MainActivity.this.addNewMessage(new Message(text, false));
//        }
//    }
}