package com.dexafree.andfgc.app.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dexafree.andfgc.app.MainActivity;
import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.utils.Constants;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;


public class WelcomeFragment extends Fragment {

    private Context mContext;

    private LinearLayout mLayout;

    private long mAnimationTime;
    private int mViewWidth = 1; // 1 and not 0 to prevent dividing by zero

    private View mDownView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.welcome_fragment, null);

        mContext = getActivity();

        mLayout = (LinearLayout) v.findViewById(R.id.mainLayout);

        mDownView = mLayout;

        LinearLayout notShowAgain = (LinearLayout)v.findViewById(R.id.dont_show_again);
        notShowAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissView();
            }
        });

        return v;
    }

    private void dismissView(){

        if (mViewWidth < 2) {
            mViewWidth = mLayout.getWidth();
        }

        mAnimationTime = mContext.getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        animate(mDownView)
                .translationX(mViewWidth)
                .alpha(0)
                .setDuration(mAnimationTime)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        performDismiss();

                    }
                });

    }

    private void performDismiss(){
        final ViewGroup.LayoutParams lp = mLayout.getLayoutParams();

        final int originalHeight = mLayout.getHeight();

        ValueAnimator animator = ValueAnimator.ofInt(originalHeight, 1).setDuration(mAnimationTime);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                onAnimationEnded();
            }
        });

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                lp.height = (Integer) valueAnimator.getAnimatedValue();
                mLayout.setLayoutParams(lp);
            }
        });

        animator.start();
    }

    private void onAnimationEnded(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        sp.edit().putBoolean(Constants.SHOW_WELCOME, false).commit();

        ((MainActivity)mContext).changeFragmentWithoutAddingToStack(new SearchFragment());
    }


}
