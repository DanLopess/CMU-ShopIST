package pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(tableName = "productsImages")
@NoArgsConstructor
public class ProductImage {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Long imageId;
    private Long productId;
    private byte[] image;
    private byte[] thumbnail;
}
