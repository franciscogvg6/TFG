package com.example.tfg;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.AsyncTask;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;




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
        holder.precioTextView.setText(String.valueOf(producto.getPrecio()));


        // Cargar la imagen utilizando AsyncTask
        new CargarImagenTask(holder.fotoImageView).execute(producto.getFoto());

    }

    private static class CargarImagenTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        CargarImagenTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            if (urls.length > 0) {
                try {
                    return cargarImagenDesdeURL(urls[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                // Manejar el error de carga de imagen
            }
        }


        private Bitmap cargarImagenDesdeURL(String urlString) throws IOException {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        }
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





