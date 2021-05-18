package pt.ulisboa.tecnico.cmov.shopist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PantryDto {
    private Long pantryId;
    private String uuid;
    private String name;
    private String description;
    private LocationDto location;
    private List<PantryProductDto> products;

    public void update(PantryDto pantryDto) {
        if (this.equals(pantryDto)) {
            this.name = pantryDto.getName();
            this.description = pantryDto.getDescription();
            this.location = pantryDto.getLocation();
            this.products = pantryDto.getProducts();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PantryDto pantryDto = (PantryDto) o;
        return Objects.equals(uuid, pantryDto.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @Override
    public String toString() {
        return "PantryDto{" +
                "pantryId=" + pantryId +
                ", uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", location=" + location +
                ", products=" + products +
                '}';
    }
}
