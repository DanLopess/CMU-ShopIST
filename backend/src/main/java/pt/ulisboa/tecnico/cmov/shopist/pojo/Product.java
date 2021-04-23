package pt.ulisboa.tecnico.cmov.shopist.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
public class Product {
    private float price;
    private String id;
    private String name;
    private String description;
    private List<String> storesIds; // in which stores this product exists
    private Set<ProductImage> images;
    private Integer quantity;
    private Integer needed;

    public void addImage(ProductImage image) {
        if (images == null) images = new HashSet<>();
        images.add(image);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) || Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
