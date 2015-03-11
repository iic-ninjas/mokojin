package com.iic.mokojin;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.iic.mokojin.cloud.operations.GoodNightOperation;
import com.iic.mokojin.views.ProgressHudDialog;

import bolts.Continuation;
import bolts.Task;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_current_match, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_goodnight){
            final ProgressHudDialog dialog = new ProgressHudDialog(this, getString(R.string.good_night_progress));
            dialog.show();
            GoodNightOperation goodNightOperation = new GoodNightOperation();
            goodNightOperation.run().continueWith(new Continuation<Void, Object>() {
                @Override
                public Object then(Task<Void> task) throws Exception {
                    dialog.dismiss();
                    //TODO: Trigger Gilad's refresh
                    return null;
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
