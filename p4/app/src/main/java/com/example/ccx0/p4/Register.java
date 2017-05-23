package com.example.ccx0.p4;

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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by CCx0 on 0018.2016.7.18.
 */
public class Register extends AppCompatActivity{

    private Button button;
    private EditText editText[]=new EditText[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        toolbarset();
        viewset();
        Buttonlistenset();
    }

    public void toolbarset() {
        TextView tv1=(TextView)findViewById(R.id.include_register_toolbar).findViewById(R.id.toolbar_textview_1);
        tv1.setText("注册");
        tv1.setTextSize(18);

        Toolbar toolbar = (Toolbar) findViewById(R.id.include_register_toolbar).findViewById(R.id.toolbar);
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

    public void viewset(){
        LinearLayout layout[]=new LinearLayout[5];
        layout[0]=(LinearLayout)findViewById(R.id.register1_1_1);
        layout[1]=(LinearLayout)findViewById(R.id.register1_1_2);
        layout[2]=(LinearLayout)findViewById(R.id.register1_1_3);
        layout[3]=(LinearLayout)findViewById(R.id.register1_1_4);
        layout[4]=(LinearLayout)findViewById(R.id.register1_1_5);

        TextView textView=(TextView) layout[0].findViewById(R.id.register1_text);
        textView.setText("账号:");
        for(int i=0;i<5;i++)
            editText[i] = (EditText) layout[i].findViewById(R.id.edtpassward_register_layout);
        editText[0].setHint("请输入账号");
        editText[0].setInputType(1);
        textView=(TextView)layout[2].findViewById(R.id.register1_text);
        textView.setText("确认密码:");
        textView=(TextView)layout[3].findViewById(R.id.register1_text);
        textView.setText("手机号码:");
        editText[3].setHint("请输入手机号码");
        editText[3].setInputType(1);
        textView=(TextView)layout[4].findViewById(R.id.register1_text);
        textView.setText("收货地址:");
        editText[4].setHint("请输入收货地址");
        editText[4].setInputType(1);

        button=(Button)findViewById(R.id.register_layout_btn);
        button.setBackgroundColor(getResources().getColor(R.color.color2));
        button.setEnabled(false);

        for(int i=0;i<5;i++)
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
                String editable = editText.getText().toString();
                String str = stringFilter(editable); //过滤特殊字符
                if (!editable.equals(str)) {
                    editText.setText(str);
                    Toast.makeText(getApplicationContext(), "禁止输入特殊字符", Toast.LENGTH_SHORT).show();
                }
                editText.setSelection(editText.length());
            }

            @Override
            public void afterTextChanged(Editable s) {
                buttonset();
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
                buttonset();
            }
        });
    }

    public void buttonset() {
        if(editText[0].length()>0&&editText[1].length()>0&&editText[2].length()>0&&editText[3].length()>0&&editText[4].length()>0){
            button.setBackground(getResources().getDrawable(R.drawable.bt_register_selector));
            button.setEnabled(true);
        }
        else {
            button.setBackgroundColor(getResources().getColor(R.color.color2));
            button.setEnabled(false);
        }
    }

    public static String stringFilter(String str) throws PatternSyntaxException {
        String regEx = "[ /\\:*?<>|\"\n\t]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    } //过滤字符

    public void Buttonlistenset() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText[1].getText().toString().equals(editText[2].getText().toString())){
                    Connect A=new Connect(handler);
                    String key=editText[0].getText().toString()+","+editText[1].getText().toString()+","+editText[3].getText().toString()+","+editText[4].getText().toString();
                    A.sendRequestWithHttpClientRegisterLogin("register",editText[0].getText().toString(),key);
                }
                else Toast.makeText(getApplicationContext(), "两次密码不一致", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 1:
                    String s=(String)msg.obj;
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                    if(s.equals("注册成功!")) onBackPressed();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "服务器连接失败，请检查网络连接后再试", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
