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

import com.xenderodriguezlopez.babynotes.adapters.SuenoAdapter;
import com.xenderodriguezlopez.babynotes.database.BabyNotesDatabase;
import com.xenderodriguezlopez.babynotes.models.Sueno;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SuenoActivity extends AppCompatActivity {

    private ListView listSueno;
    private SuenoAdapter adapter;
    private List<Sueno> registrosSueno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sueno);

        // Inicializar la lista y el adaptador
        listSueno = findViewById(R.id.listSueno);
        registrosSueno = new ArrayList<>();
        adapter = new SuenoAdapter(this, registrosSueno);
        listSueno.setAdapter(adapter);

        // Configurar el botón para añadir un registro de sueño
        Button btnAddSueno = findViewById(R.id.btnAddSueno);
        btnAddSueno.setOnClickListener(v -> showAddSuenoDialog());

        // Configurar clic largo para mostrar opciones (editar/eliminar)
        listSueno.setOnItemLongClickListener((parent, view, position, id) -> {
            showSuenoOptionsDialog(position);
            return true; // Indica que el evento está consumido
        });

        // Cargar registros de sueño desde la base de datos
        loadSuenoFromDatabase();
    }

    private void showAddSuenoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_sueno, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        // Referenciar los campos del formulario
        EditText editHoraInicio = dialogView.findViewById(R.id.editHoraInicio);
        EditText editHoraFin = dialogView.findViewById(R.id.editHoraFin);
        EditText editComentarios = dialogView.findViewById(R.id.editComentarios);
        Button btnGuardar = dialogView.findViewById(R.id.btnGuardarSueno);

        // Configurar los campos de hora
        editHoraInicio.setOnClickListener(v -> showTimePickerDialog(editHoraInicio));
        editHoraFin.setOnClickListener(v -> showTimePickerDialog(editHoraFin));

        btnGuardar.setOnClickListener(v -> {
            String horaInicio = editHoraInicio.getText().toString().trim();
            String horaFin = editHoraFin.getText().toString().trim();
            String comentarios = editComentarios.getText().toString().trim();

            if (horaInicio.isEmpty() || horaFin.isEmpty()) {
                Toast.makeText(this, "Por favor, completa las horas de inicio y fin.", Toast.LENGTH_SHORT).show();
            } else {
                Sueno sueno = new Sueno();
                sueno.setHoraInicio(horaInicio);
                sueno.setHoraFin(horaFin);
                sueno.setComentarios(comentarios);

                new Thread(() -> {
                    BabyNotesDatabase database = BabyNotesDatabase.getInstance(this);
                    database.suenoDao().insert(sueno);

                    runOnUiThread(() -> {
                        Toast.makeText(this, "Registro de sueño guardado correctamente.", Toast.LENGTH_SHORT).show();
                        loadSuenoFromDatabase();
                    });
                }).start();

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showSuenoOptionsDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opciones");
        builder.setItems(new CharSequence[]{"Editar", "Eliminar"}, (dialog, which) -> {
            if (which == 0) {
                showEditSuenoDialog(position);
            } else if (which == 1) {
                confirmDeleteSueno(position);
            }
        });
        builder.show();
    }

    private void showEditSuenoDialog(int position) {
        Sueno suenoSeleccionado = registrosSueno.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_sueno, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        // Referenciar los campos del formulario
        EditText editHoraInicio = dialogView.findViewById(R.id.editHoraInicio);
        EditText editHoraFin = dialogView.findViewById(R.id.editHoraFin);
        EditText editComentarios = dialogView.findViewById(R.id.editComentarios);
        Button btnGuardar = dialogView.findViewById(R.id.btnGuardarSueno);

        // Prellenar datos existentes
        editHoraInicio.setText(suenoSeleccionado.getHoraInicio());
        editHoraFin.setText(suenoSeleccionado.getHoraFin());
        editComentarios.setText(suenoSeleccionado.getComentarios());

        btnGuardar.setText("Actualizar");

        btnGuardar.setOnClickListener(v -> {
            suenoSeleccionado.setHoraInicio(editHoraInicio.getText().toString().trim());
            suenoSeleccionado.setHoraFin(editHoraFin.getText().toString().trim());
            suenoSeleccionado.setComentarios(editComentarios.getText().toString().trim());

            new Thread(() -> {
                BabyNotesDatabase database = BabyNotesDatabase.getInstance(this);
                database.suenoDao().update(suenoSeleccionado);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Registro actualizado correctamente.", Toast.LENGTH_SHORT).show();
                    loadSuenoFromDatabase();
                });
            }).start();

            dialog.dismiss();
        });

        dialog.show();
    }

    private void confirmDeleteSueno(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Registro");
        builder.setMessage("¿Estás seguro de que deseas eliminar este registro?");
        builder.setPositiveButton("Sí", (dialog, which) -> deleteSueno(position));
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void deleteSueno(int position) {
        Sueno suenoSeleccionado = registrosSueno.get(position);

        new Thread(() -> {
            BabyNotesDatabase database = BabyNotesDatabase.getInstance(this);
            database.suenoDao().delete(suenoSeleccionado);

            runOnUiThread(() -> {
                Toast.makeText(this, "Registro eliminado correctamente.", Toast.LENGTH_SHORT).show();
                loadSuenoFromDatabase();
            });
        }).start();
    }

    private void showTimePickerDialog(EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, selectedHour, selectedMinute) -> {
            String formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
            editText.setText(formattedTime);
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void loadSuenoFromDatabase() {
        new Thread(() -> {
            List<Sueno> dbRegistrosSueno = BabyNotesDatabase.getInstance(this).suenoDao().getAllSueno();
            runOnUiThread(() -> {
                registrosSueno.clear();
                registrosSueno.addAll(dbRegistrosSueno);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }
}
