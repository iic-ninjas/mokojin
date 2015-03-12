package com.iic.mokojin.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.iic.mokojin.Application;

/**
 * Created by yon on 3/12/15.
 */
public abstract class AbstractMokojinActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Application) getApplication()).inject(this);
    }
}
