package com.example.ccx0.p4;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.io.ByteArrayOutputStream;
import java.util.Date;

/**
 * Created by CCx0 on 0009.2016.7.9.
 */
public class Home extends Fragment{

    private RollPagerView mRollViewPager;
    private Bitmap bitmap[][]=new Bitmap[100][100],bitmap1[]=new Bitmap[4];
    private int b=0;
    private final String[] category_strings = {"服装","食品","图书","数码"};
    private LinearLayout lin;
    private LinearLayout layout_home_layout2[]=new LinearLayout[100];
    private LinearLayout layout_home_commodity[][]=new LinearLayout[100][100];
    private View view1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.home_layout, container, false);//获得视图

        view1=view;
        Data A=new Data();

        searchset(view);
        rollpagerset(view);

        layoutset(inflater,view,0);
        buttonlisten(view);

        return view;
    }

    public void layoutset(final LayoutInflater inflater, final View view, final int flag){
        final int a[]=new int[100];
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case 2:
                        String s=(String)msg.obj;
                        String[] S=s.split(",");
                        b=S.length-1;
                        for(int i=0;i<S.length;i++)
                            a[i]=Integer.parseInt(S[i]);
                        break;
                    case 3:
                        bitmap=(Bitmap[][]) msg.obj;
                        if(flag==0) layoutset1(inflater,view,a,a[b],1);
                        break;
                    case 4:
                        Toast.makeText(getActivity(), "服务器连接失败，请检查网络连接后再试", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        Connect A=new Connect(handler);
        A.sendRequestWithHttpClientImages();
    } //动态布局

    public void layoutset1(LayoutInflater inflater, final View view,int a[],int sum,int flag){
        DensityUtil A=new DensityUtil();//px与dp转化

        lin = (LinearLayout)view.findViewById(R.id.home_category_layout1);//被添加控件布局

        //SaveReadsd B=new SaveReadsd();

        for(int i=0;i<sum;i++) {
            layout_home_layout2[i] = (LinearLayout) inflater.inflate(R.layout.home_layout2, null).findViewById(R.id.home_layout2);//获取需要添加的布局（控件)
            LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(A.width(view), LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            para.setMargins(0,A.dip2px(view.getContext(),5),0,0);
            layout_home_layout2[i].setBackground(getResources().getDrawable(R.color.color1));
            layout_home_layout2[i].setLayoutParams(para);
            LinearLayout lin_home_layout2_1 = (LinearLayout)layout_home_layout2[i].findViewById(R.id.include_home_layout2);
            for(int j=0;j<a[i];j++) {
                layout_home_commodity[i][j] = (LinearLayout) inflater.inflate(R.layout.home_layout2_2, null).findViewById(R.id.home_layout2_2);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(A.dip2px(view.getContext(),100), LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                param.setMargins(A.dip2px(view.getContext(),13),0,0,0);
                layout_home_commodity[i][j].setLayoutParams(param);
                ImageView imageView=(ImageView)layout_home_commodity[i][j].findViewById(R.id.home_commodity_imagev);
                if(bitmap[i][j]!=null) {
                    imageView.setImageBitmap(bitmap[i][j]);
                    //if(flag==1) B.saveBitmap(i+"_"+j+".jpg",bitmap[i][j]);
                }
                lin_home_layout2_1.addView(layout_home_commodity[i][j]);
                final String S=i+"_"+j;
                layout_home_commodity[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(view.getContext(), Browse.class);
                        intent.putExtra("id", S);
                        startActivity(intent);
                        //Toast.makeText(getActivity(),S, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            TextView tv=(TextView)layout_home_layout2[i].findViewById(R.id.home_category_textv);
            tv.setText(category_strings[i]);
            LinearLayout tv1=(LinearLayout)layout_home_layout2[i].findViewById(R.id.home_category_textv1);
            final int b=i;
            tv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(view.getContext(), Catalog.class);
                    intent.putExtra("id", b);
                    startActivity(intent);
                }
            });
            lin.addView(layout_home_layout2[i]);
        }
    } //动态布局1

    public void rollpagerset(View view){
        mRollViewPager = (RollPagerView)view.findViewById(R.id.home_layout1_roll_view_pager);

        mRollViewPager.setPlayDelay(3000);//设置播放时间间隔
        mRollViewPager.setAnimationDurtion(1000);//设置透明度
        mRollViewPager.setAdapter(new TestLoopAdapter(mRollViewPager));//设置适配器

        //mRollViewPager.setHintView(new IconHintView(this, R.drawable.point_focus, R.drawable.point_normal));//自定义指示器图片
        mRollViewPager.setHintView(new ColorPointHintView(view.getContext(), Color.YELLOW, Color.WHITE));//设置圆点指示器颜色
        //mRollViewPager.setHintView(new TextHintView(this));//设置文字指示器
        //mRollViewPager.setHintView(null);//隐藏指示器
    } //自动播放

    private class TestLoopAdapter extends LoopPagerAdapter {
        private int[] imgs = {
                R.drawable.icon_1,
                R.drawable.icon_2,
                R.drawable.icon_3,
                R.drawable.icon_4,
        };
        public TestLoopAdapter(RollPagerView viewPager) {
            super(viewPager);
        }
        @Override
        public View getView(ViewGroup container,int position) {
            final ImageView view = new ImageView(container.getContext());
            final int position1=position;

            Handler handler=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch(msg.what){
                        case 1:
                            bitmap1=(Bitmap[])msg.obj;
                            view.setImageBitmap(bitmap1[position1]);
                            break;
                        case 4:
                            Toast.makeText(getActivity(), "服务器连接失败，请检查网络连接后再试", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            };
            Connect A=new Connect(handler);
            A.sendRequestWithHttpClientImages1();

            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            final int flag=position;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int S1;
                    Intent intent = new Intent(view1.getContext(), Browse.class);
                    switch (flag) {
                        case 0:S1=0;intent.putExtra("id", S1);break;
                        case 1:S1=1;intent.putExtra("id", S1);break;
                        case 2:S1=2;intent.putExtra("id", S1);break;
                        case 3:S1=3;intent.putExtra("id", S1);break;
                    }
                    startActivity(intent);
                }
            });
            return view;
        }
        @Override
        public int getRealCount() {
            return imgs.length;
        }
    } //自动播放适配器

    public void searchset(final View view){

        LinearLayout L=(LinearLayout)view.findViewById(R.id.search_layout);
        L.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), Search.class);
                startActivity(intent);
            }
        });
    } //自定义搜索

    public void buttonlisten(final View view){
        RelativeLayout button[]=new RelativeLayout[4];
        button[0]=(RelativeLayout)view.findViewById(R.id.home_layout1_button1);
        button[1]=(RelativeLayout)view.findViewById(R.id.home_layout1_button2);
        button[2]=(RelativeLayout)view.findViewById(R.id.home_layout1_button3);
        button[3]=(RelativeLayout)view.findViewById(R.id.home_layout1_button4);

        for(int i=0;i<4;i++){
            final int a=i;
            button[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(view.getContext(), Catalog.class);
                    intent.putExtra("id", a);
                    startActivity(intent);
                }
            });
        }
    }
}
