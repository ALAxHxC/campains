package ponny.org.democampains.negocio.modelos.bd;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Daniel on 09/03/2018.
 */
@Entity
public class OximetriaRoom implements Serializable {
@PrimaryKey
    @NonNull
    private long datatime;
    @ColumnInfo(name = "spo2")
    private int spo2;
    @ColumnInfo(name="pulse")
    private int pulse;
    @ColumnInfo(name = "pi")
    private double pi;
    @ColumnInfo (name="anotaciones")
    private String anotaciones;
    @ColumnInfo(name="paciente")
    private String idPaciente;


    public OximetriaRoom(@NonNull long datatime, int spo2, int pulse, double pi, String anotaciones, String idPaciente) {
        this.datatime = datatime;
        this.spo2 = spo2;
        this.pulse = pulse;
        this.pi = pi;
        this.anotaciones = anotaciones;
        this.idPaciente = idPaciente;
    }

    @NonNull
    public long getDatatime() {
        return datatime;
    }

    public void setDatatime(@NonNull long datatime) {
        this.datatime = datatime;
    }

    public int getSpo2() {
        return spo2;
    }

    public void setSpo2(int spo2) {
        this.spo2 = spo2;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public double getPi() {
        return pi;
    }

    public void setPi(double pi) {
        this.pi = pi;
    }

    public String getAnotaciones() {
        return anotaciones;
    }

    public void setAnotaciones(String anotaciones) {
        this.anotaciones = anotaciones;
    }

    public String getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(String idPaciente) {
        this.idPaciente = idPaciente;
    }

    public Boolean datosValidos(){
        return !(this.spo2 >= 127 || this.pulse >= 255 || this.pi == 0.0);
    }

}
