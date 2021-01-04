package com.example.demo.planewars.objs;

import android.content.Context;


public class PlaneFactory {
    public static EnemyPlane newEnemyPlane(Context context) {
        return new EnemyPlane(context);
    }

    public static MainPlane newMainPlane(Context context) {
        return new MainPlane(context);
    }
}
