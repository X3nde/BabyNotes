package com.xenderodriguezlopez.babynotes;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.xenderodriguezlopez.babynotes.adapters.CitasAdapter;
import com.xenderodriguezlopez.babynotes.database.BabyNotesDatabase;
import com.xenderodriguezlopez.babynotes.models.Cita;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CitasActivity extends AppCompatActivity {

    private BabyNotesDatabase database;
    private CitasAdapter citasAdapter;
    private List<Cita> citasList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas);

        database = BabyNotesDatabase.getInstance(this);

        citasList = new ArrayList<>();
        citasAdapter = new CitasAdapter(this, citasList);

        ListView listCitas = findViewById(R.id.listCitas);
        listCitas.setAdapter(citasAdapter);

        Button btnAddCita = findViewById(R.id.btnAddCita);
        btnAddCita.setOnClickListener(v -> showAddCitaDialog());

        listCitas.setOnItemClickListener((parent, view, position, id) -> showEditCitaDialog(position));

        listCitas.setOnItemLongClickListener((parent, view, position, id) -> {
            confirmDeleteDialog(position);
            return true;
        });

        loadCitasFromDatabase();
    }

    private void loadCitasFromDatabase() {
        new Thread(() -> {
            List<Cita> citas = database.citaDao().getAllCitas();
            runOnUiThread(() -> {
                citasList.clear();
                citasList.addAll(citas);
                citasAdapter.notifyDataSetChanged();
            });
        }).start();
    }

    private void showAddCitaDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_cita, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        EditText editTitulo = dialogView.findViewById(R.id.editTitulo);
        EditText editFecha = dialogView.findViewById(R.id.editFecha);
        EditText editHora = dialogView.findViewById(R.id.editHora);
        EditText editEspecialidad = dialogView.findViewById(R.id.editEspecialidad);
        EditText editPrioridad = dialogView.findViewById(R.id.editPrioridad);
        Button btnGuardar = dialogView.findViewById(R.id.btnGuardarCita);

        // Selección de fecha y hora
        editFecha.setOnClickListener(v -> showDatePicker(editFecha));
        editHora.setOnClickListener(v -> showTimePicker(editHora));

        btnGuardar.setOnClickListener(v -> {
            String titulo = editTitulo.getText().toString().trim();
            String fecha = editFecha.getText().toString().trim();
            String hora = editHora.getText().toString().trim();
            String especialidad = editEspecialidad.getText().toString().trim();
            String prioridad = editPrioridad.getText().toString().trim();

            if (titulo.isEmpty() || fecha.isEmpty() || hora.isEmpty() || especialidad.isEmpty() || prioridad.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
                return;
            }

            Cita cita = new Cita(titulo, fecha, hora, especialidad, prioridad);

            new Thread(() -> {
                database.citaDao().insert(cita);
                runOnUiThread(this::loadCitasFromDatabase);
            }).start();

            dialog.dismiss();
        });

        dialog.show();
    }

    private void showEditCitaDialog(int position) {
        Cita citaSeleccionada = citasList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_cita, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        EditText editTitulo = dialogView.findViewById(R.id.editTitulo);
        EditText editFecha = dialogView.findViewById(R.id.editFecha);
        EditText editHora = dialogView.findViewById(R.id.editHora);
        EditText editEspecialidad = dialogView.findViewById(R.id.editEspecialidad);
        EditText editPrioridad = dialogView.findViewById(R.id.editPrioridad);
        Button btnGuardar = dialogView.findViewById(R.id.btnGuardarCita);

        editTitulo.setText(citaSeleccionada.getTitulo());
        editFecha.setText(citaSeleccionada.getFecha());
        editHora.setText(citaSeleccionada.getHora());
        editEspecialidad.setText(citaSeleccionada.getEspecialidad());
        editPrioridad.setText(citaSeleccionada.getPrioridad());

        editFecha.setOnClickListener(v -> showDatePicker(editFecha));
        editHora.setOnClickListener(v -> showTimePicker(editHora));

        btnGuardar.setOnClickListener(v -> {
            String titulo = editTitulo.getText().toString().trim();
            String fecha = editFecha.getText().toString().trim();
            String hora = editHora.getText().toString().trim();
            String especialidad = editEspecialidad.getText().toString().trim();
            String prioridad = editPrioridad.getText().toString().trim();

            if (titulo.isEmpty() || fecha.isEmpty() || hora.isEmpty() || especialidad.isEmpty() || prioridad.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
                return;
            }

            citaSeleccionada.setTitulo(titulo);
            citaSeleccionada.setFecha(fecha);
            citaSeleccionada.setHora(hora);
            citaSeleccionada.setEspecialidad(especialidad);
            citaSeleccionada.setPrioridad(prioridad);

            new Thread(() -> {
                database.citaDao().update(citaSeleccionada);
                runOnUiThread(this::loadCitasFromDatabase);
            }).start();

            dialog.dismiss();
        });

        dialog.show();
    }

    private void confirmDeleteDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Cita");
        builder.setMessage("¿Estás seguro de que deseas eliminar esta cita?");
        builder.setPositiveButton("Sí", (dialog, which) -> deleteCita(position));
        builder.setNegativeButton("No", null);
        builder.create().show();
    }

    private void deleteCita(int position) {
        new Thread(() -> {
            database.citaDao().delete(citasList.get(position));
            runOnUiThread(this::loadCitasFromDatabase);
        }).start();
    }

    private void showDatePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> editText.setText(String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> editText.setText(String.format("%02d:%02d", hourOfDay, minute)),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        timePickerDialog.show();
    }
}
