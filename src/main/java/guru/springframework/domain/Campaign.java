package guru.springframework.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;

@Entity
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Version
    private Integer version;

    private String name;
    private String description;
    private ArrayList<Integer> promoIDs;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Integer> getPromoIDs() {
        return promoIDs;
    }

    public void setPromoIDs(ArrayList<Integer> promoIDs) {
        this.promoIDs = promoIDs;
    }
}
