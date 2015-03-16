package com.iic.mokojin.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.iic.mokojin.R;
import com.iic.mokojin.cloud.operations.GoodNightOperation;
import com.iic.mokojin.cloud.operations.InvitePlayersOperation;
import com.iic.mokojin.data.CurrentSessionStore;
import com.iic.mokojin.views.ProgressHudDialog;

import javax.inject.Inject;

import bolts.Continuation;
import bolts.Task;


public class MainActivity extends AbstractMokojinActivity {

    @Inject CurrentSessionStore mCurrentSessionStore;

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

        if (id == R.id.action_refresh) {
            performRefresh();
        }
        else if (id == R.id.action_goodnight){
            performGoodNight();
            return true;
        } else if (id == R.id.action_invite_players) {
            final ProgressHudDialog progressHudDialog = new ProgressHudDialog(this, getResources().getString(R.string.inviting_players_progress));
            progressHudDialog.show();
            new InvitePlayersOperation().run().continueWith(new Continuation<Void, Void>() {
                @Override
                public Void then(Task<Void> task) throws Exception {
                    progressHudDialog.dismiss();
                    return null;
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    private void performRefresh() {
        mCurrentSessionStore.refreshData();
    }

    private void performGoodNight() {
        final ProgressHudDialog dialog = new ProgressHudDialog(this, getString(R.string.good_night_progress));
        dialog.show();
        GoodNightOperation goodNightOperation = new GoodNightOperation();
        goodNightOperation.run().continueWith(new Continuation<Void, Object>() {
            @Override
            public Object then(Task<Void> task) throws Exception {
                dialog.dismiss();
                mCurrentSessionStore.refreshData();
                return null;
            }
        });
    }

}
