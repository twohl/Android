package com.script.muhelp.util;

import android.support.v7.view.menu.MenuBuilder;
import android.view.Menu;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by hongl on 2018/2/18.
 */

public class MenuIconUtil {

    public static void MenuIconVisible(Menu menu){
        try {
            Method method = MenuBuilder.class.getDeclaredMethod("setOptionalIconsVisible",boolean.class);
            method.invoke(menu,true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}
