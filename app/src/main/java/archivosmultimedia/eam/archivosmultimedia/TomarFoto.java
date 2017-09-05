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

    ArrayList<ImageView> fotos;
    ListView lvFotos;
    int cantidadFotos;

    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomar_foto);

        layout = (LinearLayout) findViewById(R.id.layoutFotos);

        //lvFotos = (ListView) findViewById(R.id.lvFotos);
        fotos = new ArrayList<>();
        cargarNumero();
        crearLayout();
        //recuperarFotos();
        //cargarFotos();

    }

    public void tomarfoto(View v) {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cantidadFotos == 0) {
            File foto = new File(getExternalFilesDir(null), Reunion.getActual().
                    getNombre() + "1" + ".png");
            i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(foto));
            //"android.resource://archivosmultimedia.eam.archivosmultimedia/drawable"
           // i.putExtra(MediaStore.EXTRA_OUTPUT, getExternalFilesDir(null) +
             //       "/" + Reunion.getActual().getNombre() + "1" + ".jpg");
        } else {
            File foto = new File(getExternalFilesDir(null), Reunion.getActual().getNombre()
                    + cantidadFotos + 1 + ".png");
           // i.putExtra(MediaStore.EXTRA_OUTPUT, getExternalFilesDir(null) +
             //       "/" + Reunion.getActual().getNombre() + cantidadFotos + 1 + ".jpg");
            i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(foto));
        }
        startActivity(i);
        cantidadFotos++;
        guardar();
        crearLayout();
        //recuperarFotos();
        //cargarFotos();
    }

    public void recuperarFotos() {

        fotos.clear();

        if (cantidadFotos != 0) {
            for (int i = 1; i <= cantidadFotos; i++) {
                ImageView imagen = new ImageView(getApplicationContext());
                Bitmap bitmap1 =
                        BitmapFactory.decodeFile(
                                getExternalFilesDir(null) +
                                        "/" + Reunion.getActual().getNombre() + i + ".jpg"
                        );
                imagen.setImageBitmap(bitmap1);
                fotos.add(imagen);
            }
        } else {
            Toast.makeText(this, "La galería de fotos esta vacía", Toast.LENGTH_SHORT).show();
        }

    }

    public void crearLayout() {
        layout.removeAllViews();

        if (cantidadFotos != 0) {
            for (int i = 1; i <= cantidadFotos; i++) {

                ImageView imagen = new ImageView(getApplicationContext());

                Bitmap bitmap1 =
                        BitmapFactory.decodeFile(
                                getExternalFilesDir(null) +
                                        "/" + Reunion.getActual().getNombre() + i + ".jpg"
                        );
                imagen.setImageResource(R.mipmap.ic_launcher);
                //imagen.setImageBitmap(bitmap1);
                layout.addView(imagen);
            }
        } else {
            Toast.makeText(this, "La galería de fotos esta vacía", Toast.LENGTH_SHORT).show();
        }

    }

    public void cargarNumero() {
        try {
            String[] a = fileList();
            if (existeArchivo(a, Reunion.getNomArchivo())) {
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

    public void cargarFotos() {
        ArrayAdapter<ImageView> adapter = new ArrayAdapter<ImageView>(this,
                android.R.layout.simple_list_item_1, fotos);
        lvFotos.setAdapter(adapter);
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


}
