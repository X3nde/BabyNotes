package com.xenderodriguezlopez.babynotes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.xenderodriguezlopez.babynotes.R;
import com.xenderodriguezlopez.babynotes.models.Medicamento;

import java.util.List;

public class MedicamentoAdapter extends ArrayAdapter<Medicamento> {

    public MedicamentoAdapter(Context context, List<Medicamento> medicamentos) {
        super(context, 0, medicamentos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_medicamento, parent, false);
        }

        Medicamento medicamento = getItem(position);

        TextView txtNombre = convertView.findViewById(R.id.txtNombreMedicamento);
        TextView txtDetalle = convertView.findViewById(R.id.txtDetalleMedicamento);

        txtNombre.setText(medicamento.getNombre());
        txtDetalle.setText("Posología: " + medicamento.getPosologia() + " horas");

        return convertView;
    }
}
