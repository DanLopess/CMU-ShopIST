package pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.dbEntities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "productsImages")
public class ProductImage {
    @PrimaryKey
    private String imageId;
    private String productId;
    private String image;
}
