package com.example.audiobookapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.audiobookapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.ArrayList;
import java.util.List;

public class GenreFilterBottomSheet extends BottomSheetDialogFragment {

    private OnFilterAppliedListener listener;

    public interface OnFilterAppliedListener {
        void onFilterApplied(List<String> selectedGenres);
    }

    public void setOnFilterAppliedListener(OnFilterAppliedListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_filter, container, false);

        ChipGroup chipGroup = view.findViewById(R.id.chip_group_genres);
        Button btnApply = view.findViewById(R.id.btn_apply);
        Button btnClear = view.findViewById(R.id.btn_clear);

        btnClear.setOnClickListener(v -> {
            for (int i = 0; i < chipGroup.getChildCount(); i++) {
                Chip chip = (Chip) chipGroup.getChildAt(i);
                chip.setChecked(false);
            }
        });

        btnApply.setOnClickListener(v -> {
            List<String> selected = new ArrayList<>();
            for (int i = 0; i < chipGroup.getChildCount(); i++) {
                Chip chip = (Chip) chipGroup.getChildAt(i);
                if (chip.isChecked()) {
                    selected.add(chip.getText().toString());
                }
            }
            if (listener != null) {
                listener.onFilterApplied(selected);
            }
            dismiss();
        });

        return view;
    }
}