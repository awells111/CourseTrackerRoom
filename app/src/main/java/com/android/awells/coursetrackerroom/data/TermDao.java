package com.android.awells.coursetrackerroom.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
/**
 * Created by Owner on 1/11/2018.
 */

/**
 * Data access object for Term.
 */
@Dao
public interface TermDao {

    /**
     * Counts the number of terms in the table.
     *
     * @return The number of terms.
     */
    @Query("SELECT COUNT(*) FROM " + Term.TABLE_NAME)
    int count();

    /**
     * Inserts a term into the table.
     *
     * @param term A new term.
     * @return The row ID of the newly inserted term.
     */
    @Insert
    long insert(Term term);

    /**
     * Select all terms.
     *
     * @return A {@link java.util.List<Term>} of all the terms in the table.
     */
    @Query("SELECT * FROM " + Term.TABLE_NAME)
    List<Term> selectAll();

    /**
     * Select a Term by the ID.
     *
     * @param id The row ID.
     * @return A {@link Term} of the selected term.
     */
    @Query("SELECT * FROM " + Term.TABLE_NAME + " WHERE " + Term.COLUMN_ID + " = :id")
    Term selectById(long id);

    /**
     * Delete a term by the ID.
     *
     * @param id The row ID.
     * @return A number of terms deleted. This should always be {@code 1}.
     */
    @Query("DELETE FROM " + Term.TABLE_NAME + " WHERE " + Term.COLUMN_ID + " = :id")
    int deleteById(long id);

    /**
     * Update the term. The term is identified by the row ID.
     *
     * @param term The term to update.
     * @return A number of terms updated. This should always be {@code 1}.
     */
    @Update
    int update(Term term);
}
