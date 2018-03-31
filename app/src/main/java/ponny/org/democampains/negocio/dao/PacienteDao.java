package ponny.org.democampains.negocio.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ponny.org.democampains.negocio.modelos.bd.PacienteRoom;

/**
 * Created by Daniel on 06/03/2018.
 */
@Dao
public interface PacienteDao {
    @Query("SELECT * FROM PacienteRoom")
    List<PacienteRoom> getAll();

    @Insert
    void insertAll(PacienteRoom... PacienteRooms);

    @Delete
    void delete(PacienteRoom pacienteRoom);

    @Update
    int updatePaciente(PacienteRoom pacienteRoom);
}
