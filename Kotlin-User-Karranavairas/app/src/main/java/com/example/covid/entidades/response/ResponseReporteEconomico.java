package com.example.covid.entidades.response;

import com.example.covid.entidades.ReporteEconomico;

public class ResponseReporteEconomico {

    private ReporteEconomico reporteEconomico;

    private String mensaje;

    public ReporteEconomico getReporteEconomico() {
        return reporteEconomico;
    }

    public void setReporteEconomico(ReporteEconomico reporteEconomico) {
        this.reporteEconomico = reporteEconomico;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
