package com.example.covid.actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.covid.R;

public class Menu extends AppCompatActivity {
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
                Intent intent = new Intent(getApplicationContext(), Triaje.class);
                startActivity(intent);
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
                Intent intent = new Intent(getApplicationContext(), SituacionEconomica.class);
                startActivity(intent);
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
}
