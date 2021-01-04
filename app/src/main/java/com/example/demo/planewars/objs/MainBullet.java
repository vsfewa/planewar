package com.example.demo.planewars.objs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.example.demo.planewars.ActionMove;
import com.example.demo.planewars.R;
import com.example.demo.planewars.Constant;


public class MainBullet extends BaseObj implements ActionMove {

    public int mPower;

    private Bitmap mBulletBmp;

    public MainBullet(Context context) {
        mPower = Constant.BASE_BULLET_POWER;
        mBulletBmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet);
        w = mBulletBmp.getWidth();
        h = mBulletBmp.getHeight();
    }

    @Override
    public void init(int speedRate, float centerX, float centerY) {
        isAlive = true;
        speed = Constant.BASE_BULLET_SPEED * speedRate;
        x = centerX - w / 2;
        y = centerY - h;
    }

    @Override
    public void draw(Canvas canvas) {
        if (isAlive) {
            canvas.save();
            canvas.drawBitmap(mBulletBmp, x, y, paint);
            canvas.restore();
            move(0, speed);
        }
    }

    @Override
    public void move(float moveX, float moveY) {
        if (y >= 0) {
            y = y - moveY;
        } else {
            isAlive = false;
        }
    }

    public boolean checkCollide(BaseObj obj) {
        if (y >= obj.y && x >= obj.x && x + w <= obj.x + obj.w) {
            isAlive = false;
            return true;
        } else {
            return false;
        }
    }
}
