package ponny.org.democampains.vistas.listas;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ponny.org.democampains.R;
import ponny.org.democampains.negocio.modelos.bd.PacienteRoom;
import ponny.org.democampains.vistas.VistaPaciente;
import ponny.org.democampains.vistas.popup.Mensajes;

/**
 * Created by Daniel on 07/03/2018.
 */

public class PacientesList extends ParentList<PacienteRoom> {


    public PacientesList(Context context, List<PacienteRoom> lista) {
        super(context, lista, VistaPaciente.class);
    }
    @Override
    public long getItemId(int position) {
        return lista.get(position).hashCode();
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_paciente, null);
        try{
        final PacienteRoom pacienteRoom=lista.get(position);
            ImageView imageView=(ImageView)convertView.findViewById(R.id.imageProfilePaciente);
            TextView cc = (TextView) convertView.findViewById(R.id.textViewCC_list);
            TextView names=(TextView)convertView.findViewById(R.id.textViewNames_list);
            cc.setText(pacienteRoom.getIdentificacion());
            if(pacienteRoom.getNombres().toLowerCase().contains("juan")){
                imageView.setImageResource(R.drawable.profile67);
            }
            else if(pacienteRoom.getNombres().toLowerCase().contains("eliana")){
                imageView.setImageResource(R.drawable.profile66);
            }
            else
            {
                imageView.setImageResource(R.drawable.profile65);
            }
            names.setText(pacienteRoom.getNombres()+"  "+pacienteRoom.getApellidos());
            names.setOnClickListener(clickListener(pacienteRoom));
            cc.setOnClickListener(clickListener(pacienteRoom));
            imageView.setOnClickListener(clickListener(pacienteRoom));
            convertView.setOnClickListener(clickListener(pacienteRoom));


        }catch (Exception ex)
        {
            Log.println(Log.ASSERT,super.tag,ex.getMessage());
            ex.printStackTrace();
        }
        return convertView;
    }

    @Override
    protected void irActividad() {
        context.startActivity(PacientesList.super.intent);
    }
    public View.OnClickListener clickListener(final PacienteRoom pacienteRoom){
        return  new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mensajes mensajes=new Mensajes(context);
                mensajes.generarToast(R.string.cargando);
                Bundle bundle = new Bundle();
                bundle.putSerializable(context.getString(R.string.paciente_rom), pacienteRoom);
                PacientesList.super.intent.putExtras(bundle);

                irActividad();
            }
        };

    }
    public  List<PacienteRoom> getList(){
        return lista;
    }
    public void getList( List<PacienteRoom> list){
        this.lista=list;
    }
}
