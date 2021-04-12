package pt.ulisboa.tecnico.cmov.shopist.pojo;

import android.location.Location;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ProductList implements Serializable {
    public enum Category {PANTRY, SHOPPING}

    protected UUID uuid;
    protected String name;
    protected String description;
    protected LocalDateTime creationDate;
    protected LocalDateTime updateDate;
    protected Category category;
    protected Location location;

    protected ProductList(String name, String description, Category category, Location location) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.location = location;
    }
}
