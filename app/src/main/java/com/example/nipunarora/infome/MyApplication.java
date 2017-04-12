package com.example.nipunarora.infome;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.lang.reflect.Array;
import java.text.Format;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by nipunarora on 29/12/16.
 */
public class MyApplication extends Application {
    public static Boolean isactive=Boolean.FALSE;
    private BeaconManager beaconManager;
    RequestQueue queue1;
    ArrayList<Banner> imgSource;
    Banner b1,b2,b3;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("haha", "hahahahahah");
        queue1=VolleySingleton.getInstance(this).getRequestQueue();
        imgSource=new ArrayList<Banner>();
//        b1=new Banner("jsjsksjkj");
        try {
            b1 = new Banner();
            b2 = new Banner();
            b3 = new Banner();
            imgSource.add(0, b1);
            imgSource.add(1, b2);
            imgSource.add(2, b3);
            Log.d("banner url", b1.url);
        } catch(Exception e)
        {
            Log.d("Arraylist exception",e.toString());
        }
        //hard coding here creating two banner objects and adding them to imgsource arraylist


        //add the beacon managers
        beaconManager = new BeaconManager(getApplicationContext());
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                Log.d("entry", "you entered this range");
                //SEND THE UUID OR OTHER UNIQUE IDENTIFIER OF THE BEACON FOUND
                //String identifierFound=region.getProximityUUID().toString();
                String s = region.getIdentifier();
                Log.d("identifier", s);
                Context context = getApplicationContext();
                CharSequence text = s;
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                Log.d("status beacon", "it worked");

                showNotification(s);

            }

            @Override
            public void onExitedRegion(Region region) {
                // could add an "exit" notification too if you want (-:
//                showNotification(
//                        "Welcome to Coding Ninjas.",
//                        "Pioneer in the field of Programming Tutorials.We prepare you for Industrial Programming",
//
//                );
                Log.d("exit", "Exited this region" + region.getIdentifier());
                int duration = Toast.LENGTH_SHORT;
                CharSequence text = "exited" + region.getIdentifier();

                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();
                showNotification(region.getIdentifier());
            }
        });

// add this below:
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                Region re = new Region("QutabMinar", UUID.fromString("23A01AF0-232A-4518-9C0E-323FB773F5EF"), 36641, 40925);
                Region re1 = new Region("generic", null, null, null);
                Region re2 = new Region("Conference", UUID.fromString("23A01AF0-232A-4518-9C0E-323FB773F5EF"), 24225, 12553);
                Region re3 = new Region("PacificMall", UUID.fromString("23A01AF0-232A-4518-9C0E-323FB773F5EF"), 43197, 23035);
                //Region re4 = new Region("apnewala", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 52779, 9224);

                beaconManager.startMonitoring(re);
                beaconManager.startMonitoring(re1);
                beaconManager.startMonitoring(re2);
                beaconManager.startMonitoring(re3);
                //beaconManager.startMonitoring(re4);

            }
        });
    }
    public void showNotification(final String identifier) {
        Log.d("urlwehave", String.format("http://178.62.41.110:88/info?title=%s",identifier));
        //Send the volley request to get the image urls and know more status and link
        StringRequest getNotificationPayload = new StringRequest(Request.Method.GET, String.format("http://178.62.41.110:88/info?title=%s",identifier),
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
                            String knowmoreurlserver=res.getString("knowmore");
                            String message=res.getString("description");
                            String title=res.getString("title");
                            /*String title=res.getString("title");
                            String message=res.getString("description");*/
                            //edit this pasrt of the code
                            // ArrayList<String> imgsrc=new ArrayList<String>().add
                            Integer uniqueid;
                            switch (identifier)
                            {
                                case "QutabMinar":uniqueid=1;
                                case "PacificMall":uniqueid=2;
                                case "Conference":uniqueid=3;
                                    default:uniqueid=4;
                            }
                            buildNotification(title,message,imgSource,knowmoreurlserver,uniqueid);





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
    public void buildNotification(String title,String message,ArrayList<Banner> imgsrcserver,String knowmoreurlserver,Integer uniqid)
    {
        if (MyApplication.isactive==Boolean.TRUE)
        {
            final Intent notifyIntent = new Intent(this, notification.class);
            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            notifyIntent.putExtra("imgsource", imgsrcserver);
            notifyIntent.putExtra("knowmoreurl",knowmoreurlserver);
            startActivity(notifyIntent);
        }
        else {
            final Intent notifyIntent = new Intent(this, notification.class);
            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            notifyIntent.putExtra("imgsource", imgsrcserver);
            notifyIntent.putExtra("knowmoreurl", knowmoreurlserver);
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

            notificationManager.notify(uniqid, notification);
        }
    }
}
