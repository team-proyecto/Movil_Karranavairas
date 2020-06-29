package com.example.covid.entidades;

import com.example.covid.entidades.response.ResponseReporteMedico;
import com.example.covid.entidades.response.ResponseRest;

import java.util.Date;

public class ReporteMedico extends ResponseReporteMedico {
        private Long id;
        private  boolean resultadoTriaje;
        private EstadoMedico estadoMedico;
        private boolean estado;
        private Date fechaRegistro;
        private UsuarioCasos usuarioCaso;

    public void prePersist() {
        this.fechaRegistro = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getResultadoTriaje() {
        return resultadoTriaje;
    }

    public void setResultadoTriaje(boolean resultadoTriaje) {
        this.resultadoTriaje = resultadoTriaje;
    }

    public EstadoMedico getEstadoMedico() {
        return estadoMedico;
    }

    public void setEstadoMedico(EstadoMedico estadoMedico) {
        this.estadoMedico = estadoMedico;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public UsuarioCasos getUsuarioCasos() {
        return usuarioCaso;
    }

    public void setUsuarioCasos(UsuarioCasos usuarioCaso) {
        this.usuarioCaso = usuarioCaso;
    }
}
