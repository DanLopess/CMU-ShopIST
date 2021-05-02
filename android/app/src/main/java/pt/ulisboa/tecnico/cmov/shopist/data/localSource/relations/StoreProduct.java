package pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations;

import androidx.room.Embedded;

import lombok.Data;
import lombok.NoArgsConstructor;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Product;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Store;

@Data
@NoArgsConstructor
public class StoreProduct {

    @Embedded
    public Store store;

    @Embedded
    private Product product;

    private Double price;
    private Integer qttNeeded;
    private Integer qttCart;
}
