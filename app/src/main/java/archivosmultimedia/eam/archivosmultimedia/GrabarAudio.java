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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import archivosmultimedia.eam.archivosmultimedia.modelo.Reunion;

public class GrabarAudio extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    MediaRecorder recoder;

    MediaPlayer player;

    File archivo;

    ListView listaAudios;

    int cantidadAudios;

    ArrayList<String> audios;

    Button btnGrabarAudio, btnDetener;

    ArrayList<File> archivos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grabar_audio);
        btnGrabarAudio = (Button) findViewById(R.id.btnGrabarAudio);
        btnDetener = (Button) findViewById(R.id.btnDetener);
        listaAudios = (ListView) findViewById(R.id.listaAudios);
        audios = new ArrayList<>();
        archivos = new ArrayList<>();

        cargarNumero();
        listarAudios();
        listarArchivos();

        btnDetener.setEnabled(false);


        listaAudios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                reproducir(position);
            }
        });

    }

    public void reproducir(int pos) {
        try {

            player = new MediaPlayer();
            player.setOnCompletionListener(this);
            archivo = archivos.get(pos);

            player.setDataSource(archivo.getAbsolutePath());
            player.prepare();

            player.start(); // Se reproduce el archivo
            btnGrabarAudio.setEnabled(false);
            btnDetener.setEnabled(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void grabarAudio(View v) {

        try {

            recoder = new MediaRecorder();

            //definimos el microfono como fuente del audio
            recoder.setAudioSource(MediaRecorder.AudioSource.MIC);

            //definimos el formato de salida en 3.GP
            recoder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

            //definimos el codec para el 3.GP
            recoder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            //obtenemos la ruta absoluta de la interna y se crea un archivo temporal
            File ruta = new File(Environment.getExternalStorageDirectory().getPath());

            if (cantidadAudios == 0) {

                //creamos el archivo donde se almacena el audio
                archivo = File.createTempFile(Reunion.getActual().getNombre() + "1",
                        ".3GP", ruta);

            } else {

                //creamos el archivo donde se almacena el audio
                archivo = File.createTempFile(Reunion.getActual().getNombre()
                        + cantidadAudios + 1, ".3GP", ruta);

            }

            archivos.add(archivo);

            //definimos que el archivo  creado es donde se almacena el audio
            recoder.setOutputFile(archivo.getAbsolutePath());

            //preparamos el archivo para iniciar la grabacion
            recoder.prepare();

            //iniciamos la grabacion
            recoder.start();

            btnGrabarAudio.setEnabled(false);
            btnDetener.setEnabled(true);

            cantidadAudios++;
            guardar();
            guardarArchivo();
            listarAudios();

        } catch (IOException e) {
            e.printStackTrace();
        }

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
                        openFileInput("audios" + Reunion.getActual().getNombre() + ".txt"));
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

    public void listarAudios() {

        audios.clear();
        if (cantidadAudios == 0) {
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
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        btnGrabarAudio.setEnabled(true);
        //bloqueamos el boton de detener
        btnDetener.setEnabled(false);
    }

    public void listarArchivos() {
        try {
            String[] a = fileList();
            if (existeArchivo(a, "archivo"+Reunion.getActual().getNombre()+".txt")) {
                ObjectInputStream reader = new ObjectInputStream(openFileInput
                        ("archivo"+Reunion.getActual().getNombre()+".txt"));
                archivos = (ArrayList<File>) reader.readObject();
                reader.close();
            }
        } catch (IOException e) {

        } catch (ClassNotFoundException c) {

        }
    }

    public void guardarArchivo() {
        try {
            ObjectOutputStream writer = new ObjectOutputStream
                    (openFileOutput("archivo"+Reunion.getActual().getNombre()+".txt",
                            Activity.MODE_PRIVATE));
            writer.writeObject(archivos);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Toast.makeText(this, "Error al crear el archivo", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}