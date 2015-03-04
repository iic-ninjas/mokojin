package com.iic.mokojin;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.parse.Parse;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
    
    public void setup(){
        Parse.initialize(getContext(), "", "");
    }
}