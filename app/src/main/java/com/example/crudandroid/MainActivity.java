package com.example.crudandroid;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Database db;
    EditText editNome, editCpf, editTelefone, editSintomas;
    Button btnSalvar, btnVisualizar, btnEditar, btnExcluir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new Database(this);

        editNome = findViewById(R.id.nomeMoradorInput);
        editCpf = findViewById(R.id.cpfInput);
        editTelefone = findViewById(R.id.telefoneInput);
        editSintomas = findViewById(R.id.sintomasDengueInput);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnVisualizar = findViewById(R.id.btnVisualizar);
        btnEditar = findViewById(R.id.btnEditar);
        btnExcluir = findViewById(R.id.btnExcluir);

        AddData();
        viewAll();

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecordListActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecordListActivity.class);
                intent.putExtra("FOR_DELETION", true);
                startActivityForResult(intent, 2);
            }
        });
    }

    public void AddData() {
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer recordId = (Integer) btnSalvar.getTag();
                boolean isInserted;

                if (recordId == null) {
                    isInserted = db.insertData(editNome.getText().toString(),
                            editCpf.getText().toString(),
                            editTelefone.getText().toString(),
                            editSintomas.getText().toString());
                } else {
                    isInserted = db.updateData(recordId,
                            editNome.getText().toString(),
                            editCpf.getText().toString(),
                            editTelefone.getText().toString(),
                            editSintomas.getText().toString());
                }

                if (isInserted) {
                    Toast.makeText(MainActivity.this, "Registro Salvo", Toast.LENGTH_LONG).show();
                    clearFields(); // Clear the fields after saving
                } else {
                    Toast.makeText(MainActivity.this, "Erro ao salvar", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void viewAll() {
        btnVisualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = db.getAllData();
                if (res.getCount() == 0) {
                    showMessage("Error", "Nenhum registro foi encontrado");
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                    buffer.append("Nome: " + res.getString(1) + "\n");
                    buffer.append("Cpf: " + res.getString(2) + "\n");
                    buffer.append("Telefone: " + res.getString(3) + "\n");
                    buffer.append("Sintomas: " + res.getString(4) + "\n\n");
                }

                showMessage("Registros", buffer.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1 || requestCode == 2) {
                int recordId = data.getIntExtra("RECORD_ID", -1);
                if (recordId != -1) {
                    loadRecordForEditing(recordId);
                }
            }
        }
    }

    private void loadRecordForEditing(int id) {
        Cursor res = db.getDataById(id);
        if (res.getCount() == 0) {
            return;
        }

        if (res.moveToFirst()) {
            editNome.setText(res.getString(1));
            editCpf.setText(res.getString(2));
            editTelefone.setText(res.getString(3));
            editSintomas.setText(res.getString(4));
            btnSalvar.setTag(id);
        }
    }

    private void clearFields() {
        editNome.setText("");
        editCpf.setText("");
        editTelefone.setText("");
        editSintomas.setText("");
        btnSalvar.setTag(null); // Clear the tag after saving
    }

    public void showMessage(String title, String message) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}