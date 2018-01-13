package com.android.awells.coursetrackerroom;

/**
 * Created by Owner on 1/13/2018.
 */

import android.widget.Spinner;
/** Helper methods that don't fit in a class*/
public class CourseTrackerHelper {

    /**
     * Find the index of a String in a given Spinner
     *
     * spinner.setSelection(getIndex(spinner, string));
     *
     * @param spinner The Spinner to be searched
     * @param string The String to be found
     * @return The location of a String in the spinner
     * */
    public static int getIndex(Spinner spinner, String string)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(string)){
                index = i;
                break;
            }
        }
        return index;
    }
}
