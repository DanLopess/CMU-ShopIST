package pt.ulisboa.tecnico.cmov.shopist.pojo;

import lombok.Data;

@Data
public class ProductImage {
    private String productId;
    private String imageName;
    private byte[] imageBytes;
}
