package com.example.covid.entidades.global;

import android.app.Application;

import com.example.covid.entidades.Departamentos;
import com.example.covid.entidades.Distritos;
import com.example.covid.entidades.Gps;
import com.example.covid.entidades.Nacionalidad;
import com.example.covid.entidades.Provincias;
import com.example.covid.entidades.ReporteEconomico;
import com.example.covid.entidades.ReporteMedico;
import com.example.covid.entidades.TipoDocumento;
import com.example.covid.entidades.TipoUsuario;
import com.example.covid.entidades.UsuarioCasos;

import java.util.Date;
import java.util.List;


public class ClaseGlobal extends Application {

    private Long id;
    private String nombre;
    private String apellido;
    private Nacionalidad nacionalidad;
    private TipoDocumento tipoDocumento;
    private String numeroDocumento;
    private String nacimiento;
    private Distritos distrito;
    private String telefono;
    private String direccionDomicilio;
    private Integer codigoConfirmacion;
    private Boolean condicionUso;
    private Date fechaRegistro;
    private Gps gps;
    private TipoUsuario tipoUsuario;
    private ReporteEconomico reporteEconomico;
    private ReporteMedico reporteMedico;
    private Boolean estado;

    private UsuarioCasos usuarioCasos;

    private Departamentos departamentos;
    private Provincias provincias;

    private List<TipoDocumento> listaDocumentos;
    private List<Nacionalidad> listaNacionalidad;


    public List<TipoDocumento> getListaDocumentos() {
        return listaDocumentos;
    }

    public void setListaDocumentos(List<TipoDocumento> listaDocumentos) {
        this.listaDocumentos = listaDocumentos;
    }

    public List<Nacionalidad> getListaNacionalidad() {
        return listaNacionalidad;
    }

    public void setListaNacionalidad(List<Nacionalidad> listaNacionalidad) {
        this.listaNacionalidad = listaNacionalidad;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Departamentos getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(Departamentos departamentos) {
        this.departamentos = departamentos;
    }

    public Provincias getProvincias() {
        return provincias;
    }

    public void setProvincias(Provincias provincias) {
        this.provincias = provincias;
    }

    public UsuarioCasos getUsuarioCasos() {
        return usuarioCasos;
    }

    public void setUsuarioCasos(UsuarioCasos usuarioCasos) {
        this.usuarioCasos = usuarioCasos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }



    public Nacionalidad getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(Nacionalidad nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getNacimiento() {
        return nacimiento;
    }

    public void setNacimiento(String nacimiento) {
        this.nacimiento = nacimiento;
    }

    public Distritos getDistrito() {
        return distrito;
    }

    public void setDistrito(Distritos distrito) {
        this.distrito = distrito;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccionDomicilio() {
        return direccionDomicilio;
    }

    public void setDireccionDomicilio(String direccionDomicilio) {
        this.direccionDomicilio = direccionDomicilio;
    }

    public Integer getCodigoConfirmacion() {
        return codigoConfirmacion;
    }

    public void setCodigoConfirmacion(Integer codigoConfirmacion) {
        this.codigoConfirmacion = codigoConfirmacion;
    }

    public Boolean getCondicionUso() {
        return condicionUso;
    }

    public void setCondicionUso(Boolean condicionUso) {
        this.condicionUso = condicionUso;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Gps getGps() {
        return gps;
    }

    public void setGps(Gps gps) {
        this.gps = gps;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public ReporteEconomico getReporteEconomico() {
        return reporteEconomico;
    }

    public void setReporteEconomico(ReporteEconomico reporteEconomico) {
        this.reporteEconomico = reporteEconomico;
    }

    public ReporteMedico getReporteMedico() {
        return reporteMedico;
    }

    public void setReporteMedico(ReporteMedico reporteMedico) {
        this.reporteMedico = reporteMedico;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
