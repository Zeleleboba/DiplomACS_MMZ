package Diplom.hibernate.dao;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "StaffCalculation", schema = "dbo", catalog = "ACS_WORK_COPY")
public class StaffCalculationEntity {
    private int calculationId;
    private Integer departmentId;
    private String departmentName;
    private Integer areaId;
    private String areaName;
    private Integer professionCode;
    private String professionName;
    private Date calculationDate;
    private Integer countWorkers;
    private Integer countWorkersCalculated;
    private Integer fifthDays;
    private Integer sixthDays;
    private Integer workingDays;
    private Double requiredTime;
    private Double startWorkingTime;
    private Double workingTime;
    private Double overworkingTimePerMonth;
    private Integer workingSaturdays;
    private Integer staffNeed;
    private Integer workersDiff;
    private Integer parametrIndex;
    private Byte paramOverWork;
    private Byte paramWorkSatur;
    private Byte paramReplaceProf;
    private Byte paramDepMove;
    private Byte paramAreaMove;

    @Id
    @Column(name = "Calculation_Id", nullable = false)
    public int getCalculationId() {
        return calculationId;
    }

    public void setCalculationId(int calculationId) {
        this.calculationId = calculationId;
    }

    @Basic
    @Column(name = "Department_Id", nullable = true)
    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    @Basic
    @Column(name = "Department_Name", nullable = true, length = 100)
    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Basic
    @Column(name = "Area_Id", nullable = true)
    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    @Basic
    @Column(name = "Area_Name", nullable = true, length = 100)
    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    @Basic
    @Column(name = "Profession_Code", nullable = true)
    public Integer getProfessionCode() {
        return professionCode;
    }

    public void setProfessionCode(Integer professionCode) {
        this.professionCode = professionCode;
    }

    @Basic
    @Column(name = "Profession_Name", nullable = true, length = 100)
    public String getProfessionName() {
        return professionName;
    }

    public void setProfessionName(String professionName) {
        this.professionName = professionName;
    }

    @Basic
    @Column(name = "Calculation_Date", nullable = true)
    public Date getCalculationDate() {
        return calculationDate;
    }

    public void setCalculationDate(Date calculationDate) {
        this.calculationDate = calculationDate;
    }

    @Basic
    @Column(name = "Count_Workers", nullable = true)
    public Integer getCountWorkers() {
        return countWorkers;
    }

    public void setCountWorkers(Integer countWorkers) {
        this.countWorkers = countWorkers;
    }

    @Basic
    @Column(name = "Count_Workers_Calculated", nullable = true)
    public Integer getCountWorkersCalculated() {
        return countWorkersCalculated;
    }

    public void setCountWorkersCalculated(Integer countWorkersCalculated) {
        this.countWorkersCalculated = countWorkersCalculated;
    }

    @Basic
    @Column(name = "Fifth_Days", nullable = true)
    public Integer getFifthDays() {
        return fifthDays;
    }

    public void setFifthDays(Integer fifthDays) {
        this.fifthDays = fifthDays;
    }

    @Basic
    @Column(name = "Sixth_Days", nullable = true)
    public Integer getSixthDays() {
        return sixthDays;
    }

    public void setSixthDays(Integer sixthDays) {
        this.sixthDays = sixthDays;
    }

    @Basic
    @Column(name = "Working_Days", nullable = true)
    public Integer getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(Integer workingDays) {
        this.workingDays = workingDays;
    }

    @Basic
    @Column(name = "Required_Time", nullable = true, precision = 0)
    public Double getRequiredTime() {
        return requiredTime;
    }

    public void setRequiredTime(Double requiredTime) {
        this.requiredTime = requiredTime;
    }

    @Basic
    @Column(name = "Start_Working_Time", nullable = true, precision = 0)
    public Double getStartWorkingTime() {
        return startWorkingTime;
    }

    public void setStartWorkingTime(Double startWorkingTime) {
        this.startWorkingTime = startWorkingTime;
    }

    @Basic
    @Column(name = "Working_Time", nullable = true, precision = 0)
    public Double getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(Double workingTime) {
        this.workingTime = workingTime;
    }

    @Basic
    @Column(name = "Overworking_Time_Per_Month", nullable = true, precision = 0)
    public Double getOverworkingTimePerMonth() {
        return overworkingTimePerMonth;
    }

    public void setOverworkingTimePerMonth(Double overworkingTimePerMonth) {
        this.overworkingTimePerMonth = overworkingTimePerMonth;
    }

    @Basic
    @Column(name = "Working_Saturdays", nullable = true)
    public Integer getWorkingSaturdays() {
        return workingSaturdays;
    }

    public void setWorkingSaturdays(Integer workingSaturdays) {
        this.workingSaturdays = workingSaturdays;
    }

    @Basic
    @Column(name = "Staff_Need", nullable = true)
    public Integer getStaffNeed() {
        return staffNeed;
    }

    public void setStaffNeed(Integer staffNeed) {
        this.staffNeed = staffNeed;
    }

    @Basic
    @Column(name = "Workers_Diff", nullable = true)
    public Integer getWorkersDiff() {
        return workersDiff;
    }

    public void setWorkersDiff(Integer workersDiff) {
        this.workersDiff = workersDiff;
    }

    @Basic
    @Column(name = "Parametr_Index", nullable = true)
    public Integer getParametrIndex() {
        return parametrIndex;
    }

    public void setParametrIndex(Integer parametrIndex) {
        this.parametrIndex = parametrIndex;
    }

    @Basic
    @Column(name = "Param_OverWork", nullable = true)
    public Byte getParamOverWork() {
        return paramOverWork;
    }

    public void setParamOverWork(Byte paramOverWork) {
        this.paramOverWork = paramOverWork;
    }

    @Basic
    @Column(name = "Param_WorkSatur", nullable = true)
    public Byte getParamWorkSatur() {
        return paramWorkSatur;
    }

    public void setParamWorkSatur(Byte paramWorkSatur) {
        this.paramWorkSatur = paramWorkSatur;
    }

    @Basic
    @Column(name = "Param_ReplaceProf", nullable = true)
    public Byte getParamReplaceProf() {
        return paramReplaceProf;
    }

    public void setParamReplaceProf(Byte paramReplaceProf) {
        this.paramReplaceProf = paramReplaceProf;
    }

    @Basic
    @Column(name = "Param_DepMove", nullable = true)
    public Byte getParamDepMove() {
        return paramDepMove;
    }

    public void setParamDepMove(Byte paramDepMove) {
        this.paramDepMove = paramDepMove;
    }

    @Basic
    @Column(name = "Param_AreaMove", nullable = true)
    public Byte getParamAreaMove() {
        return paramAreaMove;
    }

    public void setParamAreaMove(Byte paramAreaMove) {
        this.paramAreaMove = paramAreaMove;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StaffCalculationEntity that = (StaffCalculationEntity) o;
        return calculationId == that.calculationId &&
                Objects.equals(departmentId, that.departmentId) &&
                Objects.equals(departmentName, that.departmentName) &&
                Objects.equals(areaId, that.areaId) &&
                Objects.equals(areaName, that.areaName) &&
                Objects.equals(professionCode, that.professionCode) &&
                Objects.equals(professionName, that.professionName) &&
                Objects.equals(calculationDate, that.calculationDate) &&
                Objects.equals(countWorkers, that.countWorkers) &&
                Objects.equals(countWorkersCalculated, that.countWorkersCalculated) &&
                Objects.equals(fifthDays, that.fifthDays) &&
                Objects.equals(sixthDays, that.sixthDays) &&
                Objects.equals(workingDays, that.workingDays) &&
                Objects.equals(requiredTime, that.requiredTime) &&
                Objects.equals(startWorkingTime, that.startWorkingTime) &&
                Objects.equals(workingTime, that.workingTime) &&
                Objects.equals(overworkingTimePerMonth, that.overworkingTimePerMonth) &&
                Objects.equals(workingSaturdays, that.workingSaturdays) &&
                Objects.equals(staffNeed, that.staffNeed) &&
                Objects.equals(workersDiff, that.workersDiff) &&
                Objects.equals(parametrIndex, that.parametrIndex) &&
                Objects.equals(paramOverWork, that.paramOverWork) &&
                Objects.equals(paramWorkSatur, that.paramWorkSatur) &&
                Objects.equals(paramReplaceProf, that.paramReplaceProf) &&
                Objects.equals(paramDepMove, that.paramDepMove) &&
                Objects.equals(paramAreaMove, that.paramAreaMove);
    }

    @Override
    public int hashCode() {
        return Objects.hash(calculationId, departmentId, departmentName, areaId, areaName, professionCode, professionName, calculationDate, countWorkers, countWorkersCalculated, fifthDays, sixthDays, workingDays, requiredTime, startWorkingTime, workingTime, overworkingTimePerMonth, workingSaturdays, staffNeed, workersDiff, parametrIndex, paramOverWork, paramWorkSatur, paramReplaceProf, paramDepMove, paramAreaMove);
    }
}
