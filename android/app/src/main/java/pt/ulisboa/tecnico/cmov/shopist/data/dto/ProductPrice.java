package pt.ulisboa.tecnico.cmov.shopist.data.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ProductPrice {
    private Double average;
    private Double lastPrice;
    private List<Float> prices;

    public ProductPrice() {
        this.average = (double) 0.0f;
        this.prices = new ArrayList<>();
    }
}
