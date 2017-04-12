package com.example.nipunarora.infome;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class notification extends AppCompatActivity {
    ListView bannerlist;
    Button knowmore;
    String knowmoreurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        bannerlist=(ListView)findViewById(R.id.listView);
        knowmore=(Button)findViewById(R.id.knowmorebutton);
        ArrayList<Banner> imgsource= (ArrayList<Banner>) getIntent().getSerializableExtra("imgsource");
        knowmoreurl=getIntent().getStringExtra("knowmoreurl");
        Log.d("imgsource",imgsource.toString());
        bannerArrayAdapter adapter = new bannerArrayAdapter(this,imgsource);
// Attach the adapter to a ListView
        bannerlist.setAdapter(adapter);

        //create the banner arraylist here instead of creating it in the myapplication class because and array adapter has to be assigned here

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }
    public void setKnowMore(View view)
    {
        Intent intent=new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(knowmoreurl));
        startActivity(intent);
    }


}
