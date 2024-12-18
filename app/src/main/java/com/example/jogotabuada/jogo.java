package com.example.jogotabuada;

import static android.app.ProgressDialog.show;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private int pontuacaoTotal;
    private int acertosConsecutivos;

    private TextView num1, num2, txtPontuacao, txtConsecutivos;
    private EditText resposta;
    private Button calcular;
    private MediaPlayer somCorreto, somErrado;

    // salvando dados
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "JogoTabuadaPrefs";
    public static final String KEY_PONTUACAO_TOTAL = "pontuacaoTotal";
    public static final String KEY_ACERTOS_CONSECUTIVOS = "acertosConsecutivos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);

        num1 = findViewById(R.id.num1Jogo);
        num2 = findViewById(R.id.num2Jogo);
        txtPontuacao = findViewById(R.id.txtPontuacao);
        txtConsecutivos = findViewById(R.id.txtConsecutivos);

        // talvez não precise mais
//        int randomNum1 = random.nextInt(10) + 1;
//        int randomNum2 = random.nextInt(10) + 1;
//        num1.setText(String.valueOf(randomNum1));
//        num2.setText(String.valueOf(randomNum2));

        resposta = findViewById(R.id.editRespostaJogo);
        calcular = findViewById(R.id.btn_calcular);

        somCorreto = MediaPlayer.create(this, R.raw.correto);
        somErrado = MediaPlayer.create(this, R.raw.errado);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Recupera pontuação e acertos consecutivos salvos
        pontuacaoTotal = sharedPreferences.getInt(KEY_PONTUACAO_TOTAL, 0);
        acertosConsecutivos = sharedPreferences.getInt(KEY_ACERTOS_CONSECUTIVOS, 0);

        atualizarPontuacoes();

        Random random = new Random();
        gerarNumerosAleatorios(random);

        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultado = Integer.parseInt(num1.getText().toString()) * Integer.parseInt(num2.getText().toString());

                // Verificação para quando não tem valor para não tentar pegar a resposta, pois pode causar um erro (impossível fazer operação de ""(String) com int)
                if (resposta.getText().toString().equals("")) {
                    Snackbar.make(v, "Coloque o resultado", Snackbar.LENGTH_LONG).show();
                    somErrado.start();
                } else {
                    respostaInt = Integer.parseInt(resposta.getText().toString());

                    if (respostaInt == resultado) {
                        Toast.makeText(jogo.this, "Correto!", Toast.LENGTH_SHORT).show();
                        somCorreto.start(); // toca o som

                        pontuacaoTotal++;
                        acertosConsecutivos++;
                        salvarPontuacoes();

                        resposta.setText("");
                        gerarNumerosAleatorios(random);
                    } else {
                        Snackbar.make(v, "Errado", Snackbar.LENGTH_SHORT).show();
                        somErrado.start();

                        acertosConsecutivos = 0; // Reinicia a contagem de acertos consecutivos
                        salvarPontuacoes();

                        resposta.setText("");
                    }

                    atualizarPontuacoes();
                }
            }
        });
    }

    // Gera um número entre 1 e 10
    private void gerarNumerosAleatorios(Random random) {
        int randomNum1 = random.nextInt(10) + 1;
        int randomNum2 = random.nextInt(10) + 1;

        num1.setText(String.valueOf(randomNum1));
        num2.setText(String.valueOf(randomNum2));
    }

    private void salvarPontuacoes() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_PONTUACAO_TOTAL, pontuacaoTotal);
        editor.putInt(KEY_ACERTOS_CONSECUTIVOS, acertosConsecutivos);
        editor.apply();
    }

    // Atualiza as pontuações da HUD
    private void atualizarPontuacoes() {
        txtPontuacao.setText("Pontuação: " + pontuacaoTotal);
        txtConsecutivos.setText("Acertos consecutivos: " + acertosConsecutivos);
    }
}