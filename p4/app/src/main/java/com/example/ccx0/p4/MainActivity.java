package com.example.ccx0.p4;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private android.app.FragmentManager fragmentManager;
    private RadioGroup radioGroup;
    private Home homefragment;
    private Shopping shoppingfragment;
    private User userfragment;
    private String username,password;


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_layout);

        localread();
        Tabset();
    }

    public void Tabset() {
        //SaveReadsd A=new SaveReadsd();
        //A.createdestDir();
        fragmentManager = getFragmentManager();
        radioGroup = (RadioGroup) findViewById(R.id.tab_radiogroup);
        selecttab(R.id.bt_bottom_near);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selecttab(checkedId);
            }
        });
    } //tab切换

    public void selecttab(int checkedId) {
        android.app.FragmentTransaction transaction=fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (checkedId) {
            case R.id.bt_bottom_near:
                if(homefragment==null)
                {
                    homefragment=new Home();
                    transaction.add(R.id.main_fragment, homefragment);
                }
                else transaction.show(homefragment);
                break;
            case R.id.bt_bottom_near1:
                shoppingfragment=new Shopping();
                transaction.add(R.id.main_fragment, shoppingfragment,"shopcart");
                break;
            case R.id.bt_bottom_near2:
                userfragment=new User();
                transaction.add(R.id.main_fragment, userfragment,"user");
                break;
        }
        transaction.commit();
    } //tab切换

    private void hideFragments(FragmentTransaction transaction) {
        if (homefragment != null) {
            transaction.hide(homefragment);
        }
        if (shoppingfragment != null) {
            transaction.remove(shoppingfragment);
        }
        if (userfragment != null) {
            transaction.remove(userfragment);
        }
    } //tab切换

    public void localread(){
        SharedPreferences preferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        username = preferences.getString("username", "");
        password = preferences.getString("password", "");
        if(!username.equals("")&&!password.equals("")){
            Connect A=new Connect(handler);
            String key=username+","+password;
            A.sendRequestWithHttpClientRegisterLogin("login",username,key);
        }
    } //本地读取

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 1:
                    String s=(String)msg.obj;
                    if(s.equals("登录成功!")) {
                        Data.login=1;
                        Data.username=username;
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(getApplicationContext(), "登陆信息已过期请重新登陆", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "服务器连接失败，请检查网络连接后再试", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
