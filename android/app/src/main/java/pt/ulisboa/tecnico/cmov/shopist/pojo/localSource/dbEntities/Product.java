package pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.dbEntities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "products", foreignKeys = {@ForeignKey(entity = ProductImage.class, parentColumns = "imageId", childColumns = "imageId")})
public class Product {
    @PrimaryKey
    public String productId;
    public String name;
    public String imageId;
}
