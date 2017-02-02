package com.android.jh.musicplayer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    ViewPager viewPager;
    ImageButton btnRew, btnPlay, btnFf;
    ArrayList<Music> datas;
    PlayerAdapter adapter;

    MediaPlayer player;
    SeekBar seekBar;

    //플레이어 상태 플러그
    private static final int PLAY = 0;
    private static final int PAUSE = 1;
    private static final int STOP = 2;

    private static int playStatus ;
    //현재 음악 위치
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);


        btnRew = (ImageButton) findViewById(R.id.btnRew);
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnFf = (ImageButton) findViewById(R.id.btnFf);

        btnRew.setOnClickListener(clickListener);
        btnPlay.setOnClickListener(clickListener);
        btnPlay.setOnClickListener(clickListener);

        seekBar= (SeekBar)findViewById(R.id.seekBar);

        // 0. 데이터 가져오기
        datas = DataLoader.get(this);

        // 1. 뷰페이저 가져오기
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        // 2. 뷰페이저용 아답터 생성
        adapter = new PlayerAdapter(datas ,this);
        // 3. 뷰페이저 아답터 연결
        viewPager.setAdapter( adapter );

        // 특정 페이지 호출
        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            position = bundle.getInt("position");

            //실제 페이지 값 게산 처리

            //페이지 이동
            viewPager.setCurrentItem(position);
        }

    }

    View.OnClickListener clickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnPlay:
                    play();
                    break;
                case R.id.btnRew:
                    prev();
                    break;
                case R.id.btnFf:
                    next();
                    break;
            }
        }
    };

    private void play() {

        switch (playStatus){
            case STOP :// 플레이중이 아니면 음악 실행
                Uri musicUri = datas.get(position).uri;
                //시스템자원, 음원파일Uri
                player = MediaPlayer.create(this, musicUri);

                player.setLooping(false);// 반복여부
                player.start();
                playStatus = PLAY;
                //클릭시 버튼이미지 변경
                btnPlay.setImageResource(android.R.drawable.ic_media_pause);
                break;
            case PLAY : // 플레이중이면 멈춤
                player.pause();
                playStatus = PAUSE;
                btnPlay.setImageResource(android.R.drawable.ic_media_play);
                break;
            case PAUSE : // pause 상태이면 그자리부터 다시시작
                player.start();
                playStatus = PLAY;
                btnPlay.setImageResource(android.R.drawable.ic_media_pause);
                break;
        }
    }

    private void prev() {

    }

    private void next() {

    }
}
