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

import android.content.BroadcastReceiver;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.mahdi.touchcontrol.R;

import java.io.File;
import java.util.List;


public class BootReceiver extends BroadcastReceiver {

    public static final String TOUCHCONTROL_PREFERENCES = "com.mahdi.touchcontrol_preferences";

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences preferences = context.getSharedPreferences(
                    TOUCHCONTROL_PREFERENCES, Activity.MODE_PRIVATE);

        boolean setOnBoot = preferences.getBoolean("set_on_boot", false);
        Intent service = new Intent(context, BootService.class);
        if (setOnBoot == true) {
            context.startService(service);
        }
    }
}
