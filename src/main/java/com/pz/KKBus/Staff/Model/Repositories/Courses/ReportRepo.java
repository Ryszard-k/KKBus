package com.pz.KKBus.Staff.Model.Repositories.Courses;

import com.pz.KKBus.Staff.Model.Entites.Courses.Courses;
import com.pz.KKBus.Staff.Model.Entites.Courses.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportRepo extends JpaRepository<Report, Long> {

    Optional<Report> findByCourses(Courses courses);
}
