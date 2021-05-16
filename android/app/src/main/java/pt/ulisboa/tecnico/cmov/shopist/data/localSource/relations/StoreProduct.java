package pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations;

import androidx.room.Embedded;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Product;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Store;

@Data
@NoArgsConstructor
public class StoreProduct {

    @Embedded
    public Store store;

    @Embedded
    public Product product;

    private Double price;

    @Setter(AccessLevel.NONE) //Custom setter
    private Integer qttNeeded;

    private Integer qttCart;
    private Boolean shown;

    public void setQttNeeded(Integer qttNeeded) {
        if (qttNeeded > 99)
            this.qttNeeded = 99;
        else
            this.qttNeeded = qttNeeded;
    }

    public void increaseQttCart() {
        if (qttCart < 99)
            qttCart++;
    }

    public void updateShown() {
        shown = qttNeeded > 0 || qttCart > 0;
    }
}
