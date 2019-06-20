package Diplom.hibernate.dao;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "WorkingDemand", schema = "dbo", catalog = "ACS_WORK_COPY")
public class WorkingDemandEntity {
    private int demandId;
    private Date demandDate;
    private Double requiredTime;
    private ProfessionsEntity profession;
    private DepartmentsSectionsEntity department;
    @Id
    @Column(name = "Demand_Id", nullable = false)
    public int getDemandId() {
        return demandId;
    }

    public void setDemandId(int demandId) {
        this.demandId = demandId;
    }

    @Basic
    @Column(name = "Demand_Date", nullable = true)
    public Date getDemandDate() {
        return demandDate;
    }

    public void setDemandDate(Date demandDate) {
        this.demandDate = demandDate;
    }

    @Basic
    @Column(name = "Required_Time", nullable = true, precision = 0)
    public Double getRequiredTime() {
        return requiredTime;
    }

    public void setRequiredTime(Double requiredTime) {
        this.requiredTime = requiredTime;
    }


    @ManyToOne
    @JoinColumn(name = "Profession_Id")
    public ProfessionsEntity getProfession() {
        return this.profession;
    }

    public void setProfession(ProfessionsEntity profession) {
        this.profession = profession;
    }

    @ManyToOne
    @JoinColumn(name = "Department_Section_Id")
    public DepartmentsSectionsEntity getDepartment() {
        return this.department;
    }

    public void setDepartment(DepartmentsSectionsEntity department) {
        this.department = department;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkingDemandEntity that = (WorkingDemandEntity) o;
        return demandId == that.demandId &&
                Objects.equals(demandDate, that.demandDate) &&
                Objects.equals(requiredTime, that.requiredTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(demandId, demandDate, requiredTime);
    }
}
