package ponny.org.democampains.vistas.listas;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import ponny.org.democampains.R;
import ponny.org.democampains.negocio.modelos.bd.OximetriaRoom;
import ponny.org.democampains.negocio.modelos.bd.PacienteRoom;
import ponny.org.democampains.vistas.popup.PopupOximetria;

import static ponny.org.democampains.servicio.Sesion.formatoFecha;

/**
 * Created by Daniel on 10/03/2018.
 */

public class OximetriasList extends ParentList<OximetriaRoom> {


    public OximetriasList(Context context, List<OximetriaRoom> lista) {
        super(context, lista, OximetriasList.class);
    }

    @Override
    protected void irActividad() {

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_oximetria, null);
        try {

            final OximetriaRoom oximetriaRoom = lista.get(position);
            TextView idView = (TextView) convertView.findViewById(R.id.textViewIdItem);
            TextView pulso = (TextView) convertView.findViewById(R.id.textViewPulsoItem);
            TextView spo2 = (TextView) convertView.findViewById(R.id.textViewSPO2Item);
            TextView pi = (TextView) convertView.findViewById(R.id.textViewPIItem);
            idView.setText(formatoFecha.format(oximetriaRoom.getDatatime()));
            pulso.setText(oximetriaRoom.getPulse() + "");
            pi.setText(oximetriaRoom.getPi() + "");
            spo2.setText(oximetriaRoom.getSpo2() + "");
            convertView.setOnClickListener(clickListener(oximetriaRoom));


//            convertView.setOnClickListener(clickListener(pacienteRoom));

        } catch (Exception ex) {
            Log.println(Log.ASSERT, super.tag, ex.getMessage());
            ex.printStackTrace();
        }
        return convertView;
    }
    public View.OnClickListener clickListener(final OximetriaRoom oximetriaRoom){
        return  new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupOximetria popupOximetria=new PopupOximetria(context);
                popupOximetria.dialogoRegistroPaciente(oximetriaRoom);
            }
        };
    }
}
