package com.example.noyan.mediaplayer;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String MAINACTIVITY_LOG = MainActivity.class.getName();

    @BindView(R.id.geri_sarma)
    Button geriSar;

    @BindView(R.id.oynatma)
    Button oynat;

    @BindView(R.id.ileri_sarma)
    Button ileriSar;

    @BindView(R.id.baslanganc_saniyesi)
    TextView sarkiBaslangicZamani;

    @BindView(R.id.bitis_saniyesi)
    TextView sarkiBitisZamani;

    @BindView(R.id.sarki_seekbar)
    SeekBar seekBar;

    // TODO 2 ) Müzik dosyasının çalması için MediaPlayer tanımladık
    private MediaPlayer mediaPlayer;

    // TODO 3 ) Sarki başlangıç ve bitiş zamanını belirleme
    private double baslancicZamani = 0;
    private double bitisZamani = 0;

    // TODO 4 ) Sarki ileri sarma ve geri sarma ( 5 sn )
    private int ileriSarmaZamani = 5000;
    private int geriSarmaZamani = 5000;

    // TODO 5 ) Sarkinin bitip bitmediğini kontrol etme
    public static boolean sarkiKontrol = false;

    // TODO 6 ) Oynetma button basıldı mı basılmadı mı
    public static boolean oynatKontrol = false;

    // TODO 7 ) Handler tanımladık uygulama çalışırken seekbar hareket etsin diye
    private Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // TODO 8 ) Seekbar basılmayacak
        seekBar.setClickable(false);

        // TODO 9 ) Müzik dosyası tanımlanılacak
        mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.nomatterwhatyoudo);



        // TODO 10 ) Geri sarma tuşuna bir kere basıldığında
        geriSar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baslancicZamani = 0;
                bitisZamani = 0;
                seekBar.setProgress(0);
                Toast.makeText(getApplicationContext(), "Müzik Başa Alındı",Toast.LENGTH_SHORT).show();
                mediaPlayer.seekTo(0);
                mediaPlayer.stop();
            }
        });

        // TODO 11 ) Geri sarma tuşuna uzun basıldığında
        geriSar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int temp = (int)baslancicZamani;

                if((temp-geriSarmaZamani)>0){
                    baslancicZamani = baslancicZamani - geriSarmaZamani;
                    mediaPlayer.seekTo((int) baslancicZamani);
                    Toast.makeText(getApplicationContext(),"5 sn geri gitti",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"5 sn geri gidemezsin",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });


        // TODO 12 ) Geri sarma tuşuna bir kere basıldığında
        ileriSar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitisZamani = mediaPlayer.getDuration();
                seekBar.setProgress((int)bitisZamani);
                mediaPlayer.seekTo((int) bitisZamani);
                Toast.makeText(getApplicationContext(), "Müzik Bitti",Toast.LENGTH_SHORT).show();
                mediaPlayer.stop();
            }
        });

        // TODO 13 ) Geri sarma tuşuna uzun basıldığında
        ileriSar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int temp = (int)baslancicZamani;

                if((temp+ileriSarmaZamani)<=bitisZamani){
                    baslancicZamani = baslancicZamani + ileriSarmaZamani;
                    mediaPlayer.seekTo((int) baslancicZamani);
                    Toast.makeText(getApplicationContext(),"5 sn ileri sarma.",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"5 sn ileri sarılmaz.",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        // TODO 14 ) Oynat tuşuna uzun basıldığında
        oynat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(oynatKontrol == true){
                    mediaPlayer.pause();
                    Toast.makeText(getApplicationContext(), "Müzik Başlamadı.",Toast.LENGTH_SHORT).show();
                    oynat.setText(R.string.durdurma);
                    oynatKontrol = false;
                }
                else{
                    mediaPlayer.start();
                    oynat.setText(R.string.oynat);
                    Toast.makeText(getApplicationContext(), "Müzik Başladı.",Toast.LENGTH_SHORT).show();

                    baslancicZamani = mediaPlayer.getCurrentPosition();
                    bitisZamani = mediaPlayer.getDuration();

                    // TODO 15 ) Şarkı Başladığında
                    if (sarkiKontrol == false) {
                        seekBar.setMax((int) bitisZamani);
                        sarkiKontrol = true;
                    }

                    sarkiBaslangicZamani.setText(String.format("%d dk, %d sn",
                            TimeUnit.MILLISECONDS.toMinutes((long) baslancicZamani),
                            TimeUnit.MILLISECONDS.toSeconds((long) baslancicZamani) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                            baslancicZamani))));

                    sarkiBitisZamani.setText(String.format("%d dk, %d sn",
                            TimeUnit.MILLISECONDS.toMinutes((long) bitisZamani),
                            TimeUnit.MILLISECONDS.toSeconds((long) bitisZamani) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                            bitisZamani))));

                    // TODO 16 ) Şarkı SeekBar baslangıç zamanını belirleme
                    seekBar.setProgress((int)baslancicZamani);

                    // TODO 17 ) Şarkı SeekBar Hareketi belirleme
                    myHandler.postDelayed(sarkiSeekBarHareket,100);

                    oynatKontrol = true;
                }

            }
        });

    }


    // TODO 18 ) Şarkı SeekBar Hareketi ( Uygulama çalışırken arka planda bunun çalışması)
    private Runnable sarkiSeekBarHareket = new Runnable() {
        public void run() {
            baslancicZamani = mediaPlayer.getCurrentPosition();

            sarkiBaslangicZamani.setText(String.format("%d dk, %d sn",
                    TimeUnit.MILLISECONDS.toMinutes((long) baslancicZamani),
                    TimeUnit.MILLISECONDS.toSeconds((long) baslancicZamani) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                    baslancicZamani))));

            seekBar.setProgress((int)baslancicZamani);
            myHandler.postDelayed(this, 100);
        }
    };
}
