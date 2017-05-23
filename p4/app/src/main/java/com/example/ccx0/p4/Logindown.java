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
public class Logindown extends AppCompatActivity{

    private EditText editText[]=new EditText[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logindown_layout);

        toolbarset();
        getListView();
        listenset();
    }

    public void toolbarset() {
        TextView tv1=(TextView)findViewById(R.id.include_logindown_toolbar).findViewById(R.id.toolbar_textview_1);
        tv1.setText("用户设置");
        tv1.setTextSize(18);

        Toolbar toolbar = (Toolbar) findViewById(R.id.include_logindown_toolbar).findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.color1));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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

        Connect A=new Connect(handler);
        A.sendRequestWithHttpClientInformation("logindown",Data.username);
    }

    public void viewset(String S[]){
        LinearLayout layout[]=new LinearLayout[3];
        layout[0]=(LinearLayout)findViewById(R.id.logindown1);
        layout[1]=(LinearLayout)findViewById(R.id.logindown2);
        layout[2]=(LinearLayout)findViewById(R.id.logindown3);

        TextView textView=(TextView) layout[0].findViewById(R.id.register1_text);
        textView.setText("性别:");
        for(int i=0;i<3;i++)
            editText[i] = (EditText) layout[i].findViewById(R.id.edtpassward_register_layout);
        editText[0].setText(S[0]);
        editText[0].setInputType(1);
        textView=(TextView)layout[1].findViewById(R.id.register1_text);
        textView.setText("手机号码:");
        editText[1].setText(S[1]);
        editText[1].setInputType(1);
        textView=(TextView)layout[2].findViewById(R.id.register1_text);
        textView.setText("收货地址:");
        editText[2].setText(S[2]);
        editText[2].setInputType(1);

        for(int i=0;i<3;i++)
            editlistenset(layout[i]);
    } //页面设定

    public void editlistenset(LinearLayout layout) {
        final EditText editText=(EditText)layout.findViewById(R.id.edtpassward_register_layout);
        final ImageView imageView=(ImageView)layout.findViewById(R.id.login_register_close2);
        imageView.setVisibility(View.GONE);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editText.length()>0) {
                    imageView.setVisibility(View.VISIBLE);
                }
                else imageView.setVisibility(View.GONE);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                imageView.setVisibility(View.GONE);
            }
        });
    }

    public void listenset(){
        Button button1=(Button)findViewById(R.id.logindown_save_layout_btn);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connect A=new Connect(handler1);
                String key=Data.username+","+editText[0].getText().toString()+","+editText[1].getText().toString()+","+editText[2].getText().toString();
                A.sendRequestWithHttpClientInformation("infoalter",key);
            }
        });

        Button button=(Button)findViewById(R.id.logindown_layout_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.login=0;
                Data.size=0;
                Intent intent = new Intent();
                Logindown.this.setResult(RESULT_OK, intent);
                localsave();
                onBackPressed();
            }
        });
    }

    public void localsave() {
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putString("username", "");
        editor.putString("password", "");
        editor.commit();//提交修改
    } //本地存储

    private Handler handler1=new Handler(){
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
}
