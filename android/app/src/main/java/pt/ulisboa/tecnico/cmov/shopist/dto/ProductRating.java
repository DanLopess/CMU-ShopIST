package pt.ulisboa.tecnico.cmov.shopist.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class ProductRating {
    private Float average;
    private final Map<Integer, Integer> ratings; //<rating(1-5), numberOfRatings>

    public ProductRating(Integer rating) {
        ratings = new HashMap<>();
        ratings.put(1, 0);
        ratings.put(2, 0);
        ratings.put(3, 0);
        ratings.put(4, 0);
        ratings.put(5, 0);

        average = 0f;
        addNewRating(rating);
    }

    public void computeAverage() {
        int ratingsSum = 0;
        int totalRatings = 0;
        for (Integer rating : ratings.keySet()) {
            ratingsSum += rating * ratings.get(rating);
            totalRatings += ratings.get(rating);
        }
        average = (float) ratingsSum/totalRatings;
    }

    public void addNewRating(Integer rating) {
        if (rating != null) {
            Integer n = ratings.get(rating);
            if (n != null)
                ratings.put(rating, ++n);
            computeAverage();
        }
    }

    public void addRating(Integer prevR, Integer newR) {
        if (prevR != null) {
            Integer n = ratings.get(prevR);
            if (n != null)
                ratings.put(prevR, --n);
        }
        addNewRating(newR);
    }
}
