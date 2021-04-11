package pt.ulisboa.tecnico.cmov.shopist.data;


import android.graphics.Bitmap;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Product {
    private String name;
    private Bitmap image;
}
