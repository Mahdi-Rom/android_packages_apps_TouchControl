/*
 * Performance Control - An Android CPU Control application Copyright (C) 2012
 * James Roberts
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.mahdi.touchcontrol.service;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;

import com.mahdi.touchcontrol.R;
import com.mahdi.touchcontrol.settings.TouchControlSettings;
import com.mahdi.touchcontrol.util.Constants;
import com.mahdi.touchcontrol.util.FileUtils;

import java.io.File;
import java.util.List;

public class BootService extends Service implements Constants {
    public static boolean servicesStarted = false;
    Context context;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = this;
        if (intent == null) {
            stopSelf();
        }
        new BootWorker(this).execute();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class BootWorker extends AsyncTask<Void, Void, Void> {
        Context c;

        public BootWorker(Context c) {
            this.c = c;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected Void doInBackground(Void... args) {

            SharedPreferences preferences = context.getSharedPreferences(
                        TouchControlSettings.TOUCH_CONTROL_SETTINGS, Activity.MODE_PRIVATE);
            final StringBuilder sb = new StringBuilder();

            if (new File(DT2W_FILE).exists()) {
                if (preferences.getBoolean(PREF_DOUBLETAP2WAKE, false)) {
                    sb.append("busybox echo 1 > " + DT2W_FILE + ";\n");
                } else {
                    sb.append("busybox echo 0 > " + DT2W_FILE + ";\n");
                }
            }

            if (new File(DT2W2_FILE).exists()) {
                if (preferences.getBoolean(PREF_DOUBLETAP2WAKE2, false)) {
                    sb.append("busybox echo 1 > " + DT2W2_FILE + ";\n");
                } else {
                    sb.append("busybox echo 0 > " + DT2W2_FILE + ";\n");
                }
            }

            if (new File(S2W_FILE).exists()) {
                if (preferences.getBoolean(PREF_SWEEP2WAKE, false)) {
                    sb.append("busybox echo 1 > " + S2W_FILE + ";\n");
                } else {
                    sb.append("busybox echo 0 > " + S2W_FILE + ";\n");
                }
            }

            if (new File(S2S_FILE).exists()) {
                if (preferences.getBoolean(PREF_SWEEP2SLEEP, false)) {
                    sb.append("busybox echo 1 > " + S2S_FILE + ";\n");
                } else {
                    sb.append("busybox echo 0 > " + S2S_FILE + ";\n");
                }
            }

            if (new File(PWKS_FILE).exists()) {
                if (preferences.getBoolean(PREF_POWERKEYSUSPEND, false)) {
                    sb.append("busybox echo Y > " + PWKS_FILE + ";\n");
                } else {
                    sb.append("busybox echo N > " + PWKS_FILE + ";\n");
                }
            }

            if (new File(WAKE_TIMEOUT_FILE).exists()) {
                sb.append("busybox echo ").append(preferences.getInt(
                    PREF_WAKE_TIMEOUT, Integer.parseInt(FileUtils.readOneLine(WAKE_TIMEOUT_FILE))))
                    .append(" > ").append(WAKE_TIMEOUT_FILE).append(";\n");
            }

            if (new File(VIBRATION_STRENGTH_FILE).exists()) {
                sb.append("busybox echo ").append(preferences.getInt(
                    PREF_VIBRATION_STRENGTH, Integer.parseInt(FileUtils.readOneLine(VIBRATION_STRENGTH_FILE))))
                    .append(" > ").append(VIBRATION_STRENGTH_FILE).append(";\n");
            }

            if (new File(T2W_FILE).exists()) {
                if (preferences.getBoolean(PREF_TOUCHWAKE, false)) {
                    sb.append("busybox echo 1 > " + T2W_FILE + ";\n");
                } else {
                    sb.append("busybox echo 0 > " + T2W_FILE + ";\n");
                }
            }

            if (new File(T2W_TIMEOUT_FILE).exists()) {
                sb.append("busybox echo ").append(preferences.getInt(
                    PREF_TOUCHWAKE_TIMEOUT, Integer.parseInt(FileUtils.readOneLine(T2W_TIMEOUT_FILE))))
                    .append(" > ").append(T2W_TIMEOUT_FILE).append(";\n");
            }

            sb.append(preferences.getString(PREF_SH, "# no custom shell command")).append(";\n");
            FileUtils.shExec(sb, context, true);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            servicesStarted = true;
            stopSelf();
        }
    }
}
