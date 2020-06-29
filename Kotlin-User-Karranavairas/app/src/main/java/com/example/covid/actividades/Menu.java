package com.example.covid.actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.covid.MainActivity;
import com.example.covid.R;
import com.example.covid.entidades.global.ClaseGlobal;

public class Menu extends AppCompatActivity {
    private static final String TAG = "LogsAndroid";
    CardView triaje;
    CardView situacionEconomica;
    CardView reportes;
    CardView informacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        triaje = findViewById(R.id.triaje);
        situacionEconomica = findViewById(R.id.situacionEconomica);
        reportes = findViewById(R.id.reportes);
        informacion = findViewById(R.id.info);

        triaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClaseGlobal objLec = (ClaseGlobal) getApplicationContext();

                if(objLec.getReporteMedico()!=null){
                    if(objLec.getReporteMedico().getResultadoTriaje()){
                        Log.i(TAG, " No puedes cambiar de Infectado a Recuperado --llamar al administrador");
                        Toast.makeText(getApplicationContext(),"No puedes cambiar de Infectado a Recuperado",Toast.LENGTH_LONG).show();

                    }else{
                        Log.i(TAG, " El usuario no se considero infectado en el registro, puede hacerlo aun");
                        Intent intent = new Intent(getApplicationContext(), Triaje.class);
                        startActivity(intent);
                    }

                }


            }
        });

        informacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Informacion.class);
                startActivity(intent);
            }
        });

        situacionEconomica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClaseGlobal objLec = (ClaseGlobal) getApplicationContext();

                if(objLec.getReporteEconomico()!=null) {

                    Log.i(TAG, " Ya existe un reporte Economico");
                    Toast.makeText(getApplicationContext(), "Solo puedes hacer el test economico 1 vez", Toast.LENGTH_LONG).show();

                }else {
                    Log.i(TAG, " No existe registro aun puede crear su reporte economico");
                    Intent intent = new Intent(getApplicationContext(), SituacionEconomica.class);
                    startActivity(intent);
                }


            }
        });

        reportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Infectados.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        return false;
    }
}
