package com.yxr.easytagedittext.helper;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * @author YIN.XR
 */
public class SoftKeyboardHelper {  
    public static void observeSoftKeyboard(Activity activity, final OnSoftKeyboardChangeListener listener) {  
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
                previousKeyboardHeight = keyboardHeight;  
            }  
        });  
    }  
  
    public interface OnSoftKeyboardChangeListener {  
        void onSoftKeyBoardChange(int keyboardHeight, boolean visible);  
    }  
}  