package com.yojiokisoft.savetheearth;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

import static android.view.animation.Animation.*;

/**
 * Created by taoka on 14/02/06.
 */
public class MainActivity extends Activity implements TouchView.Callback, AdListener {
    private int mMaxTrees;
    private Vibrator mVibrator = null;
    private AlphaAnimation mAnimation;
    private TextView mMessage;
    private TouchView mEarthImage;
    private AdView mAdViewBanner;
    private FrameLayout mEnding;
    private TextView mEndingMessage;
    private MediaPlayer mPlayer = null;
    private ImageView mForeImage;
    private ImageView mBackImage;
    private int mImageIndex;
    private int[] mAnimImages = {
            R.drawable.ending_01,
            R.drawable.ending_02,
            R.drawable.ending_03,
            R.drawable.ending_04,
            R.drawable.ending_05,
            R.drawable.ending_06,
            R.drawable.ending_07,
            R.drawable.ending_08,
            R.drawable.ending_09,
            R.drawable.ending_10,
            R.drawable.ending_11,
            R.drawable.ending_12,
            R.drawable.ending_13,
            R.drawable.ending_14,
    };
    private int[] mEndingMessages = {
            R.string.ending_message_01,
            R.string.ending_message_02,
            R.string.ending_message_03,
            R.string.ending_message_04,
            R.string.ending_message_05,
            R.string.ending_message_06,
            R.string.ending_message_07,
            R.string.ending_message_08,
            R.string.ending_message_09,
            R.string.ending_message_10,
            R.string.ending_message_11,
            R.string.ending_message_12,
            R.string.ending_message_13,
            R.string.ending_message_14,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        mPlayer = MediaPlayer.create(this, R.raw.ending_music);
        mPlayer.setLooping(true);

        mMessage = (TextView) findViewById(R.id.message_text);
        mEarthImage = (TouchView) findViewById(R.id.earth_image);
        mEnding = (FrameLayout) findViewById(R.id.congratulation_view);
        mEndingMessage = (TextView) findViewById(R.id.ending_message);
        mForeImage = (ImageView) findViewById(R.id.fore_image);
        mBackImage = (ImageView) findViewById(R.id.back_image);

        AdRequest adRequest = AdCatalogUtils.createAdRequest();
        mAdViewBanner = (AdView) findViewById(R.id.adview_banner);
        mAdViewBanner.setAdListener(this);
        mAdViewBanner.loadAd(adRequest);

        float density = MyUtils.getDensity();
        mMaxTrees = (int) (20000 * density * density + 0.5f);

        startGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_reset) {
            startGame();
            mEarthImage.reset();
            return true;
        } else if (id == R.id.action_show_ending) {
            showEnding();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startGame() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
        if (mAnimation != null) {
            mAnimation.cancel();
            mAnimation = null;
        }

        mEnding.setVisibility(View.INVISIBLE);
        mEarthImage.setVisibility(View.VISIBLE);
    }

    private void showEnding() {
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                mPlayer.seekTo(0);
            }
            mPlayer.start();
        }

        mEarthImage.setVisibility(View.INVISIBLE);

        if (mAnimation != null) {
            mAnimation.cancel();
            mAnimation = null;
        }

        mImageIndex = 0;
        mEndingMessage.setText(getString(mEndingMessages[mImageIndex]));
        mForeImage.setImageResource(mAnimImages[mImageIndex]);
        mAnimation = new AlphaAnimation(0.0f, 1);
        mAnimation.setDuration(5000);
        mAnimation.setAnimationListener(mFirstAnimationListener);
        mEnding.setAnimation(mAnimation);
        mEnding.setVisibility(View.VISIBLE);
    }

    @Override
    public void treeChanged(int treeCount) {
        if (treeCount <= 0) {
            mMessage.setText(getText(R.string.play_message));
        } else {
            String msg = String.format(getString(R.string.count_message, treeCount));
            mMessage.setText(msg);
            mVibrator.vibrate(600);
        }

        if (treeCount > mMaxTrees) {
            showEnding();
        }
    }

    private AnimationListener mFirstAnimationListener = new AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mAnimation = null;

            mImageIndex++;
            mEndingMessage.setText(getString(mEndingMessages[mImageIndex]));
            mBackImage.setImageResource(mAnimImages[mImageIndex]);
            mAnimation = new AlphaAnimation(0.0f, 1);
            mAnimation.setDuration(5000);
            mAnimation.setAnimationListener(mPagerAnimationListener);
            mBackImage.setAnimation(mAnimation);
            mBackImage.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private AnimationListener mPagerAnimationListener = new AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mAnimation = null;

            mImageIndex++;
            if (mAnimImages.length <= mImageIndex) {
                mImageIndex = 0;
            }
            mEndingMessage.setText(getString(mEndingMessages[mImageIndex]));
            if (mImageIndex % 2 == 0) {
                mForeImage.setImageResource(mAnimImages[mImageIndex]);
                mAnimation = new AlphaAnimation(1, 0.0f);
            } else {
                mBackImage.setImageResource(mAnimImages[mImageIndex]);
                mAnimation = new AlphaAnimation(0.0f, 1);
            }
            mAnimation.setStartOffset(4000);
            mAnimation.setDuration(3000);
            mAnimation.setAnimationListener(mPagerAnimationListener);
            mBackImage.setAnimation(mAnimation);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
        mEarthImage.save();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mAdViewBanner != null) {
            mAdViewBanner.destroy();
        }
    }

    @Override
    public void onReceiveAd(Ad ad) {
        Log.d("Banners_class", "I received an ad");
    }

    @Override
    public void onFailedToReceiveAd(Ad ad, AdRequest.ErrorCode error) {
        Log.d("Banners_class", "I failed to receive an ad");
    }

    @Override
    public void onPresentScreen(Ad ad) {
        Log.d("Banners_class", "Presenting screen");
    }

    @Override
    public void onDismissScreen(Ad ad) {
        Log.d("Banners_class", "Dismissing screen");
    }

    @Override
    public void onLeaveApplication(Ad ad) {
        Log.d("Banners_class", "Leaving application");
    }
}
