package ponny.org.democampains.vistas;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

import ponny.org.democampains.R;
import ponny.org.democampains.error.ExceptionHandler;
import ponny.org.democampains.negocio.controlador.OximetriaController;
import ponny.org.democampains.negocio.modelos.bd.PacienteRoom;
import ponny.org.democampains.vistas.listas.OximetriasList;

public class OximeterList extends AppCompatActivity {
    private ListView listView;
    private View parentView;
   private PacienteRoom pacienteRoom;
   private OximetriaController oximetriaController;
    private TabHost tabshost;
    private TabHost.TabSpec pulso, spo2, registros ;
    private LineChart lineChartPulso, lineChartOximetro;
    private TextView spo2Var;
    private TextView avPulse;
    private TextView avSPO2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oximeter_list);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        avSPO2=findViewById(R.id.textViewSPO2View);
        avPulse=findViewById(R.id.textViewPusloAltoView);
        spo2Var=findViewById(R.id.textViewPulsoBajoView);
       parentView = findViewById(R.id.contPefil);
        CargarHost();
        configPulsoChart();
     pacienteRoom = (PacienteRoom) bundle.getSerializable(getString(R.string.paciente_rom));
        listView=(ListView)findViewById(R.id.listOximetrias);
        try {
            oximetriaController=new OximetriaController(this,listView);
            oximetriaController.mostrarListaToda(pacienteRoom.getIdentificacion(), lineChartOximetro,lineChartPulso,spo2Var,avPulse,avSPO2);

        }
      catch (Exception ex){
            ex.printStackTrace();
          Log.println(Log.ASSERT,"sql",ex.getMessage());

      }

    }
    private void configPulsoChart(){
        lineChartPulso = (LineChart) findViewById(R.id.chartPulso);
        lineChartOximetro = (LineChart) findViewById(R.id.chartOximetro);
    }


    private void CargarHost() {
        tabshost = (TabHost) findViewById(R.id.tabhostRegistros);
        tabshost.setup();

        pulso = tabshost.newTabSpec(getString(R.string.pulso));
        pulso.setContent(R.id.tabPulso);
        pulso.setIndicator(getString(R.string.pulso));

        spo2 = tabshost.newTabSpec(getString(R.string.oximetria));
        spo2.setContent(R.id.tabOximetro);
        spo2.setIndicator(getString(R.string.oximetria));

        registros = tabshost.newTabSpec(getString(R.string.registros));
        registros.setContent(R.id.tabhostRegistrosOximetrias);
        registros.setIndicator(getString(R.string.registros));

        tabshost.addTab(registros);
        tabshost.addTab(pulso);
        tabshost.addTab(spo2);

    }
}
