package com.example.trabalhofinal;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TelaAdmin extends AppCompatActivity {
    Button btRetornar, btCadastrar;
    EditText edtData, edtLocal, edtStatus, edtDetalhes;
    Spinner spRastreio;
    ListView lista;
    Date data;
    Evento evTemp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_admin);
        btRetornar = findViewById(R.id.btRetornarAdmin);
        btCadastrar = findViewById(R.id.btCadastrarAdmin);
        edtData = findViewById(R.id.edtDataEvento);
        edtLocal = findViewById(R.id.edtLocalEvento);
        edtStatus = findViewById(R.id.edtStatusEvento);
        edtDetalhes = findViewById(R.id.edtDetalhesEvento);
        spRastreio = findViewById(R.id.spPedidoAdmin);
        lista = findViewById(R.id.listAdmin);
        btRetornar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        spRastreio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Boolean> ls = verificaErros();
                if (ls.contains(true)) {
                    Toast.makeText(getApplicationContext(), "Erro, há campos vazios!", Toast.LENGTH_LONG).show();
                } else {
                    if (verificaData()) {
                        Toast.makeText(getApplicationContext(), "Erro, data menor que a do ultimo evento!", Toast.LENGTH_LONG).show();
                    } else {
                        Produto p = (Produto) spRastreio.getSelectedItem();
                        if (p != null) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                Date dt = sdf.parse(edtData.getText().toString());
                                evTemp = new Evento(p.getId(), dt, edtLocal.getText().toString(), edtStatus.getText().toString(), edtDetalhes.getText().toString());
                                new enviaEvento().execute();
                                reiniciarCampos();
                                new buscaEJson().execute(String.valueOf(p.getId()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
        new buscaJson().execute();
    }

    public void reiniciarCampos(){
        edtData.setText("");
        edtLocal.setText("");
        edtStatus.setText("");
        edtDetalhes.setText("");
    }

    public boolean verificaData() {
        if (data == null) {
            return false;
        }
        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dataFormatada = form.parse(edtData.getText().toString());
            if (data.getTime() > dataFormatada.getTime()) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Boolean> verificaErros() {
        List<Boolean> ls = new LinkedList<>();
        ls.add(false);
        if (edtData.getText().equals("") || edtData.getText().length() == 0) {
            ls.add(true);
        }
        if (edtLocal.getText().equals("") || edtLocal.getText().length() == 0) {
            ls.add(true);
        }
        if (edtDetalhes.getText().equals("") || edtDetalhes.getText().length() == 0) {
            ls.add(true);
        }
        if (edtStatus.getText().equals("") || edtStatus.getText().length() == 0) {
            ls.add(true);
        }
        return ls;
    }

    class enviaEvento extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... arg0) {
            try {
                String url = "http://192.168.3.7/correios/cadastra_evento.php";
                JSONObject jsonValores = new JSONObject();
                jsonValores.put("produto_id", evTemp.getProduto_id());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dataFormatada = sdf.format(evTemp.getData());
                jsonValores.put("data", dataFormatada);
                jsonValores.put("local", evTemp.getLocal().toString());
                jsonValores.put("status", evTemp.getStatus().toString());
                jsonValores.put("detalhes", evTemp.getDetalhes().toString());
                conexaouniversal mandar = new conexaouniversal();
                String mensagem = mandar.postJSONObject(url, jsonValores);
                return mensagem;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while (itr.hasNext()) {

                String key = itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String resultado) {

            super.onPostExecute(resultado);

            if (resultado == null) {
                Toast.makeText(getApplicationContext(), "Erro, resposta vazia!", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject obj = new JSONObject(resultado);
                String status = obj.getString("status");
                if (status.contains("ok")) {
                    Toast.makeText(getApplicationContext(), "Evento cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Erro no cadastro do evento!", Toast.LENGTH_LONG).show();
                }
                return;
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_LONG).show();
            }
        }
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
                    new ArrayAdapter<>(TelaAdmin.this,
                            android.R.layout.simple_spinner_item,
                            ls);
            spRastreio.setAdapter(adapter);
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
                if (!ev.isEmpty()) {
                    Evento ult = ev.get(array.length() - 1);
                    data = ult.getData();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ev;
        }

        @Override
        protected void onPostExecute(ArrayList<Evento> lsV) {
            ArrayAdapter<Evento> adapter =
                    new ArrayAdapter<>(TelaAdmin.this,
                            android.R.layout.simple_list_item_1,
                            lsV);
            lista.setAdapter(adapter);
        }
    }
}