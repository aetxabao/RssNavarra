package com.pmdm.rssnavarra;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ListView lstTemas;

    private String[] temasNombre;
    private String[] temasNum;

    protected URL url;
    protected String strUrl;

    protected String rss;
    protected String tema;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temasNombre = getResources().getStringArray(R.array.nombreTema);
        temasNum = getResources().getStringArray(R.array.numTema);

        lstTemas = (ListView)findViewById(R.id.ListTemas);

        TemaAdapter adapter = new TemaAdapter(this, temasNombre);

        lstTemas.setAdapter(adapter);
        lstTemas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tema = temasNombre[position];
                try {
                    strUrl = getResources().getString(R.string.url);
                    if (position>0){
                        strUrl += "?Tema=" + temasNum[position];
                    }
                    url = new URL(strUrl);
                } catch (MalformedURLException e) {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.urlError),
                            Toast.LENGTH_LONG).show();
                    Log.d(getResources().getString(R.string.logError),
                            getResources().getString(R.string.urlError) + "\n" + e.toString());
                    return;
                }
                new HttpGetTask().execute();
            }
        });
    }

    public void callTemaActivity(){
        Intent intentA = new Intent(MainActivity.this, TemaActivity.class);
        intentA.putExtra("RSS", rss);
        intentA.putExtra("TEMA", tema);
        startActivity(intentA);
        //MainActivity.this.finish();
    }

    public void displayAsyncTaskError(){
        Toast.makeText(getApplicationContext(),
                getResources().getString(R.string.conError),
                Toast.LENGTH_LONG).show();
    }


    //http://developer.android.com/reference/android/os/AsyncTask.html
    private class HttpGetTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try{
                //http://developer.android.com/intl/es/reference/java/net/HttpURLConnection.html
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", "");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();
                //https://es.wikipedia.org/wiki/Anexo:C%C3%B3digos_de_estado_HTTP
                Log.d("Cliente HTTP", "respuesta " + conn.getResponseCode());
                InputStream inputStream = new BufferedInputStream(conn.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder("");
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString();
            }catch (Exception e){
                Log.d(getResources().getString(R.string.logError),
                        getResources().getString(R.string.conError) + "\n" + e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result){
            rss = result;
            if (rss==null){
                displayAsyncTaskError();
            }else{
                callTemaActivity();
            }
        }
    }
}
