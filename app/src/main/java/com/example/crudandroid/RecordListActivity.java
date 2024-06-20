package com.example.crudandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RecordListActivity extends AppCompatActivity {
    Database db;
    ListView listViewRecords;
    ArrayList<String> recordList;
    ArrayList<Integer> recordIds;
    boolean isForDeletion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);

        db = new Database(this);
        listViewRecords = findViewById(R.id.listViewRecords);
        recordList = new ArrayList<>();
        recordIds = new ArrayList<>();

        loadRecords();

        listViewRecords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                int recordId = recordIds.get(position);
                if (isForDeletion) {
                    confirmDeletion(recordId);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("RECORD_ID", recordId);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        isForDeletion = getIntent().getBooleanExtra("FOR_DELETION", false);
    }

    private void loadRecords() {
        Cursor res = db.getAllData();
        if (res.getCount() == 0) {
            return;
        }

        while (res.moveToNext()) {
            recordIds.add(res.getInt(0));
            recordList.add("Nome: " + res.getString(1) + "\nCPF: " + res.getString(2));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recordList);
        listViewRecords.setAdapter(adapter);
    }

    private void confirmDeletion(final int recordId) {
        new AlertDialog.Builder(this)
                .setTitle("Excluir Registro")
                .setMessage("Deseja realmente excluir o registro?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteData(recordId);
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }
}