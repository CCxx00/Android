package com.example.ccx0.p4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by CCx0 on 0021.2016.7.21.
 */
public class Friendconcern extends AppCompatActivity{

    private EditText editText[]=new EditText[3];
    private String id="";
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendconcern_layout);

        toolbarset();
        getListView();
        listenset();
    }

    public void toolbarset() {
        TextView tv1=(TextView)findViewById(R.id.include_friendconcern_toolbar).findViewById(R.id.toolbar_textview_1);
        tv1.setText("用户简介");
        tv1.setTextSize(18);

        Toolbar toolbar = (Toolbar) findViewById(R.id.include_friendconcern_toolbar).findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.color1));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Intent intent = new Intent(v.getContext(), Friendrecommend.class);
                startActivityForResult(intent, 1);
            }
        });
    } //toolbar设定

    public void getListView() {
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case 1:
                        String s=(String)msg.obj;
                        if(!s.equals("null")){
                            String[] S=s.split(",");
                            viewset(S);
                        }
                        else {
                            String S[]={"未知错误","未知错误","未知错误"};
                            viewset(S);
                            Toast.makeText(getApplicationContext(), "未知错误,请稍后再试", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "服务器连接失败，请检查网络连接后再试", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        intent = getIntent();   // 获取 Intent
        id= intent.getStringExtra("id");
        Connect A=new Connect(handler);
        A.sendRequestWithHttpClientInformation("logindown",id);
    }

    public void viewset(String S[]){
        LinearLayout layout[]=new LinearLayout[3];
        layout[0]=(LinearLayout)findViewById(R.id.friendconcerm1);
        layout[1]=(LinearLayout)findViewById(R.id.friendconcerm2);
        layout[2]=(LinearLayout)findViewById(R.id.friendconcerm3);

        TextView textView=(TextView) layout[0].findViewById(R.id.register1_text);
        textView.setText("性别:");
        for(int i=0;i<3;i++)
            editText[i] = (EditText) layout[i].findViewById(R.id.edtpassward_register_layout);
        editText[0].setText(S[0]);
        textView=(TextView)layout[1].findViewById(R.id.register1_text);
        textView.setText("手机号码:");
        editText[1].setText(S[1]);
        textView=(TextView)layout[2].findViewById(R.id.register1_text);
        textView.setText("收货地址:");
        editText[2].setText(S[2]);

        for(int i=0;i<3;i++) editText[i].setKeyListener(null);

        String judge= intent.getStringExtra("judge");
        Button button=(Button)findViewById(R.id.friendconcerm_layout_btn);
        if(judge.equals("ture")) button.setText("关注");
        else button.setText("取消关注");
    } //页面设定

    public void listenset(){
        Button button1=(Button)findViewById(R.id.friendconcerm_layout_btn);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setconcern();
            }
        });
    }

    public void setconcern() {
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case 1:
                        String s=(String)msg.obj;
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "服务器连接失败，请检查网络连接后再试", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        Connect A=new Connect(handler);
        Button button=(Button)findViewById(R.id.friendconcerm_layout_btn);
        if(button.getText().toString().equals("关注")){
            A.sendRequestWithHttpClientInformation("concern",Data.username+","+id+",1");
            button.setText("取消关注");
        }
        else {
            A.sendRequestWithHttpClientInformation("concern",Data.username+","+id+",2");
            button.setText("关注");
        }
    }
}
