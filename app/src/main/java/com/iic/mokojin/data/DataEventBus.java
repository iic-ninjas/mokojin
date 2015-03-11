package com.iic.mokojin.data;

import com.squareup.otto.Bus;

/**
 * Created by giladgo on 3/10/15.
 */
public class DataEventBus {

    private static Bus mEventBus;

    static {
        mEventBus = new Bus("Service Event Bus");
    }

    public static Bus getEventBus() {
        return mEventBus;
    }
}
