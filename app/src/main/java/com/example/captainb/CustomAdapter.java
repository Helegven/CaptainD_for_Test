package com.example.captainb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Message> objects;
    CustomAdapter(Context context, ArrayList<Message> messages){
        ctx = context;
        objects = messages;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }
    // товар по позиции
    Message getMessage(int position) {
        return ((Message) getItem(position));
    }
    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        Message message = (Message)this.getItem(position);
        View view = convertView;
        RecyclerView.ViewHolder holder;
        if (view == null) {
            view = lInflater.inflate(R.layout.list_item, parent, false);
        }else {
            holder = (RecyclerView.ViewHolder)convertView.getTag();
        }

        // было p
        Message mes = getMessage(position);

        if (mes.isMine()){
            ((TextView) view.findViewById(R.id.massage_userText)).setText(mes.getTextMessage());
            ((TextView) view.findViewById(R.id.massage_captainText)).setVisibility(View.GONE);
        }else {
            // заполняем View в пункте списка данными
            ((TextView) view.findViewById(R.id.massage_captainText)).setText(mes.getTextMessage());
        }

        return view;
    }
}
