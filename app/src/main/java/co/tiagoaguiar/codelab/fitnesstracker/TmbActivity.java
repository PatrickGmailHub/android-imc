package co.tiagoaguiar.codelab.fitnesstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import co.tiagoaguiar.codelab.fitnesstracker.R;

public class TmbActivity extends AppCompatActivity {

    private EditText editAltura;
    private EditText editPeso;
    private EditText editIdade;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmb);

        editAltura = findViewById(R.id.edit_tmb_altura);
        editPeso = findViewById(R.id.edit_tmb_peso);
        editIdade = findViewById(R.id.edit_tmb_idade);
        spinner = findViewById(R.id.spinner_tmb_lifestyle);

        Button btn_calcular = findViewById(R.id.btn_tmb_calcular);

        btn_calcular.setOnClickListener(view -> {
            if(!validar()) {
                Toast.makeText(getBaseContext(), "Todos os campos devem ser\n maiores que zero.", Toast.LENGTH_SHORT).show();
                return;
            }

            int altura = Integer.parseInt(editAltura.getText().toString());
            int peso = Integer.parseInt(editPeso.getText().toString());
            int idade = Integer.parseInt(editIdade.getText().toString());

            double res = calcTmb(peso, altura, idade);

            double tmbResposta = tmbResposta(res);
            Log.d("Tmb_Resposta", String.valueOf(tmbResposta));

            AlertDialog dialog = new AlertDialog.Builder(TmbActivity.this)
                    .setMessage(getString(R.string.tmb_resposta, tmbResposta))
                    .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    })
                    .setNegativeButton(R.string.save, ((dialog1, which) -> {
                        new Thread(() -> {
                            long calcId = SqlHelper.getInstance(TmbActivity.this).addItem("tmb", tmbResposta);
                            runOnUiThread(() -> {
                                if (calcId > 0) {
                                    Toast.makeText(TmbActivity.this, R.string.calc_saved, Toast.LENGTH_SHORT).show();

                                    //Depois de salvar mudar para listagem dos dados!
                                    openListCalcActivity();
                                }
                            });
                        }).start();
                    }))
                    .create();

            dialog.show();

            //Controle do teclado
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editAltura.getWindowToken(),0);
            imm.hideSoftInputFromWindow(editPeso.getWindowToken(),0);
            imm.hideSoftInputFromWindow(editIdade.getWindowToken(),0);
        });

    }

    private double tmbResposta(double res) {
        int index = spinner.getSelectedItemPosition();
        switch (index) {
            case 0: return res * 1.2;
            case 1: return res * 1.375;
            case 2: return res * 1.55;
            case 3: return res * 1.725;
            case 4: return res * 1.9;
            default: return 0;
        }
    }

    private double calcTmb(int peso, int altura, int idade) {
        return 66 + (peso * 13.8) + (altura * 5) - (idade * 6.8);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_list) {
            openListCalcActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openListCalcActivity() {
        Intent it = new Intent(getBaseContext(), ListCalcActivity.class);
        it.putExtra("type", "tmb");
        startActivity(it);
    }

    private boolean validar() {
        return (!editAltura.getText().toString().startsWith("0")
                && !editPeso.getText().toString().startsWith("0")
                && !editIdade.getText().toString().startsWith("0")
                && !editAltura.getText().toString().isEmpty()
                && !editPeso.getText().toString().isEmpty()
                && !editIdade.getText().toString().isEmpty());
    }
}