package co.tiagoaguiar.codelab.fitnesstracker;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import co.tiagoaguiar.codelab.myapplication.R;

public class ImcActivity extends AppCompatActivity {

    private EditText editAltura;
    private EditText editPeso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imc);

        editAltura = findViewById(R.id.edit_imc_altura);
        editPeso = findViewById(R.id.edit_imc_peso);

        Button btn_calcula_imc = findViewById(R.id.btn_icm_calcular);

        btn_calcula_imc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validar()) {
                    Toast.makeText(ImcActivity.this, "Todos os campos devem ser\n maiores que zero.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int altura = Integer.parseInt(editAltura.getText().toString());
                int peso = Integer.parseInt(editPeso.getText().toString());

                double res = calcImc(peso, altura);

                int imcRespostaId = imcResposta(res);

                AlertDialog dialog = new AlertDialog.Builder(ImcActivity.this)
                        .setTitle(getString(R.string.imc_resposta, res))
                        .setMessage(imcRespostaId)
                        .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                        })
                        .setNegativeButton(R.string.save, ((dialog1, which) -> {

                            new Thread(() -> {
                                long calcId = SqlHelper.getInstance(ImcActivity.this).addItem("imc", res);
                                runOnUiThread(() -> {
                                    if (calcId > 0)
                                        Toast.makeText(ImcActivity.this, R.string.calc_saved, Toast.LENGTH_SHORT).show();
                                });
                            }).start();

                        }))
                        .create();

                dialog.show();

                //Controle do teclado
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editAltura.getWindowToken(),0);
                imm.hideSoftInputFromWindow(editPeso.getWindowToken(),0);

                //Toast.makeText(ImcActivity.this, imcRespostaId, Toast.LENGTH_LONG).show();

                Log.d("IMC", "resultado: " + res);
            }
        });
    }

    @StringRes
    private int imcResposta(double imc) {
        if (imc <15)
            return R.string.imc_severely_low_weight;
        else if (imc < 16)
            return R.string.imc_very_low_weight;
        else if (imc < 18.5)
            return R.string.imc_low_weight;
        else if (imc < 25)
            return R.string.normal;
        else if (imc < 30)
            return R.string.imc_high_weight;
        else if (imc < 35)
            return R.string.imc_so_high_weight;
        else if (imc < 40)
            return R.string.imc_severely_high_weight;
        else
            return R.string.imc_extreme_weight;
    }

    private boolean validar() {
        return (!editAltura.getText().toString().startsWith("0")
                && !editPeso.getText().toString().startsWith("0")
                && !editAltura.getText().toString().isEmpty()
                && !editPeso.getText().toString().isEmpty());
    }

    private double calcImc(int peso, int altura) {
        // peso / (altura * Altura)
        return (peso / ( ((double) altura/100) * ((double) altura/100) ));
    }
}