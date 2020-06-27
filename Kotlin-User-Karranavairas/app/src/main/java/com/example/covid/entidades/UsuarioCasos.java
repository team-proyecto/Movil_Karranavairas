package com.example.covid.entidades;

import com.example.covid.entidades.response.ResponseRest;

import java.util.Date;


public class UsuarioCasos extends ResponseRest {
    private Long id;
    private String nombre;
    private String apellido;
    private String numeroDocumento;
    private TipoDocumento tipoDocumento;
    private String nacimiento;
    private Distritos distrito;
    private String telefono;
    private String direccionDomicilio;
    private Integer codigoConfirmacion;
    private boolean condicionUso;
    private Date fechaRegistro;
    private Nacionalidad nacionalidad;
    private Gps gps;
    private TipoUsuario tipoUsuario;
    private ReporteEconomico reporteEconomico;
    private ReporteMedico reporteMedico;
    private boolean estado;

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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
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

    public boolean getCondicionUso() {
        return condicionUso;
    }

    public void setCondicionUso(boolean condicionUso) {
        this.condicionUso = condicionUso;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Nacionalidad getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(Nacionalidad nacionalidad) {
        this.nacionalidad = nacionalidad;
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

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
