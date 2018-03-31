package ponny.org.democampains.negocio.controlador;

import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ponny.org.democampains.R;
import ponny.org.democampains.negocio.manager.AppDatabase;
import ponny.org.democampains.negocio.modelos.bd.OximetriaRoom;
import ponny.org.democampains.negocio.modelos.bd.PacienteRoom;
import ponny.org.democampains.negocio.session.Oximetria;
import ponny.org.democampains.vistas.listas.OximetriasList;
import ponny.org.democampains.vistas.listas.PacientesList;
import ponny.org.democampains.vistas.popup.Mensajes;

/**
 * Created by Daniel on 09/03/2018.
 */

public class OximetriaController {
    private Context context;
    private AppDatabase mDb;
    private Mensajes mensajes;
    private List<OximetriaRoom> list;
    private ListView lista;
    private ArrayList<String> valorex;
    public OximetriaController(Context context, ListView lista) {
        this.mDb = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.class.getName()).build();
        mensajes = new Mensajes(context);
        this.context = context;
        this.lista = lista;
        list = new ArrayList<>();
    }

    public synchronized void insertar(final OximetriaRoom oximetriaRoom, final View parentView, final Dialog dialog) {
        {
            new AsyncTask<OximetriaRoom, Boolean, Boolean>() {

                @Override
                protected Boolean doInBackground(OximetriaRoom... OximetriaRoom) {
                    try {
                        oximetriaRoom.setDatatime(System.currentTimeMillis());
                        mDb.oximetriaDao().insertAll(OximetriaRoom);
                        //list.addAll(mDb.pacienteDao().getAll());
                        //  mostrarListaToda();
                        Log.println(Log.ASSERT, "SQL", list.size() + "");
                        return true;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return false;
                    }
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    super.onPostExecute(aBoolean);
                    if (aBoolean) {
                        dialog.dismiss();
                        mensajes.generarSnack(parentView, R.string.registro_exito);

                    } else {
                        mensajes.generarToast(R.string.paciente_no_exito);
                    }
                }

            }.execute(oximetriaRoom);


        }
    }

    public synchronized void mostrarListaToda(final String id,  final LineChart lineChartOximetro, final LineChart lineChartPulso) {
        new AsyncTask<ListView, Void, List<OximetriaRoom>>() {

            @Override
            protected List<OximetriaRoom> doInBackground(ListView... listViews) {
                try {

                    list.clear();
                    List<OximetriaRoom> oximetriaRooms = mDb.oximetriaDao().findByName(id);
                   //
                    //Collections.sort(list,new CompareOximetria());
                    list.addAll(oximetriaRooms);
                    Collections.reverse(list);
                    Log.println(Log.ASSERT, "Lista", oximetriaRooms.size() + "");
                } catch (Exception ex) {
                    Log.println(Log.ASSERT, "sql", ex.getMessage());
                }
                return list;
            }

            @Override
            protected void onPostExecute(List<OximetriaRoom> oximetriaRooms) {

                super.onPostExecute(oximetriaRooms);

                Log.println(Log.ASSERT, "Lista", "Generando lista" + "");
                try {
                    OximetriasList oximetriasList = new OximetriasList(context, oximetriaRooms);
                   lista.setAdapter(oximetriasList);
                    cargarLineDataOximetria(lineChartOximetro,oximetriaRooms);
                    cargarLineDataPulso(lineChartPulso,oximetriaRooms);
                } catch (Exception ex ) {
                    ex.printStackTrace();
                    Log.println(Log.ASSERT, "sql", ex.toString());
                }
            }
        }.execute(lista);

    }
    public void cargarLineDataOximetria(LineChart lineChartOximetro,List<OximetriaRoom> oximetriaRooms) {
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(dataOximetria( oximetriaRooms));
        LineData lineData = new LineData(dataSets);
        ejeX(lineChartOximetro);
        lineChartOximetro.setData(lineData);

        lineChartOximetro.invalidate(); // refresh
    }

    public void cargarLineDataPulso(LineChart lineChartPulso,List<OximetriaRoom> oximetriaRooms) {
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(dataPulso( oximetriaRooms));
        LineData lineData = new LineData(dataSets);
        ejeX(lineChartPulso);
        lineChartPulso.setData(lineData);

        lineChartPulso.invalidate(); // refresh

    }
    public LineDataSet dataPulso( List<OximetriaRoom> oximetriaRooms) {
        LineDataSet pusloData = new LineDataSet(cargarEntryPulso(oximetriaRooms), context.getString(R.string.pulso));
        pusloData.setColor(Color.TRANSPARENT);
        pusloData.setFillColor(Color.GREEN);
        pusloData.setColor(Color.RED);
        pusloData.setAxisDependency(YAxis.AxisDependency.LEFT);
        return pusloData;
    }

    public LineDataSet dataOximetria( List<OximetriaRoom> oximetriaRooms) {
        LineDataSet oximetriasData = new LineDataSet(cargarEntryOximetria(oximetriaRooms), context.getString(R.string.oximetria));
        oximetriasData.setColor(Color.TRANSPARENT);
        oximetriasData.setFillColor(Color.GREEN);
        oximetriasData.setColor(Color.RED);
        oximetriasData.setAxisDependency(YAxis.AxisDependency.LEFT);
        return oximetriasData;

    }

    private List<Entry> cargarEntryPulso(List<OximetriaRoom> oximetriaRoomList) {
        List<Entry> entries = new ArrayList<Entry>();

        for (int i = 0; i < oximetriaRoomList.size(); i++) {
            Entry entry = new Entry(i, oximetriaRoomList.get(i).getPulse());
            entries.add(entry);
        }
        return entries;

    }

    public List<Entry> cargarEntryOximetria(List<OximetriaRoom> oximetriaRoomList) {
        List<Entry> entries = new ArrayList<>();
        valorex = new ArrayList<>();
        Log.println(Log.ASSERT, "BLE", "Cargara " + oximetriaRoomList.size());
        for (int i = 0; i < oximetriaRoomList.size(); i++) {
            Entry entry = new Entry((i), oximetriaRoomList.get(i).getSpo2());
            entries.add(entry);
            valorex.add(String.valueOf(i));
        }
        return entries;
    }
    public void ejeX(LineChart lineChart) {
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGridColor(Color.CYAN);

    }
}
