package ponny.org.democampains.negocio.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ponny.org.democampains.negocio.modelos.bd.OximetriaRoom;
import ponny.org.democampains.negocio.modelos.bd.PacienteRoom;

/**
 * Created by Daniel on 09/03/2018.
 */

@Dao
public interface OximetriaDao {
    @Insert
    void insertAll(OximetriaRoom... OximetriaRoom);
    @Query("SELECT * FROM OximetriaRoom where paciente = :id")
    List<OximetriaRoom> findByName(String id);


}
