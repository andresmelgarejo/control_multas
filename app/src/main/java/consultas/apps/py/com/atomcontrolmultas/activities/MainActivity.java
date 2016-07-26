package consultas.apps.py.com.atomcontrolmultas.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import consultas.apps.py.com.atomcontrolmultas.R;
import consultas.apps.py.com.atomcontrolmultas.utilities.JSONParser;
import consultas.apps.py.com.atomcontrolmultas.utilities.Messages;

public class MainActivity extends Activity implements View.OnClickListener{

    Messages msg = new Messages();
    RadioGroup radioGroup;
    EditText etBuscar;
    ImageButton imgBtnBuscar;
    ListView listMultas;
    ListAdapter adapter;

    String codigo_conductor, nombre_conductor, cedula_conductor, direccion_conductor, ciudad_conductor, codigo_multa, concepto_multa, importe_multa;
    int success = 0;
    int busqueda = 0;

//    configurar dir de la pc a 192.168.1.1 para el servidorr

    private TextView txtCiConductor, txtNombreConductor, txtDirConductor, txtCiudadConductor;
    private String parametro = "";
    private static String urlBusqCi = "http://localhost:80/scm/getMultasByCI.php";
    private static String urlBusqRua = "http://127.0.0.1:80/scm/getMultasByRua.php";

    JSONParser jsonParser;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_multas = "multas";
    JSONArray jsonArrayMultas;
    private ArrayList<HashMap<String, String>> arrayListMultas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        msg.alertMessage(this, getResources().getString(R.string.msg_instructivo));

        jsonParser = new JSONParser();
        etBuscar = (EditText)findViewById(R.id.et_buscar);
        etBuscar.setOnClickListener(this);
        imgBtnBuscar = (ImageButton)findViewById(R.id.btnBuscar);
        imgBtnBuscar.setOnClickListener(this);
        txtCiConductor = (TextView)findViewById(R.id.txtCiConductor);
        txtNombreConductor = (TextView)findViewById(R.id.txtNombreConductor);
        txtDirConductor = (TextView)findViewById(R.id.txtDirConductor);
        txtCiudadConductor = (TextView)findViewById(R.id.txtCiudadConductor);
        listMultas = (ListView)findViewById(R.id.listMultas);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radio_ci:
                        limpiarCampos();
                        etBuscar.setText("");
                        etBuscar.setHint(getResources().getString(R.string.hint_ci));
                        etBuscar.setInputType(InputType.TYPE_CLASS_NUMBER);
                        busqueda = 0;
                        break;
                    case R.id.radio_rua:
                        limpiarCampos();
                        etBuscar.setText("");
                        etBuscar.setHint(getResources().getString(R.string.hint_rua));
                        etBuscar.setInputType(InputType.TYPE_CLASS_TEXT);
                        busqueda = 1;
                        break;
                    default:
                        break;
                }
            }
        });

        arrayListMultas = new ArrayList<HashMap<String, String>>();
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.et_buscar){
            etBuscar.setSelectAllOnFocus(true);
            limpiarCampos();
        }

        if(v.getId() == R.id.btnBuscar){
            parametro = etBuscar.getText().toString().toUpperCase();
            if(!parametro.equals("")){
                if(busqueda == 0){
                    new LoadMultas().execute();
                    arrayListMultas.clear();
                }else if (busqueda == 1){
                    new LoadMultasRua().execute();
                    arrayListMultas.clear();
                }
            }else{
                if(busqueda == 0){
                    msg.messageToastCenter(this, "Ingrese número de Cédula");
                }else if(busqueda == 1){
                    msg.messageToastCenter(this, "Ingrese número de matrícula");
                }
            }
        }
    }

    void limpiarCampos()
    {
        txtCiConductor.setText("");
        txtCiudadConductor.setText("");
        txtDirConductor.setText("");
        txtNombreConductor.setText("");

        // esta parte es para limpiar la grilla
        arrayListMultas.clear();
        listMultas.setAdapter(adapter);
    }

    class LoadMultas extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("param", parametro));
            try{
                JSONObject json = jsonParser.makeHttpRequest(urlBusqCi, "GET", params);
                success = json.getInt(TAG_SUCCESS);
                if(success == 1){
                    jsonArrayMultas = json.getJSONArray(TAG_multas);
                    for(int i = 0; i < jsonArrayMultas.length(); i++){
                        JSONObject datos = jsonArrayMultas.getJSONObject(i);

                        codigo_conductor = datos.getString("codConductor");
                        nombre_conductor = datos.getString("nombreConductor");
                        cedula_conductor = datos.getString("cedulaConductor");
                        direccion_conductor = datos.getString("direccionConductor");
                        ciudad_conductor = datos.getString("ciudadConductor");
                        codigo_multa = datos.getString("codMulta");
                        concepto_multa = datos.getString("conceptoMulta");
                        importe_multa = datos.getString("importeMulta");

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("conceptoMulta", concepto_multa);
                        map.put("importeMulta", importe_multa);

                        arrayListMultas.add(map);
                    }
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            txtCiConductor.setText(cedula_conductor);
            txtNombreConductor.setText(nombre_conductor);
            txtDirConductor.setText(direccion_conductor + " - ");
            txtCiudadConductor.setText(ciudad_conductor);

            if(success == 0){
                msg.alertMessage(MainActivity.this, "Registro no encontrado!");

                txtCiConductor.setText("");
                txtNombreConductor.setText("NINGUN DATO ENCONTRADO");
                txtDirConductor.setText("");
                txtCiudadConductor.setText("");
                arrayListMultas.clear();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(MainActivity.this,
                                                            arrayListMultas,
                                                            R.layout.row_multas,
                                                            new String[]{"conceptoMulta", "importeMulta"},
                                                            new int[]{R.id.txtConceptoMulta, R.id.txtImporteMulta});
                    listMultas.setAdapter(adapter);
                }
            });
        }
    }

    class LoadMultasRua extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("param", parametro));
            try{
                JSONObject json = jsonParser.makeHttpRequest(urlBusqRua, "GET", params);
                success = json.getInt(TAG_SUCCESS);
                if(success == 1){
                    jsonArrayMultas = json.getJSONArray(TAG_multas);
                    Log.i("JSON: ", jsonArrayMultas.toString());
                    for(int i = 0; i < jsonArrayMultas.length(); i++){
                        JSONObject datos = jsonArrayMultas.getJSONObject(i);

                        codigo_conductor = datos.getString("codConductor");
                        nombre_conductor = datos.getString("nombreConductor");
                        cedula_conductor = datos.getString("cedulaConductor");
                        direccion_conductor = datos.getString("direccionConductor");
                        ciudad_conductor = datos.getString("ciudadConductor");
                        codigo_multa = datos.getString("codMulta");
                        concepto_multa = datos.getString("conceptoMulta");
                        importe_multa = datos.getString("importeMulta");

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("conceptoMulta", concepto_multa);
                        map.put("importeMulta", importe_multa);

                        arrayListMultas.add(map);
                    }
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            txtCiConductor.setText(cedula_conductor);
            txtNombreConductor.setText(nombre_conductor);
            txtDirConductor.setText(direccion_conductor + " - ");
            txtCiudadConductor.setText(ciudad_conductor);

            if(success == 0){
                msg.alertMessage(MainActivity.this, "Matrícula de vehículo no posee multas pendientes!");

                txtCiConductor.setText("");
                txtNombreConductor.setText("NINGUN DATO ENCONTRADO");
                txtDirConductor.setText("");
                txtCiudadConductor.setText("");
                arrayListMultas.clear();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter = new SimpleAdapter(MainActivity.this,
                            arrayListMultas,
                            R.layout.row_multas,
                            new String[]{"conceptoMulta", "importeMulta"},
                            new int[]{R.id.txtConceptoMulta, R.id.txtImporteMulta});
                    listMultas.setAdapter(adapter);

                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            // aqui no hace nada, lo deja para el boton SALIR
            exitApp();
        }
        return false;
    }
    public void exitApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("¿Salir de la aplicación?")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create();
        builder.show();
    }
}
