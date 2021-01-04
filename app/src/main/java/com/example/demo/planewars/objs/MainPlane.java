package com.example.demo.planewars.objs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.example.demo.planewars.ActionFire;
import com.example.demo.planewars.ActionMove;
import com.example.demo.planewars.MainActivity;
import com.example.demo.planewars.R;
import com.example.demo.planewars.Constant;

import java.util.List;


public class MainPlane extends BaseObj implements ActionFire, ActionMove {

    private static final String TAG = MainPlane.class.getSimpleName();
    private MainBullet mMainBullet;
    private Bitmap mPlaneBmp;
    private Bitmap mPlaneFireBmp;

    private int fireH;

    public MainPlane(Context context) {
        speed = Constant.BASE_PLANE_SPEED;
        isAlive = true;
        mPlaneBmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.plane);
        mPlaneFireBmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.plane_rear_fire);
        w = mPlaneBmp.getWidth();
        h = mPlaneBmp.getHeight();
        fireH = mPlaneFireBmp.getHeight();
        x = (MainActivity.mScreenWidth - w) / 2;
        y = MainActivity.mScreenHeight - h;
        centerX = x + w / 2;
        centerY = y + h / 2;
        mMainBullet = BulletFactory.newMainBullet(context);
    }

    @Override
    public void init(int speedRate, float centerX, float centerY) {
        speed = Constant.BASE_PLANE_SPEED  * (speedRate + 1);
        if (!mMainBullet.isAlive) {
            mMainBullet.init(Constant.BASE_SPEED_RATE, centerX, centerY);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (isAlive) {
            canvas.save();
            canvas.drawBitmap(mPlaneBmp, x, y - fireH, paint);
            canvas.drawBitmap(mPlaneFireBmp, x, y - fireH + h, paint);
            canvas.restore();
        }
    }

    @Override
    public void move(float moveX, float moveY) {
        Log.d(TAG, "[move] moveX = " + moveX + ", moveY = " + moveY);
        setCenterX(moveX);
        setCenterY(moveY);
    }

    @Override
    public void fire(Canvas canvas, List<EnemyPlane> enemyPlanes) {
        if (!mMainBullet.isAlive) {
            return;
        }
        for (EnemyPlane enemyPlane : enemyPlanes) {
            if (enemyPlane.canCollide() && mMainBullet.checkCollide(enemyPlane)) {
                enemyPlane.beAttacked(mMainBullet.mPower);
                if (enemyPlane.isExplosion) {
                    MainActivity.mSumScore += enemyPlane.initBlood;
                }
                break;
            }
        }
        mMainBullet.draw(canvas);
    }
}
