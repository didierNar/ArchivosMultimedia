package archivosmultimedia.eam.archivosmultimedia;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import archivosmultimedia.eam.archivosmultimedia.modelo.Reunion;

public class GrabarAudio extends AppCompatActivity implements MediaPlayer.OnCompletionListener{

    MediaRecorder recoder;

    MediaPlayer player;

    File archivo;

    ListView listaAudios;

    int cantidadAudios;

    ArrayList<String> audios;

    Button btnGrabarAudio,btnDetener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grabar_audio);
        btnGrabarAudio = (Button)findViewById(R.id.btnGrabarAudio);
        btnDetener = (Button)findViewById(R.id.btnDetener);
        listaAudios = (ListView) findViewById(R.id.listaAudios);
        audios = new ArrayList<>();
        cargarNumero();
        listarAudios();


        listaAudios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String audioSelec = (String) listaAudios.getItemAtPosition(position);

            }
        });

    }


    public void grabarAudio(View v) {


        if (cantidadAudios == 0) {

            try {

                recoder = new MediaRecorder();

                //definimos el microfono como fuente del audio
                recoder.setAudioSource(MediaRecorder.AudioSource.MIC);

                //definimos el formato de salida en 3.GP
                recoder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

                //definimos el codec para el 3.GP
                recoder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                //obtenemos la ruta absoluta de la onterna t se crea un archivo temporal
                File ruta = new File(Environment.getExternalStorageDirectory().getPath() + Reunion.getActual().getNombre() + "1");

                //creamos el archivo donde se almacena el audio
                archivo = File.createTempFile("temporal", ".3GP", ruta);
                //definimos que el archivo  creado es donde se almacena el audio
                recoder.setOutputFile(archivo.getAbsolutePath());

                //preparamos el archivo para iniciar la grabacion
                recoder.prepare();

                //iniciamos la grabacion
                recoder.start();

                btnGrabarAudio.setText("Grabando");
                btnGrabarAudio.setEnabled(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            try {
                recoder = new MediaRecorder();

                //definimos el microfono como fuente del audio
                recoder.setAudioSource(MediaRecorder.AudioSource.MIC);

                //definimos el formato de salida en 3.GP
                recoder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

                //definimos el codec para el 3.GP
                recoder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                //obtenemos la ruta absoluta de la onterna t se crea un archivo temporal
                File ruta = new File(Environment.getExternalStorageDirectory().getPath() +
                        Reunion.getActual().getNombre() + cantidadAudios + 1);

                //creamos el archivo donde se almacena el audio
                archivo = File.createTempFile("temporal", ".3GP", ruta);
                //definimos que el archivo  creado es donde se almacena el audio
                recoder.setOutputFile(archivo.getAbsolutePath());

                //preparamos el archivo para iniciar la grabacion
                recoder.prepare();

                //iniciamos la grabacion
                recoder.start();

                btnGrabarAudio.setText("Grabando");
                btnGrabarAudio.setEnabled(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        cantidadAudios++;
        guardar();
        listarAudios();
    }


    public void guardar() {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(
                    openFileOutput("audios" + Reunion.getActual().getNombre() + ".txt",
                            Activity.MODE_PRIVATE));

            writer.write(cantidadAudios + "");
            writer.flush();
            writer.close();

        } catch (IOException e) {
            Toast.makeText(this, "Error al crear el archivo", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
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
                    cantidadAudios = 0;
                } else {
                    cantidadAudios = Integer.parseInt(linea.toString());
                }
                br.close();
                reader.close();
            }
        } catch (IOException e) {

        }
    }

    public void listarAudios(){

           audios.clear();
            if (cantidadAudios == 0){
                Toast.makeText(this, "No hay audios de esta reunión",
                        Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 1; i <= cantidadAudios; i++) {
                    String nombre = Reunion.getActual().getNombre() + i + ".3GP";
                    audios.add(nombre);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, audios);
                listaAudios.setAdapter(adapter);
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


    public void detenerAudio(View v) {

        //detenemos la grabacion
        recoder.stop();
        //liberamos recursos
        recoder.release();
        //instanciamos el objeto para reproducir el archivo que ha sido grabado
        //solo se deja listo para reproducir, nunca se reproduce
        player = new MediaPlayer();
        //se referencia la funcion onCompletion definida a lo ultimo, para que cuando
        //la reproduccion se ejecute luegoesa funcion
        player.setOnCompletionListener(this);

        try {
            player.setDataSource(archivo.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //habilitamos el boton de grabar
        btnGrabarAudio.setEnabled(true);
        //bloqueamos el boton de detener
        btnDetener.setEnabled(false);

        btnGrabarAudio.setText("Listo para reproducir");
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        btnGrabarAudio.setEnabled(true);
        //bloqueamos el boton de detener
        btnDetener.setEnabled(true);
    }
}