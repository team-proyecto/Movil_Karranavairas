package com.example.covid.actividades;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.covid.MainActivity;
import com.example.covid.R;
import com.example.covid.entidades.EstadoEconomico;

import com.example.covid.entidades.ReporteEconomico;


import com.example.covid.entidades.UsuarioCasos;
import com.example.covid.entidades.global.ClaseGlobal;
import com.example.covid.servicios.ProyectoService;
import com.example.covid.util.ConnectionRest;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SituacionEconomica extends AppCompatActivity {

    private ProyectoService postService;
    private static final String TAG = "LogsAndroid";

    CheckBox chkBeneficiario;
    Button btnRegistrarse;
    TextView txtRecibo;
    EditText txtMonto;

    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_situacion_economica);

        chkBeneficiario = findViewById(R.id.chkBeneficiario);

        txtRecibo = findViewById(R.id.txtRecibo);
        txtMonto = findViewById(R.id.txtMonto);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(SituacionEconomica.this, R.id.txtMonto, "^[1-9]([0-9]{1,2})?([.][0-9]{1,2})?", R.string.error_bono);

        chkBeneficiario.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtMonto.setVisibility(View.VISIBLE);
                    txtRecibo.setVisibility(View.VISIBLE);
                } else {
                    txtMonto.setVisibility(View.INVISIBLE);
                    txtRecibo.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(awesomeValidation.validate()) {
                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();

                    Boolean beneficio = chkBeneficiario.isChecked();
                    double montoServicio = Double.parseDouble(txtMonto.getText().toString());

                    //Traer Id de UsuarioCaso de memoria
                    ClaseGlobal objLecturaGLobal = (ClaseGlobal) getApplicationContext();
                    //Long idUsuarioCaso = objLecturaGLobal.getId();
                    //Long idReporteEconomico = Long.valueOf(objLecturaGLobal.getReporteEconomico().getId());
                    Log.i(TAG, "id en la clase global : " + objLecturaGLobal.getId());



                    if(objLecturaGLobal.getReporteEconomico() == null){
                        Log.i(TAG, "No guardo reporte economico en la memoria");
                        Log.i(TAG, "No guardo reporte economico en la memoria" + objLecturaGLobal.getReporteEconomico());
                        //crear nuevo reporte economico
                        ReporteEconomico obj = new ReporteEconomico();
                        obj.setBonoAsignado(beneficio);
                        obj.setEstado(true);
                        obj.setMontoServicio(montoServicio);
                        EstadoEconomico estadoEconomico = new EstadoEconomico();
                        obj.setEstadoEconomico(estadoEconomico);
                        obj.getEstadoEconomico().setId(2);
                        if(beneficio && obj.getMontoServicio()<100){
                            obj.getEstadoEconomico().setId(1);
                        }

                        //Log.i(TAG, "UsuarioCaso a insertar en ReporteEconomico : " + obj.getUsuarioCasos().getId());
                        registrarReporteEconomico(obj);

                        //actualizar nuevo reporte economico en usaurio casos de memoria

                        Log.i(TAG, "Se actualizo ReporteEconomico en usuario caso de ClaseGlobal ");

                        Intent intent = new Intent(getApplicationContext(), Menu.class);
                        startActivity(intent);
                    }else{
                        Log.i(TAG, "Si guardo reporte economico en la memoria");
                        Log.i(TAG, "id de reporte Economico en la clase global : " + objLecturaGLobal.getReporteEconomico().getId());
                        //actualizar el reporte Economico

                        if(objLecturaGLobal.getReporteEconomico()!=null){
                            ReporteEconomico obj = new ReporteEconomico();
                            obj.setBonoAsignado(beneficio);
                            obj.setEstado(true);
                            obj.setMontoServicio(montoServicio);
                            Log.i(TAG, "MontoServicios: "+ obj.getMontoServicio());
                            EstadoEconomico estadoEconomico = new EstadoEconomico();
                            obj.setEstadoEconomico(estadoEconomico);
                            obj.getEstadoEconomico().setId(2);
                            if(beneficio==true && obj.getMontoServicio()<100.0){
                                Boolean valor = beneficio==true && obj.getMontoServicio()<100.0;
                                Log.i(TAG, "estadoEconomico: "+ valor);
                                obj.getEstadoEconomico().setId(1);
                            }

                            actualizarReporteEconomico(objLecturaGLobal.getReporteEconomico().getId(),obj);

                            Intent intent = new Intent(getApplicationContext(), Menu.class);
                            startActivity(intent);
                        }
                        else{
                            Intent intent = new Intent(getApplicationContext(), Menu.class);
                            startActivity(intent);
                        }



                    }



                    Intent intent = new Intent(getApplicationContext(), Menu.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"No Success", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //metodo para crear reporte economico
    private void registrarReporteEconomico(ReporteEconomico obj){
        Log.i(TAG, "PASO 0_crearreporteeconomico: " + obj);
        postService = ConnectionRest.getConnection().create(ProyectoService.class);
        Call<ReporteEconomico> call = postService.saveReporteEconomico(obj);
        Log.i(TAG, "PASO 0_crearreporteeconomico: " + call);
        call.enqueue(new Callback<ReporteEconomico>() {
            @Override
            public void onResponse(Call<ReporteEconomico> call, Response<ReporteEconomico> response) {
                Log.i(TAG, "PASO 0_crearreporteeconomico: " + response);
                if (!response.isSuccessful()){
                    Log.i(TAG, "Algo salio mal" + response.body());
                }else{

                    ReporteEconomico res = response.body();

                    Log.i(TAG, "Post Subido al API : " + response.body());
                    Log.i(TAG, "El ID del nuevo Reporte economico : " + res.getReporteEconomico().getId());

                    //llamar clase global y ponerle losdatos que trae el nuevo Reporte Economico
                    ClaseGlobal objGlobal = (ClaseGlobal) getApplicationContext();
                    objGlobal.setReporteEconomico(res.getReporteEconomico());

                    Log.i(TAG, "id-objGLobal-de usuariocaso: " + objGlobal.getId());

                    cargarUsuarioGlobal();


                    UsuarioCasos usuarioGlobal = objGlobal.getUsuarioCasos();
                    if(usuarioGlobal!=null){
                        Log.i(TAG, "onResponse: " + usuarioGlobal.getId() );
                        Log.i(TAG, "onResponse: " + usuarioGlobal.getNombre());
                        Log.i(TAG, "onResponse: " + usuarioGlobal.getApellido() );
                        Log.i(TAG, "onResponse: " + usuarioGlobal.getNumeroDocumento() );
                        Log.i(TAG, "onResponse: " + usuarioGlobal.getNacimiento() );
                        if(usuarioGlobal.getReporteEconomico()!=null) {
                            Log.i(TAG, "onResponse: " + usuarioGlobal.getReporteEconomico().getId());
                        }
                        Log.i(TAG, "onResponse: " + usuarioGlobal.getId() );
                        Log.i(TAG, "onResponse: " + usuarioGlobal.getId() );
                        Log.i(TAG, "onResponse: " + usuarioGlobal.getId() );
                        Log.i(TAG, "onResponse: " + usuarioGlobal.getId() );
                        Log.i(TAG, "onResponse: " + usuarioGlobal.getId() );
                        Log.i(TAG, "onResponse: " + usuarioGlobal.getId() );
                        Log.i(TAG, "onResponse: " + usuarioGlobal.getId() );

                    }

                    //problema con fecha
                    //objGlobal.getReporteEconomico().setFechaRegistro(null);


                    Log.i(TAG, "id-objGLobal-reporteEconomico: " + objGlobal.getReporteEconomico().getId());


                    actualizarUsuarioCasos(objGlobal.getId(),objGlobal.getUsuarioCasos());
                }
            }

            @Override
            public void onFailure(Call<ReporteEconomico> call, Throwable t) {
                Log.i(TAG, "Improbable subir POST al API");
                Toast.makeText(SituacionEconomica.this, "Registro Economico no Registrado (ver retorno)", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void actualizarUsuarioCasos(Long id, UsuarioCasos obj){

        //se actualiza los campos necesarios
        ClaseGlobal objGlobal = (ClaseGlobal) getApplicationContext();
        Log.i(TAG, "objeto brindado: " + obj.getNombre());
        Log.i(TAG, "objeto global reporte economico: " + objGlobal.getReporteEconomico().getId()+" "+objGlobal.getReporteEconomico().getMontoServicio());
        //Log.i(TAG, "objeto global reporte economico usuarioCaso : " + objGlobal.getUsuarioCasos().getNombre());
        //obj.setReporteEconomico(objGlobal.getReporteEconomico());
        //obj.setFechaRegistro(null);


        Log.i(TAG, "valor  de id de reporte Economico actualizado: " +  obj.getReporteEconomico().getId());
        Log.i(TAG, "valor  de id de usuariocaso a actualizar: " +  id);

        Log.i(TAG, "PASO 0_actualizarusuarioscasos: " + obj);
        postService = ConnectionRest.getConnection().create(ProyectoService.class);
        Call<UsuarioCasos> call =postService.updateUsuariosCasos(id,obj);
        Log.i(TAG, "PASO 0_actualizarusuarioscasos: " + call);
        call.enqueue(new Callback<UsuarioCasos>() {
            @Override
            public void onResponse(Call<UsuarioCasos> call, Response<UsuarioCasos> response) {
                Log.i(TAG, "PASO 0_actualizarusuarioscasos: " + response);
                Log.i(TAG, "PASO 1_actualizarusuarioscasos: " + response.body());
                Log.i(TAG, "PASO 1_actualizarusuarioscasos: " + response.body().getUsuarioCaso());
                Log.i(TAG, "PASO 1_actualizarusuarioscasos: " + response.body().getMensaje());
                if (!response.isSuccessful()){
                    Log.i(TAG, "Algo salio mal" + response.body().getMensaje());
                }else{
                    Log.i(TAG, "Put Subido al API : " + response.body().getMensaje());
                    Log.i(TAG, "Nuevo Usuario Caso : " + response.body().getUsuarioCaso());
                    Log.i(TAG, "Mensaje de Response : " + response.body().getMensaje());
                }
            }

            @Override
            public void onFailure(Call<UsuarioCasos> call, Throwable t) {
                Log.i(TAG, "Improbable subir PUT al API");
                Toast.makeText(SituacionEconomica.this, "UsuarioCasos Actualizado (ver retorno)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarReporteEconomico(Long id, ReporteEconomico obj){

        //se actualiza los campos necesarios
        ClaseGlobal objGlobal = (ClaseGlobal) getApplicationContext();
        obj.setReporteEconomico(obj);



        Log.i(TAG, "valor  de id de reporte Economico actualizado: " +  obj.getId());


        Log.i(TAG, "actualizarreporteeconomico: " + obj);
        postService = ConnectionRest.getConnection().create(ProyectoService.class);
        Call<ReporteEconomico> call =postService.updateReporteEconomico(id,obj);
        Log.i(TAG, "actualizarreporteeconomico: " + call);
        call.enqueue(new Callback<ReporteEconomico>() {
            @Override
            public void onResponse(Call<ReporteEconomico> call, Response<ReporteEconomico> response) {
                Log.i(TAG, "PASO actualizarreporteeconomico: " + response);
                Log.i(TAG, "PASO actualizarreporteeconomico: " + response.body());
                //Log.i(TAG, "PASO actualizarreporteeconomico: " + response.body().getReporteEconomico());
                //Log.i(TAG, "PASO actualizarreporteeconomico: " + response.body().getMensaje());
                if (!response.isSuccessful()){
                    Log.i(TAG, "Algo salio mal" + response.body().getMensaje());
                }else{
                    Log.i(TAG, "Put Subido al API : " + response.body().getMensaje());
                    Log.i(TAG, "Reporte actualizado : " + response.body().getReporteEconomico());
                    Log.i(TAG, "Mensaje de Response : " + response.body().getMensaje());
                }
            }

            @Override
            public void onFailure(Call<ReporteEconomico> call, Throwable t) {
                Log.i(TAG, "Improbable subir PUT al API");
                Toast.makeText(SituacionEconomica.this, "UsuarioCasos Actualizado (ver retorno)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarUsuarioGlobal(){
        ClaseGlobal obj = (ClaseGlobal) getApplicationContext();

        UsuarioCasos usuarioGlobal = new UsuarioCasos();


        usuarioGlobal.setId(obj.getId());
        usuarioGlobal.setNombre(obj.getNombre());
        usuarioGlobal.setApellido(obj.getApellido());
        usuarioGlobal.setNacionalidad(obj.getNacionalidad());
        usuarioGlobal.setTipoDocumento(obj.getTipoDocumento());
        usuarioGlobal.setNumeroDocumento(obj.getNumeroDocumento());
        usuarioGlobal.setNacimiento(obj.getNacimiento());
        usuarioGlobal.setDistrito(obj.getDistrito());
        usuarioGlobal.setTelefono(obj.getTelefono());
        usuarioGlobal.setDireccionDomicilio(obj.getDireccionDomicilio());
        usuarioGlobal.setCodigoConfirmacion(obj.getCodigoConfirmacion());
        usuarioGlobal.setCondicionUso(obj.getCondicionUso());
        usuarioGlobal.setFechaRegistro(obj.getFechaRegistro());
        usuarioGlobal.setGps(obj.getGps());
        usuarioGlobal.setTipoUsuario(obj.getTipoUsuario());
        usuarioGlobal.setReporteEconomico(obj.getReporteEconomico());
        usuarioGlobal.setReporteMedico(obj.getReporteMedico());
        //usuarioGlobal.setEstado(obj.getEstado());

        obj.setUsuarioCasos(usuarioGlobal);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        return false;
    }



}
