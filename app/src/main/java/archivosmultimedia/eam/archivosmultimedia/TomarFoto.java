package archivosmultimedia.eam.archivosmultimedia;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
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

public class TomarFoto extends AppCompatActivity {

    int cantidadFotos;

    LinearLayout layout;
    File foto;
    ArrayList<File> listaFotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomar_foto);

        layout = (LinearLayout) findViewById(R.id.layoutFotos);
        listaFotos = new ArrayList<>();

        listarFotosGuardadas();
        cargarNumero();
        crearLayout();

    }

    public void tomarfoto(View v) {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cantidadFotos == 0) {
            foto = new File(getExternalFilesDir(null), Reunion.getActual().
                    getNombre() + "1" + ".png");
            i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(foto));
            //"android.resource://archivosmultimedia.eam.archivosmultimedia/drawable"
           // i.putExtra(MediaStore.EXTRA_OUTPUT, getExternalFilesDir(null) +
             //       "/" + Reunion.getActual().getNombre() + "1" + ".jpg");
        } else {
            foto = new File(getExternalFilesDir(null), Reunion.getActual().getNombre()
                    + cantidadFotos + 1 + ".png");
           // i.putExtra(MediaStore.EXTRA_OUTPUT, getExternalFilesDir(null) +
             //       "/" + Reunion.getActual().getNombre() + cantidadFotos + 1 + ".jpg");
            i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(foto));
        }
        startActivity(i);
        cantidadFotos++;
        guardar();
        listaFotos.add(foto);
        guardarObjeto();
        listarFotosGuardadas();
        crearLayout();
    }

    public void crearLayout() {
        layout.removeAllViews();

        if (listaFotos.size() != 0) {
            for (int i = 1; i <= listaFotos.size(); i++) {

                ImageView imagen = new ImageView(getApplicationContext());

                imagen.setImageURI(Uri.parse(getExternalFilesDir(null) + "/" +
                        Reunion.getActual().getNombre() + i + ".png"));
                //imagen.setImageResource(R.mipmap.ic_launcher);
                layout.addView(imagen);
            }
        } else {
            Toast.makeText(this, "La galería de fotos esta vacía", Toast.LENGTH_SHORT).show();
        }

    }

    public void cargarNumero() {
        try {
            String[] a = fileList();
            if (existeArchivo(a, "fotos" + Reunion.getActual().getNombre()+".txt")) {
                InputStreamReader reader = new InputStreamReader(
                        openFileInput("fotos" + Reunion.getActual().getNombre()+".txt"));
                BufferedReader br = new BufferedReader(reader);
                String linea = br.readLine();
                if (linea == null) {
                    cantidadFotos = 0;
                } else {
                    cantidadFotos = Integer.parseInt(linea.toString());
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
                    openFileOutput("fotos" + Reunion.getActual().getNombre()+".txt",
                            Activity.MODE_PRIVATE));

            writer.write(cantidadFotos + "");
            writer.flush();
            writer.close();

        } catch (IOException e) {
            Toast.makeText(this, "Error al crear el archivo", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
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

    public void listarFotosGuardadas() {
        try {
            String[] a = fileList();
            if (existeArchivo(a, "listaFotos"+Reunion.getActual().getNombre())) {
                ObjectInputStream reader = new ObjectInputStream(openFileInput
                        ("listaFotos"+Reunion.getActual().getNombre()));
                listaFotos = (ArrayList<File>) reader.readObject();
                reader.close();
            }
        } catch (IOException e) {

        } catch (ClassNotFoundException c) {

        }
    }

    public void guardarObjeto() {
        try {
            ObjectOutputStream writer = new ObjectOutputStream
                    (openFileOutput("listaFotos"+Reunion.getActual().getNombre(),
                            Activity.MODE_PRIVATE));
            writer.writeObject(listaFotos);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Toast.makeText(this, "Error al crear el archivo", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


}
