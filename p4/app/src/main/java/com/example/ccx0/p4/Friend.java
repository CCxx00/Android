package com.example.ccx0.p4;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by CC on 0020.2016.10.20.
 */

public class Friend extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_layout);

        toolbarset();
        listView=(ListView) findViewById(R.id.listView_friend_layout);
        getListView();
        listen();
    }

    public void toolbarset() {
        TextView tv1=(TextView)findViewById(R.id.includ_friend_layout_toolbar).findViewById(R.id.toolbar_textview_1);
        tv1.setText("我的关注");
        tv1.setTextSize(18);

        Toolbar toolbar = (Toolbar) findViewById(R.id.includ_friend_layout_toolbar).findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    } //toolbar设定

    public void listen(){
        RelativeLayout linearLayout=(RelativeLayout)findViewById(R.id.friend_layout_bottom);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Friendrecommend.class);
                startActivityForResult(intent, 1);
                onBackPressed();
            }
        });

        RelativeLayout linearLayout1=(RelativeLayout)findViewById(R.id.friend_layout_bottom1);
        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Importantrecommend.class);
                startActivityForResult(intent, 1);
                onBackPressed();
            }
        });
    }

    public void getListView(){

        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case 1:
                        String s=(String)msg.obj;
                        if(!s.equals("false")){
                            String[] S=s.split(",");
                            setListView(S,S);
                        }
                        else listView.setAdapter(null);
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "服务器连接失败，请检查网络连接后再试", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        Connect A=new Connect(handler);
        A.sendRequestWithHttpClientInformation("myconcern",Data.username);
    }

    public void setListView(String category_strings[],final String S[]){
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, category_strings));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getButton(S,position);
            }
        });
    }

    public void getButton(final String S[],final int position) {
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case 1:
                        String s=(String)msg.obj;
                        Intent intent = new Intent(getApplicationContext(), Friendconcern.class);
                        intent.putExtra("id", S[position]);
                        intent.putExtra("judge", s);
                        startActivity(intent);
                        onBackPressed();
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "服务器连接失败，请检查网络连接后再试", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        Connect A=new Connect(handler);
        A.sendRequestWithHttpClientInformation("concernjudge",Data.username+","+S[position]);
    }
}
