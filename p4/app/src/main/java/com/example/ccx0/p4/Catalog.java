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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by llmxd on 0009.2016.9.9.
 */
public class Catalog extends AppCompatActivity {

    private ListView listView;
    private String string[]={"服装","食品","图书","数码"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalog_layout);

        listView=(ListView)findViewById(R.id.catalog_layout_listView);
        toolbarset();
    }

    public void toolbarset() {
        TextView tv1=(TextView)findViewById(R.id.includ_catalog_layout_toolbar).findViewById(R.id.toolbar_textview_1);
        Intent intent = getIntent();   // 获取 Intent
        int id= intent.getIntExtra("id",0);
        tv1.setText(string[id]);
        getListView(string[id]);
        tv1.setTextSize(18);

        Toolbar toolbar = (Toolbar) findViewById(R.id.includ_catalog_layout_toolbar).findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.color5));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    } //toolbar设定

    public void getListView(String text) {

        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case 1:
                        String s=(String)msg.obj;
                        if(!s.equals("null")){
                            String[] S=s.split(",");
                            String S1[]=new String[S.length/2];
                            String S2[]=new String[S.length/2];
                            for(int i=0;i<S.length/2;i++){
                                S1[i]=S[i];
                                S2[i]=S[i+S.length/2];
                            }
                            setListView(S1,S2);
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
        A.sendRequestWithHttpClientInformation("catalog",text);
    }

    public void setListView(String category_strings[],final String S[]){
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, category_strings));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Browse.class);
                intent.putExtra("id", S[position]);
                startActivity(intent);
            }
        });
    }
}
