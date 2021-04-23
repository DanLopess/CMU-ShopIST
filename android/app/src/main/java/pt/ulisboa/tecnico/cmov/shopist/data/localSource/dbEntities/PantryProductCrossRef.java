package pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import lombok.Data;

@Data
@Entity(tableName="pantry_product", primaryKeys = {"pantryId", "productId"})
public class PantryProductCrossRef {
    @NonNull
    private Long pantryId;

    @NonNull
    private Long productId;

    private Integer qttAvailable;
    private Integer qttNeeded;
}
