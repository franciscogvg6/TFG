package com.example.tfg;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;



import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {
    private ArrayList<Producto> productosList;
    private int layout;

    public ProductoAdapter(ArrayList<Producto> productosList, int layout) {
        this.productosList = productosList;
        this.layout = layout;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = productosList.get(position);

        holder.nombreTextView.setText(producto.getNombre());
        holder.precioTextView.setText(producto.getPrecio());
    }

    @Override
    public int getItemCount() {
        return productosList.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        ImageView fotoImageView;
        TextView nombreTextView;
        TextView precioTextView;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            fotoImageView = itemView.findViewById(R.id.imageViewFoto);
            nombreTextView = itemView.findViewById(R.id.nombre);
            precioTextView = itemView.findViewById(R.id.precio);
        }
    }

    public void updateProductos(ArrayList<Producto> productosList) {
        this.productosList = productosList;
        notifyDataSetChanged();
    }
}





