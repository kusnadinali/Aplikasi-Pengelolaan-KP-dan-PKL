package com.jtk.ps.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jtk.ps.api.model.CourseForm;

@Repository
public interface CourseFormRepository extends JpaRepository<CourseForm,Integer>{
    
    @Query(value = "select * from course_form where is_deleted = 0 and tahun_ajaran_start = :year", nativeQuery = true)
    List<CourseForm> findAllCourse(Integer year);

    @Query(value = "select * from course_form where is_deleted = 0 and tahun_ajaran_start = :year and prodi_id = :prodiId",nativeQuery = true)
    List<CourseForm> findAllCourseByYearAndProdiId(Integer year, Integer prodiId);

    @Query(value = "select distinct a.*  from course_form a join component_course b on a.id = b.course_id join criteria_component_course c on c.component_id = b.id join course_values d on d.criteria_id = c.id  join participant p on p.id = d.participant_id where p.`year` = :year and p.prodi_id = :prodiId",nativeQuery = true)
    List<CourseForm> findAllOldCourseByYearAndProdiId(Integer year, Integer prodiId);

    @Query(value = "SELECT case WHEN ( SELECT COUNT(*) FROM course_form  WHERE tahun_ajaran_start = :year AND is_finalization = 0 and prodi_id = :prodiId and is_deleted = 0) = 0 THEN 1 ELSE 0 END AS is_all_finalized", nativeQuery = true)
    Integer isAllFinalizationByYear(Integer year, Integer prodiId);
}
