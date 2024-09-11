package com.example.programacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class mostrarcapitulos extends AppCompatActivity {

    static final String SERVIDOR = "http://192.168.3.104/nube2/";
    //static final String SERVIDOR = "http://192.168.18.13/nube2/";
    ArrayList<String>ObtenerNombresseries=new ArrayList<String>();
    TextView textViewNombreSerie;
    EditText editTextTitulo, editTextTemporada, editTextNum_temp, editTextResumen;
    String datonombre="";
    ListView listViewMostrardatos;
    Button buttoninsertar;
    ArrayList<String>Tit=new ArrayList<String>();
    ArrayList<String>temp=new ArrayList<String>();
    ArrayList<String>num=new ArrayList<String>();
    ArrayList<String>res=new ArrayList<String>();
    int pos=0;
    String ti="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrarcapitulos);
        editTextTitulo=findViewById(R.id.editTextTextTitulo);
        editTextTemporada=findViewById(R.id.editTextTemporada);
        editTextNum_temp=findViewById(R.id.editTextNumberNum_temp);
        editTextResumen=findViewById(R.id.editTextTextResumen);
        textViewNombreSerie=findViewById(R.id.textViewNombreserie);
        listViewMostrardatos=findViewById(R.id.ListViewMostrardatos);
        buttoninsertar=findViewById(R.id.buttonInsertar);

        Intent intent=getIntent();
        datonombre=intent.getStringExtra(MainActivity.NOMBRE);
        textViewNombreSerie.setText(datonombre);

        Log.d("Nombre capitulos",datonombre);

        DescargarJSON descargarJSON=new DescargarJSON();
        descargarJSON.execute();

        buttoninsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String dato1=datonombre;
                String dato2=editTextTitulo.getText().toString();
                String dato3=editTextTemporada.getText().toString();
                String dato4=editTextNum_temp.getText().toString();
                String dato5=editTextResumen.getText().toString();

                insertar ins=new insertar();
                ins.execute(dato1,dato2,dato3,dato4,dato5);

                Tit.clear();
                temp.clear();
                num.clear();
                res.clear();

                DescargarJSON descargarJSON=new DescargarJSON();
                descargarJSON.execute();
            }
        });
        registerForContextMenu(listViewMostrardatos);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("OPCIONES");
        menu.add(0,v.getId(),0,"Mostrar info");
        menu.add(0,v.getId(),0,"Modificar");
        menu.add(0,v.getId(),0,"Eliminar");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        pos=info.position;
        if(item.getTitle().equals("Mostrar info"))
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle(Tit.get(pos));
            builder.setMessage("Temporada: "+temp.get(pos)+"\nNumero de temporada: "+num.get(pos)+"\nResumen: "+res.get(pos));
            builder.setPositiveButton("OK",null);

            AlertDialog dialog=builder.create();
            dialog.show();
        }
        if(item.getTitle().equals("Modificar"))
        {
            String dato1=editTextTitulo.getText().toString();
            String dato2=editTextTemporada.getText().toString();
            String dato3=editTextNum_temp.getText().toString();
            String dato4=editTextResumen.getText().toString();
            String dato5=Tit.get(pos);
            if(dato1.isEmpty()||dato2.isEmpty()||dato3.isEmpty()||dato4.isEmpty()||dato5.isEmpty())
            {
                Toast.makeText(this, "ERROR, todos los campos tienes que estar rellenos para la modificacion", Toast.LENGTH_SHORT).show();
            }
            else
            {
                modificando mod=new modificando();
                mod.execute(dato1,dato2,dato3,dato4,dato5);

                Tit.clear();
                temp.clear();
                num.clear();
                res.clear();

                DescargarJSON descargarJSON=new DescargarJSON();
                descargarJSON.execute();
            }
        }
        if(item.getTitle().equals("Eliminar"))
        {
            Log.d("Longitud",""+Tit.size());

            ti=Tit.get(pos);

            borrar bor=new borrar();
            bor.execute(ti);

            Tit.clear();
            temp.clear();
            num.clear();
            res.clear();

            DescargarJSON descargarJSON=new DescargarJSON();
            descargarJSON.execute();
        }
        return true;
    }

    private class modificando extends AsyncTask<String, Void, Void>
    {

        @Override
        protected Void doInBackground(String... strings) {

            String titul,tempo,num_tempo,resum,titul_ant;
            titul=strings[0];
            tempo=strings[1];
            num_tempo=strings[2];
            resum=strings[3];
            titul_ant=strings[4];

            URL url,url2;
            HttpURLConnection httpURLConnection;
            try {
                Log.d("Titulo antigo",titul_ant+" - "+titul);
                url=new URL(SERVIDOR+"modificarCapitulosespecifico.php?Titulo='"+titul+"'&Temporada='"+tempo+"'&Num_temp='"+num_tempo+"'&Resumen='"+resum+"'&Tituloantiguo='"+titul_ant+"'");
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
            String dato=strings[0];
            HttpURLConnection httpURLConnection;
            try {
                Log.d("Dato",""+dato);
                url=new URL(SERVIDOR+"eliminarCapitulosespecifico.php?Titulo='"+dato+"'");

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

            String dato1=strings[0];
            String dato2=strings[1];
            String dato3=strings[2];
            String dato4=strings[3];
            String dato5=strings[4];
            URL url;
            String token="6633445";
            HttpURLConnection httpURLConnection;
            try {
                url=new URL(SERVIDOR+"insertarCapitulos.php?Nombre_serie="+dato1+"&Titulo="+dato2+"&Temporada="+dato3+"&Num_temp="+dato4+"&Resumen="+dato5);
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

    void mostrar()
    {
        ArrayAdapter<String>adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,Tit);
        listViewMostrardatos.setAdapter(adapter);
        listViewMostrardatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                editTextTitulo.setText(Tit.get(i));
                editTextTemporada.setText(temp.get(i));
                editTextNum_temp.setText(num.get(i));
                editTextResumen.setText(res.get(i));
            }
        });
    }

    private class DescargarJSON extends AsyncTask<String, Void, Void>
    {
        String todo="";
        JSONArray jsonArray;
        @Override
        protected Void doInBackground(String... strings) {

            URL url;
            HttpURLConnection httpURLConnection;

            try {
                url = new URL(SERVIDOR + "obtenercapitulosespecificos.php?Nombre_serie='"+datonombre+"'");

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
            clasemostrarcapitulos datos1;
            for(int i=0;i<jsonArray.length();i++)
            {

                JSONObject jsonObject= null;
                try {
                    jsonObject = jsonArray.getJSONObject(i);
                    Tit.add(jsonObject.getString("Titulo"));
                    temp.add(jsonObject.getString("Temporada"));
                    num.add(jsonObject.getString("Num_temp"));
                    res.add(jsonObject.getString("Resumen"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            mostrar();
        }
    }
}