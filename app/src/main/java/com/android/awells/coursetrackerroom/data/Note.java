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

@Entity(tableName = Note.TABLE_NAME,
        foreignKeys = @ForeignKey(entity = Course.class,
                onUpdate = CASCADE,
                onDelete = CASCADE,
                parentColumns = Course.COLUMN_ID,
                childColumns = Note.COLUMN_COURSE_ID))
public class Note {

    /**
     * The name of the Note table.
     */
    public static final String TABLE_NAME = "notes";

    /**
     * The name of the course_id column.
     */
    public static final String COLUMN_COURSE_ID = "course_id";

    /**
     * The name of the ID column.
     */
    public static final String COLUMN_ID = BaseColumns._ID;

    /**
     * The name of the text column.
     */
    public static final String COLUMN_TEXT = "text";

    /**
     * The unique ID of the parent course
     */
    @ColumnInfo(index = true, name = COLUMN_COURSE_ID)
    private int courseId;

    /**
     * The unique ID of the note.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    private long id;

    /**
     * The text of the note.
     */
    @ColumnInfo(name = COLUMN_TEXT)
    private String text;

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="equals hashCode and toString">
    @Override
    public String toString() {
        return "Note{" +
                "courseId=" + courseId +
                ", id=" + id +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note note = (Note) o;

        if (getCourseId() != note.getCourseId()) return false;
        if (getId() != note.getId()) return false;
        return getText() != null ? getText().equals(note.getText()) : note.getText() == null;
    }

    @Override
    public int hashCode() {
        int result = getCourseId();
        result = 31 * result + (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getText() != null ? getText().hashCode() : 0);
        return result;
    }
//</editor-fold>
}
