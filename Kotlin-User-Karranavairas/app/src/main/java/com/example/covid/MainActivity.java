package com.example.covid;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import com.example.covid.actividades.Verificacion;
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

public class MainActivity extends AppCompatActivity{

    private ProyectoService postService;
    private static final String TAG = "LogsAndroid";
    AwesomeValidation awesomeValidation;
    EditText txtNumero;
    Button btnEnviar;
    CheckBox chkAceptar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNumero = findViewById(R.id.txtNumero);
        btnEnviar = findViewById(R.id.btnEnviar);
        chkAceptar = findViewById(R.id.chkAceptar);



        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);


        awesomeValidation.addValidation (MainActivity.this, R.id.txtNumero, "[0-9]{9}$", R.string.error_telefono);



        chkAceptar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    btnEnviar.setVisibility(View.VISIBLE);
                }else{
                    btnEnviar.setVisibility(View.INVISIBLE);
                }

            }
        });

        //permiso para enviar mensaje
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest
                .permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {Manifest.permission.SEND_SMS,}, 1000);
        }else{

        };

        final ClaseGlobal objGlobal = (ClaseGlobal) getApplicationContext();
        Log.i(TAG, "data en claseglobal: " + objGlobal);
        Log.i(TAG, "data en claseglobal: " + objGlobal.getId());

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()) {



                    String telefono = txtNumero.getText().toString().trim();
                    Boolean condicion = chkAceptar.isChecked();


                    try {

                        if(objGlobal != null){
                            Log.i(TAG, "Habia info en memoria");
                            obtenerUsuariosCasoPorTelefono(telefono, condicion);

                        }else{
                            Log.i(TAG, "No Habia info en memoria");
                            UsuarioCasos obj = new UsuarioCasos();
                            obj.setTelefono(telefono);
                            obj.setCondicionUso(condicion);
                            registrarUsuarioCasos(obj);

                            Intent intent = new Intent(getApplicationContext(), Verificacion.class);
                            startActivity(intent);

                            enviarMensaje("923001670", "Tú código de verificación es: 1234");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Ingrese su número", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void enviarMensaje(String numero, String mensaje){
            try {
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(numero,null,mensaje,null,null);
                Toast.makeText(getApplicationContext(),"Tu código de verificación se ha enviado",Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),"Mensaje no enviado ",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
    }

    private void registrarUsuarioCasos(UsuarioCasos obj){
        Log.i(TAG, "PASO 0_crearusuarioscasos: " + obj);
        postService = ConnectionRest.getConnection().create(ProyectoService.class);
        Call<UsuarioCasos> call =postService.saveUsuariosCasos(obj);
        Log.i(TAG, "PASO 1_crearusuarioscasos: " + call);
        call.enqueue(new Callback<UsuarioCasos>() {
            @Override
            public void onResponse(Call<UsuarioCasos> call, Response<UsuarioCasos> response) {
                Log.i(TAG, "PASO 2_crearusuarioscasos: " + response);
                if (!response.isSuccessful()){
                    Log.i(TAG, "Algo salio mal : " + response.body());
                }else{

                    UsuarioCasos res = response.body();
                    //ResponseRest res = response.body();

                    Log.i(TAG, "Post Subido al API : " + response.body().getMensaje());
                    Log.i(TAG, "crearusuarioscasos: " + response.body().getUsuarioCaso().getId());

                    //llamar clase global y ponerle losdatos que trae el response
                    ClaseGlobal objGlobal = (ClaseGlobal) getApplicationContext();
                        objGlobal.setId(res.getUsuarioCaso().getId());
                        objGlobal.setTelefono(res.getUsuarioCaso().getTelefono());
                        objGlobal.setCondicionUso(res.getUsuarioCaso().getCondicionUso());
                        objGlobal.setUsuarioCasos(res.getUsuarioCaso());

                        Log.i(TAG, "id-objGLobal: " + objGlobal.getId());
                        Log.i(TAG, "telefono-objGLobal: " + objGlobal.getTelefono());
                        Log.i(TAG, "condicionUso-objGLobal: " + objGlobal.getCondicionUso());

                }
            }

            @Override
            public void onFailure(Call<UsuarioCasos> call, Throwable t) {
                Log.i(TAG, "Improbable subir POST al API");
                Toast.makeText(MainActivity.this, "UsuarioCasos Registrado (ver retorno)", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void obtenerUsuariosCasoPorTelefono(final String telefono, final Boolean condicion) {

        postService = ConnectionRest.getConnection().create(ProyectoService.class);
        Call<UsuarioCasos> call =postService.obtenerUsuariosCasoPorTelefono(telefono);

        call.enqueue(new Callback<UsuarioCasos>() {
            @Override
            public void onResponse(Call<UsuarioCasos> call, Response<UsuarioCasos> response) {
                Log.i(TAG, "Response : " + response.body());
                if (response.isSuccessful()) {
                    try {
                        Log.i(TAG, "Entro a buscar: ");

                        UsuarioCasos com = response.body(); // Esto es json completo
                        Log.i(TAG, "Response : " + response.body());

                        ClaseGlobal reg = (ClaseGlobal) getApplicationContext();

                        reg.setId(com.getId());
                        Log.i(TAG, "id-objGLobal: " + reg.getId());
                        reg.setNombre(com.getNombre());
                        Log.i(TAG, "get Nombre: " + reg.getNombre());
                        reg.setApellido(com.getApellido());
                        Log.i(TAG, "get Apellidos: " + reg.getApellido());
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
                        reg.setNacimiento(com.getNacimiento());

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
                        reg.setUsuarioCasos(com.getUsuarioCaso());

                        enviarMensaje("923001670", "Tú código de verificación es: 1234");

                        Intent intent = new Intent(getApplicationContext(), Verificacion.class);
                        startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    Log.i(TAG, "Entro a crear: ");

                    Log.i(TAG, "Limpiando Clase Global... ");
                    limpiarClaseGlobal();



                    Log.i("Base", "No existe el usuario y se registrara" + response.errorBody());
                    ClaseGlobal regc = (ClaseGlobal) getApplicationContext();

                    Log.i(TAG, "va a registrar: " + regc.getId());

                    UsuarioCasos obj = new UsuarioCasos();
                    obj.setTelefono(telefono);
                    obj.setCondicionUso(condicion);
                    registrarUsuarioCasos(obj);
                    Log.i(TAG, "Id nuevo creado : " + obj.getId());


                    Intent intent = new Intent(getApplicationContext(), Verificacion.class);
                    startActivity(intent);

                    enviarMensaje("923001670", "Tú código de verificación es: 1234");
                }

            }

            @Override
            public void onFailure(Call<UsuarioCasos> call, Throwable t) {

            }

        });


    }

    /*@Override
    public void onBackPressed() {
        //moveTaskToBack(false);
    }*/

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }*/


    public void limpiarClaseGlobal(){
        ClaseGlobal objGlobalLimpio = (ClaseGlobal) getApplicationContext();

        objGlobalLimpio.setId(null);
        objGlobalLimpio.setNombre(null);
        objGlobalLimpio.setApellido(null);
        objGlobalLimpio.setNacionalidad(null);
        objGlobalLimpio.setTipoDocumento(null);
        objGlobalLimpio.setNumeroDocumento(null);
        objGlobalLimpio.setNacimiento(null);
        objGlobalLimpio.setDistrito(null);
        objGlobalLimpio.setTelefono(null);
        objGlobalLimpio.setDireccionDomicilio(null);
        objGlobalLimpio.setCodigoConfirmacion(null);
        objGlobalLimpio.setCondicionUso(null);
        objGlobalLimpio.setFechaRegistro(null);
        objGlobalLimpio.setGps(null);
        objGlobalLimpio.setTipoUsuario(null);
        objGlobalLimpio.setReporteEconomico(null);
        objGlobalLimpio.setReporteMedico(null);
        objGlobalLimpio.setEstado(null);
        objGlobalLimpio.setUsuarioCasos(null);

    }

}
