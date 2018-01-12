package com.android.awells.coursetrackerroom;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
/**
 * Created by Owner on 1/11/2018.
 */

public class DatePickerFragment extends DialogFragment {

    public static final String DATE_FORMAT = "MM/dd/yyyy";
    public static final String DATE_PICKER_TAG = "DATE_PICKER";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(),
                (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
    }

    public static String formatMyTime(long time) {
        SimpleDateFormat sdfDate = new SimpleDateFormat(DATE_FORMAT);
        return sdfDate.format(time);
    }

    /*Returns a long that is equal to Midnight today.*/
    public static Long getBeginningOfDayTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long time = cal.getTimeInMillis();
        return time;
    }
}