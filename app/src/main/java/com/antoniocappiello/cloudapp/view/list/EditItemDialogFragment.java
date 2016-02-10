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
import com.google.gson.Gson;

import java.util.Date;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EditItemDialogFragment extends DialogFragment {

    private static final String ARG_ITEM = "ARG_ITEM";
    private static final String ARG_ITEM_ID = "ARG_ITEM_ID";

    @Bind(R.id.edit_text_item_name)
    EditText mEditTextItemName;

    @Bind(R.id.edit_text_item_description)
    EditText mEditTextItemDescription;

    @Inject
    BackendAdapter mBackendAdapter;

    private Item mItem;
    private String mItemId;

    public static EditItemDialogFragment newInstance(String itemId, Item item) {
        EditItemDialogFragment editItemDialogFragment = new EditItemDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_ITEM_ID, itemId);
        bundle.putString(ARG_ITEM, new Gson().toJson(item));
        editItemDialogFragment.setArguments(bundle);
        return editItemDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App)getActivity().getApplication()).appComponent().inject(this);
        Bundle bundle = getArguments();
        mItemId = bundle.getString(ARG_ITEM_ID);
        mItem = new Gson().fromJson(bundle.getString(ARG_ITEM), Item.class);
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

        mEditTextItemName.setText(mItem.getName());
        mEditTextItemDescription.setText(mItem.getDescription());

        mEditTextItemName.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                saveItem();
            }
            return true;
        });

        return new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.edit_item))
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
                saveItem();
            });
        }
    }

    private void saveItem() {
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
            mBackendAdapter.updateItemInUserList(mItemId, item);
            dismiss();
        }
    }

}

