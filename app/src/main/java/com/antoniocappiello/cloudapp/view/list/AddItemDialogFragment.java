package com.antoniocappiello.cloudapp.view.list;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.antoniocappiello.cloudapp.App;
import com.antoniocappiello.cloudapp.R;
import com.antoniocappiello.cloudapp.model.Item;
import com.antoniocappiello.cloudapp.presenter.backend.BackendAdapter;

import java.util.Date;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddItemDialogFragment extends DialogFragment {

    @Bind(R.id.edit_text_item_name)
    EditText mEditTextItemName;

    @Bind(R.id.edit_text_item_description)
    EditText mEditTextItemDescription;

    @Inject
    BackendAdapter mBackendAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App)getActivity().getApplication()).appComponent().inject(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        openKeyboard();
    }

    private void openKeyboard() {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.add_item_dialog, null);
        ButterKnife.bind(this, rootView);

        mEditTextItemName.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                addItem();
            }
            return true;
        });

        return new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.add_new_item))
                .setView(rootView)
                .setPositiveButton(getString(R.string.save), null)
                .setNegativeButton(getString(R.string.cancel), null)
                .create();
    }

    @Override
    public void onStart(){
        super.onStart();
        final AlertDialog dialog = (AlertDialog)getDialog();
        if(dialog != null){
            dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                addItem();
            });
        }
    }

    private void addItem() {
        String itemName = mEditTextItemName.getText().toString();
        String itemDescription = mEditTextItemDescription.getText().toString();
        if (itemName.isEmpty()) {
            mEditTextItemName.setError(getString(R.string.error_cannot_be_empty));
        }
        else if (itemDescription.isEmpty()){
            mEditTextItemDescription.setError(getString(R.string.error_cannot_be_empty));
        }
        else {
            Item item = new Item(itemName, itemDescription, new Date(System.currentTimeMillis()).toString());
            mBackendAdapter.addItemToUserList(item);
            dismiss();
        }
    }
}

