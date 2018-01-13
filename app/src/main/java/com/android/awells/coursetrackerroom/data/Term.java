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
    public static final String COLUMN_ID = TABLE_NAME + BaseColumns._ID;

    /** The name of the title column. */
    public static final String COLUMN_TITLE = "title";

    /** The name of the start date column. */
    public static final String COLUMN_START_DATE = "start_date";

    /** The name of the end date column. */
    public static final String COLUMN_END_DATE = "end_date";

    /** The unique ID of the term. */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    private long id;

    /** The title of the term. */
    @ColumnInfo(name = COLUMN_TITLE)
    private String title;

    /** The start date of the term. */
    @ColumnInfo(name = COLUMN_START_DATE)
    private long startDate = Long.MIN_VALUE;

    /** The end date of the term. */
    @ColumnInfo(name = COLUMN_END_DATE)
    private long endDate = Long.MIN_VALUE;

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
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
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="equals hashCode and toString">
    @Override
    public String toString() {
        return "Term{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Term term = (Term) o;

        if (getId() != term.getId()) return false;
        if (getStartDate() != term.getStartDate()) return false;
        if (getEndDate() != term.getEndDate()) return false;
        return getTitle() != null ? getTitle().equals(term.getTitle()) : term.getTitle() == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
        result = 31 * result + (int) (getStartDate() ^ (getStartDate() >>> 32));
        result = 31 * result + (int) (getEndDate() ^ (getEndDate() >>> 32));
        return result;
    }
    //</editor-fold>
    
    /** Dummy data. */
    static final String[] TERMS = {
            "Term 0", "Term 1", "Term 2", "Term 3", "Term 4"
    };
}

