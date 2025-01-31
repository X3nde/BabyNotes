package com.xenderodriguezlopez.babynotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Configurar botones
        Button btnCitas = findViewById(R.id.btnCitas);
        btnCitas.setOnClickListener(v -> startActivity(new Intent(MenuActivity.this, CitasActivity.class)));

        Button btnMedicacion = findViewById(R.id.btnMedicacion);
        btnMedicacion.setOnClickListener(v -> startActivity(new Intent(MenuActivity.this, MedicacionActivity.class)));

        Button btnSueno = findViewById(R.id.btnSueno);
        btnSueno.setOnClickListener(v -> startActivity(new Intent(MenuActivity.this, SuenoActivity.class)));

        Button btnDeposiciones = findViewById(R.id.btnDeposiciones);
        btnDeposiciones.setOnClickListener(v -> startActivity(new Intent(MenuActivity.this, DeposicionesActivity.class)));

        Button btnAcercaDe = findViewById(R.id.btnAcercaDe);
        btnAcercaDe.setOnClickListener(v -> showAboutDialog());

        Button btnPreferencias = findViewById(R.id.btnPreferencias);
        btnPreferencias.setOnClickListener(v -> startActivity(new Intent(MenuActivity.this, SettingsActivity.class)));

    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Acerca de");
        builder.setMessage("BabyNotes\nVersión 1.0\nDesarrollado por Xende Rodríguez López.");
        builder.setPositiveButton("Cerrar", null);
        builder.create().show();
    }
}
