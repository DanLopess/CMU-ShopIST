package pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.dbEntities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity(tableName = "productsImages")
public class ProductImage {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Long imageId;
    private String productId;
    private String image;
}
