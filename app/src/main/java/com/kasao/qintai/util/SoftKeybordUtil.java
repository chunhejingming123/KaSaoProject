package com.kasao.qintai.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by suochunming on 17/10/18.
 */
public class SoftKeybordUtil {

    public static void setSoftKeyboardListener(Activity activity, final OnSoftKeyboardChangeListener listener) {
        
        final View decorView = activity.getWindow().getDecorView();
        
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            int previousKeyboardHeight = -1;
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                int displayHeight = rect.bottom - rect.top;
                int height = decorView.getHeight();
                
                int keyboardHeight = height - displayHeight;
                if (previousKeyboardHeight != keyboardHeight) {
                    boolean hide = (double) displayHeight / height > 0.8;
                    listener.onSoftKeyBoardChange(keyboardHeight, !hide);
                }
                previousKeyboardHeight = height;

            }
        });
    }

    public interface OnSoftKeyboardChangeListener {
        void onSoftKeyBoardChange(int softKeybardHeight, boolean isVisible);
    }
    //强制显示或者关闭系统键盘
    public static void keyBoradDelay(final EditText txtSearchKey, final boolean status, int millisecond) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager m = (InputMethodManager)
                        txtSearchKey.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (status) {
                    m.showSoftInput(txtSearchKey, InputMethodManager.SHOW_FORCED);
                } else {
                    m.hideSoftInputFromWindow(txtSearchKey.getWindowToken(), 0);
                }
            }
        }, millisecond);
    }
}
