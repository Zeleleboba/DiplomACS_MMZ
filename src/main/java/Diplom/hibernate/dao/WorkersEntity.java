package Diplom.hibernate.dao;

import javax.persistence.*;

@Entity
@Table(name = "Workers", schema = "dbo", catalog = "ACS_WORK_COPY")
public class WorkersEntity {
    private int workerId;
    private String workerSurname;
    private String workerName;
    private String workerPatronymic;
    private Byte workerPaymentType;
    private Double workerCountHours;
    private ProfessionsEntity profession;
    private DepartmentsSectionsEntity department;





    @Id
    @Column(name = "Worker_ID", nullable = false)
    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    @Basic
    @Column(name = "Worker_Surname", nullable = true, length = 35)
    public String getWorkerSurname() {
        return workerSurname;
    }

    public void setWorkerSurname(String workerSurname) {
        this.workerSurname = workerSurname;
    }

    @Basic
    @Column(name = "Worker_Name", nullable = true, length = 35)
    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    @Basic
    @Column(name = "Worker_Patronymic", nullable = true, length = 35)
    public String getWorkerPatronymic() {
        return workerPatronymic;
    }

    public void setWorkerPatronymic(String workerPatronymic) {
        this.workerPatronymic = workerPatronymic;
    }

    @ManyToOne
    @JoinColumn(name = "Worker_Profession_Id")
    public ProfessionsEntity getProfession() {
        return this.profession;
    }

    public void setProfession(ProfessionsEntity profession) {
        this.profession = profession;
    }

    @ManyToOne
    @JoinColumn(name = "Worker_Section_Id")
    public DepartmentsSectionsEntity getDepartment() {
        return this.department;
    }

    public void setDepartment(DepartmentsSectionsEntity department) {
        this.department = department;
    }

    @Basic
    @Column(name = "Worker_Payment_Type", nullable = true)
    public Byte getWorkerPaymentType() {
        return workerPaymentType;
    }

    public void setWorkerPaymentType(Byte workerPaymentType) {
        this.workerPaymentType = workerPaymentType;
    }

    @Basic
    @Column(name = "Worker_Count_Hours", nullable = true, precision = 0)
    public Double getWorkerCountHours() {
        return workerCountHours;
    }

    public void setWorkerCountHours(Double workerCountHours) {
        this.workerCountHours = workerCountHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkersEntity that = (WorkersEntity) o;

        if (workerId != that.workerId) return false;
        if (workerSurname != null ? !workerSurname.equals(that.workerSurname) : that.workerSurname != null)
            return false;
        if (workerName != null ? !workerName.equals(that.workerName) : that.workerName != null) return false;
        if (workerPatronymic != null ? !workerPatronymic.equals(that.workerPatronymic) : that.workerPatronymic != null)
            return false;
        if (workerPaymentType != null ? !workerPaymentType.equals(that.workerPaymentType) : that.workerPaymentType != null)
            return false;
        if (workerCountHours != null ? !workerCountHours.equals(that.workerCountHours) : that.workerCountHours != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = workerId;
        result = 31 * result + (workerSurname != null ? workerSurname.hashCode() : 0);
        result = 31 * result + (workerName != null ? workerName.hashCode() : 0);
        result = 31 * result + (workerPatronymic != null ? workerPatronymic.hashCode() : 0);
        result = 31 * result + (workerPaymentType != null ? workerPaymentType.hashCode() : 0);
        result = 31 * result + (workerCountHours != null ? workerCountHours.hashCode() : 0);
        return result;
    }
}
