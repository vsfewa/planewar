package com.example.demo.planewars.objs;

import android.content.Context;


public class BulletFactory {
    public static MainBullet newMainBullet(Context context) {
        return new MainBullet(context);
    }
}
