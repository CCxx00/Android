package com.example.ccx0.p4;

import android.app.Fragment;
import android.app.FragmentTransaction;
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
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by CCx0 on 0017.2016.7.17.
 */
public class Login extends AppCompatActivity{

    EditText editText1,editText2;
    Button button;
    ImageView imageView1,imageView2;
    TextView textView1,textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        toolbarset();
        listenset();
    }

    public void toolbarset() {
        TextView tv1=(TextView)findViewById(R.id.include_login_layout_toolbar).findViewById(R.id.toolbar_textview_1);
        tv1.setText("登录");
        tv1.setTextSize(18);

        Toolbar toolbar = (Toolbar) findViewById(R.id.include_login_layout_toolbar).findViewById(R.id.toolbar);
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

    public void listenset() {
        button=(Button)findViewById(R.id.btn_login_layout);
        button.setBackgroundColor(getResources().getColor(R.color.color2));
        button.setEnabled(false);

        editText1=(EditText)findViewById(R.id.edtuser_login_layout);
        editText2=(EditText)findViewById(R.id.edtpassward_login_layout);
        imageView1=(ImageView)findViewById(R.id.login_layout_close1);
        imageView2=(ImageView)findViewById(R.id.login_layout_close2);
        textView1=(TextView)findViewById(R.id.login_layout_txtForgotPwd);
        textView2=(TextView)findViewById(R.id.login_layout_register);
        imageView1.setVisibility(View.GONE);
        imageView2.setVisibility(View.GONE);

        final Register A=new Register();

        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String editable = editText1.getText().toString();
                String str = A.stringFilter(editable); //过滤特殊字符
                if (!editable.equals(str)) {
                    editText1.setText(str);
                    Toast.makeText(getApplicationContext(), "禁止输入特殊字符", Toast.LENGTH_SHORT).show();
                }
                editText1.setSelection(editText1.length());
            }

            @Override
            public void afterTextChanged(Editable s) {
                buttonset();
                if(editText1.length()>0) {
                    imageView1.setVisibility(View.VISIBLE);
                }
                else imageView1.setVisibility(View.GONE);
            }
        });

        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String editable = editText1.getText().toString();
                String str = A.stringFilter(editable); //过滤特殊字符
                if (!editable.equals(str)) {
                    editText1.setText(str);
                    Toast.makeText(getApplicationContext(), "禁止输入特殊字符", Toast.LENGTH_SHORT).show();
                }
                editText1.setSelection(editText1.length());
            }

            @Override
            public void afterTextChanged(Editable s) {
                buttonset();
                if(editText2.length()>0) {
                    imageView2.setVisibility(View.VISIBLE);
                }
                else imageView2.setVisibility(View.GONE);
            }
        });

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText1.setText("");
                imageView1.setVisibility(View.GONE);
                buttonset();
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText2.setText("");
                imageView2.setVisibility(View.GONE);
                buttonset();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connect B=new Connect(handler);
                String key=editText1.getText().toString()+","+editText2.getText().toString();
                B.sendRequestWithHttpClientRegisterLogin("login",editText1.getText().toString(),key);
            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Register.class);
                startActivity(intent);
                editText1.setText("");
                editText2.setText("");
            }
        });
    }

    public void buttonset() {
        if(editText1.length()>0&&editText2.length()>0){
            button.setBackground(getResources().getDrawable(R.drawable.bt_register_selector));
            button.setEnabled(true);
        }
        else {
            button.setBackgroundColor(getResources().getColor(R.color.color2));
            button.setEnabled(false);
        }
    }

    public void localsave() {
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putString("username", editText1.getText().toString());
        editor.putString("password", editText2.getText().toString());
        editor.commit();//提交修改
    } //本地存储

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 1:
                    String s=(String)msg.obj;
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                    if(s.equals("登录成功!")) {
                        Data.login=1;
                        Data.username=editText1.getText().toString();
                        Intent intent = new Intent();
                        Login.this.setResult(RESULT_OK, intent);
                        localsave();
                        onBackPressed();
                    }
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "服务器连接失败，请检查网络连接后再试", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
