package com.example.projekt1.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import org.jetbrains.annotations.NotNull;

public class ConfirmDialog extends AppCompatDialogFragment {

    // to pass data back to activity
    ConfirmDialogListener confirmDialogListener;

    @Override
    public @NotNull Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Confirm Dialog");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                confirmDialogListener.applyData(true);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            Toast.makeText(getActivity().getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            confirmDialogListener.applyData(false);
        });

        return builder.create();
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        try {
            confirmDialogListener = (ConfirmDialog.ConfirmDialogListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface ConfirmDialogListener {
        void applyData(boolean result);
    }
}