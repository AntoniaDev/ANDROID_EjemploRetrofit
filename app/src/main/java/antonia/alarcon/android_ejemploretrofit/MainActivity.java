package antonia.alarcon.android_ejemploretrofit;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import antonia.alarcon.android_ejemploretrofit.adapters.AlbumAdapter;
import antonia.alarcon.android_ejemploretrofit.conexiones.ApiConexiones;
import antonia.alarcon.android_ejemploretrofit.conexiones.RetrofitObject;
import antonia.alarcon.android_ejemploretrofit.databinding.ActivityMainBinding;
import antonia.alarcon.android_ejemploretrofit.modelos.Album;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private AlbumAdapter adapter;
    private RecyclerView.LayoutManager lm;
    private ArrayList<Album> listaAlbums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //inicializar la lista siempre antes que el adaptare para poder pasársela
        listaAlbums = new ArrayList<>();
        adapter = new AlbumAdapter(this, R.layout.album_view_holder, listaAlbums);
        lm = new LinearLayoutManager(this);

        binding.contentMain.contenedorAlbums.setAdapter(adapter);
        binding.contentMain.contenedorAlbums.setLayoutManager(lm);

        //queremos que nada más empezar, lo arrancara con todo
        doGetAlbums();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertarAlbum().show();
            }
        });
    }

    private AlertDialog insertarAlbum() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CREAR ALBUM");
        builder.setCancelable(false);
        View albumView = LayoutInflater.from(this).inflate(R.layout.album_view_model, null);
        EditText txtUserId = albumView.findViewById(R.id.txtUserIdAlbumViewModel);
        EditText txtTitle = albumView.findViewById(R.id.txtTitleAlbumViewModel);
        builder.setView(albumView);

        builder.setNegativeButton("CANCELAR", null);
        builder.setPositiveButton("INSERTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!txtUserId.getText().toString().isEmpty() &&
                !txtTitle.getText().toString().isEmpty()) {
                    Album a = new Album();
                    a.setUserId(Integer.parseInt(txtUserId.getText().toString()));
                    a.setTitulo(txtTitle.getText().toString());

                    doPostAlbum(a);
                }
            }
        });

        return builder.create();
    }

    private void doPostAlbum(Album a) {
        Retrofit retrofit = RetrofitObject.getConexioin();
        ApiConexiones api = retrofit.create(ApiConexiones.class);
        Call<Album> postAlbum = api.postAlbum(a);
        postAlbum.enqueue(new Callback<Album>() {
            @Override
            public void onResponse(Call<Album> call, Response<Album> response) {
                if (response.code() == HttpsURLConnection.HTTP_CREATED) {
                    listaAlbums.add(0, response.body());
                    adapter.notifyItemInserted(0);
                }
            }

            @Override
            public void onFailure(Call<Album> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doGetAlbums() {
        Retrofit retrofit = RetrofitObject.getConexioin();
        ApiConexiones api = retrofit.create(ApiConexiones.class);

        Call<ArrayList<Album>> getAlbums = api.getAlbums();

        getAlbums.enqueue(new Callback<ArrayList<Album>>() {
            //en caso de que tengamos respuesta de que ha ido bien
            @Override
            public void onResponse(Call<ArrayList<Album>> call, Response<ArrayList<Album>> response) {
                if (response.code() == HttpsURLConnection.HTTP_OK) {
                    ArrayList<Album> temp = response.body();
                    //metía la lista de golpe. No la metía directamente a la lista por la memoria volátil o algo así
                    listaAlbums.addAll(temp);
                    adapter.notifyItemRangeInserted(0, listaAlbums.size());
                } else {
                    Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            //en caso de que falle
            @Override
            public void onFailure(Call<ArrayList<Album>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}