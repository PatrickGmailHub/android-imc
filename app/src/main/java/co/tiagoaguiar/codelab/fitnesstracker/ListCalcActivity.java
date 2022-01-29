package co.tiagoaguiar.codelab.fitnesstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

import co.tiagoaguiar.codelab.myapplication.R;

public class ListCalcActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_calc);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String type = extras.getString("type");
            List<Registro> registros = SqlHelper.getInstance(this).getRegistroList(type);
            Log.d("onCreate: ", registros.toString());
        }
    }
}