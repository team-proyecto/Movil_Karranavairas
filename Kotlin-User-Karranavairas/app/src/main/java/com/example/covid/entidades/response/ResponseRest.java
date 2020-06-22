package com.example.covid.entidades.response;

import com.example.covid.entidades.ReporteEconomico;
import com.example.covid.entidades.ReporteMedico;
import com.example.covid.entidades.UsuarioCasos;

public class ResponseRest {

    private UsuarioCasos usuarioCaso;
    //private ReporteMedico reporteMedico;
   // private ReporteEconomico reporteEconomico;
    private String mensaje;

    public UsuarioCasos getUsuarioCaso() {
        return usuarioCaso;
    }

    public void setUsuarioCaso(UsuarioCasos usuarioCaso) {
        this.usuarioCaso = usuarioCaso;
    }



    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
