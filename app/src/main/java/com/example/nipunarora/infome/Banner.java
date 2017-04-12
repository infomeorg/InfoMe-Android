package com.example.nipunarora.infome;

import java.io.Serializable;

/**
 * Created by nipunarora on 27/12/16.
 */
public class Banner implements Serializable {
    public String url;
    public Banner()
    {
        url="";
    }
    public Banner(String url1)
    {
        url=url1;
    }
}
