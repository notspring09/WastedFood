package com.example.wastedfoodteam.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;

import java.sql.Time;
import java.util.ArrayList;

public class FilterDialog {
    private static final int END_TIME = 23;
    private final LayoutInflater inflater;
    private final Context context;

    private Spinner spDistance;
    private Spinner spStartTime;
    private Spinner spDiscount;
    private Spinner spEndTime;

    public FilterDialog(LayoutInflater inflater, Context context) {
        this.inflater = inflater;
        this.context = context;
    }

    public interface ModifyFilter {
        void onClear();

        void onChange();
    }


    public void showFilterDialog(final ModifyFilter modifyFilter) {
        View filterLayout = inflater.inflate(R.layout.dialog_buyer_filter, null);

        spDistance = filterLayout.findViewById(R.id.spDistance);
        spStartTime = filterLayout.findViewById(R.id.spStartTime);
        spDiscount = filterLayout.findViewById(R.id.spDiscount);
        spEndTime = filterLayout.findViewById(R.id.spEndTime);
        Button btnClear = filterLayout.findViewById(R.id.btnClear);
        Button btnConfirm = filterLayout.findViewById(R.id.btnConfirm);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyFilter.onClear();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyFilter.onChange();
            }
        });

        setSpinnerDistance();
        setSpinnerStartTime();
        setSpinnerEndTime();
        setSpinnerDiscount();

        AlertDialog.Builder builderDialogRating = new AlertDialog.Builder(context);
        builderDialogRating.setTitle("Tìm kiếm tiếp theo");
        builderDialogRating.setView(filterLayout);
        builderDialogRating.setCancelable(true);

        builderDialogRating.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialogRating = builderDialogRating.create();
        dialogRating.show();
    }

    private void setSpinnerDistance() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.direction_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDistance.setAdapter(adapter);
        spDistance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String distance = parent.getAdapter().getItem(position).toString().replace("km", "").trim();
                if (distance.equals("Tất cả"))
                    Variable.distance = "";
                else
                    Variable.distance = distance;//Get from string array
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Variable.distance = "";
            }
        });
    }

    private void setSpinnerStartTime() {
        String[] times = getTimeArray(0);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, times);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStartTime.setAdapter(adapter);
        spStartTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getAdapter().getItem(position).toString();
                if (!item.trim().isEmpty()) {
                    Variable.startTime = Time.valueOf(item.replace("h", "").trim() + ":00:00").toString();

                    String[] times = getTimeArray(position + 1);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, times);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spEndTime.setAdapter(adapter);
                } else {
                    Variable.startTime = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Variable.startTime = "";
            }
        });
    }

    private void setSpinnerEndTime() {
        String[] times = getTimeArray(0);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, times);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEndTime.setAdapter(adapter);
        spEndTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getAdapter().getItem(position).toString();
                if (!item.trim().isEmpty())
                    Variable.endTime = Time.valueOf(item.replace("h", "").trim() + ":00:00").toString();
                else Variable.endTime = "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Variable.endTime = "";
            }
        });
    }


    private String[] getTimeArray(int start) {
        ArrayList<String> times = new ArrayList<>();
        times.add("");
        for (; start <= END_TIME; start++) {
            times.add(start + "h");
        }
        return times.toArray(new String[0]);
    }

    private void setSpinnerDiscount() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.discount_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDiscount.setAdapter(adapter);
        spDiscount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getAdapter().getItem(position).toString();
                if (item.contains("%"))
                    Variable.discount = item.replace("%", "").trim();
                else
                    Variable.discount = "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Variable.discount = "";
            }
        });
    }

}
