package com.example.trabalhofinal;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TelaCadastro extends AppCompatActivity {
    Button btRetornar, btCadastrar;
    EditText txtCodigoRastreio, txtOrigem, txtDestino;
    Spinner spTransportadora, spEntrega, spPrevisao;
    Produto prodTemp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);
        btRetornar = findViewById(R.id.btRetornarCadastro);
        btCadastrar = findViewById(R.id.btCadastrarCad);
        txtCodigoRastreio = findViewById(R.id.edtCodigoRastreio);
        txtCodigoRastreio.setText(gerarCodigoRastreio());
        txtOrigem = findViewById(R.id.edtOrigem);
        txtDestino = findViewById(R.id.edtDestino);
        spTransportadora = findViewById(R.id.spTransportadora);
        spEntrega = findViewById(R.id.spEntrega);
        spPrevisao = findViewById(R.id.spPrevisao);
        btRetornar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TelaCliente.class);
                startActivity(intent);
            }
        });
        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Boolean> ls = verificaErros();
                if (ls.contains(true)) {
                    Toast.makeText(getApplicationContext(), "Erro, há campos vazios!", Toast.LENGTH_LONG).show();
                } else {
                    prodTemp = new Produto(txtCodigoRastreio.getText().toString(), spTransportadora.getSelectedItem().toString(),
                            spEntrega.getSelectedItem().toString(), "Objeto postado", txtOrigem.getText().toString(),
                            txtDestino.getText().toString(), pegarDataPrevisao(spPrevisao.getSelectedItem().toString()));
                    new Enviajsonpost().execute();
                    reiniciarCampos();
                }
            }
        });
    }

    public void reiniciarCampos(){
        txtDestino.setText("");
        txtOrigem.setText("");
        txtCodigoRastreio.setText(gerarCodigoRastreio());
    }
    class Enviajsonpost extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... arg0) {
            try {
                String url = "http://192.168.3.7/correios/cadastra_produto.php";
                JSONObject jsonValores = new JSONObject();
                jsonValores.put("codigo_rastreio", prodTemp.getCodigorastreio().toString());
                jsonValores.put("empresa", prodTemp.getEmpresa().toString());
                jsonValores.put("servico", prodTemp.getServico().toString());
                jsonValores.put("status", prodTemp.getStatus().toString());
                jsonValores.put("origem", prodTemp.getOrigem().toString());
                jsonValores.put("destino", prodTemp.getDestino().toString());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dataMysql = sdf.format(prodTemp.getDataprevisao());
                jsonValores.put("data_previsao", dataMysql);
                conexaouniversal mandar = new conexaouniversal();
                String mensagem=mandar.postJSONObject(url,jsonValores);
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

            while(itr.hasNext()){

                String key= itr.next();
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
                    Toast.makeText(getApplicationContext(), "Produto cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Erro!", Toast.LENGTH_LONG).show();
                }
                return;
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_LONG).show();
            }
        }
    }

    public Date pegarDataPrevisao(String dt) {
        int dias = 0;
        switch (dt) {
            /*
          24 Horas
        7 Dias
        14 Dias
        31 Dias<
             */
            case "24 Horas":
                dias = 1;
                break;
            case "7 Dias":
                dias = 7;
                break;
            case "14 Dias":
                dias = 14;
                break;
            case "31 Dias":
                dias = 31;
                break;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, dias);
        return calendar.getTime();
    }

    public List<Boolean> verificaErros() {
        List<Boolean> ls = new LinkedList<>();
        ls.add(false);
        if (txtOrigem.getText().equals("") || txtOrigem.getText().length() == 0) {
            ls.add(true);
        }
        if (txtDestino.getText().equals("") || txtDestino.getText().length() == 0) {
            ls.add(true);
        }
        return ls;
    }

    public String gerarCodigoRastreio() {
        StringBuilder codigo = new StringBuilder("BR");
        for (int i = 0; i < 10; i++) {
            int val = (int) (Math.random() * 10);
            codigo.append(val);
        }
        return codigo.toString();
    }
}