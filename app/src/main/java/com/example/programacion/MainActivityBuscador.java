package com.example.programacion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

public class MainActivityBuscador extends AppCompatActivity {

    static final String CABECERA = "CABECERA";
    static final String TITULO = "TITULO";
    static final String DESCRIPCION = "DESCRIPCION";
    static final String POSTER = "POSTER";
    static final String IDIOMAS = "IDIOMAS";
    EditText editTextNombreBuscar;
    Button buttonBuscar;
    ListView listViewMostrar;
    String nombre="hola";
    ArrayList<String>LosTitulos=new ArrayList<String>();
    ArrayList<String>LasImagenesCabecera=new ArrayList<String>();
    ArrayList<String>LasImagenesPoster=new ArrayList<String>();
    ArrayList<String>LasDescripciones=new ArrayList<String>();
    ArrayList<String>Losidiomas=new ArrayList<String>();
    ArrayList<clasemostrarseries> mostrarcosas=new ArrayList<clasemostrarseries>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_buscador);
        editTextNombreBuscar=findViewById(R.id.editTextTextNombreBuscar);
        buttonBuscar=findViewById(R.id.buttonBuscar);
        listViewMostrar=findViewById(R.id.ListViewListar);

        buttonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                nombre=editTextNombreBuscar.getText().toString();

                if(nombre.isEmpty())
                {
                    Toast.makeText(MainActivityBuscador.this, "ERROR, tienes que rellenar el buscador", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mostrarcosas.clear();
                    LosTitulos.clear();
                    LasImagenesCabecera.clear();
                    LasImagenesPoster.clear();
                    LasDescripciones.clear();
                    Losidiomas.clear();

                    obtenerdatosjson obten=new obtenerdatosjson();
                    obten.execute();
                }
            }
        });
    }

    void mostrar()
    {
        MainActivityBuscador.Adaptadorparalistview adaptadorparalistview = new MainActivityBuscador.Adaptadorparalistview(MainActivityBuscador.this,R.layout.misseries,mostrarcosas);
        listViewMostrar.setAdapter(adaptadorparalistview);
        listViewMostrar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent intento=new Intent(MainActivityBuscador.this,mostrardescripcion.class);
                intento.putExtra(TITULO,LosTitulos.get(i));
                intento.putExtra(CABECERA,LasImagenesCabecera.get(i));
                intento.putExtra(POSTER,LasImagenesPoster.get(i));
                intento.putExtra(DESCRIPCION,LasDescripciones.get(i));
                intento.putExtra(IDIOMAS,Losidiomas.get(i));
                startActivity(intento);
            }
        });
    }

    private class obtenerdatosjson extends AsyncTask<String, Void, Void>
    {

        String todo="";
        JSONArray jsonArray2=new JSONArray();
        JSONObject usuario;

        @Override
        protected Void doInBackground(String... strings) {

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

                clasemostrarseries datos1;
                jarray = usuario.getJSONArray("results");
                JSONObject oneobject=jarray.getJSONObject(0);

                for(int i=0;i<jarray.length();i++)
                {
                    //Log.d("Titulo",""+jarray.getJSONObject(i).getString("name"));
                    LosTitulos.add(jarray.getJSONObject(i).getString("name"));
                    LasDescripciones.add(jarray.getJSONObject(i).getString("overview"));
                    LasImagenesCabecera.add("https://image.tmdb.org/t/p/original"+jarray.getJSONObject(i).getString("backdrop_path"));
                    LasImagenesPoster.add("https://image.tmdb.org/t/p/original"+jarray.getJSONObject(i).getString("poster_path"));
                    Losidiomas.add(jarray.getJSONObject(i).getString("original_language"));
                    datos1=new clasemostrarseries(jarray.getJSONObject(i).getString("name"),"https://image.tmdb.org/t/p/original"+jarray.getJSONObject(i).getString("poster_path"));
                    mostrarcosas.add(datos1);
                }
                mostrar();
                /*imagenposter="https://image.tmdb.org/t/p/original"+oneobject.getString("poster_path");
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
                listView.setAdapter(adapter);*/
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    class Adaptadorparalistview extends ArrayAdapter<clasemostrarseries> {

        public Adaptadorparalistview(@NonNull Context context, int resource, @NonNull List<clasemostrarseries> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return super.getDropDownView(position, convertView, parent);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View miFila = inflater.inflate(R.layout.misseries, parent, false);

            TextView textViewnom = miFila.findViewById(R.id.textViewnombre);
            ImageView imageView = miFila.findViewById(R.id.imageViewcartelera);

            String dato1 = mostrarcosas.get(position).getNombre();
            String dato2 = mostrarcosas.get(position).getImagen();

            textViewnom.setText(dato1);
            Log.d("datp2",dato2);
            if(dato2==null)
            {
                imageView.setImageResource(R.drawable.galeriaimagen);
            }
            else
            {
                Picasso.get().load(dato2).into(imageView);
            }

            return miFila;
        }
    }
}