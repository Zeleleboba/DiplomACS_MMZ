package Diplom.hibernate.dao;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Details", schema = "dbo", catalog = "ACS_WORK_COPY")
public class DetailsEntity {
    private int detailId;
    private Integer detailCode;
    private String detailName;
    private Set<OperationsEntity> operations= new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    public Set<OperationsEntity> getOperations(){
        return this.operations;
    }

    public void  setOperations(Set<OperationsEntity> operations){
        this.operations = operations;
    }
    public void addOperations(OperationsEntity operation){
        operation.setDetail(this);
        this.operations.add(operation);
    }



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Detail_Id", nullable = false)
    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    @Basic
    @Column(name = "Detail_Code", nullable = true)
    public Integer getDetailCode() {
        return detailCode;
    }

    public void setDetailCode(Integer detailCode) {
        this.detailCode = detailCode;
    }

    @Basic
    @Column(name = "Detail_Name", nullable = true, length = 90)
    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DetailsEntity that = (DetailsEntity) o;

        if (detailId != that.detailId) return false;
        if (detailCode != null ? !detailCode.equals(that.detailCode) : that.detailCode != null) return false;
        if (detailName != null ? !detailName.equals(that.detailName) : that.detailName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = detailId;
        result = 31 * result + (detailCode != null ? detailCode.hashCode() : 0);
        result = 31 * result + (detailName != null ? detailName.hashCode() : 0);
        return result;
    }
}
