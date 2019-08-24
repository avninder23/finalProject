package com.example.cegap;

import android.content.Context;
import android.widget.ArrayAdapter;

public class Spinneradapter extends ArrayAdapter {
    public Spinneradapter( Context context, int resource) {
        super(context, resource);
    }

    @Override
    public int getCount() {

        // TODO Auto-generated method stub
        int count = super.getCount();

        return count>0 ? count-1 : count ;


    }
}
