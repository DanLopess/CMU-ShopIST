package pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.relations;


import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

import lombok.NoArgsConstructor;
import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.dbEntities.Pantry;
import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.dbEntities.PantryProductCrossRef;
import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.dbEntities.Product;

@NoArgsConstructor
public class PantryWithProducts {

    @Embedded
    public Pantry pantry;

    @Relation(parentColumn = "productId", entityColumn = "pantryId", associateBy = @Junction(PantryProductCrossRef.class))
    private List<Product> products;

    private Integer quantity;

    private Integer needed;

}
