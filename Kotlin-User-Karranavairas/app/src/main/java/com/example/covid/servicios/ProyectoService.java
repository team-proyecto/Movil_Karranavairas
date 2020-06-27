package com.example.covid.servicios;

import com.example.covid.entidades.Gps;
import com.example.covid.entidades.Nacionalidad;
import com.example.covid.entidades.Provincias;
import com.example.covid.entidades.ReporteEconomico;
import com.example.covid.entidades.ReporteMedico;
import com.example.covid.entidades.UsuarioCasos;
import com.example.covid.entidades.Departamentos;
import com.example.covid.entidades.Distritos;
import com.example.covid.entidades.TipoDocumento;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProyectoService {

    @GET("usuarioscasos/documentos")

    Call<List<TipoDocumento>> getTipoDocumentos();

    //llenar combo departamentos
    @GET("usuarioscasos/departamentos")
    Call<List<Departamentos>> getDepartamentos();

    //llenar provincias desde 1 departamento
    @GET("usuarioscasos/departamentos/{id}")
    Call<Departamentos> getProvincias(@Path(value="id") Long id);

    //llenar distritos desde 1 provincia
    @GET("usuarioscasos/provincias/{id}")
    Call<Provincias> getDistritos(@Path(value="id") Long id);

    //traer departamento desde nombre
    @GET("usuarioscasos/departamentos/nombre/{nombreDepartamento}")
    Call<Departamentos> getDepartamento(@Path(value="nombreDepartamento") String nombreDepartamento);

    //traer provincia desde nombre
    @GET("usuarioscasos/provincias/nombre/{nombreProvincia}")
    Call<Provincias> getProvincia(@Path(value="nombreProvincia") String nombreProvincia);

    //traer distrito desde nombre
    @GET("usuarioscasos/distritos/nombre/{nombreDistrito}")
    Call<Distritos> getDistrito(@Path(value="nombreDistrito") String nombreDistrito);


    @GET("usuarioscasos/nacionalidad")
    Call<List<Nacionalidad>> getNacionalidades();

    @POST("usuarioscasos")
    Call<UsuarioCasos> saveUsuariosCasos(@Body UsuarioCasos obj);

    @PUT("usuarioscasos/{id}")
    Call<UsuarioCasos> updateUsuariosCasos(@Path(value="id") Long id, @Body UsuarioCasos obj);

    @GET("usuarioscasos/{id}")
    Call<UsuarioCasos> obtenerUsuariosCaso(@Path(value="id") Long id);

    @GET("usuarioscasos/telefono/{telefono}")
    Call<UsuarioCasos> obtenerUsuariosCasoPorTelefono(@Path(value="telefono") String telefono);

    @POST("rmedicos")
    Call<ReporteMedico> saveReporteMedico(@Body ReporteMedico obj);

    @PUT("rmedicos/{id}")
    Call<ReporteMedico> updateReporteMedico(@Path(value="id") Long id, @Body ReporteMedico obj);

    @POST("reconomicos")
    Call<ReporteEconomico> saveReporteEconomico(@Body ReporteEconomico obj);

    @PUT("reconomicos/{id}")
    Call<ReporteEconomico> updateReporteEconomico(@Path(value="id") Long id, @Body ReporteEconomico obj);

    @POST("gps")
    Call<Gps> saveGps(@Body Gps obj);



}
