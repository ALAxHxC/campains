package ponny.org.democampains.vistas.popup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import ponny.org.democampains.R;

/**
 * Created by Daniel on 06/03/2018.
 */

public class Mensajes {
    private Context context;
    private ProgressBar progressBar;
    public Mensajes(Context context) {
        this.context = context;
        progressBar=null;
    }
    public void generarSnack(View view,int id){
        Snackbar.make(view, context.getString(id), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }
    public void generarToast(int id)
    {
        Toast.makeText(context,id,Toast.LENGTH_LONG).show();

    }

    public void showProgress(Activity activity) {
        this.progressBar=new ProgressBar(context,null,android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        activity.addContentView(progressBar,params);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);


    }
    public void hideProgressBar(Activity activity){
        if(progressBar==null){return;}
        progressBar.setVisibility(View.GONE);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    /**
     * Muestra una alerta y finaliza la aplicacion
     *
     * @param title titulo de la alerta
     * @param body  mensaje de la alerta
     */
    public void finshApp(String title, String body) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(body);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
        builder.create();
        builder.show();

    }

}
