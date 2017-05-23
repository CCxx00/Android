package com.example.ccx0.p4;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CCx0 on 0012.2016.7.12.
 */
public class Browse extends AppCompatActivity {

    private View view1, view2;//, view3;
    private ViewPager viewPager;  //对应的viewPager
    private List<View> viewList;//view数组

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_layout);

        toolbarset();
        viewpagerset();
        setView();
    }

    public void toolbarset(){
        DensityUtil A=new DensityUtil();//px与dp转化

        TextView tv1=(TextView)findViewById(R.id.includ_browse_layout_toolbar).findViewById(R.id.toolbar_textview_1);
        TextView tv2=(TextView)findViewById(R.id.includ_browse_layout_toolbar).findViewById(R.id.toolbar_textview_2);
        //TextView tv3=(TextView)findViewById(R.id.includ_browse_layout_toolbar).findViewById(R.id.toolbar_textview_3);

        LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(A.dip2px(this,0), LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        para.setMargins(0,0,A.dip2px(this,10),0);
        tv1.setText("商品");
        tv1.setLayoutParams(para);
        tv2.setText("详情");
        tv2.setLayoutParams(para);
        //tv3.setText("评论");
        //tv3.setLayoutParams(para);

        Toolbar toolbar = (Toolbar) findViewById(R.id.includ_browse_layout_toolbar).findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    } //toolbar设定

    public void viewpagerset(){
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        LayoutInflater inflater=getLayoutInflater();
        view1 = inflater.inflate(R.layout.browse_layout_viewpager1, null);
        view2 = inflater.inflate(R.layout.browse_layout_viewpager1,null);
        //view3 = inflater.inflate(R.layout.browse_layout_viewpager1, null);

        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
        viewList.add(view1);
        viewList.add(view2);
        //viewList.add(view3);

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

    public void setView(){
        final TextView v1tv=(TextView)view1.findViewById(R.id.browse_layout_view_pager1_1_textView);
        final TextView v1tv1=(TextView)view1.findViewById(R.id.browse_layout_view_pager1_1_textView1);
        final TextView v1tv2=(TextView)view2.findViewById(R.id.browse_layout_view_pager1_1_textView);

        Intent intent = getIntent();   // 获取 Intent
        final String id= intent.getStringExtra("id");

        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case 1:
                        String s=(String)msg.obj;
                        if(s.equals("null")) v1tv.setText("暂无此商品信息，请等待后台添加!");
                        else{
                            rollpagerset();
                            String[] S=s.split(",");
                            v1tv.setText(S[0]);
                            v1tv1.setText("￥:"+S[1]);
                            v1tv2.setText(S[2]);
                            listenset(id,S[0],S[1]);
                        }
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "服务器连接失败，请检查网络连接后再试", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        Connect A=new Connect(handler);
        A.sendRequestWithHttpClientInformation("commodityshow",id);
    } //view页面设置

    public void rollpagerset(){
        DensityUtil A=new DensityUtil();//px与dp转化

        RollPagerView mRollViewPager;

        mRollViewPager = (RollPagerView)view1.findViewById(R.id.browse_layout_view_pager1_1_rollviewpager);

        LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, A.dip2px(this,200), 1.0f);
        mRollViewPager.setLayoutParams(para);
        mRollViewPager.setPlayDelay(0);//设置播放时间间隔
        mRollViewPager.setAnimationDurtion(1000);//设置透明度
        mRollViewPager.setAdapter(new TestNormalAdapter());//设置适配器

        //mRollViewPager.setHintView(new IconHintView(this, R.drawable.point_focus, R.drawable.point_normal));//自定义指示器图片
        mRollViewPager.setHintView(new ColorPointHintView(this, Color.YELLOW, Color.WHITE));//设置圆点指示器颜色
        //mRollViewPager.setHintView(new TextHintView(this));//设置文字指示器
        //mRollViewPager.setHintView(null);//隐藏指示器
    } //自动播放

    private class TestNormalAdapter extends StaticPagerAdapter {
        private int[] imgs={
            R.drawable.icon_1,
                    R.drawable.icon_2,
                    R.drawable.icon_3,
                    R.drawable.icon_4,
        };

        @Override
        public View getView(ViewGroup container, final int position) {
            ImageView view = new ImageView(container.getContext());
            view.setImageResource(imgs[position]);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position){
                        case 0:Toast.makeText(getApplicationContext(), "0", Toast.LENGTH_SHORT).show();break;
                        case 1:Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();break;
                        case 2:Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT).show();break;
                        case 3:Toast.makeText(getApplicationContext(), "3", Toast.LENGTH_SHORT).show();break;
                    }
                }
            });
            return view;
        }


        @Override
        public int getCount() {
            return imgs.length;
        }
    } //自动播放

    public void listenset(final String id,final String proname,final String price){
        RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.browse_layout_bottom_buy);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Data.login==1){
                    Intent intent = new Intent(v.getContext(), Purchase.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("flag","0");
                    bundle.putString("id", id+",1");
                    bundle.putString("proname", proname);
                    bundle.putString("price", price);
                    bundle.putString("S4","");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),"未登录，请登录后使用", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplication(), Login.class);
                    startActivity(intent);
                }
            }
        });

        RelativeLayout relativeLayout1=(RelativeLayout)findViewById(R.id.browse_layout_bottom_add);
        relativeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Data.login==1){
                    int flag=1,i=0;
                    String S=id+","+proname+","+price;
                    for(;i<Data.size;i++){
                        if(Data.shopcartinfor[i].equals(S)){
                            flag=0;
                            break;
                        }
                    }
                    if(flag==1) {
                        Data.shopcartinfor[Data.size]=S;
                        Data.count[Data.size]=1;
                        Data.size++;
                    }
                    else Data.count[i]++;
                    shopcartadd(S);
                }
                else {
                    Toast.makeText(getApplicationContext(),"未登录，请登录后使用", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplication(), Login.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void shopcartadd(final String shopcartinfor){
        Handler handler=new Handler(){
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

        Connect A=new Connect(handler);
        A.sendRequestWithHttpClientshopcartadd("shopcartadd",shopcartinfor);
    }
}
