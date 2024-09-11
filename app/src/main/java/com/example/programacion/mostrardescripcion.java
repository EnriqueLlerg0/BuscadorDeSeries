package com.example.programacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class mostrardescripcion extends AppCompatActivity {

    ImageView imageViewCabecera, imageViewPoster;
    TextView textViewTitulo, textViewPais;
    ListView listView;
    String nombre="", nombre2="", imagenposter="", imagencabecera="", descripcion="", idiomaoriginal="";
    ArrayList<String>valores=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrardescripcion);
        imageViewCabecera=findViewById(R.id.imageViewCabecera);
        imageViewPoster=findViewById(R.id.imageViewPoster);
        textViewTitulo=findViewById(R.id.textViewNombre);
        textViewPais=findViewById(R.id.textViewPais);
        listView=findViewById(R.id.ListViewDescripcion);

        Intent intent=getIntent();
        nombre=intent.getStringExtra(MainActivity.NOMBRE);

        nombre2=intent.getStringExtra(MainActivityBuscador.TITULO);
        imagenposter=intent.getStringExtra(MainActivityBuscador.POSTER);
        imagencabecera=intent.getStringExtra(MainActivityBuscador.CABECERA);
        descripcion=intent.getStringExtra(MainActivityBuscador.DESCRIPCION);
        idiomaoriginal=intent.getStringExtra(MainActivityBuscador.IDIOMAS);

        if(nombre==null||nombre.isEmpty())
        {
            Picasso.get().load(imagencabecera).into(imageViewCabecera);
            Picasso.get().load(imagenposter).into(imageViewPoster);

            textViewTitulo.setText("Titulo: "+nombre2);
            textViewPais.setText("Idioma original: "+idiomaoriginal);

            valores.add(descripcion);
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(mostrardescripcion.this, android.R.layout.simple_list_item_1,valores);
            listView.setAdapter(adapter);
        }
        else
        {
            obtenerdatosjson datos=new obtenerdatosjson();
            datos.execute();
        }


    }

    private class obtenerdatosjson extends AsyncTask<String, Void, Void>
    {

        String todo="";
        JSONArray jsonArray2=new JSONArray();
        JSONObject usuario;

        @Override
        protected Void doInBackground(String... strings) {

            Log.d("Nombre",nombre);

            String urlimagen="https://api.themoviedb.org/3/search/tv?api_key=da4e26462f6dcc4d9b518444611bbc57&language=es&query="+nombre;
            URL url;

            HttpURLConnection httpURLConnection;

            try {
                url = new URL(urlimagen);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    Log.d("hola","hola");
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                    String linea = "";
                    while ((linea = br.readLine()) != null) {
                        todo += linea+"\n";
                    }
                    Log.d("mensaje", todo);
                    usuario= new JSONObject(todo);
                    br.close();
                    inputStream.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            JSONArray jarray= null;
            try {

                jarray = usuario.getJSONArray("results");
                JSONObject oneobject=jarray.getJSONObject(0);
                imagenposter="https://image.tmdb.org/t/p/original"+oneobject.getString("poster_path");
                imagencabecera="https://image.tmdb.org/t/p/original"+oneobject.getString("backdrop_path");
                descripcion=oneobject.getString("overview");
                idiomaoriginal=oneobject.getString("original_language");
                Log.d("datos","Imagencabecera: "+imagencabecera+" Imagenposter: "+imagenposter);

                Picasso.get().load(imagencabecera).into(imageViewCabecera);
                Picasso.get().load(imagenposter).into(imageViewPoster);

                textViewTitulo.setText("Titulo: "+nombre);
                textViewPais.setText("Idioma original: "+idiomaoriginal);

                valores.add(descripcion);
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(mostrardescripcion.this, android.R.layout.simple_list_item_1,valores);
                listView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}