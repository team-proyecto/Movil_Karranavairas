package com.example.covid.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.covid.R;
import com.example.covid.entidades.global.ClaseGlobal;

public class Informacion extends AppCompatActivity {

    private static final String TAG = "LogsAndroid";
    TextView txtNombre,txtApellidos,txtTipoDocumento, txtDNI, txtFechaNacimiento,
            txtTelefono, txtDireccion, txtNacionalidad,txtReporteEconomico,txtReporteMedico;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);
        txtNombre = findViewById(R.id.txtNombres);
        txtApellidos = findViewById(R.id.txtApellidos);
        //txtTipoDocumento = findViewById(R.id.txtTipoDocumento);
        txtDNI = findViewById(R.id.txtDNI);
        txtFechaNacimiento = findViewById(R.id.txtFechaNacimiento);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtDireccion = findViewById(R.id.txtDireccion);
        //txtNacionalidad = findViewById(R.id.txtNacionalidad);
        //txtReporteMedico = findViewById(R.id.txtReporteMedico);

        ClaseGlobal reg = (ClaseGlobal) getApplicationContext();


            Log.i(TAG, "nombre en memoria: " + reg.getNombre());
            //Log.i(TAG, "nombre en objeto memoria: " + reg.getUsuarioCasos().getNombre());
            txtNombre.setText("" + reg.getNombre());
            txtApellidos.setText("" + reg.getApellido());

        /*if(reg.getTipoDocumento()!=null) {
            txtTipoDocumento.setText("" + reg.getTipoDocumento().getNombreDocumento());
        }*/
            txtDNI.setText("" + reg.getNumeroDocumento());

            txtFechaNacimiento.setText("" + reg.getNacimiento());

            txtTelefono.setText("" + reg.getTelefono());
            txtDireccion.setText("" + reg.getDireccionDomicilio());

        /*if(reg.getNacionalidad()!=null) {
            txtNacionalidad.setText("" + reg.getNacionalidad().getNombreNacionalidad());
        }
        if(reg.getReporteMedico()!=null) {
            txtReporteMedico.setText("" + reg.getReporteMedico().getEstadoMedico().getNombreMedico());
        }*/
    }
}