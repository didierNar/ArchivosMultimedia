package archivosmultimedia.eam.archivosmultimedia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import archivosmultimedia.eam.archivosmultimedia.modelo.Reunion;

public class InfoReunion extends AppCompatActivity {

    TextView nombre;
    TextView fecha;
    TextView lugar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_reunion);

        nombre = (TextView) findViewById(R.id.tvNombre);
        fecha = (TextView) findViewById(R.id.tvFecha);
        lugar = (TextView) findViewById(R.id.tvLugar);

        cargarDatos();

    }

    public void cargarDatos(){
        nombre.setText("Nombre: " + Reunion.getActual().getNombre());
        fecha.setText("Fecha: " + Reunion.getActual().getFecha());
        lugar.setText("Lugar: " + Reunion.getActual().getLugar());
    }


    public void abrirGestionFotos(View v){
        Intent i = new Intent(this, TomarFoto.class);
        startActivity(i);
    }

    public void abrirGestionAudios (View v){
        Intent i = new Intent(this, GrabarAudio.class);
        startActivity(i);
    }

    public void abrirGestionVideos (View v){
        Intent i = new Intent(this, GrabarVideo.class);
        startActivity(i);
    }


}
