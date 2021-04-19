package pt.ulisboa.tecnico.cmov.shopist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.shopist.pojo.Product;

public class AddProductsActivity extends AppCompatActivity {

    private List<Product> selectedProducts;
    private DialogFragment mCreateProductDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        selectedProducts = new ArrayList<>();
    }

   public void returnResult(View v) {
        if (!selectedProducts.isEmpty()) {
            Intent data = new Intent();
            data.putExtra("Selected Products", (Parcelable) selectedProducts);
            setResult(RESULT_OK, data);
        }
        finish();
   }

   public void cancel(View v) {
        finish();
   }

   public void createProduct(MenuItem item) {

   }
}