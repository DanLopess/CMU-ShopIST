package pt.ulisboa.tecnico.cmov.shopist.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductPrice {
    private float average;
    private List<Float> prices;

    public ProductPrice() {
        this.average = 0.0f;
        this.prices = new ArrayList<>();
    }

    public void addPrice(Float price) {
        this.prices.add(price);
        calculateAverage();
    }

    private void calculateAverage() {
        var sum = 0.0f;
        for (Float p : prices) {
            sum += p;
        }
        this.average = prices.isEmpty() ? 0.0f : sum/prices.size();
    }
}
