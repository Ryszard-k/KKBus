package com.pz.KKBus.Customer.Model;

import com.pz.KKBus.Staff.Model.Entites.Courses.Courses;
import com.pz.KKBus.Staff.Model.Entites.Courses.Report;
import org.apache.catalina.LifecycleState;

import java.util.List;

public class ReportWithReservationsIds {

    private Report report;

    private List<Long> ids;

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
