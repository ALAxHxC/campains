package ponny.org.democampains.negocio.controlador;

import android.app.Activity;
import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import ponny.org.democampains.R;
import ponny.org.democampains.negocio.manager.AppDatabase;
import ponny.org.democampains.negocio.modelos.bd.PacienteRoom;
import ponny.org.democampains.vistas.MainActivity;
import ponny.org.democampains.vistas.PerfilPaciente;
import ponny.org.democampains.vistas.VistaPaciente;
import ponny.org.democampains.vistas.listas.PacientesList;
import ponny.org.democampains.vistas.popup.Mensajes;
import ponny.org.democampains.vistas.popup.PopupBusquedaPaciente;

/**
 * Created by Daniel on 06/03/2018.
 */

public class PacienteController {
    private Context context;
    private AppDatabase mDb;
    private Mensajes mensajes;
    private List<PacienteRoom> list;
    private ListView lista;
    public PacienteController(Context context, ListView lista) {
        this.mDb = Room.databaseBuilder(context,AppDatabase.class, AppDatabase.class.getName()).build();
        mensajes=new Mensajes(context);
        this.context=context;
        this.lista=lista;
        list=new ArrayList<>();

    }

    public void insertar(PacienteRoom pacienteRoom, final View parentView, final Dialog dialog) {
        {
            new AsyncTask<PacienteRoom, Boolean, Boolean>() {

                @Override
                protected Boolean doInBackground(PacienteRoom... pacienteRooms) {
                    try {
                        mDb.pacienteDao().insertAll(pacienteRooms);
                        list.addAll(mDb.pacienteDao().getAll());
                        mostrarListaToda();
                        Log.println(Log.ASSERT,"SQL",list.size()+"");
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
                                mensajes.generarSnack(parentView, R.string.paciente_exito);

                    }
                    else{
                        mensajes.generarToast(R.string.paciente_no_exito);
                    }
                }

            }.execute(pacienteRoom);


        }
    }
    public void mostrarListaToda()
    {
        new AsyncTask<ListView,Void, List<PacienteRoom>>(){

            @Override
            protected List<PacienteRoom> doInBackground(ListView... listViews) {

                list.clear();
              List<PacienteRoom> pacienteRoomList=  mDb.pacienteDao().getAll();
              list.addAll(pacienteRoomList);
              Log.println(Log.ASSERT,"Lista",pacienteRoomList.size()+"");
                return list;
            }

            @Override
            protected void onPostExecute(List<PacienteRoom> pacienteRooms) {
                super.onPostExecute(pacienteRooms);
                Log.println(Log.ASSERT,"Lista","Generando lista"+"");
                PacientesList pacientesList=new PacientesList(context,pacienteRooms);
                pacientesList.notifyDataSetChanged();

                lista.setAdapter(pacientesList);
            }
        }.execute(lista);

    }
    public void actualizarPaciente(final MainActivity activity, final PacienteRoom pacienteRoom, final View parentView){
        new AsyncTask<PacienteRoom,Void,Integer>(){


            @Override
            protected Integer doInBackground(PacienteRoom... pacienteRooms) {
       //     activity.mostrarProgress(true);
             //  mensajes.showProgress(activity);
             int actualiza=  mDb.pacienteDao().updatePaciente(pacienteRoom);
                return actualiza;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
             //   mensajes.hideProgressBar(activity);
                if(integer>0)
                {
                    activity.mostrarProgress(true);
                mensajes.generarSnack(parentView,R.string.actualizo);

                }else{
                    activity.mostrarProgress(false);
                   mensajes.generarSnack(parentView,R.string.no_actualizo);
                }

            }
        }.execute(pacienteRoom);
    }
    public void actualizarPacientePefil(final PerfilPaciente activity, final PacienteRoom pacienteRoom, final View parentView){
        new AsyncTask<PacienteRoom,Void,Integer>(){


            @Override
            protected Integer doInBackground(PacienteRoom... pacienteRooms) {
           //     activity.mostrarProgress(true);
                //  mensajes.showProgress(activity);
                int actualiza=  mDb.pacienteDao().updatePaciente(pacienteRoom);
                return actualiza;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                //   mensajes.hideProgressBar(activity);
                if(integer>0)
                {
                  //  activity.mostrarProgress(true);
                    mensajes.generarSnack(parentView,R.string.actualizo);

                }else{
                  //  activity.mostrarProgress(false);
                    mensajes.generarSnack(parentView,R.string.no_actualizo);
                }

            }
        }.execute(pacienteRoom);
    }

}
