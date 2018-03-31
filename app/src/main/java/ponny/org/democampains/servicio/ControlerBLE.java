package ponny.org.democampains.servicio;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ponny.org.democampains.R;
import ponny.org.democampains.negocio.modelos.bd.OximetriaRoom;
import ponny.org.democampains.negocio.modelos.bd.PacienteRoom;
import ponny.org.democampains.vistas.OximeterView;
import ponny.org.democampains.vistas.popup.Mensajes;

/**
 * Created by Daniel on 11/01/2017.
 */
public class ControlerBLE {
    public static final int REQUEST_ENABLE_BT = 1;
    private Context mContext;
    private BluetoothAdapter.LeScanCallback mLeScanCallback;
    private BluetoothLeService mBluetoothLeService;
    private boolean conexion;
    private Mensajes mensajes;
    java.util.Calendar calendar;
    PacienteRoom pacienteRoom;
    public ControlerBLE(Context mContext, BluetoothAdapter.LeScanCallback mLeScanCallback, Mensajes mensajes,PacienteRoom pacienteRoom) {
        this.mContext = mContext;
        this.mLeScanCallback = mLeScanCallback;
        this.mensajes = mensajes;
        conexion = false;
        calendar=java.util.Calendar.getInstance();
        this.pacienteRoom=pacienteRoom;
    }

    public ControlerBLE(Context mContext, Mensajes mensajes,PacienteRoom pacienteRoom) {
        this.mContext = mContext;
        this.mLeScanCallback = null;
        this.mensajes = mensajes;
        conexion = false;
        calendar=java.util.Calendar.getInstance();
        this.pacienteRoom=pacienteRoom;

    }
    public IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        // intentFilter.addAction(BluetoothLeService.);
        return intentFilter;
    }
    /**
     * Muestra lso servicios y perfiles del dispositivo
     *
     * @param gattServices
     */
    public void displayGattServicesService(List<BluetoothGattService> gattServices) {
        Log.println(Log.ASSERT, "BLE", "sERVICIOS");
        if (gattServices == null)
            return;
        for (BluetoothGattService gattService : gattServices) {
            String uuid = gattService.getUuid().toString();
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService
                    .getCharacteristics();

            if (uuid.equals(mContext.getString(R.string.servicio_oximetero))) {
                for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                    String uuid1 = gattCharacteristic.getUuid().toString();
                    if (uuid1.equals(mContext.getString(R.string.caracteristica_oximetro))) {
                        getmBluetoothLeService().setCharacteristicNotification(
                                gattCharacteristic, true);

                    }
                }
            }
        }
    }
    /**
     * Recibe los datos del dispopsitivo y les da tratamiento
     *
     * @param bytes
     * @param spo2
     * @param pi
     * @param pulse
     */
    public OximetriaRoom tratarDatos
    (byte[] bytes, TextView spo2, TextView pi, TextView pulse) {
        if (bytes[0] == -128) {
        } else if (bytes[0] == -127) {
            OximetriaRoom oximetriaRoom=new OximetriaRoom(
                    calendar.getTimeInMillis(),
                    ((byte) bytes[2] & 0xFF),
                    ((byte) bytes[1] & 0xFF),
                    (((byte) bytes[3] & 0xFF)/10),
                    "","");

            spo2.setText(oximetriaRoom.getSpo2() + "");
            pi.setText(oximetriaRoom.getPi() + "");
            pulse.setText(oximetriaRoom.getPulse() + "");
          //  int pulso = (byte) bytes[1] & 0xFF;
            //int oxigeno = (byte) bytes[2] & 0xFF;
            //double Pi = (byte) bytes[3] & 0xFF;
            if (oximetriaRoom.datosValidos()) {

             return oximetriaRoom;
            }
            return null;


    }
        return null;
    }


    public void validarConexionBlueetooth() {
        if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE) || (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE) == null) {
            mensajes.finshApp(mContext.getString(R.string.error_fatal), mContext.getString(R.string.No_bluetooth));
        }

    }

    public Intent verificacionBluetooth() {
        if (!((BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter().isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            return enableBtIntent ;
        }
        return null;
    }

    public BluetoothAdapter getAdapter() {
        return ((BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
    }
    public  boolean activar(boolean enable) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = bluetoothAdapter.isEnabled();
        if (enable && !isEnabled) {
            return bluetoothAdapter.enable();
        }
        else if(!enable && isEnabled) {
            return bluetoothAdapter.disable();
        }
        // No need to change bluetooth state
        return true;
    }
    /**
     * Escan de BLE
     *
     * @return
     */
    public ScanCallback scanCallback() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return (new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                      if(result.getDevice().getName()==null){
                      //  result.getScanRecord().getServiceUuids()
                          return;
                      }
                        if (result.getDevice().getName().equalsIgnoreCase(mContext.getString(R.string.name_device))) {
                            conectarDevice(result.getDevice().getAddress(), result.getDevice().getName());
                        }
                    } else {
                        Log.println(Log.ASSERT, "BLE", "no se activa");
                    }
                }
            });
        } else {
            return null;
        }
    }
    /**
     * Conecta el dispositivo
     *
     * @param address
     * @param name
     */
    public void conectarDevice(String address, String name) {
        try{
        Log.println(Log.ASSERT, "Conectara", "ble");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          getAdapter().getBluetoothLeScanner().stopScan(scanCallback());
        } else {
        getAdapter().stopLeScan(mLeScanCallback);

        }
        if (!conexion) {
            Log.println(Log.ASSERT,"BLE","CONECTARA "+address+name);
           Intent intent = new Intent(mContext, OximeterView.class);
            intent.putExtra(mContext.getString(R.string.address), address);
            intent.putExtra(mContext.getString(R.string.name_device), name);
            Bundle bundle=new Bundle();
            bundle.putSerializable(mContext.getString(R.string.paciente_rom), pacienteRoom);
            mContext.startActivity(intent);
        }
        if (!conexion) {
            conexion = true;
        }
        }catch (Exception ex){
            Log.println(Log.ASSERT,"BLE",ex.toString());
            ex.printStackTrace();
        }

    }

    public BluetoothLeService getmBluetoothLeService() {
        return mBluetoothLeService;
    }

    public void setmBluetoothLeService(BluetoothLeService mBluetoothLeService) {
        this.mBluetoothLeService = mBluetoothLeService;
    }

    /**
     * Desconecta el dispositivo
     */
    public void desconnectar() {
        if (this.getAdapter() == null || this.getAdapter() == null) {
            Log.w("BLE", "BluetoothAdapter not initialized");
            return;
        }
        Log.println(Log.ASSERT, "BLE", "Desconexion");

      //  this.getmBluetoothLeService().disconnect();
        this.getmBluetoothLeService().close();
        this.setmBluetoothLeService(null);

    }

    public final Runnable hiloScan = new Runnable() {
        @Override
        public void run() {
            if(mLeScanCallback==null)
            {
                Log.println(Log.ASSERT,"Error","Sacan nulo");
            }
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
            {
                Log.println(Log.ASSERT,"Error","Hacer scan nulo");
                // getAdapter().stopLeScan(mLeScanCallback);

                return;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Log.println(Log.ASSERT,"BLE","Escanenado 5.1");
                getAdapter().getBluetoothLeScanner().startScan(scanCallback());

            }
        }

        ;
    };
    public void stopScanner(){

    }
}
