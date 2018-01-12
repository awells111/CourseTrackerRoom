package com.android.awells.coursetrackerroom.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.location.Address;
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

    /** The name of the term_id column. */
    public static final String COLUMN_TERM_ID = "term_id";

    /** The name of the ID column. */
    public static final String COLUMN_ID = BaseColumns._ID;

    /** The name of the title column. */
    public static final String COLUMN_TITLE = "title";

    /** The name of the status column. */
    public static final String COLUMN_STATUS = "status";

    /** The name of the start_notification column. */
    public static final String COLUMN_START_NOTIFICATION = "start_notification";

    /** The name of the end_notification column. */
    public static final String COLUMN_END_NOTIFICATION = "end_notification";

    /** The name of the course_mentor column. */
    public static final String COLUMN_COURSE_MENTOR = "course_mentor";

    /** The unique ID of the parent term */
    @ColumnInfo(index = true, name = COLUMN_TERM_ID)
    private int termId;

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

    /** The start notification time of the course. */
    @ColumnInfo(name = COLUMN_START_NOTIFICATION)
    private long startNotification = Long.MIN_VALUE;

    /** The end notification time of the course. */
    @ColumnInfo(name = COLUMN_END_NOTIFICATION)
    private long endNotification = Long.MIN_VALUE;

    /** The course mentor of the course. */
    @Embedded
    @ColumnInfo(name = COLUMN_COURSE_MENTOR)
    private CourseMentor courseMentor;

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
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

    public long getStartNotification() {
        return startNotification;
    }

    public void setStartNotification(long startNotification) {
        this.startNotification = startNotification;
    }

    public long getEndNotification() {
        return endNotification;
    }

    public void setEndNotification(long endNotification) {
        this.endNotification = endNotification;
    }

    public CourseMentor getCourseMentor() {
        return courseMentor;
    }

    public void setCourseMentor(CourseMentor courseMentor) {
        this.courseMentor = courseMentor;
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="equals hashCode and toString">
    @Override
    public String toString() {
        return "Course{" +
                "termId=" + termId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", startNotification=" + startNotification +
                ", endNotification=" + endNotification +
                ", courseMentor=" + courseMentor +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        if (getTermId() != course.getTermId()) return false;
        if (getId() != course.getId()) return false;
        if (getStartNotification() != course.getStartNotification()) return false;
        if (getEndNotification() != course.getEndNotification()) return false;
        if (getTitle() != null ? !getTitle().equals(course.getTitle()) : course.getTitle() != null)
            return false;
        if (getStatus() != null ? !getStatus().equals(course.getStatus()) : course.getStatus() != null)
            return false;
        return getCourseMentor() != null ? getCourseMentor().equals(course.getCourseMentor()) : course.getCourseMentor() == null;
    }

    @Override
    public int hashCode() {
        int result = getTermId();
        result = 31 * result + (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (int) (getStartNotification() ^ (getStartNotification() >>> 32));
        result = 31 * result + (int) (getEndNotification() ^ (getEndNotification() >>> 32));
        result = 31 * result + (getCourseMentor() != null ? getCourseMentor().hashCode() : 0);
        return result;
    }
    //</editor-fold>

    /** Dummy data. */
    static final String[] COURSES = {
            "C768", "C773", "C188", "C179", "C195", "C193"
    };
}

class CourseMentor {

    private String name;

    @ColumnInfo(name = "phone_number")
    private String phoneNumber;
    private String email;

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="equals hashCode and toString">
    @Override
    public String toString() {
        return "CourseMentor{" +
                "name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CourseMentor that = (CourseMentor) o;

        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null)
            return false;
        if (getPhoneNumber() != null ? !getPhoneNumber().equals(that.getPhoneNumber()) : that.getPhoneNumber() != null)
            return false;
        return getEmail() != null ? getEmail().equals(that.getEmail()) : that.getEmail() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getPhoneNumber() != null ? getPhoneNumber().hashCode() : 0);
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        return result;
    }
    //</editor-fold>
}
