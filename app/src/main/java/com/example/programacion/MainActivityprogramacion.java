package com.example.programacion;

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
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivityprogramacion extends AppCompatActivity {

    static final String NOMBRE_CANAL = "NOMBRE_CANAL";
    ListView listViewProgramacion;
    ArrayList<String>canales=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activityprogramacion);
        listViewProgramacion=findViewById(R.id.ListViewProgramacionCanales);

        DescargarXML descargarXML=new DescargarXML();
        descargarXML.execute();

    }

    void mostrar()
    {

        ArrayAdapter <String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,canales);
        listViewProgramacion.setAdapter(adapter);

        listViewProgramacion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent intento=new Intent(MainActivityprogramacion.this,MainActivityprogramacioncanal.class);
                intento.putExtra(NOMBRE_CANAL,canales.get(i));
                startActivity(intento);
                Toast.makeText(MainActivityprogramacion.this, canales.get(i), Toast.LENGTH_SHORT).show();
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
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    Document doc = db.parse(httpURLConnection.getInputStream());
                    Element root = doc.getDocumentElement();
                    NodeList items = root.getElementsByTagName("channel");
                    for (int i = 0; i < items.getLength(); i++)
                    {
                        Node item = items.item(i);
                        NodeList elementos = item.getChildNodes();
                        for (int j = 0; j < elementos.getLength(); j++)
                        {
                            if (elementos.item(j) instanceof Element)
                            {
                                Log.d("canal",""+elementos.item(j).getTextContent());
                                canales.add(elementos.item(j).getTextContent());
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
            mostrar();
        }
    }
}