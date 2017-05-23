package com.example.ccx0.p4;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CCx0 on 0009.2016.7.9.
 */
public class User extends Fragment{

    private View view1, view2;
    private ViewPager viewPager;  //对应的viewPager
    private List<View> viewList;//view数组
    private ListView listView;
    private TextView tv,tv1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_layout, container, false);//获得视图

        toolbarset(view);
        viewset(view);
        viewpagerset(inflater,view);
        listView=(ListView)view1.findViewById(R.id.listView_user_layout_viewpager1_1);
        tv=(TextView)view1.findViewById(R.id.textView_user_layout_viewpager1_1);
        tv1=(TextView)view2.findViewById(R.id.textView_user_layout_viewpager1_1);
        if(Data.login==1) getListView();
        else tv.setText("无订单");
        tvset();
        listenset(view);

        return view;
    }

    public void toolbarset(View view) {
        TextView tv=(TextView)view.findViewById(R.id.include_user_layout_toolbar).findViewById(R.id.toolbar_textview_1);
        tv.setText("用户");
        tv.setTextSize(18);
    }

    public void viewset(View view){
        TextView textView1=(TextView)view.findViewById(R.id.user_layout_textv1);
        TextView textView2=(TextView)view.findViewById(R.id.user_layout_textv2);

        if(Data.login==0){
            textView1.setText("点击头像登录");
            textView2.setText("");
        }
        else {
            textView1.setText(Data.username);
            textView2.setText("用户设置>");
        }
    }

    public void viewpagerset(LayoutInflater inflater,View view){
        viewPager = (ViewPager)view.findViewById(R.id.user_layout_viewpager);
        view1 = inflater.inflate(R.layout.user_layout_viewpager1, null);
        view2 = inflater.inflate(R.layout.user_layout_viewpager1,null);

        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
        viewList.add(view1);
        viewList.add(view2);

        PagerAdapter pagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return viewList.size();
            } //返回要滑动的VIew的个数

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewList.get(position));
            } //从当前container中删除指定位置（position）的View

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            } //将当前视图添加到container中，返回当前View
        }; //适配器重写
        viewPager.setAdapter(pagerAdapter);
    } //滑动切换

    public void listenset(final View view) {
        ImageView imageView=(ImageView)view.findViewById(R.id.user_layout_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Data.login==0){
                    Intent intent = new Intent(view.getContext(), Login.class);
                    startActivityForResult(intent, 1);
                }
            }
        });

        TextView textView=(TextView)view.findViewById(R.id.user_layout_textv2);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), Logindown.class);
                startActivityForResult(intent, 1);
            }
        });

        RelativeLayout button=(RelativeLayout) view.findViewById(R.id.user_layout_button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Data.login==1){
                    Intent intent = new Intent(view.getContext(), Friend.class);
                    startActivityForResult(intent, 1);
                }
                else Toast.makeText(getActivity(), "请登陆后使用", Toast.LENGTH_SHORT).show();
            }
        });

        RelativeLayout button1=(RelativeLayout) view.findViewById(R.id.user_layout_button2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Data.login==1){
                    Intent intent = new Intent(view.getContext(), Goodsrecommend.class);
                    startActivityForResult(intent, 1);
                }
                else Toast.makeText(getActivity(), "请登陆后使用", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment frg = getFragmentManager().findFragmentByTag ("user");
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach ( frg );
        ft.attach ( frg );
        ft.commit ();
    }

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
                            setListView(S);
                        }
                        else {
                            listView.setAdapter(null);
                            tv.setText("无订单");
                        }
                        break;
                    case 2:
                        Toast.makeText(getActivity(), "服务器连接失败，请检查网络连接后再试", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        Connect A=new Connect(handler);
        A.sendRequestWithHttpClientInformation("order",Data.username);
    }

    public void setListView(String category_strings[]){
        listView.setAdapter(new ArrayAdapter<String>(view1.getContext(), android.R.layout.simple_list_item_1, category_strings));
    }

    public void tvset(){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case 1:
                        String s=(String)msg.obj;
                        tv1.setText(s);
                        break;
                    case 2:
                        break;
                }
            }
        };

        Connect A=new Connect(handler);
        A.sendRequestWithHttpClienText();
    }
}
