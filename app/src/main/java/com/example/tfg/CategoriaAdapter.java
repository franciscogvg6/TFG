package com.example.tfg;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.ViewHolder> {

    private int resource;
    private ArrayList<Categoria> categoriasList;

    public CategoriaAdapter(ArrayList<Categoria> categoriasList, int resource){
        this.categoriasList = categoriasList;
        this.resource = resource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(resource, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int index) {

        Categoria categoria = categoriasList.get(index);
        viewHolder.textViewCategoria.setText(categoria.getCategoria());

    }

    @Override
    public int getItemCount() {
        return categoriasList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewCategoria;
        public View view;

        public ViewHolder(View view){
            super(view);

            this.view = view;
            this.textViewCategoria = (TextView) view.findViewById(R.id.rv_render);

        }
    }

}