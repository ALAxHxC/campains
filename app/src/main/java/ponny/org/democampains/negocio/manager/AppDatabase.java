package ponny.org.democampains.negocio.manager;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ponny.org.democampains.negocio.dao.OximetriaDao;
import ponny.org.democampains.negocio.dao.PacienteDao;
import ponny.org.democampains.negocio.modelos.bd.OximetriaRoom;
import ponny.org.democampains.negocio.modelos.bd.PacienteRoom;

/**
 * Created by Daniel on 06/03/2018.
 */

@Database(entities = {PacienteRoom.class, OximetriaRoom.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PacienteDao pacienteDao();
    public abstract OximetriaDao oximetriaDao();
}