package pt.ulisboa.tecnico.cmov.shopist.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.cmov.shopist.R;
import pt.ulisboa.tecnico.cmov.shopist.StoreActivity;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.StoreProduct;

public class CartProductsAdapter extends RecyclerView.Adapter<CartProductsAdapter.ViewHolder>{

    private final List<StoreProduct> mProducts;
    private Context mContext;

    public CartProductsAdapter(List<StoreProduct> products) {
        mProducts = products;
    }

    @NonNull
    @Override
    public CartProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View ProductView = inflater.inflate(R.layout.list_item_cart_product, parent, false);
        return new ViewHolder(ProductView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StoreProduct product = mProducts.get(position);

        TextView nameTextView = holder.name;
        nameTextView.setText(product.getProduct().getProductName());

        TextView itemPrice = holder.itemPrice;
        TextView totalCost = holder.totalCost;
        Double price = product.getPrice();
        if (price != 0.0) {
            itemPrice.setVisibility(View.VISIBLE);
            totalCost.setVisibility(View.VISIBLE);
            String priceText = mContext.getString(R.string.price_unit) + ": " + price + " €";
            String totalCostText = mContext.getString(R.string.total) + ": " + (price * product.getQttCart()) + " €";
            itemPrice.setText(priceText);
            totalCost.setText(totalCostText);
        } else {
            itemPrice.setVisibility(View.GONE);
            totalCost.setVisibility(View.GONE);
        }

        TextView qttTextView = holder.qtt;
        String qttText = "x " + product.getQttCart();
        qttTextView.setText(qttText);

        ImageView imageView = holder.image;
        if(product.getProduct().getThumbnailPath() != null) {
            ((StoreActivity) mContext).getViewModel().getProductImage(product.getProduct().getThumbnailPath()).
                    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(image ->
                    imageView.setImageBitmap(Bitmap.createScaledBitmap(image, imageView.getWidth(), imageView.getHeight(), false)));
        }
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView itemPrice;
        public TextView totalCost;
        public TextView qtt;
        public ImageView image;

        public ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.item_image);
            name = view.findViewById(R.id.cartProdName_tv);
            itemPrice = view.findViewById(R.id.cartProdItemPrice_tv);
            totalCost = view.findViewById(R.id.cartProdTotalPrice_tv);
            qtt = view.findViewById(R.id.cartProdQtt_tv);
        }
    }
}
