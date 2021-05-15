package pt.ulisboa.tecnico.cmov.shopist.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.cmov.shopist.MainActivity;
import pt.ulisboa.tecnico.cmov.shopist.PantryActivity;
import pt.ulisboa.tecnico.cmov.shopist.R;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Pantry;
import pt.ulisboa.tecnico.cmov.shopist.dialog.PantryDetailsDialog;
import pt.ulisboa.tecnico.cmov.shopist.viewModel.ViewModel;

public class ListOfPantriesAdapter extends RecyclerView.Adapter<ListOfPantriesAdapter.ViewHolder>{

    private List<Pantry> mLists;
    private Context mContext;
    private ViewModel viewModel;

    public ListOfPantriesAdapter(Context context, Observable<List<Pantry>> lists) {
        lists.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(items -> {
           mLists = items;
           this.notifyDataSetChanged();
           viewModel = ((MainActivity) mContext).getViewModel();
        });
        mContext = context;
    }

    @NonNull
    @Override
    public ListOfPantriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the custom layout
        View listView = inflater.inflate(R.layout.list_item_list_of_pantries, parent, false);

        return new ViewHolder(listView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pantry list = mLists.get(position);
        Context context = holder.itemView.getContext();

        setSyncIconVisibility(list, holder);

        // Set up listeners
        View.OnClickListener itemViewGroupListener = v -> {
            Intent intent = new Intent(context, PantryActivity.class);
            intent.putExtra("PantryId", list.getPantryId());
            context.startActivity(intent);
        };

        PopupMenu.OnMenuItemClickListener menuItemClickListener = getOnMenuItemClickListener(holder, position, list);

        View.OnClickListener itemOptionsListener = v -> {
            PopupMenu listOptionsMenu = new PopupMenu(v.getContext(), v);
            MenuInflater inflater1 = listOptionsMenu.getMenuInflater();
            inflater1.inflate(R.menu.options_list_menu, listOptionsMenu.getMenu());
            setQRCodeTextVisibility(list, listOptionsMenu, holder);
            listOptionsMenu.setOnMenuItemClickListener(menuItemClickListener);
            listOptionsMenu.show();
        };

        // Set item views based on your views and data model
        TextView textView = holder.name;
        textView.setText(list.getName());

        TextView distTextView = holder.distTime;
        if (list.getLocationWrapper().toLocation() != null) {
            // TODO get location and distance in minutes
            //distTextView.setText(distText);
        }

        TextView itemNrTextView = holder.itemNr;
        viewModel.getPantrySize(list.pantryId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(size -> {
            String itemNrText = size + " " + mContext.getString(R.string.items);
            itemNrTextView.setText(itemNrText);
        });

        // Set listener to start new activity
        holder.itemView.setOnClickListener(itemViewGroupListener);

        // Set popup menu for each list item
        holder.options.setOnClickListener(itemOptionsListener);
    }

    @NonNull
    private PopupMenu.OnMenuItemClickListener getOnMenuItemClickListener(@NonNull ViewHolder holder, int position, Pantry list) {
        @SuppressLint("NonConstantResourceId") PopupMenu.OnMenuItemClickListener menuItemClickListener = item -> {
            if (item.getItemId() == R.id.list_options_delete) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle(R.string.delete_list)
                        .setMessage(R.string.delete_list_confirmation)
                        .setPositiveButton(R.string.delete, (dialog, which) -> {
                            ((MainActivity) mContext).getViewModel().deletePantry(mLists.get(position));
                            notifyDataSetChanged();
                        })
                        .setNegativeButton(R.string.cancel, (dialog, which) -> {
                            dialog.dismiss();
                        });
                builder.create().show();
                return true;
            } else if (item.getItemId() == R.id.list_options_sync) {
                viewModel.savePantryToBackend(list);
                Toast.makeText(mContext, "Sharing...", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.list_get_qr_code) {
                Toast.makeText(mContext, "Clicked to get qr code", Toast.LENGTH_SHORT).show();
                // todo open dialog with show QR code
                return true;
            } else
                return false;
        };
        return menuItemClickListener;
    }

    private void setQRCodeTextVisibility(Pantry list, PopupMenu listOptionsMenu, ViewHolder holder) {
        MenuItem syncItem = listOptionsMenu.getMenu().findItem(R.id.list_options_sync);
        MenuItem showQRItem = listOptionsMenu.getMenu().findItem(R.id.list_get_qr_code);

        if (list.isShared()) {
            syncItem.setVisible(false);
            showQRItem.setVisible(true);
        } else {
            syncItem.setVisible(true);
            showQRItem.setVisible(false);
        }
    }

    private void setSyncIconVisibility(Pantry list, ViewHolder holder) {
        ImageView imageView = holder.syncImage;

        if (list.isShared()) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView distTime;
        public TextView itemNr;
        public ImageButton options;
        public ImageView syncImage;

        public ViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.listName_textView);
            distTime = view.findViewById(R.id.listDistTime_tv);
            itemNr = view.findViewById(R.id.listItemNr_tv);
            options = view.findViewById(R.id.list_options_bt);
            syncImage = view.findViewById(R.id.sync_image_view);
        }
    }
}