package archivosmultimedia.eam.archivosmultimedia;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

public class ReproducirVideo extends AppCompatActivity {

    VideoView video;
    String nombreVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproducir_video);

        video = (VideoView) findViewById(R.id.videoReproductor);

        Bundle datos = getIntent().getExtras();
        nombreVideo = datos.getString("nomVideo");

        reproducir();

    }


    public void reproducir(){
        video.setVideoURI(Uri.parse(getExternalFilesDir(null) +
                "/" + nombreVideo));
        video.start();
    }

}
