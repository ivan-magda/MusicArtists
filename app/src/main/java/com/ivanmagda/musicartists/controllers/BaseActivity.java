package com.ivanmagda.musicartists.controllers;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ivanmagda.musicartists.R;

public class BaseActivity extends AppCompatActivity {

    // Properties.

    private Toolbar toolbar;

    // Methods.

    protected Toolbar activateToolbar() {
        if (toolbar == null) {
            toolbar = (Toolbar) findViewById(R.id.app_bar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
            }
        }

        return toolbar;
    }

}
