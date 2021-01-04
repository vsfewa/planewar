package com.example.demo.planewars;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.demo.planewars.objs.EnemyPlane;
import com.example.demo.planewars.objs.PlaneFactory;
import com.example.demo.planewars.objs.MainPlane;

import java.util.ArrayList;


public class MainActivity extends Activity implements SurfaceHolder.Callback {

    public static int mScreenWidth;
    public static int mScreenHeight;
    public static int mSumScore;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MSG_DRAW = 1000;
    private static final int MSG_FINISH = 1001;

    private DrawHandler mDrawHandler;
    private HandlerThread mHandlerThread;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;
    private Paint mPaint = new Paint();

    private ArrayList<EnemyPlane> mEnemyPlaneList = new ArrayList<EnemyPlane>();
    private MainPlane mMainPlane;

    private int mSpeedRate;
    private float mPlayPauseBtnW;
    private float mPlayPauseBtnH;
    private boolean mIsPlaying;
    private boolean mIsTouchMainPlane;
    private Bitmap mPlayBtnBmp;
    private Bitmap mPauseBtnBmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "[onCreate] + BEGIN");
        setContentView(R.layout.activity_main);
        mHandlerThread = new HandlerThread("DrawThread");
        mHandlerThread.start();
        mDrawHandler = new DrawHandler(mHandlerThread.getLooper());
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        Log.d(TAG, "[onCreate] + END");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "[surfaceCreated] + BEGIN");
        mScreenWidth = mSurfaceView.getWidth();
        mScreenHeight = mSurfaceView.getHeight();
        mSumScore = 0;

        mSpeedRate = Constant.BASE_SPEED_RATE;

        mMainPlane = PlaneFactory.newMainPlane(this);

        for (int i = 0; i < Constant.MAX_ENEMY_PLANE_COUNT; i++) {
            EnemyPlane enemyPlane = PlaneFactory.newEnemyPlane(this);
            mEnemyPlaneList.add(enemyPlane);
        }

        mPlayBtnBmp = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_media_play);
        mPauseBtnBmp = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_media_pause);
        mPlayPauseBtnW = mPlayBtnBmp.getWidth();
        mPlayPauseBtnH = mPlayBtnBmp.getHeight();

        mIsPlaying = true;
        mDrawHandler.sendEmptyMessage(MSG_DRAW);
        Log.d(TAG, "[surfaceCreated] + END");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "[surfaceChanged] + BEGIN");
        Log.d(TAG, "[surfaceChanged] + END");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "[surfaceDestroyed] + BEGIN");
        mHandlerThread.quit();
        mDrawHandler = null;
        Log.d(TAG, "[surfaceDestroyed] + END");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            mIsTouchMainPlane = false;
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            //暂停
            if (x > Constant.MARGIN_PLAY_PAUSE_BTN && x < Constant.MARGIN_PLAY_PAUSE_BTN + mPlayPauseBtnW
                    && y > Constant.MARGIN_PLAY_PAUSE_BTN && y < Constant.MARGIN_PLAY_PAUSE_BTN + mPlayPauseBtnH) {
                if (mIsPlaying) {
                    mIsPlaying = false;
                } else {
                    mIsPlaying = true;
                    mDrawHandler.sendEmptyMessage(MSG_DRAW);
                }
                return true;
            } else if (x > mMainPlane.x && x < mMainPlane.x + mMainPlane.w
                    && y > mMainPlane.y && y < mMainPlane.y + mMainPlane.h) {
                if (mIsPlaying) {
                    mIsTouchMainPlane = true;
                }
                return true;
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE && event.getPointerCount() == 1) {
            if (mIsTouchMainPlane) {
                float x = event.getX();
                float y = event.getY();
                if (x > mMainPlane.centerX + mMainPlane.w / 2) {
                    if (mMainPlane.centerX + mMainPlane.speed <= mScreenWidth) {
                        float moveX = mMainPlane.centerX + mMainPlane.speed;
                        mMainPlane.move(moveX, mMainPlane.centerY);
                    }
                } else if (x < mMainPlane.centerX - mMainPlane.w / 2) {
                    if (mMainPlane.centerX - mMainPlane.speed >= 0) {
                        float moveX = mMainPlane.centerX - mMainPlane.speed;
                        mMainPlane.move(moveX, mMainPlane.centerY);
                    }
                }
                if (y > mMainPlane.centerY + mMainPlane.h / 2) {
                    if (mMainPlane.centerY + mMainPlane.speed <= mScreenHeight) {
                        float moveY = mMainPlane.centerY + mMainPlane.speed;
                        mMainPlane.move(mMainPlane.centerX, moveY);
                    }
                } else if (y < mMainPlane.centerY - mMainPlane.h / 2) {
                    if (mMainPlane.centerY - mMainPlane.speed >= 0) {
                        float moveY = mMainPlane.centerY - mMainPlane.speed;
                        mMainPlane.move(mMainPlane.centerX, moveY);
                    }
                }
                return true;
            }
        }
        return false;
    }

    private class DrawHandler extends Handler {

        int textColor = getResources().getColor(android.R.color.holo_blue_dark);

        DrawHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case MSG_DRAW:
                    Log.d(TAG, "[handleMessage] MSG_DRAW");
                    long startTime = System.currentTimeMillis();
                    initDrawObjs();
                    invokeDrawObjs();
                    if (!mIsPlaying) {
                        this.removeMessages(MSG_DRAW);
                    } else {
                        int delayTime = 50;
                        Message newMsg = this.obtainMessage(MSG_DRAW);
                        this.sendMessageDelayed(newMsg, delayTime);
                    }
                    long endTime = System.currentTimeMillis();
                    Log.d(TAG, "[handleMessage] time cost = " + (endTime - startTime));
                    break;
                case MSG_FINISH:
                    Intent intent = new Intent(getApplication(), FinishActivity.class);
                    intent.putExtra("Score", mSumScore);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
        }

        private void initDrawObjs() {
            mMainPlane.init(mSpeedRate, mMainPlane.centerX, mMainPlane.centerY);

            for (EnemyPlane obj : mEnemyPlaneList) {
                if (!obj.isAlive) {
                    obj.init(mSpeedRate, 0, 0);
                    break;
                }
            }

            if (mSumScore >= mSpeedRate * Constant.UPGRADE_SPEED_SCORE
                    && mSpeedRate < Constant.MAX_SPEED_RATE) {
                mSpeedRate++;
            }
        }

        private void invokeDrawObjs() {
            mCanvas = mSurfaceHolder.lockCanvas();
            mCanvas.drawColor(Color.WHITE);

            for (EnemyPlane obj : mEnemyPlaneList) {
                if (obj.isAlive) {
                    obj.draw(mCanvas);
                    if (obj.canCollide() && mMainPlane.isAlive) {
                        if (obj.checkCollide(mMainPlane)) {
                            mMainPlane.isAlive = false;
                        }
                    }
                }
            }

            if (!mMainPlane.isAlive||mSumScore>=1000) {
                mIsPlaying = false;
                mDrawHandler.sendEmptyMessage(MSG_FINISH);
            }

            mCanvas.save();
            if (mIsPlaying) {
                mCanvas.drawBitmap(mPlayBtnBmp, Constant.MARGIN_PLAY_PAUSE_BTN, Constant.MARGIN_PLAY_PAUSE_BTN, mPaint);
            } else {
                mCanvas.drawBitmap(mPauseBtnBmp, Constant.MARGIN_PLAY_PAUSE_BTN, Constant.MARGIN_PLAY_PAUSE_BTN, mPaint);
            }
            mCanvas.restore();

            mMainPlane.draw(mCanvas);
            mMainPlane.fire(mCanvas, mEnemyPlaneList);

            mPaint.setTextSize(36);
            mPaint.setColor(textColor);
            mCanvas.drawText("Score:" + mSumScore + ", Speed:" + mSpeedRate, 30 + mPlayPauseBtnW, 70, mPaint);

            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

}
