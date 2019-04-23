package Diplom.hibernate.dao;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "Orders", schema = "dbo", catalog = "ACS_WORK_COPY")
public class OrdersEntity {
    private int orderId;
    private Integer orderDetailQuantity;
    private Timestamp orderReleaseDate;

    @Id
    @Column(name = "Order_Id", nullable = false)
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Basic
    @Column(name = "Order_Detail_Quantity", nullable = true)
    public Integer getOrderDetailQuantity() {
        return orderDetailQuantity;
    }

    public void setOrderDetailQuantity(Integer orderDetailQuantity) {
        this.orderDetailQuantity = orderDetailQuantity;
    }

    @Basic
    @Column(name = "Order_Release_Date", nullable = true)
    public Timestamp getOrderReleaseDate() {
        return orderReleaseDate;
    }

    public void setOrderReleaseDate(Timestamp orderReleaseDate) {
        this.orderReleaseDate = orderReleaseDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrdersEntity that = (OrdersEntity) o;

        if (orderId != that.orderId) return false;
        if (orderDetailQuantity != null ? !orderDetailQuantity.equals(that.orderDetailQuantity) : that.orderDetailQuantity != null)
            return false;
        if (orderReleaseDate != null ? !orderReleaseDate.equals(that.orderReleaseDate) : that.orderReleaseDate != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = orderId;
        result = 31 * result + (orderDetailQuantity != null ? orderDetailQuantity.hashCode() : 0);
        result = 31 * result + (orderReleaseDate != null ? orderReleaseDate.hashCode() : 0);
        return result;
    }
}
