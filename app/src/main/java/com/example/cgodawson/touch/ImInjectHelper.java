package com.example.cgodawson.touch;

import android.hardware.input.InputManager;
import android.os.SystemClock;
import android.view.InputEvent;
import android.view.KeyEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ImInjectHelper {
    public InputManager inputManager;
    private static Method key_obtain;
    private static Method Input_setSource;
    private static Method input_injectInputEvent;
    ImInjectHelper() {
        this.inputManager = getInputManager();
        try {
            Class<?> KeyEvent = Class.forName("android.view.KeyEvent") ;
            key_obtain = KeyEvent.getMethod("obtain",long.class,long.class,int.class,int.class,int.class,int.class,
                    int.class,int.class,int.class,int.class,String.class);
            Class<?> InputEvent = Class.forName("android.view.InputEvent") ;
            Input_setSource = InputEvent.getMethod("setSource",int.class);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static KeyEvent getKeyEvent(int action, int code) {

        try {
            return (KeyEvent) key_obtain.invoke(null, SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), action, code, 0, 0, 0, 0, 0, 0, null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InputManager getInputManager() {
        try {
            Class<?> InputManager = Class.forName("android.hardware.input.InputManager") ;
            Method getInstance = InputManager.getMethod("getInstance",null);
            input_injectInputEvent = InputManager.getMethod("injectInputEvent",InputEvent.class,int.class);
            return (android.hardware.input.InputManager) getInstance.invoke(null,null);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public  boolean injectInputEvent(InputEvent event, int source, boolean setSource) {

        if (setSource) {
            try {
                Input_setSource.invoke(event,source);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            return (boolean) input_injectInputEvent.invoke(inputManager,event,2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
