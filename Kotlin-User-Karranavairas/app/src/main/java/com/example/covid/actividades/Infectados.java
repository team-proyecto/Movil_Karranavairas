package com.example.covid.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.covid.R;
import com.example.covid.entidades.Departamentos;
import com.example.covid.entidades.Distritos;
import com.example.covid.entidades.Provincias;
import com.example.covid.entidades.UsuarioCasos;
import com.example.covid.entidades.global.ClaseGlobal;
import com.example.covid.servicios.ProyectoService;
import com.example.covid.util.ConnectionRest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Infectados extends AppCompatActivity {
    Spinner combo_provincias, combo_distritos,combo_departamentos;
    TextView txtInfectados, txtRecuperados, txtFallecidos, txtNecesitados;

    private ProyectoService postService;
    private static final String TAG = "LogsAndroid";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infectados);

        combo_provincias = (Spinner)findViewById(R.id.spnProvincias);
        combo_distritos = (Spinner)findViewById(R.id.spnDistritos);
        combo_departamentos = (Spinner)findViewById(R.id.spnDepartamentos);

        txtInfectados = (TextView)findViewById(R.id.totalInfectados);
        txtRecuperados = (TextView)findViewById(R.id.totalRecuperados);
        txtFallecidos = (TextView)findViewById(R.id.totalFallecidos);
        txtNecesitados = (TextView)findViewById(R.id.totalNecesitados);

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
                    List<UsuarioCasos> lista;
                    try {

                        //final TextView label = (Label) findViewById(R.id.label_id);

                        final Distritos com = response.body(); // Esto es json completo
                        ClaseGlobal objLectura = (ClaseGlobal) getApplicationContext();
                        objLectura.setDistrito(com);

                        int countInfectados=0;
                        int countNormal=0;
                        int countRecuperados=0;
                        int countFallecidos=0;
                        int countNecesitados=0;
                        int countEstable=0;

                        for (int i = 0 ; i < com.getUsuarioCasos().size() ; i++){

                            String estadoMedico = "";
                            if(com.getUsuarioCasos().get(i).getReporteMedico()!=null) {
                                estadoMedico = com.getUsuarioCasos().get(i).getReporteMedico().getEstadoMedico().getNombreMedico();
                                Log.i(TAG, "nombreEstadoMedico: " + estadoMedico);
                            }

                            String estadoEconomico = "";
                            if(com.getUsuarioCasos().get(i).getReporteEconomico()!=null) {
                                estadoEconomico = com.getUsuarioCasos().get(i).getReporteEconomico().getEstadoEconomico().getNombreEconomico();
                                Log.i(TAG, "nombreEstadoEconomico: " + estadoEconomico);
                            }

                            if(estadoMedico.equals("en riesgo")){
                                Log.i(TAG, "uno a riesgo");
                                countInfectados++;
                            }else if(estadoMedico.equals("recuperado")){
                                Log.i(TAG, "uno a recuperado");
                                countRecuperados++;
                            }else if(estadoMedico.equals("fallecido")){
                                Log.i(TAG, "uno a fallecido");
                                countFallecidos++;
                            }else{
                                Log.i(TAG, "uno a normal");
                                countNormal++;
                            }

                            if(estadoEconomico.equals("Necesitado")){
                                Log.i(TAG, "uno a Necesitado");
                                countNecesitados++;
                            }else{
                                Log.i(TAG, "uno a Estable");
                                countEstable++;
                            }
                        }

                        Log.i(TAG, "countInfectados: " + countInfectados);
                        Log.i(TAG, "countRecuperados: " + countRecuperados);
                        Log.i(TAG, "countFallecidos: " + countFallecidos);
                        Log.i(TAG, "countNecesitados: " + countNecesitados);

                        txtInfectados.setText(""+countInfectados);
                        txtRecuperados.setText(""+countRecuperados);
                        txtFallecidos.setText(""+countFallecidos);
                        txtNecesitados.setText(""+countNecesitados);

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





}
