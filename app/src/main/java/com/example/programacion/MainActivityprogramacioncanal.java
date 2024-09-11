package com.example.programacion;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivityprogramacioncanal extends AppCompatActivity {

    ListView listViewprogramacioncanal;
    String nombre="";
    ArrayList<String>programas=new ArrayList<String>();

    ArrayList<String>titulo=new ArrayList<String>();
    ArrayList<String>desc=new ArrayList<String>();
    ArrayList<String>categoria=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activityprogramacioncanal);
        listViewprogramacioncanal=findViewById(R.id.ListViewProgramacionCanal);

        Intent intent=getIntent();
        nombre=intent.getStringExtra(MainActivityprogramacion.NOMBRE_CANAL);

        DescargarXML descargarXML=new DescargarXML();
        descargarXML.execute();
    }

    void mostrar()
    {
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,titulo);
        listViewprogramacioncanal.setAdapter(adapter);
        listViewprogramacioncanal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivityprogramacioncanal.this);
                builder.setTitle(titulo.get(i));
                builder.setMessage("Categoria: "+categoria.get(i)+"\nDescripcion: "+desc.get(i));
                builder.setPositiveButton("OK",null);

                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });

    }

    private class DescargarXML extends AsyncTask<String, Void, Void> {
        ArrayList<String> fila = new ArrayList<String>();

        @Override
        protected Void doInBackground(String... strings) {
            URL url;
            HttpURLConnection httpURLConnection;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder db = dbf.newDocumentBuilder();
                url = new URL("https://www.tdtchannels.com/epg/TV.xml");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Document doc = db.parse(httpURLConnection.getInputStream());
                    Element root = doc.getDocumentElement();
                    NodeList items = root.getElementsByTagName("programme");
                    for (int i = 0; i < items.getLength(); i++)
                    {
                        Node item = items.item(i);
                        NodeList elementos = item.getChildNodes();
                        String datos2=item.getAttributes().getNamedItem("channel").getTextContent();

                        String datos3=item.getAttributes().getNamedItem("start").getTextContent();
                        /*String[] palabra2=datos3.split(" ");

                            String pal=palabra2[0].substring(0,4);
                            String pal2=palabra2[0].substring(4,6);
                            String pal3=palabra2[0].substring(6,8);

                            String pal4=palabra2[0].substring(8,10);
                            String pal5=palabra2[0].substring(10,12);
                            String pal6=palabra2[0].substring(12,14);

                        datos3=pal+"/"+pal2+"/"+pal3+" "+pal4+":"+pal5+":"+pal6;*/

                        String datos4=item.getAttributes().getNamedItem("stop").getTextContent();
                        /*String[] palabra3=datos4.split(" ");

                            String pa=palabra3[0].substring(0,4);
                            String pa2=palabra3[0].substring(4,6);
                            String pa3=palabra3[0].substring(6,8);

                            String pa4=palabra3[0].substring(8,10);
                            String pa5=palabra3[0].substring(10,12);
                            String pa6=palabra3[0].substring(12,14);
                        datos4=pa+"/"+pa2+"/"+pa3+" "+pa4+":"+pa5+":"+pa6;*/


                        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss ZZZ");
                        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));
                        String date = dateFormat.format(new Date());

                        if(datos2.equals(nombre))
                        {
                            for (int j = 0; j < elementos.getLength(); j++) {
                                if (elementos.item(j) instanceof Element)
                                {

                                    if(!elementos.item(j).getTextContent().isEmpty())
                                    {
                                        if(datos3.compareTo(date)<0&&datos4.compareTo(date)>0)
                                        {
                                            programas.add(elementos.item(j).getTextContent());
                                        }
                                    }

                                }
                            }
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
        protected void onPostExecute(Void unused)
        {
            for(int i=0;i<programas.size();i=i+3)
            {
                titulo.add(programas.get(i));
                desc.add(programas.get(i+1));
                categoria.add(programas.get(i+2));
            }
            mostrar();
        }
    }
}