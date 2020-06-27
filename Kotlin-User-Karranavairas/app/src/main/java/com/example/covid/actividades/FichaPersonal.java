package com.example.covid.actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.covid.entidades.Departamentos;
import com.example.covid.entidades.Distritos;
import com.example.covid.entidades.Gps;
import com.example.covid.entidades.Nacionalidad;
import com.example.covid.entidades.Provincias;
import com.example.covid.entidades.UsuarioCasos;
import com.example.covid.entidades.global.ClaseGlobal;
import com.example.covid.servicios.ProyectoService;
import com.example.covid.util.ConnectionRest;

import com.example.covid.R;
import com.example.covid.entidades.TipoDocumento;

import java.io.IOException;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class FichaPersonal extends AppCompatActivity implements View.OnClickListener {
    Spinner combo_tipoDocumento, combo_provincias, combo_distritos,combo_departamentos, combo_nacionalidad;
    AwesomeValidation awesomeValidation;
    //private AsyncHttpClient cliente;

    private ProyectoService postService;
    private static final String TAG = "LogsAndroid";
    Button btnRegistrarse;
    ArrayAdapter arrayAdapter;
    ArrayList<String> opcionesSpnView = new ArrayList<String>();
    List<TipoDocumento> documentos = new ArrayList<TipoDocumento>();
    private LocationManager ubicacion;
    TextView latitud, longitud;
    EditText txtID,txtNombres, txtApellidos,txtDNI,txtNacimiento,txtTelefono ,txtDireccion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_personal);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        final Date date = new Date(0);
        //cliente = new AsyncHttpClient();
        combo_tipoDocumento = (Spinner)findViewById(R.id.spnTipoDocumentos);
        combo_provincias = (Spinner)findViewById(R.id.spnProvincias);
        combo_distritos = (Spinner)findViewById(R.id.spnDistritos);
        combo_departamentos = (Spinner)findViewById(R.id.spnDepartamentos);
        txtNombres = (EditText)findViewById(R.id.txtNombres);
        txtApellidos = (EditText)findViewById(R.id.txtApellidos);
        combo_nacionalidad = (Spinner)findViewById(R.id.spnNacionalidad);
        txtDNI = (EditText)findViewById(R.id.txtDNI);
        txtNacimiento = (EditText)findViewById(R.id.txtNacimiento);
        //txtTelefono = (EditText)findViewById(R.id.txtTelefono);
        txtDireccion = (EditText) findViewById(R.id.txtDireccion);
        //para que no se edite
        txtNacimiento.setInputType(InputType.TYPE_NULL);
        awesomeValidation.addValidation (FichaPersonal.this, R.id.txtNombres, RegexTemplate.NOT_EMPTY, R.string.error_nombre);
        awesomeValidation.addValidation (FichaPersonal.this, R.id.txtApellidos, RegexTemplate.NOT_EMPTY, R.string.error_apellido);
        awesomeValidation.addValidation (FichaPersonal.this, R.id.txtDireccion, RegexTemplate.NOT_EMPTY, R.string.error_direccion);
        awesomeValidation.addValidation (FichaPersonal.this, R.id.txtNacimiento, RegexTemplate.NOT_EMPTY, R.string.error_fecha);
        awesomeValidation.addValidation (FichaPersonal.this, R.id.txtDNI, "[0-9]{8}$", R.string.error_dni);



        localizacion();
        //listaProviders();
        //mejorCriterio();
        //estadoGPS();
        registrarLocalizacion();
        cargaDocumentos();
        //cargaDistritos();
        cargaNacionalidad();
        //cargaProvincia((long) 1);
        cargaDepartamentos();




        combo_departamentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String itemText = (String) combo_departamentos.getSelectedItem();
                Log.i(TAG, "onItemSelecteddep: " + itemText);
                capturaDepartamento(itemText);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        combo_provincias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String itemText = (String) combo_provincias.getSelectedItem();
                Log.i(TAG, "onItemSelectedpro: " + itemText);
                capturaProvincia(itemText);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        combo_distritos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Seleccionar un Distrito
                String itemText = (String) combo_distritos.getSelectedItem();
                Log.i(TAG, "onItemSelecteddis: " + itemText);
                capturaDistrito(itemText);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnRegistrarse = findViewById(R.id.btnRegistarse);
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(awesomeValidation.validate()) {
                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();

                    ClaseGlobal objLecturaGlobal = (ClaseGlobal) getApplicationContext();
                    UsuarioCasos usuarioGlobal = objLecturaGlobal.getUsuarioCasos();

                    //nuevos valores a actualizar en usuariocaso
                    String nombre = txtNombres.getText().toString().trim();
                    String apellido = txtApellidos.getText().toString().trim();
                    Log.i(TAG, "apellido: " + apellido);
                    String dni = txtDNI.getText().toString().trim();


                    String nacionalidad = (String) combo_nacionalidad.getSelectedItem();
                    Log.i(TAG, "nacionalidad: " + nacionalidad);



                    String documento = (String) combo_tipoDocumento.getSelectedItem();
                    Log.i(TAG, "documento: " + documento);


                    Long distritos = Long.valueOf(0);
                    if(objLecturaGlobal.getDistrito()!=null){
                        distritos = objLecturaGlobal.getDistrito().getId();
                        Log.i(TAG, "idDistrito escogido: " + distritos);
                    }


                    String nacimiento = txtNacimiento.getText().toString().trim();
                    Log.i(TAG, "nacimiento: " + nacimiento);

                    String direccion = txtDireccion.getText().toString();

                    try {

                        usuarioGlobal.setNacimiento(nacimiento);
                        Log.i(TAG, "nacimientoGlobal: " + usuarioGlobal.getNacimiento());
                        usuarioGlobal.setNombre(nombre);
                        usuarioGlobal.setApellido(apellido);


                        if(objLecturaGlobal.getListaNacionalidad() != null) {
                            //conseguir id por nombre
                            List<Nacionalidad> com = objLecturaGlobal.getListaNacionalidad();
                            Log.i(TAG, "arreglonacionalidad : " + com);
                            for (int i = 0; i < com.size(); i++) {
                                Log.i(TAG, "arreglonacionalidadnombre : " + com.get(i).getNombreNacionalidad());
                                Log.i(TAG, "arreglonacionalidadid : " + com.get(i).getId());
                                if(com.get(i).getNombreNacionalidad().equals(nacionalidad)){
                                    Log.i(TAG, "nombrenacionalidad : " + com.get(i).getNombreNacionalidad());
                                    Nacionalidad objNacional = new Nacionalidad();
                                    objNacional.setId(com.get(i).getId());
                                    usuarioGlobal.setNacionalidad(objNacional);
                                    Log.i(TAG, "nacionalidadGlobal: " + usuarioGlobal.getNacionalidad().getId());
                                    break;
                                }
                            }
                        }


                        if(objLecturaGlobal.getListaDocumentos() != null) {
                            List<TipoDocumento> com = objLecturaGlobal.getListaDocumentos();
                            Log.i(TAG, "arreglodocumento : " + com);
                            for (int i = 0; i < com.size(); i++) {
                                Log.i(TAG, "arreglodocumentonombre : " + com.get(i).getNombreDocumento());
                                Log.i(TAG, "arreglodocumentoid : " + com.get(i).getId());
                                if(com.get(i).getNombreDocumento().equals(documento)){
                                    Log.i(TAG, "nombredocuemnto : " + com.get(i).getNombreDocumento());
                                    TipoDocumento objDocumento = new TipoDocumento();
                                    objDocumento.setId(com.get(i).getId());
                                    usuarioGlobal.setTipoDocumento(objDocumento);
                                    Log.i(TAG, "documentoGlobal: " + usuarioGlobal.getTipoDocumento().getId());
                                    break;
                                }
                            }
                        }

                        usuarioGlobal.setNumeroDocumento(dni);

                        if(!(objLecturaGlobal.getDistrito().getId()==null)) {
                            Distritos objDist = new Distritos();
                            if (!(distritos == 0)) {
                                objDist.setId(distritos);
                                usuarioGlobal.setDistrito(objDist);
                                Log.i(TAG, "distrito: " + usuarioGlobal.getDistrito().getId());
                            }
                        }

                        usuarioGlobal.setDireccionDomicilio(direccion);

                        actualizarUsuarioCasos(objLecturaGlobal.getId(),usuarioGlobal);

                        Intent intent = new Intent(getApplicationContext(), Menu.class);
                        startActivity(intent);


                    } catch (Exception e ){
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(getApplicationContext(), Menu.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(getApplicationContext(),"No Success", Toast.LENGTH_LONG).show();
                }


            }
        });
        //click para el datepicker
        txtNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(txtNacimiento);
            }
        });


}
        //para poner la fecha
        private void showDateDialog(final EditText txtNacimiento){
        final Calendar calendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    calendar.set(Calendar.YEAR,year);
                    calendar.set(Calendar.MONTH,month);
                    calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    txtNacimiento.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        new DatePickerDialog(FichaPersonal.this, dateSetListener,calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();




    }

    //alerta de confirmacion al elegir registrar
    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmación");
        builder.setMessage("¿Está segur(a) que está todo correcto?");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
            }
        });

        builder.setNegativeButton(android.R.string.cancel, null);
        Dialog dialog = builder.create();
        dialog.show();
    }

    //no se sabe que hace
    /*private void listaProviders() {
        ubicacion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> listProvider = ubicacion.getAllProviders();//listaProvider: size = 3

        String mejorProvider = ubicacion.getBestProvider(mejorCriterio(), false);
        System.out.println(mejorProvider);

        LocationProvider provider = ubicacion.getProvider(listProvider.get(0));
        System.out.println(provider.getAccuracy()); //provider: LocationProvider@4568
        System.out.println(provider.getPowerRequirement());
        System.out.println(provider.supportsAltitude());
    }

    private Criteria mejorCriterio() {
        Criteria req = new Criteria();
        req.setAccuracy(Criteria.ACCURACY_FINE);
        req.setAltitudeRequired(true);
        return req;
    }


     //para saber si el gps esta prendido
    private boolean estadoGPS() {
        ubicacion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!ubicacion.isProviderEnabled(LocationManager.GPS_PROVIDER))
            Log.d("GPS", "NO ACTIVADO");
        else {
            Log.d("GPS", "ACTIVADO");
        }
        return true;
    }*/

    //lista de tipoDocumentos
   public void cargaDocumentos(){
        //Se obtiene la solicitud REST
        ProyectoService postService = ConnectionRest.getConnection().create(ProyectoService.class);
        Call<List<TipoDocumento>> call = postService.getTipoDocumentos();
        call.enqueue(new Callback<List<TipoDocumento>>() {
            @Override
            public void onResponse(Call<List<TipoDocumento>> call, Response<List<TipoDocumento>> response) {
                ArrayList<TipoDocumento> lista=new ArrayList<TipoDocumento>();
                if(response.isSuccessful()){
                    try {
                        final List<TipoDocumento> com = response.body();
                        for (int i = 0; i < com.size(); i++) {
                            TipoDocumento reg=new TipoDocumento();
                            reg.setId(com.get(i).getId());
                            reg.setNombreDocumento(com.get(i).getNombreDocumento());
                            lista.add(reg);
                        }
                        //guardar response en clase global
                        ClaseGlobal objLectura = (ClaseGlobal) getApplicationContext();
                        objLectura.setListaDocumentos(lista);

                        String[] result = TextUtils.join(",",lista).split(",");
                        Spinner combo_documentos=(Spinner) findViewById(R.id.spnTipoDocumentos);
                        combo_documentos.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,result));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else
                {
                    Log.i("Base","El metodo ha fallado" + response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<List<TipoDocumento>> call, Throwable t) {

            }
        });

    }

    //lista de Nacionalidades
    public void cargaNacionalidad(){
        //Se obtiene la solicitud REST
        ProyectoService postService = ConnectionRest.getConnection().create(ProyectoService.class);
        Call<List<Nacionalidad>> call = postService.getNacionalidades();
        call.enqueue(new Callback<List<Nacionalidad>>() {
            @Override
            public void onResponse(Call<List<Nacionalidad>> call, Response<List<Nacionalidad>> response) {
                ArrayList<Nacionalidad> lista=new ArrayList<Nacionalidad>();
                if(response.isSuccessful()){
                    try {
                        final List<Nacionalidad> com = response.body();
                        for (int i = 0; i < com.size(); i++) {
                            Nacionalidad reg=new Nacionalidad();
                            reg.setId(com.get(i).getId());
                            reg.setNombreNacionalidad(com.get(i).getNombreNacionalidad());
                            lista.add(reg);
                        }
                        //guardar response en clase global
                        ClaseGlobal objLectura = (ClaseGlobal) getApplicationContext();
                        objLectura.setListaNacionalidad(lista);

                        String[] result = TextUtils.join(",",lista).split(",");
                        Spinner combo_nacionalidades=(Spinner) findViewById(R.id.spnNacionalidad);
                        combo_nacionalidades.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,result));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else
                {
                    Log.i("Base","El metodo ha fallado" + response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<List<Nacionalidad>> call, Throwable t) {

            }
        });
    }


    //lista de departamentos
    public void cargaDepartamentos(){
        //Se obtiene la solicitud REST
        ProyectoService postService = ConnectionRest.getConnection().create(ProyectoService.class);
        Call<List<Departamentos>> call = postService.getDepartamentos();
        Log.i(TAG, "PASO 1_departamentos");
        call.enqueue(new Callback<List<Departamentos>>() {
            @Override
            public void onResponse(Call<List<Departamentos>> call, Response<List<Departamentos>> response) {
                ArrayList<Departamentos> lista=new ArrayList<Departamentos>();
                Log.i(TAG, "PASO 2_departamentos");
                if(response.isSuccessful()){
                    Log.i(TAG, "PASO 3_departamentos");
                    try {
                        Log.i(TAG, "PASO 4_departamentos");
                        final List<Departamentos> com = response.body();
                        Log.i(TAG, "PASO 4.1_departamentos" + com);
                        for (int i = 0; i < com.size(); i++) {
                            Departamentos reg=new Departamentos();
                            //cargaProvincia(com.get(i).getId());
                            reg.setId(com.get(i).getId());
                            reg.setNombreDepartamento(com.get(i).getNombreDepartamento());
                            lista.add(reg);
                            Log.i(TAG, "PASO 5_departamentos" + reg);
                        }

                        String[] result = TextUtils.join(",",lista).split(",");
                        Spinner combo_departamentos=(Spinner) findViewById(R.id.spnDepartamentos);
                        combo_departamentos.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,result));
                        Log.i(TAG, "PASO 6_departamentos");

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else
                {
                    Log.i("Base","El metodo ha fallado" + response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<List<Departamentos>> call, Throwable t) {

            }
        });
    }

    //cargar provincias a partir de departamento
    public void cargaProvincia(Long idDepartamento){
        //Se obtiene la solicitud REST
        ProyectoService postService = ConnectionRest.getConnection().create(ProyectoService.class);
        Log.i(TAG, "valor de spinner departamentos" + idDepartamento);
        Call<Departamentos> call = postService.getProvincias((long) (idDepartamento));
        Log.i(TAG, "PASO 1_provincia" + call);
        call.enqueue(new Callback<Departamentos>() {
            @Override
            public void onResponse(Call<Departamentos> call, Response<Departamentos> response) {
                ArrayList<Provincias> lista=new ArrayList<Provincias>();
                Log.i(TAG, "PASO 2_provincia "+ call);
                Log.i(TAG, "PASO 2_provincia "+ response);
                Log.i(TAG, "PASO 2_provincia "+ response.body());
                if(response.isSuccessful()){
                    Log.i(TAG, "PASO 3_provincia");
                    try {
                        Log.i(TAG, "PASO 4_provincia");
                        final Departamentos com = response.body(); // Esto es json completo
                        Log.i(TAG, "encuentrameeeeee" + com);
                        for( Provincias item: com.getProvincias() ) {

                            Provincias reg = new Provincias();
                            reg.setId(item.getId());
                            reg.setNombreProvincia(item.getNombreProvincia());
                            lista.add(reg);
                            Log.i(TAG, "PASO 5_provincia" + reg);
                        }
                        String[] result = TextUtils.join(",",lista).split(",");
                        Spinner combo_provincias=(Spinner) findViewById(R.id.spnProvincias);
                        combo_provincias.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,result));
                        Log.i(TAG, "PASO 6_provincia");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else
                {
                    Log.i("Base","El metodo ha fallado" + response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<Departamentos> call, Throwable t) {

            }
        });
    }

    //cargar distritos a partir de provincias
    public void cargaDistritos(Long idProvincia){
        //Se obtiene la solicitud REST
        ProyectoService postService = ConnectionRest.getConnection().create(ProyectoService.class);
        Call<Provincias> call = postService.getDistritos((long) (idProvincia));
        Log.i(TAG, "cargaDistritos: " + call);
        call.enqueue(new Callback<Provincias>() {
            @Override
            public void onResponse(Call<Provincias> call, Response<Provincias> response) {
                ArrayList<Distritos> lista=new ArrayList<Distritos>();
                if(response.isSuccessful()){
                    try {
                        final Provincias com = response.body(); // Esto es json completo
                        for( Distritos item: com.getDistritos() ) {
                            Distritos reg = new Distritos();
                            reg.setId(item.getId());
                            reg.setNombreDistrito(item.getNombreDistrito());
                            lista.add(reg);
                        }
                        String[] result = TextUtils.join(",",lista).split(",");
                        Spinner combo_distritos=(Spinner) findViewById(R.id.spnDistritos);
                        combo_distritos.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,result));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else
                {
                    Log.i("Base","El metodo ha fallado" + response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<Provincias> call, Throwable t) {

            }
        });
    }

    //traer por nombre Departamento, Provincia, Distrito
    //cargar distritos a partir de provincias
    public void capturaDepartamento(String nombreDepartamento){
        ProyectoService postService = ConnectionRest.getConnection().create(ProyectoService.class);
        Call<Departamentos> call = postService.getDepartamento(nombreDepartamento);
        Log.i(TAG, "cargaDepartamento: " + call);
        call.enqueue(new Callback<Departamentos>() {
            @Override
            public void onResponse(Call<Departamentos> call, Response<Departamentos> response) {
                if(response.isSuccessful()){
                    try {
                        final Departamentos com = response.body(); // Esto es json completo
                        ClaseGlobal objLectura = (ClaseGlobal) getApplicationContext();
                        objLectura.setDepartamentos(com);

                        cargaProvincia(objLectura.getDepartamentos().getId());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else
                {
                    Log.i("Base","El metodo ha fallado" + response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<Departamentos> call, Throwable t) {
            }
        });
    }
    public void capturaProvincia(String nombreProvincia){
        ProyectoService postService = ConnectionRest.getConnection().create(ProyectoService.class);
        Call<Provincias> call = postService.getProvincia(nombreProvincia);
        Log.i(TAG, "cargaProvincia: " + call);
        call.enqueue(new Callback<Provincias>() {
            @Override
            public void onResponse(Call<Provincias> call, Response<Provincias> response) {
                if(response.isSuccessful()){
                    try {
                        final Provincias com = response.body(); // Esto es json completo
                        ClaseGlobal objLectura = (ClaseGlobal) getApplicationContext();
                        objLectura.setProvincias(com);

                        cargaDistritos(objLectura.getProvincias().getId());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else
                {
                    Log.i("Base","El metodo ha fallado" + response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<Provincias> call, Throwable t) {
            }
        });
    }
    public void capturaDistrito(String nombreDistrito){
        ProyectoService postService = ConnectionRest.getConnection().create(ProyectoService.class);
        Call<Distritos> call = postService.getDistrito(nombreDistrito);
        Log.i(TAG, "cargaDistrito: " + call);
        call.enqueue(new Callback<Distritos>() {
            @Override
            public void onResponse(Call<Distritos> call, Response<Distritos> response) {
                if(response.isSuccessful()){
                    try {
                        final Distritos com = response.body(); // Esto es json completo
                        ClaseGlobal objLectura = (ClaseGlobal) getApplicationContext();
                        objLectura.setDistrito(com);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else
                {
                    Log.i("Base","El metodo ha fallado" + response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<Distritos> call, Throwable t) {
            }
        });
    }




    //captacion de lat, long y direccion- creacion de Objeto gps y permiso para uso gps
    private void localizacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1000);
        }

        ubicacion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location loc = ubicacion.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (ubicacion != null) {
            Log.d("Latitud", String.valueOf(loc.getLatitude()));
            Log.d("Longitud", String.valueOf(loc.getLongitude()));
        }
    }
    private void registrarLocalizacion() {
        ubicacion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        ubicacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1, new miLocalizacionListener());
    }

    private class miLocalizacionListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            double lat = location.getLatitude();
            double lon = location.getLongitude();

            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> direccion1 = null;
            try {
                direccion1 = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            } catch (IOException e) {
                e.printStackTrace();
            }

            //latitud.setText(lat);
            //longitud.setText(lon);

            Gps gps = new Gps();
            gps.setLatitud((long) lat);
            gps.setLongitud((long) lon);
            gps.setDireccionGps(direccion1.get(0).getAddressLine(0));
            gps.setEstado(true);

            registrarGps(gps);


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

    }

    //metodo para crear reporte medico
    private void registrarGps(Gps obj){
        Log.i(TAG, "PASO 0_creargps: " + obj);
        postService = ConnectionRest.getConnection().create(ProyectoService.class);
        Call<Gps> call = postService.saveGps(obj);
        Log.i(TAG, "PASO 0_creargps: " + call);
        call.enqueue(new Callback<Gps>() {
            @Override
            public void onResponse(Call<Gps> call, Response<Gps> response) {
                Log.i(TAG, "PASO 0_creargps: " + response);
                if (!response.isSuccessful()){
                    Log.i(TAG, "Algo salio mal" + response.body());
                }else{

                    Gps res = response.body();

                    Log.i(TAG, "Post Subido al API : " + response.body().getMensaje());
                    Log.i(TAG, "El ID del nuevo objeto GPS : " + res.getGps().getId());

                    //llamar clase global y ponerle losdatos que trae el nuevo Objeto Gps
                    ClaseGlobal objGlobal = (ClaseGlobal) getApplicationContext();
                    objGlobal.getUsuarioCasos().setGps(res.getGps());
                    //problema con fecha
                    //objGlobal.getGps().setFechaRegistro(null);


                    Log.i(TAG, "id-objGLobal-gps: " + objGlobal.getUsuarioCasos().getGps().getId());

                    actualizarUsuarioCasos(objGlobal.getId(),objGlobal.getUsuarioCasos());
                }
            }

            @Override
            public void onFailure(Call<Gps> call, Throwable t) {
                Log.i(TAG, "Improbable subir POST al API");
                Toast.makeText(FichaPersonal.this, "Gps no Registrado (ver retorno)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarUsuarioCasos(Long id, UsuarioCasos obj){

        Log.i(TAG, "valor  de id de usuariocaso a actualizar: " +  id);

        Log.i(TAG, "PASO 0_actualizarusuarioscasos: " + obj);
        postService = ConnectionRest.getConnection().create(ProyectoService.class);
        Call<UsuarioCasos> call =postService.updateUsuariosCasos(id,obj);
        Log.i(TAG, "PASO 0_actualizarusuarioscasos: " + call);
        call.enqueue(new Callback<UsuarioCasos>() {
            @Override
            public void onResponse(Call<UsuarioCasos> call, Response<UsuarioCasos> response) {
                Log.i(TAG, "PASO 0_actualizarusuarioscasos: " + response);
                if (!response.isSuccessful()){
                    Log.i(TAG, "Algo salio mal : " + response.body());
                }else{
                    Log.i(TAG, "Put Subido al API : " + response.body().getMensaje());
                    Log.i(TAG, "Nuevo Usuario Caso : " + response.body().getUsuarioCaso());
                    Log.i(TAG, "Mensaje de Response : " + response.body().getMensaje());
                }
            }

            @Override
            public void onFailure(Call<UsuarioCasos> call, Throwable t) {
                Log.i(TAG, "Improbable subir PUT al API");
                Toast.makeText(FichaPersonal.this, "UsuarioCasos Actualizado (ver retorno)", Toast.LENGTH_SHORT).show();
            }
        });
    }




}
