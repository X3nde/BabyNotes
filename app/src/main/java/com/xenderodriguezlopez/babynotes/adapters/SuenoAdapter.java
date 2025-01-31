package com.xenderodriguezlopez.babynotes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;

import com.xenderodriguezlopez.babynotes.R;
import com.xenderodriguezlopez.babynotes.models.Sueno;

import java.util.List;

public class SuenoAdapter extends ArrayAdapter<Sueno> {

    private Context context;
    private List<Sueno> registros;

    public SuenoAdapter(@NonNull Context context, @NonNull List<Sueno> registros) {
        super(context, R.layout.item_sueno, registros);
        this.context = context;
        this.registros = registros;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_sueno, parent, false);
        }

        Sueno sueno = registros.get(position);

        // Referenciar vistas
        TextView tvHoraInicio = convertView.findViewById(R.id.tvHoraInicio);
        TextView tvHoraFin = convertView.findViewById(R.id.tvHoraFin);
        TextView tvComentarios = convertView.findViewById(R.id.tvComentarios);

        // Configurar valores
        tvHoraInicio.setText("Inicio: " + sueno.getHoraInicio());
        tvHoraFin.setText("Fin: " + sueno.getHoraFin());
        tvComentarios.setText(sueno.getComentarios().isEmpty() ? "Sin comentarios" : sueno.getComentarios());

        return convertView;
    }
}
