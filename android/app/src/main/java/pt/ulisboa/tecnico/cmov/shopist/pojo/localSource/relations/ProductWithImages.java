package pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.dbEntities.Product;
import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.dbEntities.ProductImage;

public class ProductWithImages {
    @Embedded public Product product;

    @Relation(
            parentColumn = "productId",
            entityColumn = "productId"
    )
    public List<ProductImage> productImages;
}
