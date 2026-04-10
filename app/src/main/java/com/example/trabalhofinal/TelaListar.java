package com.example.trabalhofinal;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TelaListar extends AppCompatActivity {
    Button btRetornar;
    ListView listaPedido;
    Spinner spCodigos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_listar);
        btRetornar = findViewById(R.id.btRetornarListagem);
        listaPedido = findViewById(R.id.listPedidos);
        spCodigos = findViewById(R.id.spPedidoListar);
        btRetornar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TelaCliente.class);
                startActivity(intent);
            }
        });
        spCodigos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Produto p = (Produto) parent.getItemAtPosition(position);
                if (p != null) {
                    new buscaEJson().execute(String.valueOf(p.getId()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btRetornar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TelaCliente.class);
                startActivity(i);
            }
        });
        new buscaJson().execute();
    }

    class buscaJson extends AsyncTask<Void, Void, ArrayList<Produto>> {
        @Override
        protected ArrayList<Produto> doInBackground(Void... voids) {
            ArrayList<Produto> ls = new ArrayList<>();
            try {
                String url = "http://192.168.3.7/correios/consulta_produtos.php";
                JSONObject json = new JSONObject();
                String resposta = conexaouniversal.postJSONObject(url, json);
                JSONObject root = new JSONObject(resposta);
                JSONArray array = root.getJSONArray("produto");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    Produto pr = new Produto();
                    pr.setId(obj.getInt("id"));
                    pr.setCodigorastreio(obj.getString("codigorastreio"));
                    ls.add(pr);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return ls;
        }

        @Override
        protected void onPostExecute(ArrayList<Produto> ls) {
            ArrayAdapter<Produto> adapter =
                    new ArrayAdapter<>(TelaListar.this,
                            android.R.layout.simple_spinner_item,
                            ls);
            spCodigos.setAdapter(adapter);
        }
    }

    class buscaEJson extends AsyncTask<String, Void, ArrayList<Evento>> {

        @Override
        protected ArrayList<Evento> doInBackground(String... id) {
            ArrayList<Evento> ev = new ArrayList<>();
            try {
                String url = "http://192.168.3.7/correios/consulta_eventos.php";
                JSONObject json = new JSONObject();
                json.put("id", id[0]);
                String resposta = conexaouniversal.postJSONObject(url, json);
                JSONObject root = new JSONObject(resposta);
                JSONArray array = root.getJSONArray("evento");
                SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject e = array.getJSONObject(i);
                    Evento evento = new Evento();
                    String data = e.getString("data");
                    Date dataReal = form.parse(data);
                    evento.setData(dataReal);
                    evento.setLocal(e.getString("local"));
                    evento.setStatus(e.getString("status"));
                    evento.setLocal(e.getString("local"));
                    evento.setDetalhes(e.getString("detalhes"));
                    ev.add(evento);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return ev;
        }

        @Override
        protected void onPostExecute(ArrayList<Evento> lsV) {
            ArrayAdapter<Evento> adapter =
                    new ArrayAdapter<>(TelaListar.this,
                            android.R.layout.simple_list_item_1,
                            lsV);
            listaPedido.setAdapter(adapter);
        }
    }
}