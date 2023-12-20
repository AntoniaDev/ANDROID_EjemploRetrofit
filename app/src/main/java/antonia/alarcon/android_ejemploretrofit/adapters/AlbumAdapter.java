package antonia.alarcon.android_ejemploretrofit.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import antonia.alarcon.android_ejemploretrofit.PhotoActivity;
import antonia.alarcon.android_ejemploretrofit.R;
import antonia.alarcon.android_ejemploretrofit.constantes.Constantes;
import antonia.alarcon.android_ejemploretrofit.modelos.Album;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumVH> {
    private Context context;
    private int resource;
    private List<Album> objects;
    @NonNull
    @Override
    public AlbumVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View albumView = LayoutInflater.from(context).inflate(resource, null);

        albumView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        return new AlbumVH(albumView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumVH holder, int position) {
        Album a = objects.get(position);

        holder.lbTitulo.setText(a.getTitulo());
        //para que puedan pinchar en cada fila y se abra otra actividad con la información
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PhotoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(Constantes.ALBUMID, a.getId());
                intent.putExtras(bundle);
                //el adapter no puede lanzar la actividad, es una clase y se ejecuta dentro de la actividad
                //esa actividad se ejecuta dentro del contexto, entonces es quien abrirá el activity
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class AlbumVH extends RecyclerView.ViewHolder{
        TextView lbTitulo;

        public AlbumVH(@NonNull View itemView) {
            super(itemView);

            lbTitulo = itemView.findViewById(R.id.lbTituloViewHolder);
        }
    }

    public AlbumAdapter(Context context, int resource, List<Album> objects) {
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }
}
