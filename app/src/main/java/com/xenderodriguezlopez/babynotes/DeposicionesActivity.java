package com.xenderodriguezlopez.babynotes;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.xenderodriguezlopez.babynotes.adapters.DeposicionAdapter;
import com.xenderodriguezlopez.babynotes.database.BabyNotesDatabase;
import com.xenderodriguezlopez.babynotes.models.Deposicion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DeposicionesActivity extends AppCompatActivity {

    private ListView listDeposiciones;
    private DeposicionAdapter adapter;
    private List<Deposicion> deposiciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposiciones);

        listDeposiciones = findViewById(R.id.listDeposiciones);
        deposiciones = new ArrayList<>();
        adapter = new DeposicionAdapter(this, deposiciones);
        listDeposiciones.setAdapter(adapter);

        Button btnAddDeposicion = findViewById(R.id.btnAddDeposicion);
        btnAddDeposicion.setOnClickListener(v -> showAddDeposicionDialog());

        listDeposiciones.setOnItemLongClickListener((parent, view, position, id) -> {
            showActionMenu(position);
            return true;
        });

        loadDeposicionesFromDatabase();
    }

    private void showAddDeposicionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_deposicion, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        EditText editFecha = dialogView.findViewById(R.id.editFechaDeposicion);
        EditText editHora = dialogView.findViewById(R.id.editHoraDeposicion);
        EditText editColor = dialogView.findViewById(R.id.editColorDeposicion);
        EditText editTextura = dialogView.findViewById(R.id.editTexturaDeposicion);
        EditText editComentarios = dialogView.findViewById(R.id.editComentariosDeposicion);

        editFecha.setOnClickListener(v -> showDatePicker(editFecha));
        editHora.setOnClickListener(v -> showTimePicker(editHora));

        Button btnGuardar = dialogView.findViewById(R.id.btnGuardarDeposicion);

        btnGuardar.setOnClickListener(v -> {
            String fecha = editFecha.getText().toString().trim();
            String hora = editHora.getText().toString().trim();
            String color = editColor.getText().toString().trim();
            String textura = editTextura.getText().toString().trim();
            String comentarios = editComentarios.getText().toString().trim();

            if (fecha.isEmpty() || hora.isEmpty() || color.isEmpty() || textura.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos obligatorios.", Toast.LENGTH_SHORT).show();
            } else {
                Deposicion deposicion = new Deposicion(fecha, hora, color, textura, comentarios);

                new Thread(() -> {
                    BabyNotesDatabase database = BabyNotesDatabase.getInstance(this);
                    database.deposicionDao().insert(deposicion);

                    runOnUiThread(() -> {
                        Toast.makeText(this, "Deposición guardada correctamente.", Toast.LENGTH_SHORT).show();
                        loadDeposicionesFromDatabase();
                    });
                }).start();

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showActionMenu(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Acciones");
        builder.setItems(new String[]{"Editar", "Eliminar"}, (dialog, which) -> {
            if (which == 0) {
                showEditDeposicionDialog(position);
            } else if (which == 1) {
                confirmDeleteDialog(position);
            }
        });
        builder.create().show();
    }

    private void showEditDeposicionDialog(int position) {
        Deposicion deposicionSeleccionada = deposiciones.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_deposicion, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        EditText editFecha = dialogView.findViewById(R.id.editFechaDeposicion);
        EditText editHora = dialogView.findViewById(R.id.editHoraDeposicion);
        EditText editColor = dialogView.findViewById(R.id.editColorDeposicion);
        EditText editTextura = dialogView.findViewById(R.id.editTexturaDeposicion);
        EditText editComentarios = dialogView.findViewById(R.id.editComentariosDeposicion);

        editFecha.setText(deposicionSeleccionada.getFecha());
        editHora.setText(deposicionSeleccionada.getHora());
        editColor.setText(deposicionSeleccionada.getColor());
        editTextura.setText(deposicionSeleccionada.getTextura());
        editComentarios.setText(deposicionSeleccionada.getComentarios());

        editFecha.setOnClickListener(v -> showDatePicker(editFecha));
        editHora.setOnClickListener(v -> showTimePicker(editHora));

        Button btnGuardar = dialogView.findViewById(R.id.btnGuardarDeposicion);

        btnGuardar.setText("Actualizar");
        btnGuardar.setOnClickListener(v -> {
            String fecha = editFecha.getText().toString().trim();
            String hora = editHora.getText().toString().trim();
            String color = editColor.getText().toString().trim();
            String textura = editTextura.getText().toString().trim();
            String comentarios = editComentarios.getText().toString().trim();

            if (fecha.isEmpty() || hora.isEmpty() || color.isEmpty() || textura.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos obligatorios.", Toast.LENGTH_SHORT).show();
            } else {
                deposicionSeleccionada.setFecha(fecha);
                deposicionSeleccionada.setHora(hora);
                deposicionSeleccionada.setColor(color);
                deposicionSeleccionada.setTextura(textura);
                deposicionSeleccionada.setComentarios(comentarios);

                new Thread(() -> {
                    BabyNotesDatabase database = BabyNotesDatabase.getInstance(this);
                    database.deposicionDao().update(deposicionSeleccionada);

                    runOnUiThread(() -> {
                        Toast.makeText(this, "Deposición actualizada correctamente.", Toast.LENGTH_SHORT).show();
                        loadDeposicionesFromDatabase();
                    });
                }).start();

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void confirmDeleteDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Deposición");
        builder.setMessage("¿Estás seguro de que deseas eliminar esta deposición?");
        builder.setPositiveButton("Sí", (dialog, which) -> deleteDeposicion(position));
        builder.setNegativeButton("No", null);
        builder.create().show();
    }

    private void deleteDeposicion(int position) {
        Deposicion deposicionSeleccionada = deposiciones.get(position);

        new Thread(() -> {
            BabyNotesDatabase database = BabyNotesDatabase.getInstance(this);
            database.deposicionDao().delete(deposicionSeleccionada);

            runOnUiThread(() -> {
                Toast.makeText(this, "Deposición eliminada correctamente.", Toast.LENGTH_SHORT).show();
                loadDeposicionesFromDatabase();
            });
        }).start();
    }

    private void loadDeposicionesFromDatabase() {
        new Thread(() -> {
            List<Deposicion> dbDeposiciones = BabyNotesDatabase.getInstance(this).deposicionDao().getAllDeposiciones();
            runOnUiThread(() -> {
                deposiciones.clear();
                deposiciones.addAll(dbDeposiciones);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }

    private void showDatePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String date = dayOfMonth + "/" + (month + 1) + "/" + year;
            editText.setText(date);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void showTimePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            String time = hourOfDay + ":" + (minute < 10 ? "0" + minute : minute);
            editText.setText(time);
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        timePickerDialog.show();
    }
}
