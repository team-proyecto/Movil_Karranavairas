package com.example.covid.entidades.response;

import com.example.covid.entidades.ReporteMedico;

public class ResponseReporteMedico {
    private ReporteMedico reporteMedico;
    private String mensaje;

    public ReporteMedico getReporteMedico() {
        return reporteMedico;
    }

    public void setReporteMedico(ReporteMedico reporteMedico) {
        this.reporteMedico = reporteMedico;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
