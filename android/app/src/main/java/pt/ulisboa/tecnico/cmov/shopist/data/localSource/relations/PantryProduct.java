package pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import lombok.Data;
import lombok.NoArgsConstructor;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Pantry;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.PantryProductCrossRef;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Product;

@Data
@NoArgsConstructor
public class PantryProduct {

    @Embedded
    public Pantry pantry;

    @Embedded
    private Product product;

    private Integer qttAvailable;
    private Integer qttNeeded;
    private Integer qttCart;
}
