package com.android.awells.coursetrackerroom.data;

/**
 * Created by Owner on 1/12/2018.
 */

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
/**
 * Data access object for Note.
 */
@Dao
public interface NoteDao {

    /**
     * Inserts a note into the table.
     *
     * @param note A new note.
     * @return The row ID of the newly inserted note.
     */
    @Insert
    long insert(Note note);

    /**
     * Select all notes from the given course ID.
     *
     * @param courseId The ID of the course
     * @return A {@link java.util.List<Note>} of all the notes in the given course.
     */
    @Query("SELECT * FROM " + Note.TABLE_NAME + " WHERE " + Note.COLUMN_COURSE_ID + " = :courseId")
    List<Note> selectNotesFromCourse(long courseId);

    /**
     * Select a note by the ID.
     *
     * @param id The row ID.
     * @return A {@link Note} of the selected note.
     */
    @Query("SELECT * FROM " + Note.TABLE_NAME + " WHERE " + Note.COLUMN_ID + " = :id")
    Note selectByNoteId(long id);

    /**
     * Delete a note by the ID.
     *
     * @param id The row ID.
     * @return A number of notes deleted. This should always be {@code 1}.
     */
    @Query("DELETE FROM " + Note.TABLE_NAME + " WHERE " + Note.COLUMN_ID + " = :id")
    int deleteById(long id);

    /**
     * Update the note. The note is identified by the row ID.
     *
     * @param note The note to update.
     * @return A number of notes updated. This should always be {@code 1}.
     */
    @Update
    int update(Note note);
}