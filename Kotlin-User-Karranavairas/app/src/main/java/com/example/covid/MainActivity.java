package com.example.covid;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
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
import com.example.covid.actividades.Menu;
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
import com.example.covid.entidades.response.ResponseRest;
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

        final ClaseGlobal objGlobal = (ClaseGlobal) getApplicationContext();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(txtNumero,"[1-9]{9}","Solo 9 dígitos");

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

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()) {
                    enviarMensaje("923001670", "Tú código de verificación es: 1234");


                    String telefono = txtNumero.getText().toString().trim();
                    Boolean condicion = chkAceptar.isChecked();

                /*SimpleDateFormat fecha= new SimpleDateFormat("yyyy-MM-dd");
                String sFecha = fecha.format(nacimiento);
                Date dat=new Date();*/
                    try {
                        // dat = fecha.parse(sFecha);
                        obtenerUsuariosCasoPorTelefono(telefono);

                        if((objGlobal.getId()==null)){
                            UsuarioCasos obj = new UsuarioCasos();
                            obj.setTelefono(telefono);
                            obj.setCondicionUso(condicion);
                            registrarUsuarioCasos(obj);
                            Log.i(TAG, "onClick: " + obj.getId());

                            Intent intent = new Intent(getApplicationContext(), Verificacion.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(getApplicationContext(), Menu.class);
                            startActivity(intent);
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
                    Log.i(TAG, "Algo salio mal" + response.body().toString());
                }else{

                    UsuarioCasos res = response.body();
                    //ResponseRest res = response.body();

                    Log.i(TAG, "Post Subido al API : " + response.body().getMensaje());
                    Log.i(TAG, "crearusuarioscasos: " + response.body().getUsuarioCaso().getId());

                    //llamar clase global y ponerle losdatos que trae el response
                    ClaseGlobal objGlobal = (ClaseGlobal) getApplicationContext();
                        objGlobal.setId(Long.valueOf(res.getUsuarioCaso().getId()));
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


    public void obtenerUsuariosCasoPorTelefono(String telefono) {

        ProyectoService postService = ConnectionRest.getConnection().create(ProyectoService.class);

        Call<UsuarioCasos> call = postService.obtenerUsuariosCasoPorTelefono(telefono);

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


                        //UsuarioCasos reg = new UsuarioCasos();

                        ClaseGlobal reg = (ClaseGlobal) getApplicationContext();
                       /* objGlobal.setId(Long.valueOf(res.getUsuarioCaso().getId()));
                        objGlobal.setTelefono(res.getUsuarioCaso().getTelefono());
                        objGlobal.setCondicionUso(res.getUsuarioCaso().getCondicionUso());

                        Log.i(TAG, "id-objGLobal: " + objGlobal.getId());
                        Log.i(TAG, "telefono-objGLobal: " + objGlobal.getTelefono());
                        Log.i(TAG, "condicionUso-objGLobal: " + objGlobal.getCondicionUso());*/

                        reg.setId(Long.valueOf(com.getId()));
                        reg.setNombre(com.getNombre());
                        reg.setApellidos(com.getApellidos());
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
                        if(!(com.getDistritos() == null)) {
                            dist.setId(com.getDistritos().getId());
                            reg.setDistritos(dist);
                            Log.i(TAG, "getDistritos: " + reg.getDistritos().getId());
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

}
