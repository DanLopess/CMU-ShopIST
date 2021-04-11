package pt.ulisboa.tecnico.cmov.shopist.pojo;

import android.location.Location;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductList implements Serializable {
    public enum Category {PANTRY, SHOPPING}

    private UUID uuid;
    private String name;
    private String description;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private Category category;
    private Location location;
    private List<Product> products;

    public ProductList(String name, String description, Category category, Location location) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.location = location;
    }
}
