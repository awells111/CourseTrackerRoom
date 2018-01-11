package com.android.awells.coursetrackerroom.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.provider.BaseColumns;
/**
 * Created by Owner on 1/11/2018.
 */

/**
 * Represents one record of the Term table.
 */
@Entity(tableName = Term.TABLE_NAME)
public class Term {

    /** The name of the Term table. */
    public static final String TABLE_NAME = "terms";

    /** The name of the ID column. */
    public static final String COLUMN_ID = BaseColumns._ID;

    /** The name of the name column. */
    public static final String COLUMN_TITLE = "title";

    /** The unique ID of the term. */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    public long id;

    /** The name of the term. */
    @ColumnInfo(name = COLUMN_TITLE)
    public String title;

    /** Empty constructor for the term. */
    public Term() {}

    /** Constructor for the term. */
    public Term(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Term{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }

    /** Dummy data. */
    static final String[] TERMS = {
            "Term 0", "Term 1", "Term 2", "Term 3", "Term 4"
    };

}

