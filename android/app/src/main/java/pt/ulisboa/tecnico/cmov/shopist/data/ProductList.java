package pt.ulisboa.tecnico.cmov.shopist.data;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductList implements Serializable {
    private String name;
    private String description;
    private String category;
    private String location;
    private List<Product> products;

    public ProductList(String name, String description, String category, String location) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.location = location;
    }
}
