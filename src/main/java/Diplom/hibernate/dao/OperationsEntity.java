package Diplom.hibernate.dao;

import javax.persistence.*;

@Entity
@Table(name = "Operations", schema = "dbo", catalog = "ACS_WORK_COPY")
public class OperationsEntity {
    private int operationId;
    private String operationName;
    private Integer operationStageNumber;
    private Double operationTimeExecution;
    private ProfessionsEntity professionsByOperationProfessionId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Operation_ID", nullable = false)
    public int getOperationId() {
        return operationId;
    }

    public void setOperationId(int operationId) {
        this.operationId = operationId;
    }

    @Basic
    @Column(name = "Operation_Name", nullable = true, length = 60)
    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    @Basic
    @Column(name = "Operation_Stage_Number", nullable = true)
    public Integer getOperationStageNumber() {
        return operationStageNumber;
    }

    public void setOperationStageNumber(Integer operationStageNumber) {
        this.operationStageNumber = operationStageNumber;
    }

    @Basic
    @Column(name = "Operation_Time_Execution", nullable = true, precision = 0)
    public Double getOperationTimeExecution() {
        return operationTimeExecution;
    }

    public void setOperationTimeExecution(Double operationTimeExecution) {
        this.operationTimeExecution = operationTimeExecution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OperationsEntity that = (OperationsEntity) o;

        if (operationId != that.operationId) return false;
        if (operationName != null ? !operationName.equals(that.operationName) : that.operationName != null)
            return false;
        if (operationStageNumber != null ? !operationStageNumber.equals(that.operationStageNumber) : that.operationStageNumber != null)
            return false;
        if (operationTimeExecution != null ? !operationTimeExecution.equals(that.operationTimeExecution) : that.operationTimeExecution != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = operationId;
        result = 31 * result + (operationName != null ? operationName.hashCode() : 0);
        result = 31 * result + (operationStageNumber != null ? operationStageNumber.hashCode() : 0);
        result = 31 * result + (operationTimeExecution != null ? operationTimeExecution.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "Operation_Profession_ID", referencedColumnName = "Profession_Id")
    public ProfessionsEntity getProfessionsByOperationProfessionId() {
        return professionsByOperationProfessionId;
    }

    public void setProfessionsByOperationProfessionId(ProfessionsEntity professionsByOperationProfessionId) {
        this.professionsByOperationProfessionId = professionsByOperationProfessionId;
    }
}
