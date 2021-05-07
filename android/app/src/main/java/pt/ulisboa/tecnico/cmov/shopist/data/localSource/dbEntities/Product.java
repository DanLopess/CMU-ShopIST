package pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity(tableName = "products",
        foreignKeys = {@ForeignKey(entity = ProductImage.class, parentColumns = "imageId", childColumns = "imageId")},
        indices = {@Index(value = {"code"}, unique = true)})
public class Product {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public Long productId;
    public String productName;
    public String productDescription;
    public String imageId;
    public String code;

    public Product(String productName, String productDescription, String code) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.code = code;
    }
}
