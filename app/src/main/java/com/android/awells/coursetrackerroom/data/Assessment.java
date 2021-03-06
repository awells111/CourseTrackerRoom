package com.android.awells.coursetrackerroom.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.provider.BaseColumns;

import static android.arch.persistence.room.ForeignKey.CASCADE;
/**
 * Created by Owner on 1/12/2018.
 */

@Entity(tableName = Assessment.TABLE_NAME,
        foreignKeys = @ForeignKey(entity = Course.class,
                onUpdate = CASCADE,
                onDelete = CASCADE,
                parentColumns = Course.COLUMN_ID,
                childColumns = Assessment.COLUMN_COURSE_ID))
public class Assessment {

    /** Used to schedule a notification. */
    public static final long ALARM_REQUEST_CODE = 2000;

    /** The name of the Assessment table. */
    public static final String TABLE_NAME = "assessments";

    /** The name of the course_id column. */
    public static final String COLUMN_COURSE_ID = Course.TABLE_NAME + BaseColumns._ID;

    /** The name of the ID column. */
    public static final String COLUMN_ID = TABLE_NAME + BaseColumns._ID;

    /** The name of the type column. */
    public static final String COLUMN_TYPE = "type";

    /** The name of the score column. */
    public static final String COLUMN_SCORE = "score";

    /** The name of the scheduled_time column. */
    public static final String COLUMN_SCHEDULED_TIME = "scheduled_time";

    /** The name of the start_notification column. */
    public static final String COLUMN_START_NOTIFICATION = "start_notification";

    /** The unique ID of the parent course */
    @ColumnInfo(index = true, name = COLUMN_COURSE_ID)
    private long courseId;

    /** The unique ID of the assessment. */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    private long id;

    /** The type of the assessment. */
    @ColumnInfo(name = COLUMN_TYPE)
    private String type;

    /** The score of the assessment. */
    @ColumnInfo(name = COLUMN_SCORE)
    private int score = -1;

    /** The scheduled time of the assessment. */
    @ColumnInfo(name = COLUMN_SCHEDULED_TIME)
    private long scheduledTime = Long.MIN_VALUE;

    /** The start notification of the assessment. */
    @ColumnInfo(name = COLUMN_START_NOTIFICATION)
    private boolean startNotification = false;

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(long scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public boolean isStartNotification() {
        return startNotification;
    }

    public void setStartNotification(boolean startNotification) {
        this.startNotification = startNotification;
    }

    //</editor-fold>

    @Override
    public String toString() {
        return "Assessment{" +
                "courseId=" + courseId +
                ", id=" + id +
                ", type='" + type + '\'' +
                ", score=" + score +
                ", scheduledTime=" + scheduledTime +
                ", startNotification=" + startNotification +
                '}';
    }
}
