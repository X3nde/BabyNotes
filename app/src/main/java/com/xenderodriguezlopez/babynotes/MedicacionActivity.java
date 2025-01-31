package com.xenderodriguezlopez.babynotes;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.xenderodriguezlopez.babynotes.adapters.MedicamentoAdapter;
import com.xenderodriguezlopez.babynotes.database.BabyNotesDatabase;
import com.xenderodriguezlopez.babynotes.models.Medicamento;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MedicacionActivity extends AppCompatActivity {

    private ListView listMedicamentos;
    private MedicamentoAdapter adapter;
    private List<Medicamento> medicamentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicacion);

        // Inicializar la lista y el adaptador
        listMedicamentos = findViewById(R.id.listMedicamentos);
        medicamentos = new ArrayList<>();
        adapter = new MedicamentoAdapter(this, medicamentos);
        listMedicamentos.setAdapter(adapter);

        // Configurar el botón para añadir medicación
        Button btnAddMedicacion = findViewById(R.id.btnAddMedicacion);
        btnAddMedicacion.setOnClickListener(v -> showAddMedicacionDialog());

        // Configurar clic largo en la lista
        listMedicamentos.setOnItemLongClickListener((parent, view, position, id) -> {
            showMedicacionOptionsDialog(position);
            return true; // Indica que el evento está consumido
        });

        // Cargar medicamentos desde la base de datos
        loadMedicamentosFromDatabase();
    }

    private void showAddMedicacionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_medicacion, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        // Referenciar los campos del formulario
        EditText editNombreMedicamento = dialogView.findViewById(R.id.editNombreMedicamento);
        EditText editFechaInicio = dialogView.findViewById(R.id.editFechaInicio);
        EditText editFechaFin = dialogView.findViewById(R.id.editFechaFin);
        EditText editPosologia = dialogView.findViewById(R.id.editPosologia);
        EditText editCantidad = dialogView.findViewById(R.id.editCantidad);
        EditText editNotas = dialogView.findViewById(R.id.editNotas);

        Button btnGuardar = dialogView.findViewById(R.id.btnGuardarMedicacion);

        // Configurar los campos de fecha para abrir un DatePickerDialog
        editFechaInicio.setOnClickListener(v -> showDatePickerDialog(editFechaInicio));
        editFechaFin.setOnClickListener(v -> showDatePickerDialog(editFechaFin));

        btnGuardar.setOnClickListener(v -> {
            String nombre = editNombreMedicamento.getText().toString().trim();
            String fechaInicio = editFechaInicio.getText().toString().trim();
            String fechaFin = editFechaFin.getText().toString().trim();
            String posologia = editPosologia.getText().toString().trim();
            String cantidad = editCantidad.getText().toString().trim();
            String notas = editNotas.getText().toString().trim();

            if (nombre.isEmpty() || fechaInicio.isEmpty() || fechaFin.isEmpty() || posologia.isEmpty() || cantidad.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos obligatorios.", Toast.LENGTH_SHORT).show();
            } else {
                Medicamento medicamento = new Medicamento();
                medicamento.setNombre(nombre);
                medicamento.setFechaInicio(fechaInicio);
                medicamento.setFechaFin(fechaFin);
                medicamento.setPosologia(posologia);
                medicamento.setCantidad(cantidad);
                medicamento.setNotas(notas);

                new Thread(() -> {
                    BabyNotesDatabase database = BabyNotesDatabase.getInstance(this);
                    database.medicamentoDao().insert(medicamento);

                    runOnUiThread(() -> {
                        Toast.makeText(this, "Medicamento guardado correctamente.", Toast.LENGTH_SHORT).show();
                        loadMedicamentosFromDatabase();
                    });
                }).start();

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showMedicacionOptionsDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opciones");
        builder.setItems(new CharSequence[]{"Editar", "Eliminar"}, (dialog, which) -> {
            if (which == 0) {
                showEditMedicacionDialog(position);
            } else if (which == 1) {
                confirmDeleteMedicacion(position);
            }
        });
        builder.show();
    }

    private void showEditMedicacionDialog(int position) {
        Medicamento medicamentoSeleccionado = medicamentos.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_medicacion, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        // Referenciar los campos del formulario
        EditText editNombreMedicamento = dialogView.findViewById(R.id.editNombreMedicamento);
        EditText editFechaInicio = dialogView.findViewById(R.id.editFechaInicio);
        EditText editFechaFin = dialogView.findViewById(R.id.editFechaFin);
        EditText editPosologia = dialogView.findViewById(R.id.editPosologia);
        EditText editCantidad = dialogView.findViewById(R.id.editCantidad);
        EditText editNotas = dialogView.findViewById(R.id.editNotas);

        Button btnGuardar = dialogView.findViewById(R.id.btnGuardarMedicacion);

        editNombreMedicamento.setText(medicamentoSeleccionado.getNombre());
        editFechaInicio.setText(medicamentoSeleccionado.getFechaInicio());
        editFechaFin.setText(medicamentoSeleccionado.getFechaFin());
        editPosologia.setText(medicamentoSeleccionado.getPosologia());
        editCantidad.setText(medicamentoSeleccionado.getCantidad());
        editNotas.setText(medicamentoSeleccionado.getNotas());

        btnGuardar.setText("Actualizar");

        btnGuardar.setOnClickListener(v -> {
            medicamentoSeleccionado.setNombre(editNombreMedicamento.getText().toString().trim());
            medicamentoSeleccionado.setFechaInicio(editFechaInicio.getText().toString().trim());
            medicamentoSeleccionado.setFechaFin(editFechaFin.getText().toString().trim());
            medicamentoSeleccionado.setPosologia(editPosologia.getText().toString().trim());
            medicamentoSeleccionado.setCantidad(editCantidad.getText().toString().trim());
            medicamentoSeleccionado.setNotas(editNotas.getText().toString().trim());

            new Thread(() -> {
                BabyNotesDatabase database = BabyNotesDatabase.getInstance(this);
                database.medicamentoDao().update(medicamentoSeleccionado);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Medicamento actualizado correctamente.", Toast.LENGTH_SHORT).show();
                    loadMedicamentosFromDatabase();
                });
            }).start();

            dialog.dismiss();
        });

        dialog.show();
    }

    private void confirmDeleteMedicacion(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Medicación");
        builder.setMessage("¿Estás seguro de que deseas eliminar este medicamento?");
        builder.setPositiveButton("Sí", (dialog, which) -> deleteMedicacion(position));
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void deleteMedicacion(int position) {
        Medicamento medicamentoSeleccionado = medicamentos.get(position);

        new Thread(() -> {
            BabyNotesDatabase database = BabyNotesDatabase.getInstance(this);
            database.medicamentoDao().delete(medicamentoSeleccionado);

            runOnUiThread(() -> {
                Toast.makeText(this, "Medicamento eliminado correctamente.", Toast.LENGTH_SHORT).show();
                loadMedicamentosFromDatabase();
            });
        }).start();
    }

    private void showDatePickerDialog(EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            String formattedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
            editText.setText(formattedDate);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void loadMedicamentosFromDatabase() {
        new Thread(() -> {
            List<Medicamento> dbMedicamentos = BabyNotesDatabase.getInstance(this).medicamentoDao().getAllMedicamentos();
            runOnUiThread(() -> {
                medicamentos.clear();
                medicamentos.addAll(dbMedicamentos);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }
}
