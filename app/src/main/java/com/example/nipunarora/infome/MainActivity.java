package com.example.nipunarora.infome;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;


import android.app.Application;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.SystemRequirementsChecker;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button samplenotification;
    RequestQueue queue1;
    ArrayList<Banner> imgSource;
    Banner b1,b2,b3;
   /* Boolean statusNotificationPayload=Boolean.FALSE;
    Boolean notificationStatus=Boolean.FALSE;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
           samplenotification=(Button)findViewById(R.id.b1);
        queue1=VolleySingleton.getInstance(this).getRequestQueue();
        imgSource=new ArrayList<Banner>();
        b1=new Banner();
        b2=new Banner();
        b3=new Banner();
        imgSource.add(0, b1);
        imgSource.add(1, b2);
        imgSource.add(2,b3);
        MyApplication.isactive=Boolean.TRUE;

        //Log.d("banner url",b1.url);
//  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.d("statusactivity", "the activity was stopped");
        MyApplication.isactive=Boolean.FALSE;

        Log.d("InonStop",MyApplication.isactive.toString());

    }
    {

    }
    public void showNotification(View view) {

        Log.d("urlwehave", String.format("http://192.168.1.15/json.php?id=%s","119"));
        //Send the volley request to get the image urls and know more status and link
        StringRequest getNotificationPayload = new StringRequest(Request.Method.GET, String.format("http://192.168.1.15/json.php?id=%s","119"),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try{
                            Log.d("response status","received");
                            JSONObject res=new JSONObject(response);
                            JSONObject img=new JSONObject(res.getString("images"));
                            //To avoid loops we are hard coding it
                            imgSource.get(0).url=img.getString("img1");
                            imgSource.get(1).url=img.getString("img2");
                            imgSource.get(2).url=img.getString("img3");

                            //edit this pasrt of the code
                            // ArrayList<String> imgsrc=new ArrayList<String>().add
                            buildNotification("Sample Notification","Welcome Aboard",imgSource,res.getString("knowmore"));
                            //Log.d("sent","the function call was made");





                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }

                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        // Log.d("Error.Response","hahah");
                        Log.d("error",error.toString());
                    }
                }
        );
        queue1.add(getNotificationPayload);

    }
    public void buildNotification(String title,String message,ArrayList<Banner> imgsrcserver,String knowmoreurlserver)
    {   Log.d("funcstatus","this worked");
        final Intent notifyIntent = new Intent(this, notification.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notifyIntent.putExtra("imgsource",imgsrcserver);
        notifyIntent.putExtra("knowmoreurl",knowmoreurlserver);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
