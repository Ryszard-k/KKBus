package com.pz.KKBus.Staff.Manager;

import com.pz.KKBus.Staff.Model.Entites.Courses.Courses;
import com.pz.KKBus.Staff.Model.Entites.Courses.Report;
import com.pz.KKBus.Staff.Model.Repositories.Courses.ReportRepo;
import com.pz.KKBus.Staff.Model.Repositories.Courses.StopPassengersPairRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReportManager {

    private final ReportRepo reportRepo;
    private final CoursesManager coursesManager;
    private final StopPassengersPairRepo stopPassengersPairRepo;

    @Autowired
    public ReportManager(ReportRepo reportRepo, CoursesManager coursesManager, StopPassengersPairRepo stopPassengersPairRepo) {
        this.reportRepo = reportRepo;
        this.coursesManager = coursesManager;
        this.stopPassengersPairRepo = stopPassengersPairRepo;
    }

    public List<Report> findAll(){
        return reportRepo.findAll();
    }

    public Optional<Report> findById(Long id){
        return reportRepo.findById(id);
    }

    public Optional<Report> findByCourses(Long id){
        Optional<Courses> courses = coursesManager.findById(id);
        if (courses.isPresent()){
            return reportRepo.findByCourses(courses.get());
        } else
        return Optional.empty();
    }

    public Report save(Report report) {
        return reportRepo.save(report);
    }

    public Optional<Report> deleteById(Long id){
        Optional<Report> deleted = reportRepo.findById(id);
        reportRepo.deleteById(id);

        return deleted;
    }

}
