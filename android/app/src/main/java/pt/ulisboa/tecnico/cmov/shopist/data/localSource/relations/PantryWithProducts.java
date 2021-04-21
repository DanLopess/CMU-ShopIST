package pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations;


import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Pantry;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.PantryProductCrossRef;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Product;

@Data
@NoArgsConstructor
public class PantryWithProducts {

    @Embedded
    public Pantry pantry;

    @Relation(parentColumn = "pantryId", entityColumn = "productId", associateBy = @Junction(PantryProductCrossRef.class))
    private List<Product> products;

    private Integer quantityAvailable;

    private Integer quantityWanted;

    public void increaseQuantityWanted() {
        quantityWanted++;
    }

    public void setQuantityWanted(Integer quantity) {
        quantityWanted = quantity;
        if (quantityWanted < 0)
            quantityWanted = 0;
    }

    public void setQuantityAvailable(Integer quantity) {
        quantityAvailable = quantity;
        if (quantityAvailable < 0)
            quantityAvailable = 0;
    }

}
