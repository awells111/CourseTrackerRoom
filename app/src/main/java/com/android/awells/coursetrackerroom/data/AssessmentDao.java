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
 * Data access object for Assessment.
 */
@Dao
public interface AssessmentDao {

    /**
     * Inserts an assessment into the table.
     *
     * @param assessment A new assessment.
     * @return The row ID of the newly inserted assessment.
     */
    @Insert
    long insert(Assessment assessment);

    /**
     * Select all assessments from the given course ID.
     *
     * @param courseId The ID of the course
     * @return A {@link java.util.List<Assessment>} of all the assessments in the given course.
     */
    @Query("SELECT * FROM " + Assessment.TABLE_NAME + " WHERE " + Assessment.COLUMN_COURSE_ID + " = :courseId")
    List<Assessment> selectAssessmentsFromCourse(long courseId);

    /**
     * Select an assessment by the ID.
     *
     * @param id The row ID.
     * @return A {@link Assessment} of the selected assessment.
     */
    @Query("SELECT * FROM " + Assessment.TABLE_NAME + " WHERE " + Assessment.COLUMN_ID + " = :id")
    Assessment selectByAssessmentId(long id);

    /**
     * Delete an assessment by the ID.
     *
     * @param id The row ID.
     * @return A number of assessments deleted. This should always be {@code 1}.
     */
    @Query("DELETE FROM " + Assessment.TABLE_NAME + " WHERE " + Assessment.COLUMN_ID + " = :id")
    int deleteById(long id);

    /**
     * Update the assessment. The assessment is identified by the row ID.
     *
     * @param assessment The assessment to update.
     * @return A number of assessments updated. This should always be {@code 1}.
     */
    @Update
    int update(Assessment assessment);
}