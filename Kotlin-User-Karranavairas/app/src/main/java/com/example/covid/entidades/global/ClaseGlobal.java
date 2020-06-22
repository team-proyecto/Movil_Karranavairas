package com.example.covid.entidades.global;

import android.app.Application;

import com.example.covid.entidades.Distritos;
import com.example.covid.entidades.Gps;
import com.example.covid.entidades.Nacionalidad;
import com.example.covid.entidades.ReporteEconomico;
import com.example.covid.entidades.ReporteMedico;
import com.example.covid.entidades.TipoDocumento;
import com.example.covid.entidades.TipoUsuario;
import com.example.covid.entidades.UsuarioCasos;

import java.util.Date;

public class ClaseGlobal extends Application {

    private Long id;
    private String nombre;
    private String apellidos;
    private Nacionalidad nacionalidad;
    private TipoDocumento tipoDocumento;
    private String numeroDocumento;
    private Date fechaNacimiento;
    private Distritos distritos;
    private String telefono;
    private String direccionDomicilio;
    private int codigoConfirmacion;
    private Boolean condicionUso;
    private Date fechaRegistro;
    private Gps gps;
    private TipoUsuario tipoUsuario;
    private ReporteEconomico reporteEconomico;
    private ReporteMedico reporteMedico;
    private Boolean estado;

    private UsuarioCasos usuarioCasos;

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

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
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

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Distritos getDistritos() {
        return distritos;
    }

    public void setDistritos(Distritos distritos) {
        this.distritos = distritos;
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

    public int getCodigoConfirmacion() {
        return codigoConfirmacion;
    }

    public void setCodigoConfirmacion(int codigoConfirmacion) {
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
