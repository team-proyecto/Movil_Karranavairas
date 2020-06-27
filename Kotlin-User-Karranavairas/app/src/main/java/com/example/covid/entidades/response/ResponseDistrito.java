package com.example.covid.entidades.response;

import com.example.covid.entidades.Distritos;
import com.example.covid.entidades.global.ClaseGlobal;

public class ResponseDistrito {

    private Distritos distritos;
    private String mensaje;

    public Distritos getDistritos() {
        return distritos;
    }

    public void setDistritos(Distritos distritos) {
        this.distritos = distritos;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
