package com.android.awells.coursetrackerroom.data;

/**
 * Created by Owner on 1/11/2018.
 */

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
/**
 * Data access object for Term.
 */
@Dao
public interface CourseDao {

    /**
     * Inserts a course into the table.
     *
     * @param course A new course.
     * @return The row ID of the newly inserted course.
     */
    @Insert
    long insert(Course course);

    /**
     * Select all courses from the given term ID.
     *
     * @param termId The ID of the term
     * @return A {@link java.util.List<Course>} of all the courses in the given term.
     */
    @Query("SELECT * FROM " + Course.TABLE_NAME + " WHERE " + Course.COLUMN_TERM_ID + " = :termId")
    List<Course> selectCoursesFromTerm(long termId);

    /**
     * Select a course by the ID.
     *
     * @param id The row ID.
     * @return A {@link Course} of the selected course.
     */
    @Query("SELECT * FROM " + Course.TABLE_NAME + " WHERE " + Course.COLUMN_ID + " = :id")
    Course selectByCourseId(long id);

    /**
     * Delete a course by the ID.
     *
     * @param id The row ID.
     * @return A number of courses deleted. This should always be {@code 1}.
     */
    @Query("DELETE FROM " + Course.TABLE_NAME + " WHERE " + Course.COLUMN_ID + " = :id") //todo cascade delete
    int deleteById(long id);

    /**
     * Update the course. The course is identified by the row ID.
     *
     * @param course The course to update.
     * @return A number of courses updated. This should always be {@code 1}.
     */
    @Update
    int update(Course course);
}