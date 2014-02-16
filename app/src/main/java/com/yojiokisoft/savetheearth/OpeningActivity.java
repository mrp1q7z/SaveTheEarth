package com.yojiokisoft.savetheearth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import static android.view.animation.Animation.*;

public class OpeningActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private TextView mStartText;
        private ImageView mEarthFore;
        private ImageView mEarthBack;
        private int mImageIndex;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_opening, container, false);

            mStartText = (TextView) rootView.findViewById(R.id.start_text);
            mEarthFore = (ImageView) rootView.findViewById(R.id.earth_fore);
            mEarthBack = (ImageView) rootView.findViewById(R.id.earth_back);

            AlphaAnimation alphaText = new AlphaAnimation(1, 0.0f);
            alphaText.setDuration(500);
            alphaText.setRepeatCount(INFINITE);
            mStartText.setAnimation(alphaText);

            AlphaAnimation alpha = new AlphaAnimation(0.0f, 1);
            alpha.setDuration(5000);
            alpha.setAnimationListener(mAnimationListener);

            mImageIndex = 0;
            mEarthBack.setAnimation(alpha);
            mEarthBack.setVisibility(View.VISIBLE);
            mEarthBack.setOnClickListener(mEarthBackClick);

            return rootView;
        }

        private AnimationListener mAnimationListener = new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mImageIndex++;
                AlphaAnimation alpha;
                if (mImageIndex == 1) {
                    mEarthFore.setImageResource(R.drawable.earth_desert);
                    alpha = new AlphaAnimation(1, 0.0f);
                } else {
                    mEarthFore.setImageResource(R.drawable.earth_original);
                    mEarthBack.setImageResource(R.drawable.earth_fire);
                    alpha = new AlphaAnimation(0.0f, 1);
                    mImageIndex = 0;
                }
                alpha.setStartOffset(3000);
                alpha.setDuration(4000);
                alpha.setAnimationListener(mAnimationListener);
                mEarthBack.setAnimation(alpha);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        private View.OnClickListener mEarthBackClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        };
    }

}
