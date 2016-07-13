package guru.springframework.domain;

import javax.persistence.*;

/**
 * Created by Jennifer on 6/21/16.
 */
@Entity
public class PromotionStore {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Version
    private Integer version;

    private Integer storeID;
    private Integer promoID;
    private String status;

    private String fieldLoc;
    private String fieldStatus;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStoreID() {
        return storeID;
    }

    public void setStoreID(Integer storeID) {
        this.storeID = storeID;
    }

    public Integer getPromoID() {
        return promoID;
    }

    public void setPromoID(Integer promoID) {
        this.promoID = promoID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFieldLoc() {
        return fieldLoc;
    }

    public void setFieldLoc(String fieldLoc) {
        this.fieldLoc = fieldLoc;
    }

    public String getFieldStatus() {
        return fieldStatus;
    }

    public void setFieldStatus(String fieldStatus) {
        this.fieldStatus = fieldStatus;
    }





}
