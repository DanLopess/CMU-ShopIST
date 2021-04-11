package pt.ulisboa.tecnico.cmov.shopist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import pt.ulisboa.tecnico.cmov.shopist.pojo.ProductList;

public class PantryActivity extends AppCompatActivity {
    private ProductList mlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        mlist = (ProductList) getIntent().getSerializableExtra("List");

        TextView title = findViewById(R.id.pantry_list_title);
        title.setText(mlist.getName());
    }
}