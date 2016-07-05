package guru.springframework.domain;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Version
    private Integer version;

    private String name;
    private String description;
    private String imageUrl;
    private String start;
    private String end;
    private ArrayList<Integer> storeIDs;
    private ArrayList<Integer> productIDs;
    private String fileLoc;
    private String fileName;

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() { return end; }

    public void setEnd(String end) {
        this.end = end;
    }

    public ArrayList<Integer> getProductIDs() {
        return productIDs;
    }
    public void setProductIDs(ArrayList<Integer> productIDs) {
        this.productIDs = productIDs;
    }

    public ArrayList<Integer> getStoreIDs() {
        return storeIDs;
    }
    public void setStoreIDs(ArrayList<Integer> storeIDs) {
        this.storeIDs = storeIDs;
    }

    public String getFileLoc() { return fileLoc; }
    public void setFileLoc(String fileLoc) { this.fileLoc = fileLoc; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }


}
