package com.example.ccx0.p4;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import static android.R.attr.button;

/**
 * Created by CC on 0003.2016.11.3.
 */

public class Jump extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.jump_layout);

        layoutset();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Jump.this, MainActivity.class);
                startActivity(intent);
                Jump.this.finish();
            }
        }, 2000);
    }

    public void layoutset(){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case 1:
                        Bitmap bitmap=(Bitmap)msg.obj;
                        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.jump_layout);
                        linearLayout.setBackground(new BitmapDrawable(bitmap));
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "服务器连接失败，请检查网络连接后再试", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        Connect A=new Connect(handler);
        A.sendRequestWithHttpClientJumpImage();
    } //动态布局
}
