package pt.ulisboa.tecnico.cmov.shopist.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ListOfProducts {
    private String id;
    private String name;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private List<Product> products;
    private String category;

    public ListOfProducts(String name, String category) {
        this.products = new ArrayList<>();
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.creationDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListOfProducts that = (ListOfProducts) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}