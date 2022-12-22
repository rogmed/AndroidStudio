package com.example.m08actividad02_2;


import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Controlador para extraer datos del XML
    XmlController xmlController = new XmlController();

    // Controlador para borrar, guardar y obtener datos de las monedas (currency) de SQLite
    DataController dataController = new DataController(this);

    String servidor = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";

    TextView tvConnectionStatus;
    TextView tvDate;
    ListView lvCurrencies;
    ArrayList<Currency> currencies = new ArrayList<>();
    CurrencyAdapter currencyAdapter = new CurrencyAdapter(currencies, this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvConnectionStatus = findViewById(R.id.tvConnectionStatus);
        tvConnectionStatus.setText("Cargando datos...");

        tvDate = findViewById(R.id.tvDate);
        tvDate.setText("Fecha...");

        lvCurrencies = findViewById(R.id.lvCurrencies);
        lvCurrencies.setAdapter(currencyAdapter);

        new sendGet().execute(servidor);
    }

    private class sendGet extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            String servidor = strings[0];
            HttpURLConnection conexion;
            String resultado;

            // En segundo plano se intentan descargar los datos en XML. En caso de no ser
            // posible se captura la excepción y se devuelve Null
            try{
                URL url = new URL(servidor);
                conexion = (HttpURLConnection) url.openConnection();
                conexion.setRequestMethod("GET");
                conexion.connect();

                resultado = buildResult(conexion);

            } catch (Exception e) {
                resultado = null;
            }

            return resultado;
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);

            // Si hay conexión, "resultado" no es Null y se enseñan los datos por pantalla,
            // ademas de limpiar la base de datos y actualizarla con los nuevos datos.
            // Si no hay conexión "resultado" es Null y se muestran por pantalla los datos
            // que haya guardados en la base de datos en la tabla "currency"
            if (resultado != null){
                tvConnectionStatus.setText("Conectado a servidor.");

                currencies = xmlController.getCurrencies(resultado);
                tvDate.setText("Actualizado en " + currencies.get(0).geDate());

                dataController.wipeCurrencyData();
                dataController.updateCurrencyData(currencies);
            } else {
                tvConnectionStatus.setText("Conexión fallada.");
                currencies = dataController.getCurrencyData();


                if(currencies.isEmpty()) {
                    tvDate.setText("No hay datos");
                } else {
                    tvDate.setText("Actualizado en " + currencies.get(0).geDate());
                }
            }

            currencyAdapter = new CurrencyAdapter(currencies, MainActivity.this);
            lvCurrencies.setAdapter(currencyAdapter);
        }

        private String buildResult(HttpURLConnection connection) throws IOException {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        }
    }
}
