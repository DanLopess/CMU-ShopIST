package pt.ulisboa.tecnico.cmov.shopist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.cmov.shopist.MainActivity;
import pt.ulisboa.tecnico.cmov.shopist.R;
import pt.ulisboa.tecnico.cmov.shopist.ScanCodeActivity;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Product;

import static android.app.Activity.RESULT_OK;

public class QRCodeDialog extends DialogFragment {
    private final Context mContext;
    private View mDialogView;

    public QRCodeDialog(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme_FullScreen);
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        alertDialogBuilder.setTitle(R.string.edit_product)
                .setView(mDialogView)
                .setPositiveButton(R.string.update, (dialog, id) -> {
//                    ((MainActivity) mContext).getViewModel().updateProduct(product, image);
                })
                .setNegativeButton(R.string.cancel,  (dialog, id) ->
                        Objects.requireNonNull(QRCodeDialog.this.getDialog()).cancel());
        return alertDialogBuilder.create();
    }
}
