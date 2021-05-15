package pt.ulisboa.tecnico.cmov.shopist.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Product {
    private int price;
    private UUID uuid;
    private String name;
    private String description;
    private String imagesLocation; // TODO add directory for all images of this product

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(uuid, product.uuid) || Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name);
    }
}
