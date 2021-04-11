package pt.ulisboa.tecnico.cmov.shopist.pojo;

import android.graphics.Bitmap;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
public class ProductImage {
    enum Source {LOCAL, REMOTE}

    private UUID uuid;
    private Source source;
    private LocalDateTime lastAccess;
    @Getter(AccessLevel.NONE)
    private Bitmap image;

    public Bitmap getImage() {
        lastAccess = LocalDateTime.now();
        return image;
    }
}
