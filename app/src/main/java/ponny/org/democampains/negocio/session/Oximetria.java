package ponny.org.democampains.negocio.session;

import android.arch.persistence.room.ColumnInfo;
import android.content.Context;
import android.content.SharedPreferences;

import ponny.org.democampains.negocio.modelos.bd.OximetriaRoom;

/**
 * Created by Daniel on 10/03/2018.
 */

public class Oximetria {

    private String datatime="datatime_ox";
    private String spo2="spo2_ox";
    private String pulse="pulse";
    private String pi="pi_ox";
   private SharedPreferences preferences;
    private final String preferencias="oximetrias";

    private OximetriaRoom oximetriaRoom;
   private Context context;

    public Oximetria(OximetriaRoom oximetriaRoom, Context context) {
        this.oximetriaRoom = oximetriaRoom;
        this.context = context;
        preferences = context.getSharedPreferences(preferencias, Context.MODE_PRIVATE);
    }
    public Oximetria(Context context){
        this.context=context;
        preferences = context.getSharedPreferences(preferencias, Context.MODE_PRIVATE);
    }
    public boolean guardarOximetriaTemp(){
        SharedPreferences.Editor editor = preferences.edit();
       editor.clear();
       editor.putLong(datatime,oximetriaRoom.getDatatime());
       editor.putInt(spo2,oximetriaRoom.getSpo2());
       editor.putInt(pulse,oximetriaRoom.getPulse());
       editor.putFloat(pi, (float) oximetriaRoom.getPi());
       boolean commit=editor.commit();
       editor.apply();
       return  commit;
    }
    public OximetriaRoom getOximetriaRoom(){
        OximetriaRoom oximetriaRoom=new OximetriaRoom(
                preferences.getLong(datatime,0),
                preferences.getInt(spo2,0),
                preferences.getInt(pulse,0),
                preferences.getFloat(pi,0),
                "",
                ""

        );
        return oximetriaRoom;
    }
    public void initOximetria(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(datatime,0);
        editor.putInt(spo2,0);
        editor.putInt(pulse,0);
        editor.putFloat(pi,0);
          editor.commit();
    }

}
