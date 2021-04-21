package pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "products", foreignKeys = {@ForeignKey(entity = ProductImage.class, parentColumns = "imageId", childColumns = "imageId")})
public class Product {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public Long productId;
    public String name;
    public String imageId;
}
