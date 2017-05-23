package com.example.ccx0.p4;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.RequestQueue;
import android.os.Handler;
import android.os.Message;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by CCx0 on 0019.2016.7.19.
 */
public class Connect {
    private Handler handler;

    Connect(Handler handler1) {
        handler=handler1;
    }

    public void sendRequestWithHttpClientImages(){
        new Thread(new Runnable(){

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try{
                    HttpClient httpClient=new DefaultHttpClient() ;
                    httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);// 请求超时
                    httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);// 读取超时
                    HttpGet httpGet=new HttpGet(Data.urlservics+"id=homeshow");
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    String response="";

                    if(httpResponse.getStatusLine().getStatusCode()==200){
                        HttpEntity entity=httpResponse.getEntity();
                        response= EntityUtils.toString(entity,"utf-8");
                    }
                    Message message=new Message();
                    message.what=2;
                    message.obj=response;
                    handler.sendMessage(message);

                    String[] S=response.split(",");
                    int a[]=new int[S.length];
                    for(int i=0;i<S.length;i++)
                        a[i]=Integer.parseInt(S[i]);

                    HttpClient httpClient1=new DefaultHttpClient();
                    Bitmap bitmap[][]=new Bitmap[a[S.length-1]][100];
                    for(int i=0;i<a[S.length-1];i++){
                        for(int j=0;j<a[i];j++) {
                            HttpGet httpGet1=new HttpGet(Data.urlimages+"Goods/"+i+"_"+j+".jpg");
                            HttpResponse httpResponse1=httpClient1.execute(httpGet1);

                            if(httpResponse1.getStatusLine().getStatusCode()==200){
                                HttpEntity entity = httpResponse1.getEntity();
                                InputStream in = entity.getContent();
                                bitmap[i][j] = BitmapFactory.decodeStream(in);
                            }
                        }
                    }
                    Message message1=new Message();
                    message1.what=3;
                    message1.obj=bitmap;
                    handler.sendMessage(message1);
                }
                catch(Exception e){
                    Message message=new Message();
                    message.what=4;
                    handler.sendMessage(message);
                    e.printStackTrace();
                }
            }
        }).start();
    } //多图片获取

    public void sendRequestWithHttpClientImages1(){
        new Thread(new Runnable(){

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try{
                    HttpClient httpClient=new DefaultHttpClient() ;
                    httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);// 请求超时
                    httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);// 读取超时

                    HttpClient httpClient2=new DefaultHttpClient();
                    Bitmap bitmap1[]=new Bitmap[4];
                    for(int i=0;i<4;i++){
                        HttpGet httpGet2=new HttpGet(Data.urlimages+"Roll/"+i+".jpg");
                        HttpResponse httpResponse2=httpClient2.execute(httpGet2);

                        if(httpResponse2.getStatusLine().getStatusCode()==200){
                            HttpEntity entity = httpResponse2.getEntity();
                            InputStream in = entity.getContent();
                            bitmap1[i] = BitmapFactory.decodeStream(in);
                        }
                    }
                    Message message2=new Message();
                    message2.what=1;
                    message2.obj=bitmap1;
                    handler.sendMessage(message2);
                }
                catch(Exception e){
                    Message message=new Message();
                    message.what=4;
                    handler.sendMessage(message);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendRequestWithHttpClientRegisterLogin(final String id,final String username,final String key){
        new Thread(new Runnable(){

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try{
                    HttpClient httpClient=new DefaultHttpClient() ;
                    HttpPost httppost = new HttpPost(Data.urlservics);
                    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                    formparams.add(new BasicNameValuePair("id", id));
                    formparams.add(new BasicNameValuePair("key", key));
                    UrlEncodedFormEntity uefEntity= new UrlEncodedFormEntity(formparams, HTTP.UTF_8);
                    httppost.setEntity(uefEntity);
                    httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);// 请求超时
                    httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);// 读取超时
                    HttpResponse httpResponse = httpClient.execute(httppost);
                    if(httpResponse.getStatusLine().getStatusCode()==200){
                        HttpEntity entity=httpResponse.getEntity();
                        String response= EntityUtils.toString(entity,"utf-8");
                        Message message=new Message();
                        message.what=1;
                        message.obj=response;
                        handler.sendMessage(message);

                        sendRequestWithHttpClientshopcartset("shopcartset",username);
                    }


                }catch(Exception e){
                    Message message=new Message();
                    message.what=2;
                    handler.sendMessage(message);
                    e.printStackTrace();
                }
            }
        }).start();
    } //登陆注册

    public void sendRequestWithHttpClientInformation(final String id,final String key){
        new Thread(new Runnable(){

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try{
                    HttpClient httpClient=new DefaultHttpClient() ;
                    HttpPost httppost = new HttpPost(Data.urlservics);
                    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                    formparams.add(new BasicNameValuePair("id", id));
                    formparams.add(new BasicNameValuePair("key", key));
                    UrlEncodedFormEntity uefEntity= new UrlEncodedFormEntity(formparams, HTTP.UTF_8);
                    httppost.setEntity(uefEntity);
                    httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);// 请求超时
                    httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);// 读取超时
                    HttpResponse httpResponse = httpClient.execute(httppost);
                    if(httpResponse.getStatusLine().getStatusCode()==200){
                        HttpEntity entity=httpResponse.getEntity();
                        String response= EntityUtils.toString(entity,"utf-8");
                        Message message=new Message();
                        message.what=1;
                        message.obj=response;
                        handler.sendMessage(message);
                    }
                }catch(Exception e){
                    Message message=new Message();
                    message.what=2;
                    handler.sendMessage(message);
                    e.printStackTrace();
                }
            }
        }).start();
    } //商品展示search

    public void sendRequestWithHttpClientshopcartadd(final String id,final String shopcartinfor){
        new Thread(new Runnable(){
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try{
                    HttpClient httpClient=new DefaultHttpClient() ;
                    HttpPost httppost = new HttpPost(Data.urlservics);
                    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                    formparams.add(new BasicNameValuePair("id", id));
                    formparams.add(new BasicNameValuePair("username", Data.username));
                    formparams.add(new BasicNameValuePair("shopcartinfor", shopcartinfor));
                    UrlEncodedFormEntity uefEntity= new UrlEncodedFormEntity(formparams, HTTP.UTF_8);
                    httppost.setEntity(uefEntity);
                    httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);// 请求超时
                    httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);// 读取超时
                    HttpResponse httpResponse = httpClient.execute(httppost);
                    if(httpResponse.getStatusLine().getStatusCode()==200){
                        HttpEntity entity=httpResponse.getEntity();
                        String response= EntityUtils.toString(entity,"utf-8");
                        Message message=new Message();
                        message.what=1;
                        message.obj=response;
                        handler.sendMessage(message);
                    }
                }catch(Exception e){
                    Message message=new Message();
                    message.what=2;
                    handler.sendMessage(message);
                    e.printStackTrace();
                }
            }
        }).start();
    } //上传购物车信息

    public void sendRequestWithHttpClientImage(){
        new Thread(new Runnable(){
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try{
                    HttpClient httpClient=new DefaultHttpClient();
                    httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);// 请求超时
                    httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);// 读取超时
                    Bitmap bitmap[]=new Bitmap[Data.size];
                    for(int i=0;i<Data.size;i++){
                        String[] S=Data.shopcartinfor[i].split(",");

                        HttpGet httpGet=new HttpGet(Data.urlimages+"Goods/"+S[0]+".jpg");
                        HttpResponse httpResponse=httpClient.execute(httpGet);

                        if(httpResponse.getStatusLine().getStatusCode()==200){
                            HttpEntity entity = httpResponse.getEntity();
                            InputStream in = entity.getContent();
                            bitmap[i]= BitmapFactory.decodeStream(in);
                        }
                    }
                    Message message=new Message();
                    message.what=1;
                    message.obj=bitmap;
                    handler.sendMessage(message);
                }catch(Exception e){
                    Message message=new Message();
                    message.what=2;
                    handler.sendMessage(message);
                    e.printStackTrace();
                }
            }
        }).start();
    } //购物车图片信息

    public void sendRequestWithHttpClientshopcartset(final String id,final String username){
        new Thread(new Runnable(){

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try{
                    HttpClient httpClient=new DefaultHttpClient() ;
                    HttpPost httppost = new HttpPost(Data.urlservics);
                    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                    formparams.add(new BasicNameValuePair("id", id));
                    formparams.add(new BasicNameValuePair("username", username));
                    UrlEncodedFormEntity uefEntity= new UrlEncodedFormEntity(formparams, HTTP.UTF_8);
                    httppost.setEntity(uefEntity);
                    httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);// 请求超时
                    httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);// 读取超时
                    HttpResponse httpResponse = httpClient.execute(httppost);
                    if(httpResponse.getStatusLine().getStatusCode()==200){
                        HttpEntity entity=httpResponse.getEntity();
                        String response= EntityUtils.toString(entity,"utf-8");

                        if(!response.equals("null")){
                            String[] S=response.split("@");
                            Data.size=S.length/2;
                            for(int i=0;i<Data.size;i++){
                                Data.shopcartinfor[i]=S[i];
                                Data.count[i]=Integer.parseInt(S[i+Data.size]);
                            }
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    } //购物车信息初始化

    public void sendRequestWithHttpClienText(){
        new Thread(new Runnable(){

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try{
                    HttpClient httpClient=new DefaultHttpClient() ;
                    httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);// 请求超时
                    httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);// 读取超时
                    HttpGet httpGet=new HttpGet(Data.urltext);
                    HttpResponse httpResponse=httpClient.execute(httpGet);

                    if(httpResponse.getStatusLine().getStatusCode()==200){
                        HttpEntity entity=httpResponse.getEntity();
                        String response= EntityUtils.toString(entity,"utf-8");
                        Message message=new Message();
                        message.what=1;
                        message.obj=response;
                        handler.sendMessage(message);
                    }
                }catch(Exception e){
                    Message message=new Message();
                    message.what=2;
                    handler.sendMessage(message);
                    e.printStackTrace();
                }
            }
        }).start();
    } //商品展示search

    public void sendRequestWithHttpClientJumpImage(){
        new Thread(new Runnable(){
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try{
                    HttpClient httpClient=new DefaultHttpClient();
                    httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);// 请求超时
                    httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);// 读取超时
                    Bitmap bitmap=null;

                    HttpGet httpGet=new HttpGet(Data.urlimages+"Welcome/welcome.jpg");
                    HttpResponse httpResponse=httpClient.execute(httpGet);

                    if(httpResponse.getStatusLine().getStatusCode()==200){
                        HttpEntity entity = httpResponse.getEntity();
                        InputStream in = entity.getContent();
                        bitmap= BitmapFactory.decodeStream(in);
                    }

                    Message message=new Message();
                    message.what=1;
                    message.obj=bitmap;
                    handler.sendMessage(message);
                }catch(Exception e){
                    Message message=new Message();
                    message.what=2;
                    handler.sendMessage(message);
                    e.printStackTrace();
                }
            }
        }).start();
    } //跳转图片
}
