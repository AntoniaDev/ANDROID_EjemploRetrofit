package antonia.alarcon.android_ejemploretrofit.conexiones;

import java.util.ArrayList;

import antonia.alarcon.android_ejemploretrofit.modelos.Album;
import antonia.alarcon.android_ejemploretrofit.modelos.Photo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiConexiones {
    //MOSTRAR TODOS - ALL
    @GET("/albums")
    Call<ArrayList<Album>> getAlbums();

    //MOSOTRAS TODOS CON CIERTO PARÁMETRO - FILTRAR POR PARAMETROS
    //para filtrar por parámetro
    @GET("/photos?")
    Call<ArrayList<Photo>> getPhotoAlbum(@Query("albumId") int albumId);

    //MOSTRAR TODOS DE CIERTO PATH - FILTRAR POR PATH
    //entre llaves porque lo voy a sustituir por un valor
    @GET("/albums/{albumId}/photos")
    Call<ArrayList<Photo>> getPhotoAlbumPath(@Path("albumId") int albumId);

    //INSERTAR UN ALBUM
    @POST("/albums")
    Call<Album> postAlbum(@Body Album a);

    //BORRAR UN ALBUM
    //llaves porque no le voy a poner un parámetro
    @DELETE("/albums/{id}")
    Call<Album> deleteAlbum(@Path("id") int idAlbum);
}
