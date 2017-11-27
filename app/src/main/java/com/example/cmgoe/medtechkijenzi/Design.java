package com.example.cmgoe.medtechkijenzi;

import java.io.Serializable;

/**
 * Created by cmgoe on 11/24/2017.
 */

public class Design implements Serializable {
    String title;
    String description;
    String label;

    public Design(String title, String description, String label){
        this.title = title;
        this.description = description;
        this.label = label;
    }
}
