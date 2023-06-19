package com.example.tfg;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.ViewHolder> {

    private ArrayList<Categoria> categoriasList;
    private OnItemClickListener listener;
    private int resource;
    private int selectedItem = RecyclerView.NO_POSITION;

    public CategoriaAdapter(ArrayList<Categoria> categoriasList,int resource) {
        this.categoriasList = categoriasList;
        this.resource = resource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_render, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categoria categoria = categoriasList.get(position);
        holder.bind(categoria, position, listener);

        // Resaltar la categoría seleccionada
        if (position == selectedItem) {
            holder.itemView.setBackgroundColor(Color.YELLOW);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return categoriasList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Categoria categoria, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewCategoria;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewCategoria = itemView.findViewById(R.id.cat);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        notifyItemChanged(selectedItem);
                        selectedItem = position;
                        notifyItemChanged(selectedItem);
                        listener.onItemClick(categoriasList.get(position), position);
                    }
                }
            });
        }

        public void bind(Categoria categoria, int position, OnItemClickListener listener) {
            textViewCategoria.setText(categoria.getNombre());
        }
    }
}









