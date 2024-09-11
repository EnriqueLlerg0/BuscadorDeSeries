package com.example.programacion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    static final String NOMBRE = "NOMBRE";
    String nombre;
    String imagen;
    String Cadena_emite;
    String año, temporadas;
    String recomendacion;
    ListView listViewmostrar;
    EditText editTextnombre, editTextcad_emite, editTextaño, editTexttemporadas, editTextRecomendacion;
    Button buttoninsertar;
    Button buttonNota;
    ArrayList<String> ObtenerNombreSeries=new ArrayList<String>();
    ArrayList<Integer> ObtenerAño_estrenoSeries=new ArrayList<Integer>();
    ArrayList<String> Obtenercad_imiteSeries=new ArrayList<String>();
    ArrayList<Integer> Obtenernum_tempSeries=new ArrayList<Integer>();
    ArrayList<String> ObtenerimagenSeries=new ArrayList<String>();
    ArrayList<Integer> ObtenerrecomendacionSeries=new ArrayList<Integer>();
    ArrayList<clasemostrarseries> mostrarcosas=new ArrayList<clasemostrarseries>();
    int posicionlista, bandera=0;
    static final String SERVIDOR = "http://192.168.3.104/nube2/";
    Button buttonMostrarDatos;
    //static final String SERVIDOR = "http://192.168.18.13/nube2/";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewmostrar=findViewById(R.id.ListViewMostrar);

        editTextnombre=findViewById(R.id.editTextTextNombre);
        editTextcad_emite=findViewById(R.id.editTextCadena);
        editTextaño=findViewById(R.id.editTextNumberEstreno);
        editTexttemporadas=findViewById(R.id.editTextNum_temp);
        editTextRecomendacion=findViewById(R.id.editTextNumberRecomendacion);

        buttoninsertar=findViewById(R.id.buttoninsertar);
        buttonMostrarDatos=findViewById(R.id.buttonMostraropcion);
        buttonNota=findViewById(R.id.buttonNota);

        registerForContextMenu(listViewmostrar);

        buttonNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarcosas.clear();
                ObtenerAño_estrenoSeries.clear();
                ObtenerimagenSeries.clear();
                Obtenercad_imiteSeries.clear();
                ObtenerNombreSeries.clear();
                Obtenernum_tempSeries.clear();
                ObtenerrecomendacionSeries.clear();

                DescargarJSONnota4y5 descargarJSONnota4y5 = new DescargarJSONnota4y5();
                descargarJSONnota4y5.execute("listadoJSON.php");
            }
        });

        buttonMostrarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(bandera==0)
                {
                    buttonMostrarDatos.setText("Mostrar JSON");
                    bandera=1;

                    mostrarcosas.clear();
                    ObtenerAño_estrenoSeries.clear();
                    ObtenerimagenSeries.clear();
                    Obtenercad_imiteSeries.clear();
                    ObtenerNombreSeries.clear();
                    Obtenernum_tempSeries.clear();
                    ObtenerrecomendacionSeries.clear();

                    DescargarJSON descargarJSON = new DescargarJSON();
                    descargarJSON.execute("listadoJSON.php");
                }
                else if(bandera==1)
                {
                    buttonMostrarDatos.setText("Mostrar XML");
                    bandera=2;

                    mostrarcosas.clear();
                    ObtenerAño_estrenoSeries.clear();
                    ObtenerimagenSeries.clear();
                    Obtenercad_imiteSeries.clear();
                    ObtenerNombreSeries.clear();
                    Obtenernum_tempSeries.clear();
                    ObtenerrecomendacionSeries.clear();

                    DescargarXML descargarXML=new DescargarXML();
                    descargarXML.execute("listadoXML.php");
                }
                else
                {
                    buttonMostrarDatos.setText("Mostrar CSV");
                    bandera=0;

                    mostrarcosas.clear();
                    ObtenerAño_estrenoSeries.clear();
                    ObtenerimagenSeries.clear();
                    Obtenercad_imiteSeries.clear();
                    ObtenerNombreSeries.clear();
                    Obtenernum_tempSeries.clear();
                    ObtenerrecomendacionSeries.clear();

                    DescargarCSV descargarCSV = new DescargarCSV();
                    descargarCSV.execute("listadoCSV.php");
                }
            }
        });

        buttoninsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                nombre=editTextnombre.getText().toString();
                Cadena_emite=editTextcad_emite.getText().toString();
                año=editTextaño.getText().toString();
                temporadas=editTexttemporadas.getText().toString();
                recomendacion=editTextRecomendacion.getText().toString();

                obtenerdatosjson obtenerdatosjson=new obtenerdatosjson();
                obtenerdatosjson.execute();


                if(nombre.isEmpty()||Cadena_emite.isEmpty()||año.isEmpty()||temporadas.isEmpty()||recomendacion.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "ERROR, tienes que rellenar todos los campos", Toast.LENGTH_SHORT).show();
                }
                else if(Integer.parseInt(recomendacion)>=0&&Integer.parseInt(recomendacion)<=5)
                {
                    insertar ins=new insertar();
                    ins.execute();

                    mostrarcosas.clear();
                    ObtenerAño_estrenoSeries.clear();
                    ObtenerimagenSeries.clear();
                    Obtenercad_imiteSeries.clear();
                    ObtenerNombreSeries.clear();
                    Obtenernum_tempSeries.clear();
                    ObtenerrecomendacionSeries.clear();
                    DescargarJSON descargarJSON = new DescargarJSON();
                    descargarJSON.execute("listadoJSON.php");
                }
                else
                {
                    Toast.makeText(MainActivity.this, "ERROR, la valoracion entre 0 y 5", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.item1:
                Intent intege=new Intent(MainActivity.this,MainActivityBuscador.class);
                startActivity(intege);
                break;
            case R.id.item2:
                Intent intege2=new Intent(MainActivity.this,MainActivityprogramacion.class);
                startActivity(intege2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("OPCIONES");
        menu.add(0,v.getId(),0,"Mostrar descripcion");
        menu.add(0,v.getId(),0,"Modificar");
        menu.add(0,v.getId(),0,"Eliminar");
        menu.add(0,v.getId(),0,"Capitulos");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        posicionlista=info.position;
        if(item.getTitle().equals("Eliminar"))
        {

            borrar borr=new borrar();
            borr.execute();
            Toast.makeText(this,"eliminando serie: "+ObtenerNombreSeries.get(posicionlista),Toast.LENGTH_SHORT).show();
            mostrarcosas.clear();
            ObtenerAño_estrenoSeries.clear();
            ObtenerimagenSeries.clear();
            Obtenercad_imiteSeries.clear();
            ObtenerNombreSeries.clear();
            Obtenernum_tempSeries.clear();
            ObtenerrecomendacionSeries.clear();
            DescargarJSON descargarJSON = new DescargarJSON();
            descargarJSON.execute("listadoJSON.php");
        }
        if(item.getTitle().equals("Modificar"))
        {

            nombre=editTextnombre.getText().toString();

            if(!nombre.isEmpty())
            {
                obtenerdatosjson obtenerdatosjson=new obtenerdatosjson();
                obtenerdatosjson.execute();
            }

            String dato1=editTextnombre.getText().toString();
            String dato2=editTextcad_emite.getText().toString();
            String dato3=editTextaño.getText().toString();
            String dato4=editTexttemporadas.getText().toString();
            String dato5=imagen;
            Log.d("imagen valor",""+imagen);
            String dato6=ObtenerNombreSeries.get(posicionlista);
            String dato7=editTextRecomendacion.getText().toString();

            Toast.makeText(this,"modificando serie: "+ObtenerNombreSeries.get(posicionlista),Toast.LENGTH_SHORT).show();

            if(dato1.isEmpty()||dato2.isEmpty()||dato3.isEmpty()||dato4.isEmpty()||dato5.isEmpty()||dato7.isEmpty())
            {
                Toast.makeText(this, "ERROR, todos los campos tienes que estar rellenos para la modificacion", Toast.LENGTH_SHORT).show();
            }
            else
            {
                modificando mod=new modificando();
                mod.execute(dato1,dato2,dato3,dato4,dato5,dato6,dato7);

                mostrarcosas.clear();
                ObtenerAño_estrenoSeries.clear();
                ObtenerimagenSeries.clear();
                Obtenercad_imiteSeries.clear();
                ObtenerNombreSeries.clear();
                Obtenernum_tempSeries.clear();
                ObtenerrecomendacionSeries.clear();

                DescargarJSON descargarJSON = new DescargarJSON();
                descargarJSON.execute("listadoJSON.php");
            }
        }
        if(item.getTitle().equals("Mostrar descripcion"))
        {
            Intent intento=new Intent(MainActivity.this,mostrardescripcion.class);
            intento.putExtra(NOMBRE,ObtenerNombreSeries.get(posicionlista));
            Log.d("Nombre",ObtenerNombreSeries.get(posicionlista));
            startActivity(intento);
        }
        if(item.getTitle().equals("Capitulos"))
        {
            Intent intento2=new Intent(MainActivity.this,mostrarcapitulos.class);
            intento2.putExtra(NOMBRE,ObtenerNombreSeries.get(posicionlista));
            Log.d("Nombre",ObtenerNombreSeries.get(posicionlista));
            startActivity(intento2);
        }
        return true;
    }

    private class modificando extends AsyncTask<String, Void, Void>
    {

        @Override
        protected Void doInBackground(String... strings) {

            String nom,añ,cad,num,imag,nombre_ant,recom;
            nom=strings[0];
            cad=strings[1];
            añ=strings[2];
            num=strings[3];
            imag=imagen;
            nombre_ant=strings[5];
            recom=strings[6];
            Log.d("mensaje",""+nombre_ant);

            URL url,url2;
            HttpURLConnection httpURLConnection;
            try {
                url=new URL(SERVIDOR+"modificarSeries.php?Nombre='"+nom+"'&año_estreno='"+añ+"'&cad_imite='"+cad+"'&num_temp='"+num+"'&imagen='"+imag+"'&Nombre_antiguo='"+nombre_ant+"'"+"&recomendacion="+recom);
                //url2=new URL(SERVIDOR+"modificarSeries.php?Nombre='"+ObtenerNombreSeries.get(posicionlista)+"'&año_estreno='"+ObtenerAño_estrenoSeries.get(posicionlista)+"'&cad_imite='"+Obtenercad_imiteSeries.get(posicionlista)+"'&num_temp='"+Obtenernum_tempSeries.get(posicionlista)+"'&imagen='"+ObtenerimagenSeries.get(posicionlista)+"'");

                httpURLConnection=(HttpURLConnection) url.openConnection();
                BufferedReader in=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String linea;
                while((linea=in.readLine())!=null)
                {
                    Log.d("estado",""+linea);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class borrar extends AsyncTask<String, Void, Void>
    {

        @Override
        protected Void doInBackground(String... strings) {
            URL url, url2;
            String token="6633445";
            HttpURLConnection httpURLConnection;
            try {
                Log.d("nombre",ObtenerNombreSeries.get(posicionlista));
                url=new URL(SERVIDOR+"eliminarSeries.php?Nombre='"+ObtenerNombreSeries.get(posicionlista)+"'");
                url2=new URL(SERVIDOR+"eliminarCapitulos.php?Nombre_serie='"+ObtenerNombreSeries.get(posicionlista)+"'");

                httpURLConnection=(HttpURLConnection) url2.openConnection();
                BufferedReader in=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String linea;
                while((linea=in.readLine())!=null)
                {
                    Log.d("estado",""+linea);
                }

                httpURLConnection=(HttpURLConnection) url.openConnection();
                BufferedReader in2=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String linea2;
                while((linea2=in2.readLine())!=null)
                {
                    Log.d("estado",""+linea2);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class insertar extends AsyncTask<String, Void, Void>
    {


        @Override
        protected Void doInBackground(String... strings) {

            URL url;
            String token="6633445";
            HttpURLConnection httpURLConnection;
            try {
                Log.d("nombre insertar",nombre+" "+año+" "+Cadena_emite+" "+temporadas+" "+imagen);
                url=new URL(SERVIDOR+"insertarSeries.php?nombre="+nombre+"&ano_estreno="+año+"&cad_imite="+Cadena_emite+"&num_temp="+temporadas+"&imagen="+imagen+"&recomendacion="+recomendacion);
                httpURLConnection=(HttpURLConnection) url.openConnection();
                BufferedReader in=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String linea;
                while((linea=in.readLine())!=null)
                {
                    Log.d("estado",""+linea);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

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

                jarray = usuario.getJSONArray("results");
                JSONObject oneobject=jarray.getJSONObject(0);
                imagen="https://image.tmdb.org/t/p/original"+oneobject.getString("poster_path");
                Log.d("datos","Nombre: "+nombre+" Imagen: "+imagen);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    void mostrar()
    {
        Log.d("mensaje",""+mostrarcosas.size());


            Adaptadorparalistview adaptadorparalistview = new Adaptadorparalistview(this,R.layout.misseries,mostrarcosas);
            listViewmostrar.setAdapter(adaptadorparalistview);
            listViewmostrar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {
                    editTextnombre.setText(ObtenerNombreSeries.get(i));
                    editTextcad_emite.setText(Obtenercad_imiteSeries.get(i));
                    editTextaño.setText(ObtenerAño_estrenoSeries.get(i)+"");
                    editTexttemporadas.setText(Obtenernum_tempSeries.get(i)+"");
                    editTextRecomendacion.setText(ObtenerrecomendacionSeries.get(i));
                }
            });

    }

    private class DescargarJSON extends AsyncTask<String, Void, Void>
    {
        String todo="";
        JSONArray jsonArray;
        @Override
        protected Void doInBackground(String... strings) {

            String script = strings[0];
            URL url;
            HttpURLConnection httpURLConnection;

            try {
                url = new URL(SERVIDOR + script);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Log.d("hola","hola");
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                    String linea = "";
                    while ((linea = br.readLine()) != null) {
                        todo += linea+"\n";
                        Log.d("hola","hola2");
                    }
                    jsonArray = new JSONArray(todo);
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
            ArrayAdapter<String> adapter;
            List<String> list=new ArrayList<String>();
            clasemostrarseries datos1;
            for(int i=0;i<jsonArray.length();i++)
            {

                JSONObject jsonObject= null;
                try {
                    jsonObject = jsonArray.getJSONObject(i);
                    ObtenerNombreSeries.add(jsonObject.getString("Nombre"));
                    ObtenerAño_estrenoSeries.add(jsonObject.getInt("ano_estreno"));
                    Obtenercad_imiteSeries.add(jsonObject.getString("cad_imite"));
                    Obtenernum_tempSeries.add(jsonObject.getInt("num_temp"));
                    ObtenerimagenSeries.add(jsonObject.getString("imagen"));
                    ObtenerrecomendacionSeries.add(jsonObject.getInt("recomendacion"));
                    datos1=new clasemostrarseries(jsonObject.getString("Nombre"),jsonObject.getString("imagen"),jsonObject.getString("recomendacion"));
                    mostrarcosas.add(datos1);
                    Log.d("mensaje","Nombre: "+jsonObject.getString("Nombre")+"Año estreño: "+jsonObject.getInt("ano_estreno")+" Cadena que emite: "+jsonObject.getString("cad_imite")+" Nuemro temporada: "+jsonObject.getInt("num_temp")+" Imagen: "+jsonObject.getString("imagen"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            mostrar();
        }
    }

    private class DescargarJSONnota4y5 extends AsyncTask<String, Void, Void>
    {
        String todo="";
        JSONArray jsonArray;
        @Override
        protected Void doInBackground(String... strings) {

            String script = strings[0];
            URL url;
            HttpURLConnection httpURLConnection;

            try {
                url = new URL(SERVIDOR + script);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Log.d("hola","hola");
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                    String linea = "";
                    while ((linea = br.readLine()) != null) {
                        todo += linea+"\n";
                        Log.d("hola","hola2");
                    }
                    jsonArray = new JSONArray(todo);
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
            ArrayAdapter<String> adapter;
            List<String> list=new ArrayList<String>();
            clasemostrarseries datos1;
            for(int i=0;i<jsonArray.length();i++)
            {

                JSONObject jsonObject= null;
                try {
                    jsonObject = jsonArray.getJSONObject(i);
                    ObtenerNombreSeries.add(jsonObject.getString("Nombre"));
                    ObtenerAño_estrenoSeries.add(jsonObject.getInt("ano_estreno"));
                    Obtenercad_imiteSeries.add(jsonObject.getString("cad_imite"));
                    Obtenernum_tempSeries.add(jsonObject.getInt("num_temp"));
                    ObtenerimagenSeries.add(jsonObject.getString("imagen"));
                    ObtenerrecomendacionSeries.add(jsonObject.getInt("recomendacion"));
                    if(jsonObject.getInt("recomendacion")==4||jsonObject.getInt("recomendacion")==5)
                    {
                        datos1=new clasemostrarseries(jsonObject.getString("Nombre"),jsonObject.getString("imagen"),jsonObject.getString("recomendacion"));
                        mostrarcosas.add(datos1);
                    }
                    Log.d("mensaje","Nombre: "+jsonObject.getString("Nombre")+"Año estreño: "+jsonObject.getInt("ano_estreno")+" Cadena que emite: "+jsonObject.getString("cad_imite")+" Nuemro temporada: "+jsonObject.getInt("num_temp")+" Imagen: "+jsonObject.getString("imagen"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            mostrar();
        }
    }

    private class DescargarCSV extends AsyncTask<String, Void, Void> {
        String todo = "";

        @Override
        protected Void doInBackground(String... strings) {
            String script = strings[0];
            URL url;
            HttpURLConnection httpURLConnection;

            try {
                url = new URL(SERVIDOR + script);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    String linea = "";
                    while ((linea = br.readLine()) != null) {
                        todo += linea+"\n";
                        Log.d("mensaje",todo);
                    }
                    br.close();
                    inputStream.close();

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            ArrayAdapter<String> adapter;
            List<String> list = new ArrayList<String>();
            String[] lineas = todo.split("\n");
            Log.d("longi",""+lineas.length);

            clasemostrarseries datos1;
            for (String linea : lineas) {
                String[] campos = linea.split(";");
                String dato = "Nombre: " + campos[0];
                dato += " Año_estreno: " + campos[1];
                dato += " cad_imite: " + campos[2];
                dato += " num_temp: " + campos[3];
                dato += " imagen: " + campos[4];
                dato += " recomendacion: " + campos[5];
                Log.d("mesanje",dato);
                ObtenerNombreSeries.add(campos[0]);
                ObtenerAño_estrenoSeries.add(Integer.parseInt(campos[1]));
                Obtenercad_imiteSeries.add(campos[2]);
                Obtenernum_tempSeries.add(Integer.parseInt(campos[3]));
                ObtenerimagenSeries.add(campos[4]);
                ObtenerrecomendacionSeries.add(Integer.parseInt(campos[5]));
                datos1 = new clasemostrarseries(campos[0], campos[4], campos[5]);
                mostrarcosas.add(datos1);
            }
            mostrar();
        }
    }

    private class DescargarXML extends AsyncTask<String, Void, Void>
    {
        ArrayList<String>fila=new ArrayList<String>();
        @Override
        protected Void doInBackground(String... strings) {
            String script = strings[0];
            URL url;
            HttpURLConnection httpURLConnection;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            clasemostrarseries datos1;
            String n,c,a,t;
            try {
                DocumentBuilder db = dbf.newDocumentBuilder();
                url = new URL(SERVIDOR+script);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    Document doc = db.parse(httpURLConnection.getInputStream());
                    Element root = doc.getDocumentElement();
                    NodeList items = root.getElementsByTagName("articulo");
                    for (int i = 0; i < items.getLength(); i++)
                    {
                        Node item = items.item(i);
                        NodeList elementos = item.getChildNodes();
                        for (int j = 0; j < elementos.getLength(); j=j+6)
                        {
                            datos1 = new clasemostrarseries(elementos.item(j).getTextContent(), elementos.item(j+4).getTextContent(),elementos.item(j+5).getTextContent());
                            mostrarcosas.add(datos1);
                            Log.d("Nombre ",""+elementos.item(j).getTextContent());
                            Log.d("año_estreno ",""+elementos.item(j+1).getTextContent());
                            Log.d("cad_imite ",""+elementos.item(j+2).getTextContent());
                            Log.d("num_temp ",""+elementos.item(j+3).getTextContent());
                            Log.d("imagen ",""+elementos.item(j+4).getTextContent());
                            ObtenerNombreSeries.add(elementos.item(j).getTextContent());
                            ObtenerAño_estrenoSeries.add(Integer.parseInt(elementos.item(j+1).getTextContent()));
                            Obtenercad_imiteSeries.add(elementos.item(j+2).getTextContent());
                            Obtenernum_tempSeries.add(Integer.parseInt(elementos.item(j+3).getTextContent()));
                            ObtenerimagenSeries.add(elementos.item(j+4).getTextContent());
                            ObtenerrecomendacionSeries.add(Integer.parseInt(elementos.item(j+5).getTextContent()));

                        }
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            mostrar();
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
            TextView textViewnot = miFila.findViewById(R.id.textViewNota);
            ImageView imageView = miFila.findViewById(R.id.imageViewcartelera);

            String dato1 = mostrarcosas.get(position).getNombre();
            String dato2 = mostrarcosas.get(position).getImagen();
            String dato3 = mostrarcosas.get(position).getNota();

            textViewnom.setText(dato1);
            textViewnot.setText("Nota: "+dato3);
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