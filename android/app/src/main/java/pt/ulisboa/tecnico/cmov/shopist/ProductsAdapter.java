package pt.ulisboa.tecnico.cmov.shopist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

import pt.ulisboa.tecnico.cmov.shopist.data.Product;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder>{

    private List<Product> mProducts;

    public ProductsAdapter(List<Product> products) {
        mProducts = products;
    }

    @NonNull
    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View ProductView = inflater.inflate(R.layout.product_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(ProductView);

        LinearLayout layout = ProductView.findViewById(R.id.productItemLinearLayout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = v.findViewById(R.id.itemName);
                Log.d("Click Product", textView.getText().toString());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = mProducts.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.name;
        textView.setText(product.getName());
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public ViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.itemName);
        }
    }
}
