package Diplom.hibernate.dao;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Replaceable_Professions", schema = "dbo", catalog = "ACS_WORK_COPY")
public class ReplaceableProfessionsEntity {
    private int replaceId;
    private DepartmentsSectionsEntity department;
    private ProfessionsEntity profession;
    private ProfessionsEntity new_profession;


    @Id
    @Column(name = "Replace_id", nullable = false)
    public int getReplaceId() {
        return replaceId;
    }

    public void setReplaceId(int replaceId) {
        this.replaceId = replaceId;
    }

    @ManyToOne
    @JoinColumn(name = "Department_Id")
    public DepartmentsSectionsEntity getDepartment() {
        return this.department;
    }

    public void setDepartment(DepartmentsSectionsEntity department) {
        this.department = department;
    }


    @ManyToOne
    @JoinColumn(name = "profession_code")
    public ProfessionsEntity getProfession() {
        return this.profession;
    }

    public void setProfession(ProfessionsEntity profession) {
        this.profession = profession;
    }


    @ManyToOne
    @JoinColumn(name = "profession_code_new")
    public ProfessionsEntity getNewProfession() {
        return this.new_profession;
    }

    public void setNewProfession(ProfessionsEntity profession) {
        this.new_profession = profession;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReplaceableProfessionsEntity that = (ReplaceableProfessionsEntity) o;
        return replaceId == that.replaceId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(replaceId);
    }
}
