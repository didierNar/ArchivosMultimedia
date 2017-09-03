package archivosmultimedia.eam.archivosmultimedia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import archivosmultimedia.eam.archivosmultimedia.modelo.Reunion;

public class GrabarVideo extends AppCompatActivity {

    ListView listaVideos;

    int cantidadVideos;

    ArrayList<String> videos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grabar_video);

        listaVideos = (ListView) findViewById(R.id.listaVideos);
        cargarNumero();
        videos = new ArrayList<>();
        listarVideos();

        listaVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String videoSelec = (String) listaVideos.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), ReproducirVideo.class);
                intent.putExtra("nomVideo", videoSelec);
                startActivity(intent);
            }
        });

    }

    public void grabarVideo(View v) {
        Intent i = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (cantidadVideos == 0) {
            i.putExtra(MediaStore.EXTRA_OUTPUT, getExternalFilesDir(null) +
                    "/" + Reunion.getActual().getNombre() + "1" + ".mp4");
        } else {
            i.putExtra(MediaStore.EXTRA_OUTPUT, getExternalFilesDir(null) +
                    "/" + Reunion.getActual().getNombre() + cantidadVideos + 1 + ".mp4");
        }

        cantidadVideos++;
        guardar();

        startActivity(i);
        listarVideos();
    }

    public void cargarNumero() {
        try {
            String[] a = fileList();
            if (existeArchivo(a, Reunion.getNomArchivo())) {
                InputStreamReader reader = new InputStreamReader(
                        openFileInput("videos" + Reunion.getActual().getNombre() + ".txt"));
                BufferedReader br = new BufferedReader(reader);
                String linea = br.readLine();
                if (linea == null) {
                    cantidadVideos = 0;
                } else {
                    cantidadVideos = Integer.parseInt(linea.toString());
                }
                br.close();
                reader.close();
            }
        } catch (IOException e) {

        }
    }

    public void guardar() {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(
                    openFileOutput("videos" + Reunion.getActual().getNombre() + ".txt",
                            Activity.MODE_PRIVATE));

            writer.write(cantidadVideos + "");
            writer.flush();
            writer.close();

        } catch (IOException e) {
            Toast.makeText(this, "Error al crear el archivo", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void listarVideos (){
        videos.clear();
        if (cantidadVideos == 0){
            Toast.makeText(this, "No hay videos de esta reunión",
                    Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 1; i <= cantidadVideos; i++) {
                String nombre = Reunion.getActual().getNombre() + i + ".mp4";
                videos.add(nombre);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, videos);
            listaVideos.setAdapter(adapter);
        }
    }

    private boolean existeArchivo(String[] archivos, String archBuscar) {

        for (int f = 0; f < archivos.length; f++) {
            if (archBuscar.equals(archivos[f])) {
                return true;
            }

        }
        return false;


    }

}
