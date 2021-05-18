package pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity(tableName="pantry_product", primaryKeys = {"pantryId", "productId"})
public class PantryProductCrossRef {
    @NonNull
    private Long pantryId;

    @NonNull
    private Long productId;

    private Integer qttAvailable;
    private Integer qttNeeded;

    @Ignore
    public PantryProductCrossRef(@NonNull Long pantryId, @NonNull Long productId) {
        this.pantryId = pantryId;
        this.productId = productId;
        this.qttAvailable = 0;
        this.qttNeeded = 1;
    }
}
