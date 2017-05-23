package com.example.ccx0.p4;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by CCx0 on 0014.2016.7.14.
 */
public class Purchase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_layout);

        toolbarset();
        viewset();
    }

    public void toolbarset() {
        TextView tv1=(TextView)findViewById(R.id.include_purchase_layout_toolbar).findViewById(R.id.toolbar_textview_1);
        tv1.setText("确认订单");
        tv1.setTextSize(18);

        Toolbar toolbar = (Toolbar) findViewById(R.id.include_purchase_layout_toolbar).findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    } //toolbar设定

    public void viewset() {
        Intent intent = getIntent();   // 获取 Intent
        final String promname= intent.getExtras().getString("proname");
        final String price= intent.getExtras().getString("price");
        final String flag= intent.getExtras().getString("flag");
        final String id= Data.username+","+flag+","+intent.getExtras().getString("id");
        final String S4= intent.getExtras().getString("S4");

        TextView textView=(TextView)findViewById(R.id.text_purchase_layout);
        TextView textView1=(TextView)findViewById(R.id.textlarge_purchase_layout);

        textView.setText(price);
        textView1.setText(promname);

        RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.btn_purchase_layout);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog("合计:￥ "+price,"确认支付",id,flag,S4);
            }
        });

        Purchase.this.setResult(RESULT_OK, intent);
    }

    public void dialog(String S,String S1,final String S2,final String flag,final String S4){
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setMessage(S);
        builder.setTitle(S1);
        builder.setPositiveButton("支付", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(flag.equals("1")){
                    String[] S=S4.split(",");
                    int a[]=new int[S.length];
                    for(int i=0;i<S.length;i++)
                        a[i]=Integer.parseInt(S[i]);
                    for(int i=0;i<S.length;i++){
                        for(int j=a[i];j<Data.size-1;j++){
                            Data.shopcartinfor[j]=Data.shopcartinfor[j+1];
                            Data.count[j]=Data.count[j+1];
                        }
                        Data.size--;
                        if(i<S.length-1) a[i+1]--;
                    }
                }
                postinfo(S2);
                dialog.dismiss();
                onBackPressed();
            }
        });

        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    public void postinfo(String S2){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case 1:
                        String S=(String)msg.obj;
                        Toast.makeText(getApplicationContext(), S, Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "服务器连接失败，请检查网络连接后再试", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        Connect A=new Connect(handler);
        A.sendRequestWithHttpClientInformation("purchase",S2);
    }
}
