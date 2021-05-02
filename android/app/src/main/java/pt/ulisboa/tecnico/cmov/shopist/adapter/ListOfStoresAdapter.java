package pt.ulisboa.tecnico.cmov.shopist.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.cmov.shopist.MainActivity;
import pt.ulisboa.tecnico.cmov.shopist.R;
import pt.ulisboa.tecnico.cmov.shopist.StoreActivity;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Store;
import pt.ulisboa.tecnico.cmov.shopist.viewModel.ViewModel;

public class ListOfStoresAdapter extends RecyclerView.Adapter<ListOfStoresAdapter.ViewHolder>{

    private List<Store> mLists;
    private Context mContext;
    private ViewModel viewModel;

    public ListOfStoresAdapter(Context context, Observable<List<Store>> lists) {
        lists.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(items -> {
           mLists = items;
           this.notifyDataSetChanged();
           viewModel = ((MainActivity) mContext).getViewModel();
        });
        mContext = context;
    }

    @NonNull
    @Override
    public ListOfStoresAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the custom layout
        //TODO maybe change??
        View listView = inflater.inflate(R.layout.list_of_lists_item, parent, false);

        // Return a new holder instance
        return new ViewHolder(listView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Store list = mLists.get(position);
        Context context = holder.itemView.getContext();

        // Set up listeners
        View.OnClickListener itemViewGroupListener = v -> {
            //TODO Activity
            Intent intent = new Intent(context, StoreActivity.class);
            intent.putExtra("StoreId", list.getStoreId());
            context.startActivity(intent);
        };

        PopupMenu.OnMenuItemClickListener menuItemClickListener = item -> {switch (item.getItemId()) {
            case R.id.list_options_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle(R.string.delete_list)
                        .setMessage(R.string.delete_list_confirmation)
                        .setPositiveButton(R.string.delete, (dialog, which) -> {
                            ((MainActivity) mContext).getViewModel().deleteStore(list);
                            notifyDataSetChanged();
                        })
                        .setNegativeButton(R.string.cancel, (dialog, which) -> {dialog.dismiss();});
                builder.create().show();
                return true;
            case R.id.list_options_sync:
                // TODO sync with server
                return true;
            default:
                return false;
        }};

        View.OnClickListener itemOptionsListener = v -> {
            PopupMenu listOptionsMenu = new PopupMenu(v.getContext(), v);
            MenuInflater inflater1 = listOptionsMenu.getMenuInflater();
            inflater1.inflate(R.menu.list_options_menu, listOptionsMenu.getMenu());
            listOptionsMenu.setOnMenuItemClickListener(menuItemClickListener);
            listOptionsMenu.show();
        };

        // Set item views based on your views and data model
        TextView textView = holder.name;
        textView.setText(list.getName());

        TextView distTextView = holder.distTime;
        // TODO String distText = list.getDriveTime() + " " + mContext.getString(R.string.drive_time);
        //distTextView.setText(distText);

        TextView itemNrTextView = holder.itemNr;
        viewModel.getPantrySize(list.storeId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(size -> {
            String itemNrText = size + " " + mContext.getString(R.string.items);
            itemNrTextView.setText(itemNrText);
        });

        // Set listener to start new activity
        holder.itemView.setOnClickListener(itemViewGroupListener);

        // Set popup menu for each list item
        holder.options.setOnClickListener(itemOptionsListener);
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

        public ViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.listName_textView);
            distTime = view.findViewById(R.id.listDistTime_tv);
            itemNr = view.findViewById(R.id.listItemNr_tv);
            options = view.findViewById(R.id.list_options_bt);
        }
    }
}