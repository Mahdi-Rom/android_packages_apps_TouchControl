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

package com.mahdi.touchcontrol.util;

public interface Constants {

    public static final String DT2W_FILE = "/sys/android_touch/doubletap2wake";
    public static final String DT2W2_FILE = "/sys/devices/virtual/input/lge_touch/dt_wake_enabled";
    public static final String S2W_FILE = "/sys/android_touch/sweep2wake";
    public static final String S2S_FILE = "/sys/android_touch/sweep2sleep";
    public static final String GESTURES_FILE = "/sys/android_touch/wake_gestures";
    public static final String PWKS_FILE = "/sys/module/qpnp_power_on/parameters/pwrkey_suspend";
    public static final String WAKE_TIMEOUT_FILE = "/sys/android_touch/wake_timeout";
    public static final String VIBRATION_STRENGTH_FILE = "/sys/android_touch/vib_strength";
    public static final String T2W_FILE = "/sys/devices/virtual/misc/touchwake/enabled";
    public static final String T2W_TIMEOUT_FILE = "/sys/devices/virtual/misc/touchwake/delay";

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

    public static final String PREF_SH = "pref_sh";
    public static final String NOT_FOUND = "not found";

    public static final String PREF_SOB = "set_on_boot";
}


