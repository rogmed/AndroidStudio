package com.example.m08actividad03;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    private Spinner spSource;
    private ArrayList<String> sources;
    private ArrayAdapter<String> adapter;

    // Cases para el spinner
    final String MUSICA_RAW_DIRECTO = "Musica Raw Directo";
    final String MUSICA_URI_RAW = "Musica URI Raw";
    final String VIDEO = "Video";

    private Button btPlay;
    private Button btPause;
    private Button btStop;

    private MediaPlayer mp = new MediaPlayer();
    private VideoView video;

    private ProgressBar progressBar;
    private TextView lbStatus;
    private Button btMas10seg;
    private Button btMenos10seg;
    private TextView lbMetadata;

    private MediaObserver observer = null;
    private VideoObserver videoObserver;

    private boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Desplegable con modos de escucha (Raw Directo, URI Raw y Video)
        spSource = (Spinner)findViewById(R.id.spSource);
        sources = SourceListFactory();
        adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, sources);
        spSource.setAdapter(adapter);
        spSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                video.setVisibility(View.GONE);
                lbStatus.setText("");
                String source = spSource.getSelectedItem().toString();
                updateSource(source);
                showMetadata(source);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Boton PLAY
        btPlay = (Button)findViewById(R.id.btPlay);

        // Boton PAUSA
        btPause = (Button)findViewById(R.id.btPause);

        // Boton STOP
        btStop = (Button)findViewById(R.id.btStop);

        // Video Player
        video = (VideoView)findViewById(R.id.videoView);

        // Barra de progreso
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        lbStatus = (TextView)findViewById(R.id.lbStatus);

        // Botones +/- 10 segundos
        btMas10seg = (Button) findViewById(R.id.btForward);
        btMenos10seg = (Button) findViewById(R.id.btBack);

        // Metadatos
        lbMetadata = (TextView) findViewById(R.id.lbMetadata);
    }

    private ArrayList<String> SourceListFactory() {
        ArrayList<String> sources = new ArrayList<>();
        sources.add(MUSICA_RAW_DIRECTO);
        sources.add(MUSICA_URI_RAW);
        sources.add(VIDEO);
        return sources;
    }

    private void updateSource(@NonNull String source) {
        // Limpiar memoria
        clearPlayers();

        switch (source){
            case MUSICA_RAW_DIRECTO:{
                mp = MediaPlayer.create(MainActivity.this, R.raw.funk);
                // Nueva barra de progreso
                observer = new MediaObserver();
                new Thread(observer).start();

                // PLAY
                btPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!mp.isPlaying()){
                            if(observer.stop.get() == true) {
                                observer = new MediaObserver();
                                new Thread(observer).start();
                            }
                            mp.start();
                        }
                        lbStatus.setText("Reproduciendo...");
                    }
                });

                // PAUSE
                btPause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mp.isPlaying()){
                            mp.pause();
                            lbStatus.setText("Pausa");
                        }
                    }
                });

                // STOP
                btStop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        observer.stop();
                        progressBar.setProgress(0);
                        mp.stop();
                        mp = MediaPlayer.create(MainActivity.this, R.raw.funk);
                        lbStatus.setText("Stop");
                    }
                });

                // MAS 10 SEGUNDOS
                btMas10seg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mp.getCurrentPosition() >= mp.getDuration() - 10000) {
                            mp.stop();
                            mp = MediaPlayer.create(MainActivity.this, R.raw.funk);
                            lbStatus.setText("...");
                        } else {
                            mp.seekTo(mp.getCurrentPosition() + 10000);
                        }
                    }
                });

                // MENOS 10 SEGUNDOS
                btMenos10seg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mp.seekTo(mp.getCurrentPosition() - 10000);
                    }
                });

                // COMPLETION
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                        mp = MediaPlayer.create(MainActivity.this, R.raw.funk);
                        lbStatus.setText("...");
                    }
                });

                break;
            }

            case MUSICA_URI_RAW:{
                mp = new MediaPlayer();
                try {
                    mp.setDataSource(this, Uri.parse("android.resource://" +
                            getPackageName() + "/" + R.raw.breakingin));
                    mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Nueva barra de progreso
                observer = new MediaObserver();
                new Thread(observer).start();

                // PLAY
                btPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!mp.isPlaying()){
                            if(observer.stop.get() == true) {
                                observer = new MediaObserver();
                                new Thread(observer).start();
                            }
                            mp.start();
                        }
                        lbStatus.setText("Reproduciendo...");
                    }
                });

                // PAUSE
                btPause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mp.isPlaying()){
                            mp.pause();
                            lbStatus.setText("Pausa");
                        }
                    }
                });

                // STOP
                btStop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        observer.stop();
                        mp.stop();
                        try {
                            mp.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        progressBar.setProgress(0);
                        lbStatus.setText("Stop");
                    }
                });

                // MAS 10 SEGUNDOS
                btMas10seg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mp.seekTo(mp.getCurrentPosition() + 10000);
                    }
                });

                // MENOS 10 SEGUNDOS
                btMenos10seg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mp.seekTo(mp.getCurrentPosition() - 10000);
                    }
                });

                // COMPLETION
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        observer.stop();
                        mp.stop();
                        try {
                            mp.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        progressBar.setProgress(0);
                        lbStatus.setText("...");
                    }
                });

                break;
            }

            case VIDEO:{
                // Cargar video
                String vidAddress = "android.resource://"+getPackageName()+"/"+R.raw.video;
                Uri vidUri = Uri.parse(vidAddress);
                video.setVideoURI(vidUri);

                // Nueva barra de progreso
                videoObserver = new VideoObserver();
                new Thread(videoObserver).start();

                // Mostrar Video
                video.setVisibility(View.VISIBLE);

                // PLAY
                btPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!video.isPlaying()) {
                            if(videoObserver.stop.get() == true) {
                                videoObserver = new VideoObserver();
                                new Thread(videoObserver).start();
                            }
                            video.start();
                            lbStatus.setText("Reproduciendo...");
                        }
                    }
                });

                // PAUSE
                btPause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(video.isPlaying()) {
                            video.pause();
                            lbStatus.setText("Pausa");
                            isPaused = true;
                        }
                    }
                });

                // STOP
                btStop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        videoObserver.stop();
                        video.stopPlayback();
                        video.resume();
                        progressBar.setProgress(mp.getCurrentPosition());
                        lbStatus.setText("STOP");
                    }
                });

                // MAS 10 SEGUNDOS
                btMas10seg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        video.seekTo(video.getCurrentPosition() + 10000);
                    }
                });

                // MENOS 10 SEGUNDOS
                btMenos10seg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        video.seekTo(video.getCurrentPosition() - 10000);
                    }
                });

                // COMPLETION
                video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        videoObserver.stop();
                        video.stopPlayback();
                        video.resume();
                        progressBar.setProgress(video.getCurrentPosition());
                        lbStatus.setText("...");
                    }
                });

                break;
            }
        }
    }

    private void clearPlayers() {
        if (observer != null) {
            observer.stop();
        }

        if (videoObserver != null) {
            videoObserver.stop();
        }

        if (mp != null) {
            mp.stop();
        }

        if (video != null) {
            video.stopPlayback();
            video.resume();
        }
    }

    private void showMetadata(String source) {

        MediaMetadataRetriever meta = new MediaMetadataRetriever();

        switch (source) {
            case MUSICA_RAW_DIRECTO:
                meta.setDataSource(getApplicationContext(), Uri.parse("android.resource://" +
                        getPackageName() + "/" + R.raw.funk));
                break;

            case MUSICA_URI_RAW:
                meta.setDataSource(getApplicationContext(), Uri.parse("android.resource://" +
                        getPackageName() + "/" + R.raw.breakingin));
                break;

            case VIDEO:
                meta.setDataSource(getApplicationContext(), Uri.parse("android.resource://" +
                        getPackageName() + "/" + R.raw.video));
                break;
        }

        String title = "Titulo: " + meta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String artist = "Artista: " + meta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
        int miliseconds = Integer.parseInt(meta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        int seconds = miliseconds / 1000;
        String duration = "Duracion: " + seconds + " s";
        String date = "Fecha: " + meta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR);

        lbMetadata.setText(title + "\n" + artist + "\n" + duration + "\n" + date);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.stop();
    }

    // Barra progreso MediaPlayer
    private class MediaObserver implements Runnable {
        // Se define el stop para poder parar el thread desde la aplicación
        private AtomicBoolean stop = new AtomicBoolean(false);
        public void stop() {
            stop.set(true);
        }
        @Override
        public void run() {

            while (!stop.get()) {
                progressBar.setProgress((int)(mp.getCurrentPosition()
                        / (double)mp.getDuration()*100));
                try {
                    Thread.sleep(200);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // Barra progreso Video
    private class VideoObserver implements Runnable {
        // Se define el stop para poder parar el thread desde la aplicación
        private AtomicBoolean stop = new AtomicBoolean(false);
        public void stop() {
            stop.set(true);
        }
        @Override
        public void run() {

            while (!stop.get()) {
                progressBar.setProgress((int)(video.getCurrentPosition()
                        / (double)video.getDuration()*100));
                try {
                    Thread.sleep(200);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
