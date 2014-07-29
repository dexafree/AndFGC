package com.dexafree.andfgc.app.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import android.support.v7.widget.CardView;
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

    private CardView cardView;

    private long mAnimationTime;
    private int mViewWidth = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.welcome_fragment, null);

        mContext = getActivity();

        cardView = (CardView)v.findViewById(R.id.cardView);

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
            mViewWidth = cardView.getWidth();
        }

        mAnimationTime = mContext.getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        animate(cardView)
                .translationX(mViewWidth)
                .alpha(0)
                .setDuration(mAnimationTime)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        onAnimationEnded();
                    }
                });

    }

    private void onAnimationEnded(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        sp.edit().putBoolean(Constants.SHOW_WELCOME, false).commit();

        ((MainActivity)mContext).setActionBarTitle(0);
        ((MainActivity)mContext).changeFragmentWithoutAddingToStack(new SearchFragment());
    }


}
