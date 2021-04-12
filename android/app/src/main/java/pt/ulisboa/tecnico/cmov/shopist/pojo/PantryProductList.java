package pt.ulisboa.tecnico.cmov.shopist.pojo;

import android.location.Location;

import java.util.List;

public class PantryProductList extends ProductList {
    private List<PantryProduct> products;

    public PantryProductList(String name, String description, Category category, Location location) {
        super(name, description, category, location);
    }
}
