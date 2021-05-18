package pt.ulisboa.tecnico.cmov.shopist.data.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Pantry;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.PantryProduct;

@Data
public class PantryDto {
    private Long pantryId;
    private String uuid;
    private String name;
    private String description;
    private LocationDto location;
    private List<PantryProductDto> products;

    public PantryDto(Pantry pantry, List<PantryProduct> productList) {
        this.pantryId = pantry.getPantryId();
        this.uuid = pantry.getUuid();
        this.name = pantry.getName();
        this.description = pantry.getDescription();
        this.products = new ArrayList<>();
        this.location = new LocationDto(pantry.getLocationWrapper().getLatitude(), pantry.getLocationWrapper().getLongitude());

        productList.forEach(p -> {
            PantryProductDto pDto = new PantryProductDto(p);
            products.add(pDto);
        });
    }
}
