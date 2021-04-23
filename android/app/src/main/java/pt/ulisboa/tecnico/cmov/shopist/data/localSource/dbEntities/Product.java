package pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities;

import androidx.annotation.NonNull;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity(tableName = "products", foreignKeys = {@ForeignKey(entity = ProductImage.class, parentColumns = "imageId", childColumns = "imageId")})
public class Product {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public Long productId;

    public String productName;
    public String productDescription;
    public String imageId;

    public Product(String productName, String productDescription) {
        this.productName = productName;
        this.productDescription = productDescription;
    }
}
