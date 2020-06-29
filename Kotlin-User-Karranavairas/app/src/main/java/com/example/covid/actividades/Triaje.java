package com.example.covid.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.covid.MainActivity;
import com.example.covid.R;
import com.example.covid.entidades.Distritos;
import com.example.covid.entidades.EstadoMedico;
import com.example.covid.entidades.Gps;
import com.example.covid.entidades.Nacionalidad;
import com.example.covid.entidades.ReporteEconomico;
import com.example.covid.entidades.ReporteMedico;
import com.example.covid.entidades.TipoDocumento;
import com.example.covid.entidades.TipoUsuario;
import com.example.covid.entidades.UsuarioCasos;
import com.example.covid.entidades.global.ClaseGlobal;
import com.example.covid.entidades.response.ResponseReporteMedico;
import com.example.covid.servicios.ProyectoService;
import com.example.covid.util.ConnectionRest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Triaje extends AppCompatActivity  {

    private ProyectoService postService;
    private static final String TAG = "LogsAndroid";
    RadioButton rb1,rb2,rb3,rb4,rb5;
    CheckBox chkSintomas;
    Button btnEvaluacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_triaje);


        chkSintomas=findViewById(R.id.chkSintomas);
        btnEvaluacion = findViewById(R.id.btnEvaluacion);


        btnEvaluacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean sintomas = chkSintomas.isChecked();

                //Traer Id de UsuarioCaso de memoria
                ClaseGlobal objLecturaGLobal = (ClaseGlobal) getApplicationContext();
                //Long idUsuarioCaso = objLecturaGLobal.getId();
                //Long idReporteMedico = Long.valueOf(objLecturaGLobal.getReporteMedico().getId());
                Log.i(TAG, "id en la clase global : " + objLecturaGLobal.getId());


                if(objLecturaGLobal.getReporteMedico() == null){
                    Log.i(TAG, "No guardo reporte medico en la memoria");
                    Log.i(TAG, "No guardo reporte medico en la memoria" + objLecturaGLobal.getReporteEconomico());
                    //crear nuevo reporte medico
                    ReporteMedico obj = new ReporteMedico();
                    obj.setResultadoTriaje(sintomas);
                    obj.setEstado(true);
                    EstadoMedico estadoMedico = new EstadoMedico();
                    obj.setEstadoMedico(estadoMedico);
                    obj.getEstadoMedico().setId(1);
                    if((sintomas)){
                        obj.getEstadoMedico().setId(2);
                    }

                    //Log.i(TAG, "UsuarioCaso a insertar en ReporteMedico : " + obj.getUsuarioCasos().getId());
                    registrarReporteMedico(obj);

                    //actualizar nuevo reporte medico en usaurio casos de memoria

                    Log.i(TAG, "Se actualizo ReporteMedico en usuario caso de ClaseGlobal ");

                    Intent intent = new Intent(getApplicationContext(), FichaPersonal.class);
                    startActivity(intent);
                }else{
                    Log.i(TAG, "Si guardo reporte medico en la memoria");
                    Log.i(TAG, "resultado triaje : " + objLecturaGLobal.getReporteMedico().getResultadoTriaje());

                    //actualizar el reporte Medico

                    if(objLecturaGLobal.getReporteMedico()!=null){
                        ReporteMedico obj = new ReporteMedico();
                        obj.setId(objLecturaGLobal.getReporteMedico().getId());
                        obj.setResultadoTriaje(sintomas);
                        EstadoMedico estadoMedico = new EstadoMedico();
                        obj.setEstadoMedico(estadoMedico);
                        obj.getEstadoMedico().setId(1);
                        if((sintomas)){
                            obj.getEstadoMedico().setId(2);
                        }
                        actualizarReporteMedico(objLecturaGLobal.getReporteMedico().getId(),obj);

                        Intent intent = new Intent(getApplicationContext(), Menu.class);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(getApplicationContext(), Menu.class);
                        startActivity(intent);
                    }



                }

            }
        });


    }

    public void mensaje(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.mensaje);
        builder.setMessage(R.string.mensaje2);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), Menu.class );
                startActivity(intent);
            }
        });

        builder.setNegativeButton(android.R.string.cancel,null);
        Dialog dialog = builder.create();
        dialog.show();
    }

    //metodo para crear reporte medico
    private void registrarReporteMedico(ReporteMedico obj){
        Log.i(TAG, "PASO 0_crearreportemedico: " + obj);
        postService = ConnectionRest.getConnection().create(ProyectoService.class);
        Call<ReporteMedico> call = postService.saveReporteMedico(obj);
        Log.i(TAG, "PASO 0_crearreportemedico: " + call);
        call.enqueue(new Callback<ReporteMedico>() {
            @Override
            public void onResponse(Call<ReporteMedico> call, Response<ReporteMedico> response) {
                Log.i(TAG, "PASO 0_crearreportemedico: " + response);
                if (!response.isSuccessful()){
                    Log.i(TAG, "Algo salio mal" + response.body());
                }else{

                    ReporteMedico res = response.body();

                    Log.i(TAG, "Post Subido al API : " + response.body().getMensaje());
                    Log.i(TAG, "El ID del nuevo Reporte medico : " + res.getReporteMedico().getId());

                    //llamar clase global y ponerle losdatos que trae el nuevo Reporte Medico
                    ClaseGlobal objGlobal = (ClaseGlobal) getApplicationContext();
                    objGlobal.setReporteMedico(res.getReporteMedico());

                    //problema con fecha
                    //objGlobal.getReporteMedico().setFechaRegistro(null);


                    Log.i(TAG, "id-objGLobal-reporteMedico: " + objGlobal.getReporteMedico().getId());

                    actualizarUsuarioCasos(objGlobal.getId(),objGlobal.getUsuarioCasos());
                }
            }

            @Override
            public void onFailure(Call<ReporteMedico> call, Throwable t) {
                Log.i(TAG, "Improbable subir POST al API");
                Toast.makeText(Triaje.this, "Registro Medico no Registrado (ver retorno)", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void actualizarUsuarioCasos(Long id, UsuarioCasos obj){

        //se actualiza los campos necesarios
        ClaseGlobal objGlobal = (ClaseGlobal) getApplicationContext();
        obj.setReporteMedico(objGlobal.getReporteMedico());
        obj.setFechaRegistro(null);


        Log.i(TAG, "valor  de id de reporte Médico actualizado: " +  obj.getReporteMedico().getId());
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
                Toast.makeText(Triaje.this, "UsuarioCasos Actualizado (ver retorno)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarReporteMedico(Long id, ReporteMedico obj){

        //se actualiza los campos necesarios en la clase global
        ClaseGlobal objGlobal = (ClaseGlobal) getApplicationContext();
        objGlobal.setReporteMedico(obj);
        //objGlobal.getUsuarioCasos().setReporteMedico(obj);


        Log.i(TAG, "valor  de id de reporte Médico actualizado: " +  obj.getId());


        Log.i(TAG, "actualizarreportemedico: " + obj);
        postService = ConnectionRest.getConnection().create(ProyectoService.class);
        Call<ReporteMedico> call =postService.updateReporteMedico(id,obj);
        Log.i(TAG, "actualizarreportemedico: " + call);
        call.enqueue(new Callback<ReporteMedico>() {
            @Override
            public void onResponse(Call<ReporteMedico> call, Response<ReporteMedico> response) {
                Log.i(TAG, "PASO actualizarreportemedico: " + response);
                Log.i(TAG, "PASO actualizarreportemedico: " + response.body());
                Log.i(TAG, "PASO actualizarreportemedico: " + response.body().getReporteMedico());
                Log.i(TAG, "PASO actualizarreportemedico: " + response.body().getMensaje());
                if (!response.isSuccessful()){
                    Log.i(TAG, "Algo salio mal" + response.body().getMensaje());
                }else{
                    Log.i(TAG, "Put Subido al API : " + response.body().getMensaje());
                    Log.i(TAG, "Reporte actualizado : " + response.body().getReporteMedico());
                    Log.i(TAG, "Mensaje de Response : " + response.body().getMensaje());
                }
            }

            @Override
            public void onFailure(Call<ReporteMedico> call, Throwable t) {
                Log.i(TAG, "Improbable subir PUT al API");
                Toast.makeText(Triaje.this, "UsuarioCasos Actualizado (ver retorno)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarUsuarioGlobal(){
        ClaseGlobal obj = (ClaseGlobal) getApplicationContext();
        UsuarioCasos usuarioGlobal =  obj.getUsuarioCasos();

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
        usuarioGlobal.setEstado(obj.getEstado());
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
/*
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb1:
                if(rb1.isSelected()){
                    rb1.setSelected(false);
                    rb1.setChecked(false);
            } else {
                    rb1.setSelected(true);
                    rb1.setChecked(true);

            }
            break;

            case R.id.rb2:
                if(rb2.isSelected()){
                    rb2.setSelected(false);
                    rb2.setChecked(false);
                } else {
                    rb2.setSelected(true);
                    rb2.setChecked(true);

                }
                break;

            case R.id.rb3:
                if(rb3.isSelected()){
                    rb3.setSelected(false);
                    rb3.setChecked(false);
                } else {
                    rb3.setSelected(true);
                    rb3.setChecked(true);

                }
                break;
            case R.id.rb4:
                if(rb4.isSelected()){
                    rb4.setSelected(false);
                    rb4.setChecked(false);
                } else {
                    rb4.setSelected(true);
                    rb4.setChecked(true);

                }
                break;

            case R.id.rb5:
                if(rb5.isSelected()){
                    rb5.setSelected(false);
                    rb5.setChecked(false);
                } else {
                    rb5.setSelected(true);
                    rb5.setChecked(true);

                }
                break;

        }
    }
}*/
