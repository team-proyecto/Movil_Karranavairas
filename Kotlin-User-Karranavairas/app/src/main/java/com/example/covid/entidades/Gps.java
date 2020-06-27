package com.example.covid.entidades;

import com.example.covid.entidades.response.ResponseGps;

import java.util.Date;

public class Gps extends ResponseGps {

        private Long id;
        private Long latitud;
        private Long longitud;
        private String direccionGps;
        private boolean estado;
        //private Date fechaRegistro;
        private UsuarioCasos usuarioCaso;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLatitud() {
        return latitud;
    }

    public void setLatitud(Long latitud) {
        this.latitud = latitud;
    }

    public Long getLongitud() {
        return longitud;
    }

    public void setLongitud(Long longitud) {
        this.longitud = longitud;
    }

    public String getDireccionGps() {
        return direccionGps;
    }

    public void setDireccionGps(String direccionGps) {
        this.direccionGps = direccionGps;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public UsuarioCasos getUsuarioCaso() {
        return usuarioCaso;
    }

    public void setUsuarioCaso(UsuarioCasos usuarioCaso) {
        this.usuarioCaso = usuarioCaso;
    }
}
