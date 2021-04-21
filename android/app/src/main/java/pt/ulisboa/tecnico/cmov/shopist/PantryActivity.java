package pt.ulisboa.tecnico.cmov.shopist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Pantry;
import pt.ulisboa.tecnico.cmov.shopist.viewModel.ViewModel;

public class PantryActivity extends AppCompatActivity {

    public static final int ADD_PRODUCTS_REQ = 1;

    private List<Pantry> pantryList = new ArrayList<>();
    private ViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        initialize();
    }

    public void addProducts(MenuItem item) {
        Intent intent = new Intent(this, AddProductsActivity.class);
        startActivityForResult(intent, ADD_PRODUCTS_REQ);
    }

    public ViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //TODO hanlde activity return
        if (requestCode == ADD_PRODUCTS_REQ && resultCode == RESULT_OK) {
            //mlist.(data.getSerializableExtra());
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initialize() {
        Toolbar toolbar = findViewById(R.id.select_products_toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        viewModel = ViewModelProviders.of(this).get(ViewModel.class);


        TextView title = findViewById(R.id.add_product_title);
        title.setText("PantriesXXX");
    }
}