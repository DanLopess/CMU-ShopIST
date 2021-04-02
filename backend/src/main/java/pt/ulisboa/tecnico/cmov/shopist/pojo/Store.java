package pt.ulisboa.tecnico.cmov.shopist.pojo;

import lombok.Data;

import java.util.Objects;
import java.util.UUID;

@Data
public class Store {
    private String name;
    private String coordinates;
    private UUID uuid;

    public Store(String name, String coordinates) {
        this.name = name;
        this.coordinates = coordinates;
        this.uuid = UUID.randomUUID();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return Objects.equals(coordinates, store.coordinates) || Objects.equals(uuid, store.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinates, uuid);
    }
}
