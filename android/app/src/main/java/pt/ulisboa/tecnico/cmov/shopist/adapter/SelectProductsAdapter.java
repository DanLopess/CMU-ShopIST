package pt.ulisboa.tecnico.cmov.shopist.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.shopist.R;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Product;

public class SelectProductsAdapter extends RecyclerView.Adapter<SelectProductsAdapter.ViewHolder>{

    private List<Product> mProducts;
    private List<Product> mSelectedProducts = new ArrayList<>();

    public SelectProductsAdapter(List<Product> products) {
        mProducts = products;
    }

    @NonNull
    @Override
    public SelectProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View ProductView = inflater.inflate(R.layout.select_product_item, parent, false);

        // Return a new holder instance
        return new ViewHolder(ProductView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = mProducts.get(position);

        // Set item views based on your views and data model
        TextView tvItemName = holder.name;
        tvItemName.setText(product.productName);

        TextView tvItemDescription = holder.description;
        tvItemDescription.setText(product.productDescription);

        /*ImageView imageView = holder.image;
        if(product.getImage() != null) {
            imageView.setImageBitmap(product.getImage());
        }*/

        // Listener to select multiple items from the list
        holder.layout.setOnClickListener(v -> {
            if (!v.isSelected()) {
                v.setSelected(true);
                mSelectedProducts.add(product);
            } else {
                v.setSelected(false);
                mSelectedProducts.remove(product);
            }
            v.setBackgroundColor(v.isSelected() ? Color.LTGRAY : Color.TRANSPARENT);
        });
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public List<Product> getSelectedItems() {
        if (!mSelectedProducts.isEmpty())
            return mSelectedProducts;
        return null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layout;
        public TextView name;
        public TextView description;
        // public ImageView image;

        public ViewHolder(View view) {
            super(view);
            // image = view.findViewById(R.id.item_image);
            name = view.findViewById(R.id.product_item_name);
            description = view.findViewById(R.id.product_description);
            layout = view.findViewById(R.id.productItemLinearLayout);
        }
    }
}
