package com.example.jogotabuada;

import static android.app.ProgressDialog.show;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import android.media.MediaPlayer;

import com.google.android.material.snackbar.Snackbar;


public class jogo extends AppCompatActivity {

    private int resultado;
    private int respostaInt;
    private TextView num1, num2;
    private EditText resposta;
    private Button calcular;
    private MediaPlayer somCorreto, somErrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);

        num1 = findViewById(R.id.num1Jogo);
        num2 = findViewById(R.id.num2Jogo);

        Random random = new Random();
        // Gera um número entre 1 e 10
        int randomNum1 = random.nextInt(10) + 1;
        int randomNum2 = random.nextInt(10) + 1;

        num1.setText(String.valueOf(randomNum1));
        num2.setText(String.valueOf(randomNum2));

        resposta = findViewById(R.id.editRespostaJogo);
        calcular = findViewById(R.id.btn_calcular);

        somCorreto = MediaPlayer.create(this, R.raw.correto); // substitua pelo nome do arquivo de som
        somErrado = MediaPlayer.create(this, R.raw.errado); // substitua pelo nome do arquivo de som

        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultado = randomNum1 * randomNum2;

                // Verificação para quando não tem valor para não tentar pegar a resposta, pois pode causar um erro (impossível fazer operação de ""(String) com int)
                if (resposta.getText().toString().equals("")) {
                    Snackbar.make(v, "Coloque o resultado", Snackbar.LENGTH_LONG).show();
                    somErrado.start();
                } else {
                    respostaInt = Integer.parseInt(resposta.getText().toString());

                    if (respostaInt == resultado) {
                        Toast.makeText(jogo.this, "Correto!", Toast.LENGTH_SHORT).show();
                        somCorreto.start(); // toca o som
                        Intent i = new Intent(jogo.this, jogo.class);
                        startActivity(i);
                    } else {
                        Snackbar.make(v, "Errado", Snackbar.LENGTH_SHORT).show();
                        somErrado.start();
                        resposta.setText("");
                    }
                }
            }
        });
    }
}