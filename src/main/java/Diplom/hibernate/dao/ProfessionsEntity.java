package Diplom.hibernate.dao;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.*;

@Entity
@Table(name = "Professions", schema = "dbo", catalog = "ACS_WORK_COPY")
public class ProfessionsEntity {
    private int professionId;
    private int professionCode;
    private String professionName;



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Profession_Id", nullable = false)
    public int getProfessionId() {
        return professionId;
    }

    public void setProfessionId(int professionId) {
        this.professionId = professionId;
    }

    @Basic
    @Column(name = "Profession_Code", nullable = false)
    public int getProfessionCode() {
        return professionCode;
    }

    public void setProfessionCode(int professionCode) {
        this.professionCode = professionCode;
    }

    @Basic
    @Column(name = "Profession_Name", nullable = false, length = 60)
    public String getProfessionName() {
        return professionName;
    }


    public void setProfessionName(String professionName) {
        this.professionName = professionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProfessionsEntity that = (ProfessionsEntity) o;

        if (professionId != that.professionId) return false;
        if (professionCode != that.professionCode) return false;
        if (professionName != null ? !professionName.equals(that.professionName) : that.professionName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = professionId;
        result = 31 * result + professionCode;
        result = 31 * result + (professionName != null ? professionName.hashCode() : 0);
        return result;
    }
}
