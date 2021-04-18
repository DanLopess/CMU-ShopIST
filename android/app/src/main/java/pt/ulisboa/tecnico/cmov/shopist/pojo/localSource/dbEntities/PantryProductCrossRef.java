package pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.dbEntities;

import androidx.room.Entity;

@Entity(primaryKeys = {"pantryId", "productId"})
public class PantryProductCrossRef {
    private String pantryId;
    private String productId;
    private Integer quantity;
    private Integer needed;

}
