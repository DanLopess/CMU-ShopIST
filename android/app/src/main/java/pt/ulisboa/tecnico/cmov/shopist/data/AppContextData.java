package pt.ulisboa.tecnico.cmov.shopist.data;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class AppContextData extends Application {

    List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        products.add(product);
    }
}
