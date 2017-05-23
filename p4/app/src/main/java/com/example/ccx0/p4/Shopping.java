package com.example.ccx0.p4;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by CCx0 on 0009.2016.7.9.
 */
public class Shopping extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shopcart_layout, container, false);//获得视图

        toolbarset(view);
        setview(inflater,view);

        return view;
    }

    public void toolbarset(View view) {
        TextView tv=(TextView)view.findViewById(R.id.include_shopcart_toobar).findViewById(R.id.toolbar_textview_1);
        tv.setText("购物车");
        tv.setTextSize(18);

        LinearLayout layout=(LinearLayout)view.findViewById(R.id.shopcart_layout1_layout);
        if(Data.size!=0) layout.setVisibility(View.GONE);
        else layout.setVisibility(View.VISIBLE);
    }

    public void setview1(final LayoutInflater inflater, final View view,final Bitmap image[]) {
        DensityUtil A=new DensityUtil();
        LinearLayout lin = (LinearLayout)view.findViewById(R.id.include_shopcart_layout);
        LinearLayout l=(LinearLayout)view.findViewById(R.id.shopcart_layout_sum);

        final LinearLayout shopcart_layout1[]=new LinearLayout[100];
        LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(A.width(view), LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        for(int i=0;i<Data.size;i++){
            shopcart_layout1[i]=(LinearLayout) inflater.inflate(R.layout.shopcart_layout1_1, null).findViewById(R.id.shopcart_layout1_1);
            LinearLayout lv=(LinearLayout)shopcart_layout1[i].findViewById(R.id.shopcart_layout1_2_list);

            ImageView imageView1=(ImageView)shopcart_layout1[i].findViewById(R.id.image_shopcart_layout1_2);
            LinearLayout imageView=(LinearLayout) shopcart_layout1[i].findViewById(R.id.shopcart_layout1_2_minus);
            final EditText editText=(EditText)shopcart_layout1[i].findViewById(R.id.shopcart_layout1_2_edit);
            TextView textView=(TextView)shopcart_layout1[i].findViewById(R.id.shopcart_layout1_2_text);
            final TextView textView1=(TextView)shopcart_layout1[i].findViewById(R.id.shopcart_layout1_2_text1);
            final TextView textView2=(TextView)view.findViewById(R.id.text_shopcart_layout);
            final TextView textView3=(TextView)view.findViewById(R.id.text1_shopcart_layout);
            final CheckBox checkBox=(CheckBox)shopcart_layout1[i].findViewById(R.id.shopcart_layout1_2_checkbox);

            String[] S=Data.shopcartinfor[i].split(",");
            imageView1.setImageBitmap(image[i]);
            textView.setText(S[1]);
            textView1.setText(S[2]);
            editText.setText(Data.count[i]+"");

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        textView2.setText((Float.parseFloat(textView2.getText().toString())+Float.parseFloat(textView1.getText().toString())*Float.parseFloat(editText.getText().toString()))+"");
                        textView3.setText((Integer.parseInt(textView3.getText().toString())+1)+"");
                    }
                    else {
                        textView2.setText((Float.parseFloat(textView2.getText().toString())-Float.parseFloat(textView1.getText().toString())*Float.parseFloat(editText.getText().toString()))+"");
                        textView3.setText((Integer.parseInt(textView3.getText().toString())-1)+"");
                    }
                }
            });

            final String s=Data.shopcartinfor[i];
            final int d=i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Integer.parseInt(editText.getText().toString())>1) {
                        editText.setText((Integer.parseInt(editText.getText().toString())-1)+"");
                        Data.count[d]--;
                        shopcartminus(s);
                        if(checkBox.isChecked()) textView2.setText((Float.parseFloat(textView2.getText().toString())-Float.parseFloat(textView1.getText().toString()))+"");
                    }
                }
            });
            imageView=(LinearLayout) shopcart_layout1[i].findViewById(R.id.shopcart_layout1_2_add);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Integer.parseInt(editText.getText().toString())<100){
                        editText.setText((Integer.parseInt(editText.getText().toString())+1)+"");
                        Data.count[d]++;
                        shopcartadd(s);
                        if(checkBox.isChecked()) textView2.setText((Float.parseFloat(textView2.getText().toString())+Float.parseFloat(textView1.getText().toString()))+"");
                    }
                }
            });
            lv.setLayoutParams(para);
            lin.addView(shopcart_layout1[i]);
        }

        final LinearLayout lin1=lin;
        final LinearLayout l1=l;
        final int size=Data.size;
        RelativeLayout rl;
        for(int i=0;i<Data.size;i++){
            final int j=i;
            rl=(RelativeLayout)shopcart_layout1[i].findViewById(R.id.shopcart_layout1_2_deletebutton);
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lin1.removeView(shopcart_layout1[j]);
                    deleteshopcart(Data.username+","+Data.shopcartinfor[j]);
                    for(int k=j;k<Data.size-1;k++) {
                        Data.shopcartinfor[k]=Data.shopcartinfor[k+1];
                        Data.count[k]=Data.count[k+1];
                    }
                    Data.size--;
                    if(Data.size==0) l1.setVisibility(View.GONE);
                    Fragment frg = getFragmentManager().findFragmentByTag ("shopcart");
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach ( frg );
                    ft.attach ( frg );
                    ft.commit ();
                }
            });
        }

        CheckBox ck=(CheckBox)view.findViewById(R.id.shopcart_layout_checkbox);
        ck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for(int i=0;i<size;i++){
                    CheckBox cb=(CheckBox)shopcart_layout1[i].findViewById(R.id.shopcart_layout1_2_checkbox);
                    cb.setChecked(isChecked);
                }
            }
        });

        final TextView textView2=(TextView)view.findViewById(R.id.text_shopcart_layout);
        RelativeLayout relativeLayout=(RelativeLayout)view.findViewById(R.id.buy_shopcart_layout);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!textView2.getText().toString().equals("0")){
                    String S="",S1="",S3="",S4="";
                    for(int i=0;i<Data.size;i++){
                        TextView textView=(TextView)shopcart_layout1[i].findViewById(R.id.shopcart_layout1_2_text);
                        CheckBox checkBox=(CheckBox)shopcart_layout1[i].findViewById(R.id.shopcart_layout1_2_checkbox);
                        if(checkBox.isChecked()) {
                            S=S+textView.getText().toString()+" ";
                            String[] S2=Data.shopcartinfor[i].split(",");
                            S1=S1+S2[0]+",";
                            S3=S3+Data.count[i]+",";
                            S4=S4+i+",";
                        }
                    }
                    S1=S1+S3;
                    Intent intent = new Intent(v.getContext(), Purchase.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("flag","1");
                    bundle.putString("id", S1);
                    bundle.putString("proname", S);
                    bundle.putString("price",textView2.getText().toString());
                    bundle.putString("S4",S4);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
                }
                else Toast.makeText(getActivity(), "请选择商品!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setview(final LayoutInflater inflater, final View view){
        LinearLayout l=(LinearLayout)view.findViewById(R.id.shopcart_layout_sum);
        l.setVisibility(View.GONE);
        if(Data.size>0) l.setVisibility(View.VISIBLE);

        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case 1:
                        Bitmap[] s=(Bitmap[])msg.obj;
                        setview1(inflater,view,s);
                        break;
                    case 2:
                        Toast.makeText(getActivity(), "服务器连接失败，请检查网络连接后再试", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        Connect A=new Connect(handler);
        A.sendRequestWithHttpClientImage();
    }

    public void deleteshopcart(String S){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case 1:
                        break;
                    case 2:
                        Toast.makeText(getActivity(), "服务器连接失败，请检查网络连接后再试", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        Connect A=new Connect(handler);
        A.sendRequestWithHttpClientInformation("deleteshopcart",S);
    }

    public void shopcartadd(final String shopcartinfor){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case 1:
                        break;
                    case 2:
                        Toast.makeText(getActivity(), "服务器连接失败，请检查网络连接后再试", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        Connect A=new Connect(handler);
        A.sendRequestWithHttpClientshopcartadd("shopcartadd",shopcartinfor);
    }

    public void shopcartminus(final String shopcartinfor){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case 1:
                        break;
                    case 2:
                        Toast.makeText(getActivity(), "服务器连接失败，请检查网络连接后再试", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        Connect A=new Connect(handler);
        A.sendRequestWithHttpClientshopcartadd("shopcartminus",shopcartinfor);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment frg = getFragmentManager().findFragmentByTag ("shopcart");
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach ( frg );
        ft.attach ( frg );
        ft.commit ();
    }
}
