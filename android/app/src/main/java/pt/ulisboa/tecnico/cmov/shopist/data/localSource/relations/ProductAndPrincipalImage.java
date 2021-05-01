package pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Product;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.ProductImage;

public class ProductAndPrincipalImage {
    @Embedded public Product product;

    @Relation(parentColumn = "productId", entityColumn = "productId")
    public ProductImage productImage;

    public ProductAndPrincipalImage() {
        product = new Product("nomeprod", "descProd");
        productImage = new ProductImage();
    }

}
