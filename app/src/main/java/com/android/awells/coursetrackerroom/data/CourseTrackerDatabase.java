package com.android.awells.coursetrackerroom.data;

/**
 * Created by Owner on 1/11/2018.
 */

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.VisibleForTesting;
/**
 * The Room database.
 */
@Database(entities = {Term.class}, version = 1)
public abstract class CourseTrackerDatabase extends RoomDatabase{

    /**
     * @return The DAO for the Term table.
     */
    public abstract TermDao term();

    /** The only instance */
    private static CourseTrackerDatabase sInstance;

    /**
     * Gets the singleton instance of SampleDatabase.
     *
     * @param context The context.
     * @return The singleton instance of SampleDatabase.
     */
    public static synchronized CourseTrackerDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), CourseTrackerDatabase.class, "ex") //todo database name?
                    .allowMainThreadQueries() //todo SHORT TERM SOLUTION JUST FOR THIS SCHOOL PROJECT
                    .build();
            sInstance.populateInitialData();
        }
        return sInstance;
    }

    /**
     * Switches the internal implementation with an empty in-memory database.
     *
     * @param context The context.
     */
    @VisibleForTesting
    public static void switchToInMemory(Context context) {
        sInstance = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                CourseTrackerDatabase.class).build();
    }

    /**
     * Inserts the dummy data into the database if it is currently empty.
     */
    private void populateInitialData() { //todo Add course sample data
        if (term().count() == 0) {
            Term term = new Term();
            beginTransaction();
            try {
                for (int i = 0; i < Term.TERMS.length; i++) {
                    term.setTitle(Term.TERMS[i]);
                    term().insert(term);
                }
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }
}
