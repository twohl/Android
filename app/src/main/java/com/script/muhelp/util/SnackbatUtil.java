package com.script.muhelp.util;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.script.muhelp.R;

/**
 * Created by hongl on 2018/2/23.
 */

public class SnackbatUtil {

    public static void showLong(View view,String text,int bg,int color){
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_INDEFINITE);
        snackbar.getView().setBackgroundResource(bg);
        snackbar.setActionTextColor(color);
        snackbar.show();
    }
    public static void showErrorLong(View view,String text){
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundResource(R.color.colorRed);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.show();
    }
    public static void showMessageLong(View view,String text){
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundResource(R.color.colorAccent);
        snackbar.setActionTextColor(Color.BLACK);
        snackbar.show();
    }
}
