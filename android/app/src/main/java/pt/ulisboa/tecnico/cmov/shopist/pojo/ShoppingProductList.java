package pt.ulisboa.tecnico.cmov.shopist.pojo;

import android.location.Location;

import java.util.List;

public class ShoppingProductList extends ProductList {
    private List<StoreProduct> products;

    public ShoppingProductList(String name, String description, Category category, Location location) {
        super(name, description, category, location);
    }
}
