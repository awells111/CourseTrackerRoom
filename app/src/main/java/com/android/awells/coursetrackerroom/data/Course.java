package com.android.awells.coursetrackerroom.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
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

    /** Used to schedule a notification. */
    public static final long ALARM_REQUEST_CODE = 1000;

    /** The name of the Course table. */
    public static final String TABLE_NAME = "courses";

    /** The name of the term_id column. */
    public static final String COLUMN_TERM_ID = Term.TABLE_NAME + BaseColumns._ID;

    /** The name of the ID column. */
    public static final String COLUMN_ID = TABLE_NAME + BaseColumns._ID;

    /** The name of the title column. */
    public static final String COLUMN_TITLE = "title";

    /** The name of the status column. */
    public static final String COLUMN_STATUS = "status";

    /** The name of the start_date column. */
    public static final String COLUMN_START_DATE = "start_date";

    /** The name of the end_date column. */
    public static final String COLUMN_END_DATE = "end_date";

    /** The name of the course_mentor column. */
    public static final String PREFIX_COURSE_MENTOR = "mentor_";

    /** The unique ID of the parent term */
    @ColumnInfo(index = true, name = COLUMN_TERM_ID)
    private long termId;

    /** The unique ID of the course. */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    private long id;

    /** The title of the course. */
    @ColumnInfo(name = COLUMN_TITLE)
    private String title;

    /** The title of the course. */
    @ColumnInfo(name = COLUMN_STATUS)
    private String status;

    /** The start date of the course. */
    @ColumnInfo(name = COLUMN_START_DATE)
    private long startDate = Long.MIN_VALUE;

    /** The end date of the course. */
    @ColumnInfo(name = COLUMN_END_DATE)
    private long endDate = Long.MIN_VALUE;

    /** The course mentor of the course.
     * Embedded columns will be listed as mentor_name, mentor_phone_number, mentor_email*/
    @Embedded(prefix = PREFIX_COURSE_MENTOR)
    private CourseMentor courseMentor;

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">

    public long getTermId() {
        return termId;
    }

    public void setTermId(long termId) {
        this.termId = termId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public CourseMentor getCourseMentor() {
        return courseMentor;
    }

    public void setCourseMentor(CourseMentor courseMentor) {
        this.courseMentor = courseMentor;
    }

    //</editor-fold>

    @Override
    public String toString() {
        return "Course{" +
                "termId=" + termId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", courseMentor=" + courseMentor +
                '}';
    }

    /** Dummy data. */
    static final String[] COURSES = {
            "C768", "C773", "C188", "C179", "C195", "C193"
    };
}

