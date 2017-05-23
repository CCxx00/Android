package com.example.ccx0.p4;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by CCx0 on 0017.2016.7.17.
 */
public class Search extends AppCompatActivity{

    private EditText mEtSearch;// 输入搜索内容
    private Button mBtnClearSearchText;// 清空搜索信息的按钮
    private LinearLayout mLayoutClearSearchText;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        searchset();
    }

    public void searchset(){

        listView=(ListView)findViewById(R.id.listView_search_layout);
        mEtSearch = (EditText)findViewById(R.id.et_search_layout);
        mBtnClearSearchText = (Button)findViewById(R.id.btn_clear_search_text);
        mLayoutClearSearchText = (LinearLayout)findViewById(R.id.layout_clear_search_text);
        LinearLayout layout=(LinearLayout)findViewById(R.id.btn_back_search_layout);

        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mEtSearch.getText().length() > 0) getListView(mEtSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                int textLength = mEtSearch.getText().length();
                if (textLength > 0) {
                    mLayoutClearSearchText.setVisibility(View.VISIBLE);
                } else {
                    mLayoutClearSearchText.setVisibility(View.GONE);
                    listView.setAdapter(null);
                }
            }
        });

        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_SEND ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER))
                {
                    Toast.makeText(getApplicationContext(), mEtSearch.getText().toString(), Toast.LENGTH_SHORT).show();
                    return false;
                }
                return false;
            }
        });

        mBtnClearSearchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtSearch.setText("");
                mLayoutClearSearchText.setVisibility(View.GONE);
                listView.setAdapter(null);
            }
        });

        mLayoutClearSearchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (mEtSearch.getText().length() > 0) getListView(mEtSearch.getText().toString());
                }
                return false;
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    } //自定义搜索

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
        A.sendRequestWithHttpClientInformation("search",text);
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
