package com.example.covid.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.covid.R;
import com.example.covid.entidades.global.ClaseGlobal;

public class Informacion extends AppCompatActivity {
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

        txtNombre.setText(""+reg.getUsuarioCasos().getNombre());
        txtApellidos.setText(""+reg.getUsuarioCasos().getApellido());

        /*if(reg.getTipoDocumento()!=null) {
            txtTipoDocumento.setText("" + reg.getTipoDocumento().getNombreDocumento());
        }*/
        txtDNI.setText(""+reg.getUsuarioCasos().getNumeroDocumento());

        txtFechaNacimiento.setText("" + reg.getUsuarioCasos().getNacimiento());

        txtTelefono.setText(""+reg.getUsuarioCasos().getTelefono());
        txtDireccion.setText(""+reg.getUsuarioCasos().getDireccionDomicilio());

        /*if(reg.getNacionalidad()!=null) {
            txtNacionalidad.setText("" + reg.getNacionalidad().getNombreNacionalidad());
        }
        if(reg.getReporteMedico()!=null) {
            txtReporteMedico.setText("" + reg.getReporteMedico().getEstadoMedico().getNombreMedico());
        }*/
    }
}