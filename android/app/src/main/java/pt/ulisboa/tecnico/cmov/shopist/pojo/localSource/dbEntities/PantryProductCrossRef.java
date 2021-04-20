package pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.dbEntities;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import lombok.Data;

@Data
@Entity(primaryKeys = {"pantryId", "productId"})
public class PantryProductCrossRef {
    @NonNull
    private String pantryId;

    @NonNull
    private String productId;

    private Integer quantity;

    private Integer needed;

}
