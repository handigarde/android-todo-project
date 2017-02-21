package com.handigarde.todoapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by ryanhandy on 2/20/17.
 */

public class EditItemDialogFragment extends DialogFragment implements Button.OnClickListener {

    private EditText etEditTextFrag;
    private Spinner spEditPriorityFrag;
    private Button btnSaveFrag;
    private String itemPriority;
    private boolean isPending;
    private int position;

    public interface EditItemDialogListener {
        void onFinishEditDialog(String taskname, String taskPriority, boolean isPending,
                                int position);
    }


    public EditItemDialogFragment() {
        // Empty constructor required
        // Use `newInstance` as defined below
    }

    public static EditItemDialogFragment newInstance(String taskName, String taskPriority,
                                                     boolean isPending, int position) {
        EditItemDialogFragment frag = new EditItemDialogFragment();
        Bundle args = new Bundle();
        args.putString("taskName", taskName);
        args.putString("priority", taskPriority);
        args.putBoolean("isPending", isPending);
        args.putInt("position", position);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_item, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get fields from view
        etEditTextFrag = (EditText) view.findViewById(R.id.etEditTextFrag);
        spEditPriorityFrag = (Spinner) view.findViewById(R.id.spEditPriorityFrag);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.priorities_array, android.R.layout.simple_spinner_item);
        spEditPriorityFrag.setAdapter(spinnerAdapter);
        btnSaveFrag = (Button) view.findViewById(R.id.btnSaveFrag);

        // Fetch arguments from bundle and set title
        String task = getArguments().getString("taskName", "");
        isPending = getArguments().getBoolean("isPending");
        position = getArguments().getInt("position");
        etEditTextFrag.setText(task);
        getDialog().setTitle("Edit item");

        // Set button handler to save when clicked
        btnSaveFrag.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        EditItemDialogListener listener = (EditItemDialogListener) getActivity();
        listener.onFinishEditDialog(etEditTextFrag.getText().toString(),
                spEditPriorityFrag.getSelectedItem().toString(), isPending, position);
        dismiss();
    }
}
