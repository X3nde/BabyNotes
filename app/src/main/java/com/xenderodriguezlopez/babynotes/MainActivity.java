package com.xenderodriguezlopez.babynotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.PopupMenu;
import android.view.MenuInflater;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Desplegar el menú
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView  tvBienvenida = findViewById(R.id. tvBienvenida);
        tvBienvenida.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
        });
    }


    // Manejar la selección de opciones del menú
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Manejar clics en los ítems del menú
        int id = item.getItemId();

        if (id == R.id.action_citas) {
            startActivity(new Intent(this, CitasActivity.class));
            return true;
        } else if (id == R.id.action_medicacion) {
            startActivity(new Intent(this, MedicacionActivity.class));
            return true;
        } else if (id == R.id.action_sueno) {
            startActivity(new Intent(this, SuenoActivity.class));
            return true;
        } else if (id == R.id.action_deposiciones) {
            startActivity(new Intent(this, DeposicionesActivity.class));
            return true;
        } else if (id == R.id.action_about) {
            showAboutDialog();
            return true;
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    // Mostrar el diálogo "Acerca de"
    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Acerca de");
        builder.setMessage("BabyNotes\nVersión 1.0\nDesarrollado por Xende Rodríguez López.");
        builder.setPositiveButton("Cerrar", null);
        builder.create().show();
    }

}
