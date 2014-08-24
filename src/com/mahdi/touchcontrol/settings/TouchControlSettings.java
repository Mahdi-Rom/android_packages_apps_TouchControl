/*
 * Copyright (C) 2014 Mahdi-Rom
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mahdi.touchcontrol.settings;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import com.mahdi.touchcontrol.R;
import com.mahdi.touchcontrol.preference.SeekBarPreference;
import com.mahdi.touchcontrol.util.CMDProcessor;
import com.mahdi.touchcontrol.util.Constants;
import com.mahdi.touchcontrol.util.FileUtils;

import java.io.File;

public class TouchControlSettings extends PreferenceFragment
        implements OnPreferenceChangeListener, Constants {

    private static final String SETTINGS_METADATA_NAME = "com.android.settings";

    public static final String TOUCH_CONTROL_SETTINGS = "touch_control_settings";

    public static final String PREF_SOB = "set_on_boot";
    public static final String PREF_DOUBLETAP2WAKE = "doubletap_2_wake";
    public static final String PREF_DOUBLETAP2WAKE2 = "doubletap_2_wake2";
    public static final String PREF_SWEEP2WAKE = "sweep_2_wake";
    public static final String PREF_SWEEP2SLEEP = "sweep_2_sleep";
    public static final String PREF_ENABLE_GESTURES = "enable gestures";
    public static final String PREF_POWERKEYSUSPEND = "powerkey_suspend";
    public static final String PREF_WAKE_TIMEOUT = "wake_timeout";
    public static final String PREF_VIBRATION_STRENGTH = "vibration_strength";
    public static final String PREF_TOUCHWAKE = "touch_wake";
    public static final String PREF_TOUCHWAKE_TIMEOUT = "touch_wake_timeout";

    private CheckBoxPreference mSetOnBoot;
    private ListPreference mDoubletap2Wake;
    private CheckBoxPreference mDoubletap2Wake2;
    private ListPreference mSweep2Wake;
    private ListPreference mSweep2Sleep;
    private CheckBoxPreference mEnableGestures;
    private CheckBoxPreference mPowerkeySuspend;
    private SeekBarPreference mWakeTimeout;
    private SeekBarPreference mVibrationStrength;
    private CheckBoxPreference mTouchWake;
    private SeekBarPreference mTouchWakeTimeout;

    private SharedPreferences mPreferences;

    private AlertDialog alertDialog;

    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        addPreferencesFromResource(R.xml.touch_control);
        PreferenceScreen prefs = getPreferenceScreen();
        mPreferences = getActivity().getSharedPreferences(
                TOUCH_CONTROL_SETTINGS, Activity.MODE_PRIVATE);

        boolean firstrun = mPreferences.getBoolean("firstrun", true);
        if (firstrun) {
            SharedPreferences.Editor e = mPreferences.edit();
            e.putBoolean("firstrun", false);
            e.commit();
            /* Display the warning dialog */
            alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle(R.string.touch_control_warning_title);
            alertDialog.setMessage(getResources().getString(R.string.touch_control_warning));
            alertDialog.setCancelable(false);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                }
            });
            alertDialog.show();
        }

        mSetOnBoot = (CheckBoxPreference) prefs.findPreference(PREF_SOB);
        mDoubletap2Wake = (ListPreference) prefs.findPreference(PREF_DOUBLETAP2WAKE);
        mDoubletap2Wake2 = (CheckBoxPreference) prefs.findPreference(PREF_DOUBLETAP2WAKE2);
        mSweep2Wake = (ListPreference) prefs.findPreference(PREF_SWEEP2WAKE);
        mSweep2Sleep = (ListPreference) prefs.findPreference(PREF_SWEEP2SLEEP);
        mEnableGestures = (CheckBoxPreference) prefs.findPreference(PREF_ENABLE_GESTURES);
        mPowerkeySuspend = (CheckBoxPreference) prefs.findPreference(PREF_POWERKEYSUSPEND);
        mWakeTimeout = (SeekBarPreference) prefs.findPreference(PREF_WAKE_TIMEOUT);
        mVibrationStrength = (SeekBarPreference) prefs.findPreference(PREF_VIBRATION_STRENGTH);
        mTouchWake = (CheckBoxPreference) prefs.findPreference(PREF_TOUCHWAKE);
        mTouchWakeTimeout = (SeekBarPreference) prefs.findPreference(PREF_TOUCHWAKE_TIMEOUT);

        if (!new File(DT2W_FILE).exists()) {
            Preference hideCat = (ListPreference) findPreference(PREF_DOUBLETAP2WAKE);
            prefs.removePreference(hideCat);
        } else {
            int doubletap2Wake = Integer.parseInt(FileUtils.readOneLine(DT2W_FILE));
            mDoubletap2Wake.setValue(String.valueOf(doubletap2Wake));
            mDoubletap2Wake.setSummary(mDoubletap2Wake.getEntry());
            mDoubletap2Wake.setOnPreferenceChangeListener(this);
        }

        if (!new File(DT2W2_FILE).exists()) {
            Preference hideCat = (CheckBoxPreference) findPreference(PREF_DOUBLETAP2WAKE2);
            prefs.removePreference(hideCat);
        } else {
            mDoubletap2Wake2.setChecked(FileUtils.readOneLine(DT2W2_FILE).equals("1"));
            mDoubletap2Wake2.setOnPreferenceChangeListener(this);
        }

        if (!new File(S2W_FILE).exists()) {
            Preference hideCat = (ListPreference) findPreference(PREF_SWEEP2WAKE);
            prefs.removePreference(hideCat);
        } else {
            int sweep2Wake = Integer.parseInt(FileUtils.readOneLine(S2W_FILE));
            mSweep2Wake.setValue(String.valueOf(sweep2Wake));
            mSweep2Wake.setSummary(mSweep2Wake.getEntry());
            mSweep2Wake.setOnPreferenceChangeListener(this);
        }

        if (!new File(S2S_FILE).exists()) {
            Preference hideCat = (ListPreference) findPreference(PREF_SWEEP2SLEEP);
            prefs.removePreference(hideCat);
        } else {
            int sweep2Sleep = Integer.parseInt(FileUtils.readOneLine(S2S_FILE));
            mSweep2Sleep.setValue(String.valueOf(sweep2Sleep));
            mSweep2Sleep.setSummary(mSweep2Sleep.getEntry());
            mSweep2Sleep.setOnPreferenceChangeListener(this);
        }

        if (!new File(GESTURES_FILE).exists()) {
            Preference hideCat = (CheckBoxPreference) findPreference(PREF_ENABLE_GESTURES);
            prefs.removePreference(hideCat);
        } else {
            mEnableGestures.setChecked(FileUtils.readOneLine(GESTURES_FILE).equals("1"));
            mEnableGestures.setOnPreferenceChangeListener(this);
        }

        if (!new File(PWKS_FILE).exists()) {
            Preference hideCat = (CheckBoxPreference) findPreference(PREF_POWERKEYSUSPEND);
            prefs.removePreference(hideCat);
        } else {
            mPowerkeySuspend.setChecked(FileUtils.readOneLine(PWKS_FILE).equals("Y"));
            mPowerkeySuspend.setOnPreferenceChangeListener(this);
        }

        if (!new File(WAKE_TIMEOUT_FILE).exists()) {
            Preference hideCat = (SeekBarPreference) findPreference(PREF_WAKE_TIMEOUT);
            prefs.removePreference(hideCat);
        } else {
            int wakeTimeout = Integer.parseInt(FileUtils.readOneLine(WAKE_TIMEOUT_FILE));
            mWakeTimeout.setValue(wakeTimeout);
            mWakeTimeout.setOnPreferenceChangeListener(this);
        }

        if (!new File(VIBRATION_STRENGTH_FILE).exists()) {
            Preference hideCat = (SeekBarPreference) findPreference(PREF_VIBRATION_STRENGTH);
            prefs.removePreference(hideCat);
        } else {
            int vibrationStrength = Integer.parseInt(FileUtils.readOneLine(VIBRATION_STRENGTH_FILE));
            mVibrationStrength.setValue(vibrationStrength);
            mVibrationStrength.setOnPreferenceChangeListener(this);
        }

        if (!new File(T2W_FILE).exists()) {
            Preference hideCat = (Preference) findPreference(PREF_TOUCHWAKE);
            prefs.removePreference(hideCat);
        } else {
            mTouchWake.setChecked(FileUtils.readOneLine(T2W_FILE).equals("1"));
            mTouchWake.setOnPreferenceChangeListener(this);
        }

        if (!new File(T2W_TIMEOUT_FILE).exists()) {
            Preference hideCat = (SeekBarPreference) findPreference(PREF_TOUCHWAKE_TIMEOUT);
            prefs.removePreference(hideCat);
        } else {
            int i1 = Integer.parseInt(FileUtils.readOneLine(T2W_TIMEOUT_FILE));
            int i2 = (i1 / 1000);
            mTouchWakeTimeout.setValue(i2);
            mTouchWakeTimeout.setOnPreferenceChangeListener(this);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (preference == mDoubletap2Wake) {
            if (Integer.parseInt(FileUtils.readOneLine(DT2W_FILE)) >= 0) {
                int index = mDoubletap2Wake.findIndexOfValue((String) newValue);
                int doubletap2Wake = Integer.valueOf((String) newValue);
                new CMDProcessor().su.runWaitFor(
                        "busybox echo " + doubletap2Wake + " > " + DT2W_FILE);
                mDoubletap2Wake.setSummary(mDoubletap2Wake.getEntries()[index]);
            }
            mPreferences.edit().putString(PREF_DOUBLETAP2WAKE, (String) newValue).commit();
            return true;
        } else if (preference == mDoubletap2Wake2) {
            if (Integer.parseInt(FileUtils.readOneLine(DT2W2_FILE)) == 0) {
                new CMDProcessor().su.runWaitFor("busybox echo 1 > " + DT2W2_FILE);
            } else {
                new CMDProcessor().su.runWaitFor("busybox echo 0 > " + DT2W2_FILE);
            }
            mPreferences.edit().putBoolean(PREF_DOUBLETAP2WAKE2, (Boolean) newValue).commit();
            return true;
        } else if (preference == mSweep2Wake) {
            if (Integer.parseInt(FileUtils.readOneLine(S2W_FILE)) >= 0) {
                int index = mSweep2Wake.findIndexOfValue((String) newValue);
                int sweep2Wake = Integer.valueOf((String) newValue);
                new CMDProcessor().su.runWaitFor(
                        "busybox echo " + sweep2Wake + " > " + S2W_FILE);
                mSweep2Wake.setSummary(mSweep2Wake.getEntries()[index]);
            }
            mPreferences.edit().putString(PREF_SWEEP2WAKE, (String) newValue).commit();
            return true;
        } else if (preference == mSweep2Sleep) {
            if (Integer.parseInt(FileUtils.readOneLine(S2S_FILE)) >= 0) {
                int index = mSweep2Sleep.findIndexOfValue((String) newValue);
                int sweep2Sleep = Integer.valueOf((String) newValue);
                new CMDProcessor().su.runWaitFor(
                        "busybox echo " + sweep2Sleep + " > " + S2S_FILE);
                mSweep2Sleep.setSummary(mSweep2Sleep.getEntries()[index]);
            }
            mPreferences.edit().putString(PREF_SWEEP2SLEEP, (String) newValue).commit();
            return true;
        } else if (preference == mEnableGestures) {
            if (Integer.parseInt(FileUtils.readOneLine(GESTURES_FILE)) == 0) {
                new CMDProcessor().su.runWaitFor("busybox echo 1 > " + GESTURES_FILE);
            } else {
                new CMDProcessor().su.runWaitFor("busybox echo 0 > " + GESTURES_FILE);
            }
            mPreferences.edit().putBoolean(PREF_ENABLE_GESTURES, (Boolean) newValue).commit();
            return true;
        } else if (preference == mPowerkeySuspend) {
            if (newValue.toString().equals("true")) {
                new CMDProcessor().su.runWaitFor("busybox echo Y > " + PWKS_FILE);
            } else {
                new CMDProcessor().su.runWaitFor("busybox echo N > " + PWKS_FILE);
            }
            mPreferences.edit().putBoolean(PREF_POWERKEYSUSPEND, (Boolean) newValue).commit();
            return true;
        } else if (preference == mWakeTimeout) {
            if (Integer.parseInt(FileUtils.readOneLine(WAKE_TIMEOUT_FILE)) >= 0) {
                mWakeTimeout.setValue((Integer) newValue);
                int value = ((Integer) newValue);
                new CMDProcessor().su.runWaitFor(
                        "busybox echo " + value + " > " + WAKE_TIMEOUT_FILE);
            }
            mPreferences.edit().putInt(PREF_WAKE_TIMEOUT, (Integer) newValue).commit();
            return true;
        } else if (preference == mVibrationStrength) {
            if (Integer.parseInt(FileUtils.readOneLine(VIBRATION_STRENGTH_FILE)) >= 0) {
                mVibrationStrength.setValue((Integer) newValue);
                int value = ((Integer) newValue);
                new CMDProcessor().su.runWaitFor(
                        "busybox echo " + value + " > " + VIBRATION_STRENGTH_FILE);
            }
            mPreferences.edit().putInt(PREF_VIBRATION_STRENGTH, (Integer) newValue).commit();
            return true;
        } else if (preference == mTouchWake) {
            if (Integer.parseInt(FileUtils.readOneLine(T2W_FILE)) == 0) {
                new CMDProcessor().su.runWaitFor("busybox echo 1 > " + T2W_FILE);
            } else {
                new CMDProcessor().su.runWaitFor("busybox echo 0 > " + T2W_FILE);
            }
            mPreferences.edit().putBoolean(PREF_TOUCHWAKE, (Boolean) newValue).commit();
            return true;
        } else if (preference == mTouchWakeTimeout) {
            if (Integer.parseInt(FileUtils.readOneLine(T2W_TIMEOUT_FILE)) >= 0) {
                mTouchWakeTimeout.setValue((Integer) newValue);
                int value = ((Integer) newValue) * 1000;
                new CMDProcessor().su.runWaitFor(
                        "busybox echo " + value + " > " + T2W_TIMEOUT_FILE);
            }
            mPreferences.edit().putInt(PREF_TOUCHWAKE_TIMEOUT, (Integer) newValue).commit();
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
