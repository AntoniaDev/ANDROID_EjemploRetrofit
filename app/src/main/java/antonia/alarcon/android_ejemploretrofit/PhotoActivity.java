package antonia.alarcon.android_ejemploretrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import antonia.alarcon.android_ejemploretrofit.adapters.PhotoAdapter;
import antonia.alarcon.android_ejemploretrofit.conexiones.ApiConexiones;
import antonia.alarcon.android_ejemploretrofit.conexiones.RetrofitObject;
import antonia.alarcon.android_ejemploretrofit.constantes.Constantes;
import antonia.alarcon.android_ejemploretrofit.modelos.Photo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PhotoActivity extends AppCompatActivity {
    private PhotoAdapter adapter;
    private RecyclerView.LayoutManager lm;
    private ArrayList<Photo> listaPhotos;

    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        recycler = findViewById(R.id.contenedorPhoto);
        listaPhotos = new ArrayList<>();
        adapter = new PhotoAdapter(this, R.layout.photo_view_holder, listaPhotos);
        lm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        //unir tod0
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(lm);

        //recibir la información del album
        Intent intent = getIntent();
        if (intent.getExtras().getInt(Constantes.ALBUMID) != 0) {
            int albumid = intent.getExtras().getInt(Constantes.ALBUMID);
            doGetPhotos(albumid);
        }
    }

    private void doGetPhotos(int albumid) {
        Retrofit retrofit = RetrofitObject.getConexioin();
        //crear según la interfaz
        ApiConexiones api = retrofit.create(ApiConexiones.class);

        //ejecutar uno que me devolverá un alista de fotos
        //hacen lo mismo pero por caminos distintos
        //Call<ArrayList<Photo>> getPhotos = api. getPhotoAlbum(albumid);
        Call<ArrayList<Photo>> getPhotos = api.getPhotoAlbumPath(albumid);

        getPhotos.enqueue(new Callback<ArrayList<Photo>>() {
            @Override
            public void onResponse(Call<ArrayList<Photo>> call, Response<ArrayList<Photo>> response) {
                if (response.code() == HttpsURLConnection.HTTP_OK) {
                    ArrayList<Photo> temp = response.body();
                    listaPhotos.addAll(temp);
                    adapter.notifyItemRangeInserted(0, listaPhotos.size());
                } else {
                    //si no me devuelve un 200
                    Toast.makeText(PhotoActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Photo>> call, Throwable t) {
                Toast.makeText(PhotoActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}