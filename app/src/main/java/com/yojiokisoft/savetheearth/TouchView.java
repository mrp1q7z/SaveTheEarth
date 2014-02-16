package com.yojiokisoft.savetheearth;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by taoka on 14/02/06.
 */
public class TouchView extends View {
    public interface Callback {
        void treeChanged(int treeCount);
    }

    private final static String SAVE_FILE_NAME = "save.png";
    private final static int EARTH_WIDTH = 300; // dp
    private final static int EARTH_HEIGHT = 301; // dp

    private Activity mActivity;
    private Bitmap mBitmap;
    private int mTreeCount;
    private int mTreeColor;
    private int mTreeMargin;

    public TouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            mActivity = (Activity) context;
            float density = MyUtils.getDensity();
            if (density == 1.0f) {
                mTreeMargin = 1;
            } else {
                mTreeMargin = Math.round(density + 0.5f);
            }
        }
        mTreeColor = getResources().getColor(R.color.tree);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isInEditMode()) {
            setMeasuredDimension(EARTH_WIDTH, EARTH_HEIGHT);
        } else {
            setMeasuredDimension(MyUtils.dpi2Px(EARTH_WIDTH), MyUtils.dpi2Px(EARTH_HEIGHT));
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (!isInEditMode()) {
            mTreeCount = SettingDao.getInstance().getTreeCount();
            ((TouchView.Callback) mActivity).treeChanged(mTreeCount);
        }
        readBitmap();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        if (!(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            return true;
        }

        int oldTreeCount = mTreeCount;
        int touchX = (int) event.getX();
        int touchY = (int) event.getY();
        for (int x = touchX - mTreeMargin; x <= touchX + mTreeMargin; x++) {
            for (int y = touchY - mTreeMargin; y <= touchY + mTreeMargin; y++) {
                if (isGround(x, y)) {
                    mBitmap.setPixel(x, y, mTreeColor);
                    mTreeCount++;
                }
            }
        }
        if (oldTreeCount != mTreeCount) {
            invalidate();
            ((TouchView.Callback) mActivity).treeChanged(mTreeCount);
        }

        return true;
    }

    /**
     * 赤茶けた大地？
     *
     * @param x x座標
     * @param y y座標
     * @return true=大地、false=大地以外
     */
    private boolean isGround(int x, int y) {
        if (x < 0 || mBitmap.getWidth() <= x) {
            return false;
        }
        if (y < 0 || mBitmap.getHeight() <= y) {
            return false;
        }

        try {
            int color = mBitmap.getPixel(x, y);
            int r = Color.red(color);

            if (0x70 <= r && r <= 0xaf) {
                return true;
            }
        } catch (IllegalArgumentException e) {
            Log.d("IllegalArgumentException", "x,y=" + x + "," + y + " w,h=" + mBitmap.getWidth() + "," + mBitmap.getHeight());
        }
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    public void save() {
        MyBitmap.save(mActivity, mBitmap, SAVE_FILE_NAME);
        SettingDao.getInstance().setTreeCount(mTreeCount);
    }

    public void reset() {
        mTreeCount = 0;
        SettingDao.getInstance().setTreeCount(mTreeCount);
        ((TouchView.Callback) mActivity).treeChanged(mTreeCount);

        MyBitmap.delete(mActivity, SAVE_FILE_NAME);
        readBitmap();
        invalidate();
    }

    private void readBitmap() {
        int bitmapWidth;
        int bitmapHeight;
        if (isInEditMode()) {
            mBitmap = null;
            bitmapWidth = EARTH_WIDTH;
            bitmapHeight = EARTH_HEIGHT;
        } else {
            mBitmap = MyBitmap.read(mActivity, SAVE_FILE_NAME);
            bitmapWidth = MyUtils.dpi2Px(EARTH_WIDTH);
            bitmapHeight = MyUtils.dpi2Px(EARTH_HEIGHT);
        }

        if (mBitmap == null) {
            mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.earth_red);
        }
        mBitmap = mBitmap.copy(Bitmap.Config.ARGB_8888, true);
        mBitmap = mBitmap.createScaledBitmap(mBitmap, bitmapWidth, bitmapHeight, false);
    }
}
