package com.script.muhelp.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.script.muhelp.R;

/**
 * Created by hongl on 2018/2/23.
 */

public class StyleUtil {

    public static void showProgress(View view,boolean flag){
        View progress= view.findViewById(R.id.progress);
        View form = view.findViewById(R.id.form);
        if(flag){
            progress.setVisibility(View.VISIBLE);
            form.setVisibility(View.GONE);
        }else{
            progress.setVisibility(View.GONE);
            form.setVisibility(View.VISIBLE);
        }
    }
    public static void setzhuangtailan(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
