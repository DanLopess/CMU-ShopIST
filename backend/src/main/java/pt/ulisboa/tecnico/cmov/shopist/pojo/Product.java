package pt.ulisboa.tecnico.cmov.shopist.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Product {
    private int price;
    private UUID uuid;
    private String name;
    private String description;
    private List<Store> stores; // in which stores this product exists
    private String imagesLocation; // TODO add directory for all images of this product

    public Product(int price, String name, String description) {
        this.price = price;
        this.name = name;
        this.description = description;
        this.stores = new ArrayList<>();
        this.imagesLocation = Integer.toString(name.hashCode());
        this.uuid = UUID.randomUUID();
    }
}
