package com.example.captainb;

import static com.example.captainb.R.id.choiseButton1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
public class EventForButtons extends Activity implements View.OnClickListener{
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);

       //Кнопки которые будет получать информацию через API
       Button choiseButton1 = (Button) findViewById(R.id.choiseButton1);
       Button choiseButton2 = (Button) findViewById(R.id.choiseButton2);
       Button choiseButton3 = (Button) findViewById(R.id.choiseButton3);
       Button choiseButton4 = (Button) findViewById(R.id.choiseButton4);

       // устанавливаем один обработчик для всех кнопок
       choiseButton1.setOnClickListener(EventForButtons.this);
       choiseButton2.setOnClickListener(EventForButtons.this);
       choiseButton3.setOnClickListener(EventForButtons.this);
       choiseButton4.setOnClickListener(EventForButtons.this);

   }
       @Override
       public void onClick(View v){
       Button btn = (Button) v;
       MainActivity mainActivity = new MainActivity();
           if(btn.getText()  ==  "Как играть?" ){
               mainActivity.onHelpClicked();
       }
//           switch (v.getId()) {
//               case R.id.choiseButton1: EventButton(btn); break;
//               case R.id.choiseButton2: EventButton(btn); break;
//               case R.id.choiseButton3: EventButton(btn); break;
//               case R.id.choiseButton4: EventButton(btn); break;
//           }
       }
//    final OnClickListener buttonClickListener = new OnClickListener() {
//        public void onClick(final View v) {
//            Button b = (Button)v;
//            if(b.getText().toString().equals("something")){
//                buttonToSetText.setText("value to set");
//            }
//        }
//    };
//    public void EventButton(Button but){
//        if(but.getText() ==  "Как играть?"){
//
//        };
//
//    };
}
