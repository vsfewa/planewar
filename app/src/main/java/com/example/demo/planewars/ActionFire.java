package com.example.demo.planewars;

import android.graphics.Canvas;

import com.example.demo.planewars.objs.EnemyPlane;

import java.util.List;


public interface ActionFire {

    public void fire(Canvas canvas, List<EnemyPlane> enemyPlanes);

}
