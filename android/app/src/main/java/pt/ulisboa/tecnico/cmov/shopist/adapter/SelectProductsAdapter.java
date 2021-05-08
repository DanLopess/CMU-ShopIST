package pt.ulisboa.tecnico.cmov.shopist.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.cmov.shopist.AddPantryProductsActivity;
import pt.ulisboa.tecnico.cmov.shopist.AddStoreProductsActivity;
import pt.ulisboa.tecnico.cmov.shopist.MainActivity;
import pt.ulisboa.tecnico.cmov.shopist.PantryActivity;
import pt.ulisboa.tecnico.cmov.shopist.R;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Product;
import pt.ulisboa.tecnico.cmov.shopist.viewModel.ViewModel;

public class SelectProductsAdapter extends RecyclerView.Adapter<SelectProductsAdapter.ViewHolder>{

    private List<Product> mProducts;
    private List<Product> mSelectedProducts = new ArrayList<>();
    private final Context mContext;

    public SelectProductsAdapter(Context context, List<Product> products) {
        mProducts = products;
        mContext = context;
    }

    @NonNull
    @Override
    public SelectProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View ProductView = inflater.inflate(R.layout.list_item_select_product, parent, false);

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

        ImageView imageView = holder.image;
        if(product.getThumbnailPath() != null) {
            ViewModel viewModel = null;
            try {
                viewModel = ((AddPantryProductsActivity) mContext).getViewModel();
            } catch (Exception e) {
                viewModel = ((AddStoreProductsActivity) mContext).getViewModel();
            }
            if(viewModel != null) {
                viewModel.getProductImage(product.getThumbnailPath()).
                        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(image -> {
                    imageView.setImageBitmap(Bitmap.createScaledBitmap(image, imageView.getWidth(), imageView.getHeight(), false));
                });
            }
        }

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
        return mSelectedProducts;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layout;
        public TextView name;
        public TextView description;
         public ImageView image;

        public ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.item_image);
            name = view.findViewById(R.id.storeProdName_tv);
            description = view.findViewById(R.id.storeProdInfoText_tv);
            layout = view.findViewById(R.id.productItemLinearLayout);
        }
    }
}
