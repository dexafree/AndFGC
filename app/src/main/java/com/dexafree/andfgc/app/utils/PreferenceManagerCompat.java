package com.dexafree.andfgc.app.utils;

//THANKS TO KOLAVAR
//SOURCE: https://github.com/kolavar/android-support-v4-preferencefragment/blob/master/src/android/support/v4/preference/PreferenceManagerCompat.java

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import com.dexafree.andfgc.app.fragments.PreferenceFragment;

import java.lang.reflect.*;

public class PreferenceManagerCompat {

    private static final String TAG = PreferenceManagerCompat.class.getSimpleName();

    /**
     * Interface definition for a callback to be invoked when a
     * {@link Preference} in the hierarchy rooted at this {@link PreferenceScreen} is
     * clicked.
     */
    public interface OnPreferenceTreeClickListener {
        /**
         * Called when a preference in the tree rooted at this
         * {@link PreferenceScreen} has been clicked.
         *
         * @param preferenceScreen The {@link PreferenceScreen} that the
         *        preference is located in.
         * @param preference The preference that was clicked.
         * @return Whether the click was handled.
         */
        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference);
    }

    public static PreferenceManager newInstance(Activity activity, int firstRequestCode) {
        try {
            Constructor<PreferenceManager> c = PreferenceManager.class.getDeclaredConstructor(Activity.class, int.class);
            c.setAccessible(true);
            return c.newInstance(activity, firstRequestCode);
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * Sets the owning preference fragment
     */
    public static void setFragment(PreferenceManager manager, PreferenceFragment fragment) {
        // stub
    }

    /**
     * Sets the callback to be invoked when a {@link Preference} in the
     * hierarchy rooted at this {@link PreferenceManager} is clicked.
     *
     * @param listener The callback to be invoked.
     */
    public static void setOnPreferenceTreeClickListener(PreferenceManager manager, final OnPreferenceTreeClickListener listener) {
        try {
            Field onPreferenceTreeClickListener = PreferenceManager.class.getDeclaredField("mOnPreferenceTreeClickListener");
            onPreferenceTreeClickListener.setAccessible(true);
            if (listener != null) {
                Object proxy = Proxy.newProxyInstance(
                        onPreferenceTreeClickListener.getType().getClassLoader(),
                        new Class[] { onPreferenceTreeClickListener.getType() },
                        new InvocationHandler() {
                            public Object invoke(Object proxy, Method method, Object[] args) {
                                if (method.getName().equals("onPreferenceTreeClick")) {
                                    return Boolean.valueOf(listener.onPreferenceTreeClick((PreferenceScreen) args[0], (Preference) args[1]));
                                } else {
                                    return null;
                                }
                            }
                        });
                onPreferenceTreeClickListener.set(manager, proxy);
            } else {
                onPreferenceTreeClickListener.set(manager, null);
            }
        } catch (Exception e) {

        }
    }


    public static PreferenceScreen inflateFromIntent(PreferenceManager manager, Intent intent, PreferenceScreen screen) {
        try {
            Method m = PreferenceManager.class.getDeclaredMethod("inflateFromIntent", Intent.class, PreferenceScreen.class);
            m.setAccessible(true);
            PreferenceScreen prefScreen = (PreferenceScreen) m.invoke(manager, intent, screen);
            return prefScreen;
        } catch (Exception e) {

        }
        return null;
    }


    public static PreferenceScreen inflateFromResource(PreferenceManager manager, Activity activity, int resId, PreferenceScreen screen) {
        try {
            Method m = PreferenceManager.class.getDeclaredMethod("inflateFromResource", Context.class, int.class, PreferenceScreen.class);
            m.setAccessible(true);
            PreferenceScreen prefScreen = (PreferenceScreen) m.invoke(manager, activity, resId, screen);
            return prefScreen;
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * Returns the root of the preference hierarchy managed by this class.
     *
     * @return The {@link PreferenceScreen} object that is at the root of the hierarchy.
     */
    public static PreferenceScreen getPreferenceScreen(PreferenceManager manager) {
        try {
            Method m = PreferenceManager.class.getDeclaredMethod("getPreferenceScreen");
            m.setAccessible(true);
            return (PreferenceScreen) m.invoke(manager);
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * Called by the {@link PreferenceManager} to dispatch a subactivity result.
     */
    public static void dispatchActivityResult(PreferenceManager manager, int requestCode, int resultCode, Intent data) {
        try {
            Method m = PreferenceManager.class.getDeclaredMethod("dispatchActivityResult", int.class, int.class, Intent.class);
            m.setAccessible(true);
            m.invoke(manager, requestCode, resultCode, data);
        } catch (Exception e) {

        }
    }

    /**
     * Called by the {@link PreferenceManager} to dispatch the activity stop
     * event.
     */
    public static void dispatchActivityStop(PreferenceManager manager) {
        try {
            Method m = PreferenceManager.class.getDeclaredMethod("dispatchActivityStop");
            m.setAccessible(true);
            m.invoke(manager);
        } catch (Exception e) {

        }
    }

    /**
     * Called by the {@link PreferenceManager} to dispatch the activity destroy
     * event.
     */
    public static void dispatchActivityDestroy(PreferenceManager manager) {
        try {
            Method m = PreferenceManager.class.getDeclaredMethod("dispatchActivityDestroy");
            m.setAccessible(true);
            m.invoke(manager);
        } catch (Exception e) {

        }
    }


    public static boolean setPreferences(PreferenceManager manager, PreferenceScreen screen) {
        try {
            Method m = PreferenceManager.class.getDeclaredMethod("setPreferences", PreferenceScreen.class);
            m.setAccessible(true);
            return ((Boolean) m.invoke(manager, screen));
        } catch (Exception e) {

        }
        return false;
    }

}