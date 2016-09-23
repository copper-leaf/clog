package com.caseyjbrooks.clog.spells;

import android.text.TextUtils;
import android.util.Log;

import com.caseyjbrooks.clog.Spell;

public class TheStandardBookOfSpells {

    @Spell(name="uppercase")
    public static String toUpper(Object data) {
        return data.toString().toUpperCase();
    }

    @Spell(name="lowercase")
    public static String toLower(Object data) {
        return data.toString().toLowerCase();
    }

    @Spell
    public static String capitalize(Object data) {
        String input = data.toString();
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    @Spell
    public static String repeat(Object data, int times) {
        String output = "";
        for(int i = 0; i < times; i++) {
            if(data != null) {
                output += data.toString();
            }
        }

        return output;
    }

    @Spell
    public static String join(Object[] data, String separator) {
        return TextUtils.join(separator, data);
    }

    @Spell
    public static String join(Iterable data, String separator) {
        return TextUtils.join(separator, data);
    }

    @Spell(name="className")
    public static String getClassName(Object data) {
        Log.i("Join", "className: " + data.getClass().getSimpleName());
        return data.getClass().getSimpleName();
    }

    @Spell
    public static String length(String data) {
        return Integer.toString(data.length());
    }
}
