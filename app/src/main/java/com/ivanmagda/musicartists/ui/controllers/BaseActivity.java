package com.ivanmagda.musicartists.ui.controllers;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ivanmagda.musicartists.R;

public class BaseActivity extends AppCompatActivity {

    // Properties.

    private Toolbar toolbar;

    // Configure.

    protected Toolbar activateToolbar() {
        if (toolbar == null) {
            toolbar = (Toolbar) findViewById(R.id.app_bar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
            }
        }

        return toolbar;
    }

    protected Toolbar activateToolbarWithHomeEnabled() {
        activateToolbar();

        if (toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        return toolbar;
    }

}
