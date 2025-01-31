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
import com.xenderodriguezlopez.babynotes.models.Cita;

import java.util.List;

public class CitasAdapter extends ArrayAdapter<Cita> {

    private Context context;
    private List<Cita> citas;

    public CitasAdapter(@NonNull Context context, @NonNull List<Cita> citas) {
        super(context, 0, citas);
        this.context = context;
        this.citas = citas;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cita, parent, false);
        }

        Cita cita = citas.get(position);

        // Referenciar los elementos del diseño
        TextView textTitulo = convertView.findViewById(R.id.textTitulo);
        TextView textFechaHora = convertView.findViewById(R.id.textFechaHora);
        TextView textEspecialidad = convertView.findViewById(R.id.textEspecialidad);

        // Configurar los valores
        textTitulo.setText(cita.getTitulo());
        textFechaHora.setText(cita.getFecha() + " " + cita.getHora());
        textEspecialidad.setText(cita.getEspecialidad());

        return convertView;
    }
}
