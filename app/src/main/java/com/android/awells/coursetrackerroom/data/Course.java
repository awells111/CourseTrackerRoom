package com.android.awells.coursetrackerroom.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.provider.BaseColumns;

import static android.arch.persistence.room.ForeignKey.CASCADE;
/**
 * Created by Owner on 1/11/2018.
 */

/**
 * Represents one record of the Course table.
 */
@Entity(tableName = Course.TABLE_NAME,
        foreignKeys = @ForeignKey(entity = Term.class,
        onUpdate = CASCADE,
        onDelete = CASCADE,
        parentColumns = Term.COLUMN_ID,
        childColumns = Course.COLUMN_TERM_ID))
public class Course {

    /** The name of the Course table. */
    public static final String TABLE_NAME = "courses";

    /** The name of the ID column. */
    public static final String COLUMN_ID = BaseColumns._ID;

    /** The name of the name column. */
    public static final String COLUMN_TITLE = "title";

    /** The name of the term_id column. */
    public static final String COLUMN_TERM_ID = "term_id";

    /** The unique ID of the course. */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    public long id;

    /** The name of the course. */
    @ColumnInfo(name = COLUMN_TITLE)
    public String title;

    /** The unique ID of the parent term */
    @ColumnInfo(name = COLUMN_TERM_ID)
    private int termId;

    public Course() {}

    public Course(String title, int termId) {
        this.title = title;
        this.termId = termId;
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

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", termId=" + termId +
                '}';
    }

    /** Dummy data. */
    static final String[] COURSES = {
            "C768", "C773", "C188", "C179", "C195", "C193"
    };
}
