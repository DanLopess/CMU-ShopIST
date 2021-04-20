package pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.dbEntities.Product;
import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.dbEntities.ProductImage;

public class ProductAndPrincipalImage {
    @Embedded public Product product;

    @Relation(parentColumn = "productId", entityColumn = "productId")
    public ProductImage productImage;

}
