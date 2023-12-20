package antonia.alarcon.android_ejemploretrofit.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import antonia.alarcon.android_ejemploretrofit.PhotoActivity;
import antonia.alarcon.android_ejemploretrofit.R;
import antonia.alarcon.android_ejemploretrofit.conexiones.ApiConexiones;
import antonia.alarcon.android_ejemploretrofit.conexiones.RetrofitObject;
import antonia.alarcon.android_ejemploretrofit.constantes.Constantes;
import antonia.alarcon.android_ejemploretrofit.modelos.Album;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = RetrofitObject.getConexioin();
                ApiConexiones api = retrofit.create(ApiConexiones.class);

                Call<Album> deleteAlbum = api.deleteAlbum(a.getId());

                deleteAlbum.enqueue(new Callback<Album>() {
                    @Override
                    public void onResponse(Call<Album> call, Response<Album> response) {
                        if (response.code() == HttpsURLConnection.HTTP_OK) {
                            objects.remove(a);
                            notifyItemRemoved(holder.getAdapterPosition());
                        } else {
                            Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Album> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class AlbumVH extends RecyclerView.ViewHolder{
        TextView lbTitulo;
        ImageButton imgDelete;

        public AlbumVH(@NonNull View itemView) {
            super(itemView);

            lbTitulo = itemView.findViewById(R.id.lbTituloViewHolder);
            imgDelete = itemView.findViewById(R.id.imgDeleteAlbumViewHolder);
        }
    }

    public AlbumAdapter(Context context, int resource, List<Album> objects) {
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }
}
