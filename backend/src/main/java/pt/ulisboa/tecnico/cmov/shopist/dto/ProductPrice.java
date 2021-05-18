package pt.ulisboa.tecnico.cmov.shopist.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductPrice {
    private Double average;
    private Double lastPrice;
    private List<Double> prices;

    public ProductPrice() {
        this.lastPrice =  (double) 0.0f;
        this.prices = new ArrayList<>();
        calculateAverage();
    }

    public ProductPrice(Double price) {
        this.average = price;
        this.prices = new ArrayList<>();
        calculateAverage();
    }

    public void addPrice(Double price) {
        this.prices.add(price);
        this.lastPrice = price;
        calculateAverage();
    }

    private void calculateAverage() {
        var sum = 0.0f;
        for (Double p : prices) {
            sum += p;
        }
        this.average = prices.isEmpty() ? (double) 0.0f : sum/prices.size();
    }
}
