package Diplom.hibernate.dao;

import Diplom.hibernate.util.HibernateSessionFactory;
import javafx.fxml.FXML;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Departments_Sections", schema = "dbo", catalog = "ACS_WORK_COPY")
public class DepartmentsSectionsEntity {
    private int departmentId;
    private int departmentCode;
    private String departmentName;
    private Integer departmentParentId;
    private String departmentParentName;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Department_Id", nullable = false)
    public int getDepartmentId() {
        return departmentId;
    }
    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    @Basic
    @Column(name = "Department_Code", nullable = false)
    public int getDepartmentCode() {
        return departmentCode;
    }
    public void setDepartmentCode(int departmentCode) {
        this.departmentCode = departmentCode;
    }

    @Basic
    @Column(name = "Department_Parent_Name", nullable = true, length = 100)
    public String getDepartmentParentName() {
        return departmentParentName;
    }
    public void setDepartmentParentName(String departmentParentName) {
        this.departmentParentName = departmentParentName;
    }

    @Basic
    @Column(name = "Department_Name", nullable = true, length = 50)
    public String getDepartmentName() {
        return departmentName;
    }
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepartmentsSectionsEntity that = (DepartmentsSectionsEntity) o;
        return departmentId == that.departmentId &&
                departmentCode == that.departmentCode &&
                Objects.equals(departmentName, that.departmentName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentId, departmentCode, departmentName);
    }

    @Basic
    @Column(name = "Department_Parent_Id", nullable = true)
    public Integer getDepartmentParentId() {
        return departmentParentId;
    }

    public void setDepartmentParentId(Integer departmentParentId) {
        this.departmentParentId = departmentParentId;
    }




}
