package com.xenderodriguezlopez.babynotes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.BaseAdapter;

import com.xenderodriguezlopez.babynotes.R;
import com.xenderodriguezlopez.babynotes.models.Deposicion;

import java.util.List;

public class DeposicionAdapter extends BaseAdapter {

    private final Context context;
    private final List<Deposicion> deposiciones;

    public DeposicionAdapter(Context context, List<Deposicion> deposiciones) {
        this.context = context;
        this.deposiciones = deposiciones;
    }

    @Override
    public int getCount() {
        return deposiciones.size();
    }

    @Override
    public Object getItem(int position) {
        return deposiciones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_deposicion, parent, false);
        }

        Deposicion deposicion = deposiciones.get(position);

        TextView tvFecha = convertView.findViewById(R.id.tvFechaDeposicion);
        TextView tvHora = convertView.findViewById(R.id.tvHoraDeposicion);
        TextView tvColor = convertView.findViewById(R.id.tvColorDeposicion);
        TextView tvTextura = convertView.findViewById(R.id.tvTexturaDeposicion);
        TextView tvComentarios = convertView.findViewById(R.id.tvComentariosDeposicion);

        tvFecha.setText(deposicion.getFecha());
        tvHora.setText(deposicion.getHora());
        tvColor.setText(deposicion.getColor());
        tvTextura.setText(deposicion.getTextura());
        tvComentarios.setText(deposicion.getComentarios());

        return convertView;
    }
}
