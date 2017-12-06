package com.example.cmgoe.medtechkijenzi;

import java.io.Serializable;

/**
 * Created by cmgoe on 11/24/2017.
 */

public class Design implements Serializable {
    public String title;
    public String desc;
    public String url;

    public Design () {

    }

    public Design(String title, String desc, String url){
        this.title = title;
        this.desc = desc;
        this.url = url;
    }
}
