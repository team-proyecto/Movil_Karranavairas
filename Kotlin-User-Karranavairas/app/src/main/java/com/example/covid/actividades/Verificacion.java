package com.example.covid.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.covid.R;
import com.example.covid.entidades.Distritos;
import com.example.covid.entidades.Gps;
import com.example.covid.entidades.Nacionalidad;
import com.example.covid.entidades.ReporteEconomico;
import com.example.covid.entidades.ReporteMedico;
import com.example.covid.entidades.TipoDocumento;
import com.example.covid.entidades.TipoUsuario;
import com.example.covid.entidades.UsuarioCasos;
import com.example.covid.entidades.global.ClaseGlobal;
import com.example.covid.servicios.ProyectoService;
import com.example.covid.util.ConnectionRest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Verificacion extends AppCompatActivity {

    private ProyectoService postService;
    private static final String TAG = "LogsAndroid";
    AwesomeValidation awesomeValidation;
    EditText txtCodigo;
    Button btnContinuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificacion);

        btnContinuar = findViewById(R.id.btnContinuar);
        txtCodigo = findViewById(R.id.txtCodigo);


        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(txtCodigo,"[1-9]{4}","Solo 4 dígitos");

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()) {


                    ClaseGlobal objLecturaGLobal = (ClaseGlobal) getApplicationContext();
                    Long id = objLecturaGLobal.getId();
                    Log.i(TAG, "id en la clase global : " + id);


                    /*SimpleDateFormat fecha= new SimpleDateFormat("yyyy-MM-dd");
                String sFecha = fecha.format(nacimiento);
                Date dat=new Date();*/
                    try {
                        if(objLecturaGLobal.getTipoDocumento()==null){
                            updateUsuariosCaso(id);

                            Intent intent = new Intent(getApplicationContext(), Triaje.class);
                            startActivity(intent);
                        }else{
                            updateUsuariosCaso(id);

                            Intent intent = new Intent(getApplicationContext(), Menu.class);
                            startActivity(intent);
                        }




                    } catch (Exception e ){
                        e.printStackTrace();
                    }



                }else{
                    Toast.makeText(getApplicationContext(),"Codígo Inválido",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    public void updateUsuariosCaso(Long id) {

        ProyectoService postService = ConnectionRest.getConnection().create(ProyectoService.class);

        Call<UsuarioCasos> call = postService.obtenerUsuariosCaso(id);

        call.enqueue(new Callback<UsuarioCasos>() {
            @Override
            public void onResponse(Call<UsuarioCasos> call, Response<UsuarioCasos> response) {
                //UsuarioCasos usuarioOriginal;
                if (response.isSuccessful()) {
                    /*SimpleDateFormat fecha= new SimpleDateFormat("yyyy-MM-dd");
                    String sFecha = fecha.format(fecha);
                    Date dat=new Date();*/
                    try {

                        final UsuarioCasos com = response.body(); // Esto es json completo


                        UsuarioCasos reg = new UsuarioCasos();

                        reg.setId(com.getId());
                        reg.setNombre(com.getNombre());
                        reg.setApellido(com.getApellido());
                        Nacionalidad nacional = new Nacionalidad();
                        if(!(com.getNacionalidad() == null)){
                            nacional.setId(com.getNacionalidad().getId());
                            reg.setNacionalidad(nacional);
                            Log.i(TAG, "getNacionalidad: " + reg.getNacionalidad().getId());
                        }
                        TipoDocumento documento = new TipoDocumento();
                        if(!(com.getTipoDocumento() == null)) {
                            documento.setId(com.getTipoDocumento().getId());
                            reg.setTipoDocumento(documento);
                            Log.i(TAG, "getTipoDocumento: " + reg.getTipoDocumento().getId());
                        }
                        reg.setNumeroDocumento(com.getNumeroDocumento());
                        //reg.setFechaNacimiento(com.getFechaNacimiento());
                        //Log.i(TAG, "fechanacimiento: " + dat);
                        Distritos dist = new Distritos();
                        if(!(com.getDistrito() == null)) {
                            dist.setId(com.getDistrito().getId());
                            reg.setDistrito(dist);
                            Log.i(TAG, "getDistritos: " + reg.getDistrito().getId());
                        }
                        reg.setTelefono(com.getTelefono());
                        reg.setDireccionDomicilio(com.getDireccionDomicilio());
                        reg.setCodigoConfirmacion(com.getCodigoConfirmacion());
                        reg.setCondicionUso(com.getCondicionUso());
                        //reg.setFechaRegistro(com.getFechaRegistro());
                        //Log.i(TAG, "fecharegistro: " + dat;
                        Gps gps = new Gps();
                        if(!(com.getGps() == null)) {
                            gps.setId(com.getGps().getId());
                            reg.setGps(gps);
                            Log.i(TAG, "getGps: " + reg.getGps().getId());
                        }
                        TipoUsuario tipoUsuario = new TipoUsuario();
                        if(!(com.getTipoUsuario() == null)) {
                            tipoUsuario.setId(com.getTipoUsuario().getId());
                            reg.setTipoUsuario(tipoUsuario);
                            Log.i(TAG, "getTipoUsuario: " + reg.getTipoUsuario().getId());
                        }
                        ReporteEconomico reporteEconomico = new ReporteEconomico();
                        if(!(com.getReporteEconomico() == null)) {
                            reporteEconomico.setId(com.getReporteEconomico().getId());
                            reg.setReporteEconomico(reporteEconomico);
                            Log.i(TAG, "getReporteEconomico: " + reg.getReporteEconomico().getId());
                        }
                        ReporteMedico reporteMedico = new ReporteMedico();
                        if(!(com.getReporteMedico() == null)) {
                            reporteMedico.setId(com.getReporteMedico().getId());
                            reg.setReporteMedico(reporteMedico);
                            Log.i(TAG, "getReporteMedico: " + reg.getReporteMedico().getId());
                        }
                        reg.setEstado(com.getEstado());

                        //logs
                        /*Log.i(TAG, "getId: " + reg.getId());
                        Log.i(TAG, "getNombre: " + reg.getNombre());
                        Log.i(TAG, "getApellidos: " + reg.getApellidos());
                        Log.i(TAG, "getNumeroDocumento: " + reg.getNumeroDocumento());
                        Log.i(TAG, "getFechaNacimiento: " + reg.getFechaNacimiento());
                        Log.i(TAG, "getTelefono: " + reg.getTelefono());
                        Log.i(TAG, "getDireccionDomicilio: " + reg.getDireccionDomicilio());
                        Log.i(TAG, "getCodigoConfirmacion: " + reg.getCodigoConfirmacion());
                        Log.i(TAG, "getCondicionUso: " + reg.getCondicionUso());
                        Log.i(TAG, "getFechaRegistro: " + reg.getFechaRegistro());
                        Log.i(TAG, "getEstado: " + reg.getEstado());

                        Log.i(TAG, "idpara actualizar: " + Long.valueOf(reg.getId()));*/

                        actualizarUsuarioCasos(Long.valueOf(reg.getId()),reg);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.i("Base", "El metodo ha fallado" + response.errorBody());
                }

            }

            @Override
            public void onFailure(Call<UsuarioCasos> call, Throwable t) {

            }

        });


    }


    private void actualizarUsuarioCasos(Long id, UsuarioCasos obj){

        //se actualiza los campos necesarios
        int codigoConfirmacion = Integer.parseInt(txtCodigo.getText().toString().trim());
        obj.setCodigoConfirmacion(codigoConfirmacion);

        ClaseGlobal objGLobal = (ClaseGlobal) getApplicationContext();
        UsuarioCasos usuarioGLobal = objGLobal.getUsuarioCasos();
        usuarioGLobal.setCodigoConfirmacion(codigoConfirmacion);
        objGLobal.setCodigoConfirmacion(codigoConfirmacion);

        Log.i(TAG, "valor  de codigo confirmación actualizado: " +  obj.getCodigoConfirmacion());
        Log.i(TAG, "valor  de codigo confirmación actualizado: " +  obj.getId());

        Log.i(TAG, "PASO 0_actualizarusuarioscasos: " + obj);
        postService = ConnectionRest.getConnection().create(ProyectoService.class);
        Call<UsuarioCasos> call =postService.updateUsuariosCasos(id,obj);
        Log.i(TAG, "PASO 0_actualizarusuarioscasos: " + call);
        call.enqueue(new Callback<UsuarioCasos>() {
            @Override
            public void onResponse(Call<UsuarioCasos> call, Response<UsuarioCasos> response) {
                Log.i(TAG, "PASO 0_actualizarusuarioscasos: " + response);
                Log.i(TAG, "PASO 1_actualizarusuarioscasos: " + response.body());
                if (!response.isSuccessful()){
                    Log.i(TAG, "Algo salio mal" + response.body().toString());
                }else{
                    Log.i(TAG, "Put Subido al API" + response.body().toString());
                    Log.i(TAG, "PASO 3.0_actualizarusuarioscasos: " + response);
                    Log.i(TAG, "PASO 3.0_actualizarusuarioscasos: " + response.body().getMensaje());
                }
            }

            @Override
            public void onFailure(Call<UsuarioCasos> call, Throwable t) {
                Log.i(TAG, "Improbable subir PUT al API");
                Toast.makeText(Verificacion.this, "UsuarioCasos Actualizado (ver retorno)", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
