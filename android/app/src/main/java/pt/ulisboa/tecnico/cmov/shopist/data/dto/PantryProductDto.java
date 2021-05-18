package pt.ulisboa.tecnico.cmov.shopist.data.dto;

import lombok.Data;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Product;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.PantryProduct;

@Data
public class PantryProductDto {
    private Long productId;
    private String uuid;
    private String productName;
    private String productDescription;
    private String barcode;
    private Integer qttAvailable;
    private Integer qttNeeded;

    public PantryProductDto(PantryProduct p) {
        Product prod = p.getProduct();
        this.productId = prod.getProductId();
        this.uuid = prod.getProductUuid();
        this.productName = prod.getProductName();
        this.productDescription = prod.getProductDescription();
        this.barcode = prod.getCode();
        this.qttAvailable = p.getQttAvailable();
        this.qttNeeded = p.getQttNeeded();
    }
}
