package pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.List;

import lombok.Data;
import pt.ulisboa.tecnico.cmov.shopist.dto.PantryProductDto;

@Data
@Entity(tableName = "products",
        indices = {@Index(value = {"code"}, unique = true)})
public class Product {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public Long productId;
    public String productName;
    public String productDescription;
    public String imagePath;
    public String thumbnailPath;
//    public List<String> crowdSourceImage;

    public String code;

    public Product(String productName, String productDescription, String code, String imagePath, String thumbnailPath) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.code = code;
        this.imagePath = imagePath;
        this.thumbnailPath = thumbnailPath;
    }

    public Product(PantryProductDto productDto) {
        this.productName = productDto.getProductName();
        this.productDescription = productDto.getProductDescription();
        this.code = productDto.getBarcode();
    }
}
