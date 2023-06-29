package com.example.tfg;

import androidx.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CarritoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView carrProductoNombre, carrProductoCantidad, carrProductoPrecio;
    private ItemClickListener itemClickListener;
    public CarritoViewHolder(@NonNull View itemView) {
        super(itemView);
        carrProductoNombre=itemView.findViewById(R.id.carr_producto_nombre);
        carrProductoCantidad=itemView.findViewById(R.id.carr_producto_cantidad);
        carrProductoPrecio=itemView.findViewById(R.id.carr_producto_precio);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }
}
