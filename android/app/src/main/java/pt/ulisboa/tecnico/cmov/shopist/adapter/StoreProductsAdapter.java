package pt.ulisboa.tecnico.cmov.shopist.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.cmov.shopist.PantryActivity;
import pt.ulisboa.tecnico.cmov.shopist.R;
import pt.ulisboa.tecnico.cmov.shopist.StoreActivity;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.StoreProduct;
import pt.ulisboa.tecnico.cmov.shopist.dialog.PantryProductDetailsDialog;
import pt.ulisboa.tecnico.cmov.shopist.dialog.ProductDetailsDialog;
import pt.ulisboa.tecnico.cmov.shopist.dialog.StoreProductDetailsDialog;

public class StoreProductsAdapter extends RecyclerView.Adapter<StoreProductsAdapter.ViewHolder>{

    private List<StoreProduct> mProducts;
    private Context mContext;

    public StoreProductsAdapter(List<StoreProduct> products) {
        mProducts = products;
    }

    @NonNull
    @Override
    public StoreProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View ProductView = inflater.inflate(R.layout.list_item_store_product, parent, false);
        return new ViewHolder(ProductView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StoreProduct product = mProducts.get(position);

        if (!product.getShown()) {
            mProducts.remove(product);
            notifyItemRemoved(position);
            return;
        }

        PopupMenu.OnMenuItemClickListener menuItemClickListener = item -> {
            if (item.getItemId() == R.id.pantry_store_product_options_delete) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle(R.string.delete_product)
                        .setMessage(R.string.delete_product_confirmation)
                        .setPositiveButton(R.string.delete, (dialog, which) -> {
                            ((StoreActivity) mContext).getViewModel()
                                    .deleteStoreProduct(product);
                            notifyDataSetChanged();
                        })
                        .setNegativeButton(R.string.cancel, (dialog, which) -> {
                            dialog.dismiss();
                        });
                builder.create().show();
                return true;
            } else
                return false;
        };

        View.OnClickListener itemOptionsListener = v -> {
            PopupMenu listOptionsMenu = new PopupMenu(v.getContext(), v);
            MenuInflater inflater1 = listOptionsMenu.getMenuInflater();
            inflater1.inflate(R.menu.options_pantry_store_product_menu, listOptionsMenu.getMenu());
            listOptionsMenu.setOnMenuItemClickListener(menuItemClickListener);
            listOptionsMenu.show();
        };

        View.OnClickListener addToCartListener = v -> {
            if (product.getQttNeeded() > 0) {
                product.increaseQttCart();
                product.updateShown();
                ((StoreActivity) mContext).getViewModel().updateStoreProduct(product);
                notifyDataSetChanged();
            }
        };

        TextView nameTextView = holder.name;
        nameTextView.setText(product.getProduct().getProductName());

        TextView infoTextView = holder.infoText;
        ((StoreActivity) mContext).getViewModel().getQttNeeded(product.getProduct()).subscribeOn(
                Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(qtt -> {
                    product.setQttNeeded(qtt);
                    product.updateShown();
            String infoText = mContext.getString(R.string.needed) + ": " + product.getQttNeeded() +
                    " / " + mContext.getString(R.string.in_cart) + ": " + product.getQttCart();
            infoTextView.setText(infoText);
        });

        holder.addToCart.setOnClickListener(addToCartListener);

        holder.options.setOnClickListener(itemOptionsListener);

        ImageView imageView = holder.image;
        if(product.getProduct().getThumbnailPath() != null) {
            ((StoreActivity) mContext).getViewModel().getProductImage(product.getProduct().getThumbnailPath()).
                    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(image -> {
                imageView.setImageBitmap(Bitmap.createScaledBitmap(image, imageView.getWidth(), imageView.getHeight(), false));
            });
        }

        holder.productClickableArea.setOnClickListener(v -> {
            StoreProductDetailsDialog storeProductDetailsDialog = new StoreProductDetailsDialog(mContext, product);
            storeProductDetailsDialog.show(((StoreActivity) mContext).getSupportFragmentManager(), "product_details");
        });
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView infoText;
        public Button addToCart;
        public ImageButton options;
        public ImageView image;
        public LinearLayout productClickableArea;

        public ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.item_image);
            name = view.findViewById(R.id.storeProdName_tv);
            addToCart = view.findViewById(R.id.addToCart_bt);
            options = view.findViewById(R.id.store_product_options_bt);
            infoText = view.findViewById(R.id.storeProdInfoText_tv);
            productClickableArea = view.findViewById(R.id.product_item_clickable_area);
        }
    }
}
