package pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity(tableName="store_product", primaryKeys = {"storeId", "productId"})
public class StoreProductCrossRef {
    @NonNull
    private Long storeId;

    @NonNull
    private Long productId;

    private Double price;
    private Integer qttNeeded;
    private Integer qttCart;
    private Boolean shown;

    @Ignore
    public StoreProductCrossRef(Long storeId, Long productId) {
        this.storeId = storeId;
        this.productId = productId;
        this.price = 0.0;
        this.qttCart = 0;
    }

    public void updateShown() {
        shown = qttNeeded > 0 || qttCart > 0;
    }
}
