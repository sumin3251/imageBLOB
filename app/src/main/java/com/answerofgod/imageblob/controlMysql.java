package com.answerofgod.imageblob;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by user on 2017-07-26.
 */
public class controlMysql extends Thread {

    public static boolean active=false;
    Handler mHandler;
    String url=null;
    String serverIP="localhost";  //change yours
    String phppath="/var/www/html/";
    String add_url="addImage.php?";

    public controlMysql(String image){ //이미지 추가

        mHandler= new Handler(Looper.getMainLooper());
        String imageurl="image="+image;

        url=serverIP+phppath+add_url+imageurl;
        Log.e("add to image",url);
    }



    /**
     * Calls the <code>run()</code> method of the Runnable object the receiver
     * holds. If no Runnable is set, does nothing.
     *
     * @see Thread#start
     */
    @Override
    public void run() {
        super.run();
        if(active){

            StringBuilder jsonHtml = new StringBuilder();
            try {
                URL phpUrl = new URL(url);

                HttpURLConnection conn = (HttpURLConnection)phpUrl.openConnection();

                if ( conn != null ) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    //conn.setRequestProperty("Content-Length", Integer.toString(url.length()));

                    if ( conn.getResponseCode() == HttpURLConnection.HTTP_OK ) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        while ( true ) {
                            String line = br.readLine();
                            if ( line == null )
                                break;
                            jsonHtml.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
                Log.e("controlMysql","success"+jsonHtml.toString()+"end");
                //Log.e("controlMysql",jsonHtml.toString());
                show(jsonHtml.toString());
            } catch ( Exception e ) {
              //  e.printStackTrace();
                Log.e("controlMysql","fail"+e.toString());
                show("false");
            }

        }



    }

    void show(final String result){

        mHandler.post(new Runnable(){

            @Override
            public void run() {
                MainActivity.add_image(result);

            }
       });

    }


}
