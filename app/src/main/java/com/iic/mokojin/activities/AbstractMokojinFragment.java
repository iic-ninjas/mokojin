package com.iic.mokojin.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.iic.mokojin.Application;

/**
 * Created by yon on 3/12/15.
 */
public abstract class AbstractMokojinFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Application) getActivity().getApplication()).inject(this);
    }
}
