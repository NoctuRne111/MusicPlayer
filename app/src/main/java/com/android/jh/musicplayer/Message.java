package com.android.jh.musicplayer;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by JH on 2017-02-08.
 */

public class Message {
    public static void show(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
