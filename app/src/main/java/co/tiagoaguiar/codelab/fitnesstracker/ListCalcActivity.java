package co.tiagoaguiar.codelab.fitnesstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import co.tiagoaguiar.codelab.myapplication.R;

public class ListCalcActivity extends AppCompatActivity {

    private RecyclerView rvListCalc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_calc);

        Bundle extras = getIntent().getExtras();

        List<Registro> registros = new ArrayList<>();

        if (extras != null) {
            String type = extras.getString("type");
            registros = SqlHelper.getInstance(this).getRegistroList(type);
            //Log.d("onCreate: ", registros.toString());
        }

        rvListCalc = findViewById(R.id.recycler_view_list);
        rvListCalc.setLayoutManager(new LinearLayoutManager(this));

        ListCalcAdapter adapter = new ListCalcAdapter(registros);

        rvListCalc.setAdapter(adapter);

    }

    private class ListCalcAdapter extends RecyclerView.Adapter<ListCalcViewHolder> {

        List<Registro> registroList;

        public ListCalcAdapter(List<Registro> registros) {
            registroList = registros;
        }

        @NonNull
        @Override
        public ListCalcViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ListCalcViewHolder(getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ListCalcViewHolder holder, int position) {
            Registro registro = registroList.get(position);
            holder.bind(registro);
        }

        @Override
        public int getItemCount() {
            return registroList.size();
        }
    }

    private class ListCalcViewHolder extends RecyclerView.ViewHolder {

        public ListCalcViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(Registro registro) {
            Date dateSaved = null;
            SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("pt", "BR"));
            try {
                dateSaved = dtf.parse(registro.getCreatedDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dtf = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));
            String dateFormated = dtf.format(dateSaved);


            /* Poderia se rfeito dessa maneira pois sabemos que o itemView é um TextView
            ((TextView) itemView).setText(registro.getType() + " - " + registro.response + " - " + registro.createdDate);
            * */
            TextView txtRegistro = itemView.findViewById(android.R.id.text1);
            txtRegistro.setText(registro.getType() + ": " + registro.response + " - Data: " + dateFormated);
        }
    }
}