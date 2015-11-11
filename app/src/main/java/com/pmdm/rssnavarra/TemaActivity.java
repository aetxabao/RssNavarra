package com.pmdm.rssnavarra;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

public class TemaActivity extends AppCompatActivity {

    protected String tema;
    protected String rss;
    protected Item[] datos;
    protected String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tema);

        Intent intent = this.getIntent();
        tema = intent.getStringExtra("TEMA");
        rss = intent.getStringExtra("RSS");

        TextView tvTemaTitle = (TextView) findViewById(R.id.LblTemaTitle);
        tvTemaTitle.setText(tema);

        datos = parseRss(rss);

        ItemAdapter adapter = new ItemAdapter(this, datos);

        final ListView listView = (ListView) findViewById(R.id.ListItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                link = datos[position].getLink();
//                Log.d(getResources().getString(R.string.logError), "xxxxxxxxxxxxxxxxxxx");
//                Toast.makeText(getApplicationContext(), datos[position].getTitle(), Toast.LENGTH_LONG).show();
                callWebActivity();
            }
        });
    }

    public void callWebActivity(){
        Intent intent = new Intent(TemaActivity.this, WebActivity.class);
        intent.putExtra("LINK", link);
        startActivity(intent);
    }

    protected Item[] parseRss(String input)  {
        DocumentBuilderFactory factory;
        DocumentBuilder builder;
        Document doc;
        XPathFactory xpf;
        XPath xp;
        XPathExpression xpe;
        NodeList items;
        int n;
        String str;
        Item[] itemArray = null;
        try {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();

            doc = builder.parse(new ByteArrayInputStream(input.getBytes()));
            doc.getDocumentElement().normalize();

            xpf = XPathFactory.newInstance();
            xp = xpf.newXPath();

            xpe = xp.compile("/rss/channel/item");
            items = (NodeList) xpe.evaluate(doc.getDocumentElement(), XPathConstants.NODESET);
            n = items.getLength();
            if (n==0) return null;
            itemArray = new Item[n];
            for (int i = 0; i < n; i++) {
                Element item = (Element) items.item(i);
                itemArray[i] = new Item();
                itemArray[i].setTitle(xp.evaluate("title", item));
                itemArray[i].setPubDateParsed(xp.evaluate("pubDateParsed", item).replaceAll("T"," "));
                itemArray[i].setLink(xp.evaluate("link", item));
                itemArray[i].setEnclosureUrl(xp.evaluate("enclosure/@url", item));
                itemArray[i].setDescription(xp.evaluate("description", item));
            }
            return itemArray;
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.rssError),
                    Toast.LENGTH_LONG).show();
            Log.d(getResources().getString(R.string.logError),
                    getResources().getString(R.string.rssError) + "\n" + e.toString());
            return null;
        }
    }

}
