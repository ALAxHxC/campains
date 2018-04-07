package ponny.org.democampains.vistas;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import ponny.org.democampains.LoginActivity;
import ponny.org.democampains.R;
import ponny.org.democampains.error.ExceptionHandler;
import ponny.org.democampains.negocio.controlador.PacienteController;
import ponny.org.democampains.negocio.session.Usuario;
import ponny.org.democampains.servicio.Sesion;
import ponny.org.democampains.vistas.listas.PacientesList;
import ponny.org.democampains.vistas.popup.Mensajes;
import ponny.org.democampains.vistas.popup.PopPaciente;
import ponny.org.democampains.vistas.popup.PopupBusquedaPaciente;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
private PopPaciente popPaciente;
private PacienteController pacienteController;
private ListView pacientes;
private Usuario usuario;
private Mensajes mensajes;
private View layout;
private PopupBusquedaPaciente busquedaPaciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout=(View)findViewById(R.id.drawer_layout);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Sesion.pacienteRoom=null;
        Sesion.oximetriaRoom=null;
        setSupportActionBar(toolbar);
    usuario=new Usuario(this);
        pacientes=findViewById(R.id.listaPaciente);
        pacienteController=new PacienteController(this,pacientes);
        popPaciente=new PopPaciente(this,pacientes);
        pacienteController.mostrarListaToda();
        mensajes=new Mensajes(this);
        mensajes.generarSnack(layout,R.string.cargando_datos);
        busquedaPaciente=new PopupBusquedaPaciente(this);
        loadData();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popPaciente.dialogoRegistroPaciente(view);
            /*    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            busquedaPaciente.generarDialogo(pacientes);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }  else if (id == R.id.logout) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            usuario.setSession(false);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
           startActivity(new Intent(this,LoginActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void loadData(){
        try {
          /*  TextView name = (TextView) findViewById(R.id.txtNameMedico);
            name.setText("Daniel Ortiz");
            TextView correo = (TextView) findViewById(R.id.textViewCorreoMedico);
            correo.setText(usuario.getUsuario());*/
        }catch (Exception ex){
            Log.println(Log.ASSERT,"SQL",ex.getMessage());
            ex.printStackTrace();
        }
        }
}
