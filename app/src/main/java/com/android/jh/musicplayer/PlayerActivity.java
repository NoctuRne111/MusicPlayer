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
import android.widget.TextView;

import java.util.List;

public class PlayerActivity extends AppCompatActivity {

    ViewPager viewPager;
    ImageButton btnRew, btnPlay, btnFf;
    List<Music> datas;
    PlayerAdapter adapter;
    MediaPlayer player;
    SeekBar seekBar;
    TextView txtDuration,txtCurrent;

    //플레이어 상태 플러그
    private static final int PLAY = 0;
    private static final int PAUSE = 1;
    private static final int STOP = 2;

    private static int playStatus = STOP;
    //현재 음악 위치
    int position = 0;

    // 핸들러 상태 플래그
    public static final int PROGRESS_SET = 101;

    /*// 핸들러
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch(msg.what){
                case PROGRESS_SET:
                    if(player != null) {
                        seekBar.setProgress(player.getCurrentPosition());
                        txtCurrent.setText(player.getCurrentPosition());
                    }
                    break;
            }
        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        playStatus = STOP;
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        //레이아웃 초기화
        layoutInit();
        // 0. 데이터 가져오기
        datas = DataLoader.get(this);
        // 1. 뷰페이저 가져오기
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        // 2. 뷰페이저용 아답터 생성
        adapter = new PlayerAdapter(datas ,this);
        // 3. 뷰페이저 아답터 연결
        viewPager.setAdapter( adapter );
        // 4. 뷰 페이저 리스너 연결
        viewPager.addOnPageChangeListener(viewPagerListener);
        // 4-1. 페이지 트렌스 포머 연결
        viewPager.setPageTransformer(false,pageTransformer);


        //특정 페이지 호출
        returnPage();
    }

    private void returnPage() {
        // 특정 페이지 호출
        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            position = bundle.getInt("position");
            //실제 페이지 값 게산 처리
            //페이지 이동
            viewPager.setCurrentItem(position);
            // 첫페이지 일 경우 만 init 호출
            // 이유 : 첫페이지가 아닐경우 위에 setCurrentItem 에 의해서 ViewPagerdml onPageSelected가 호출된다.
            if(position == 0) {
                init();
            } else {
                viewPager.setCurrentItem(position);
            }
        }
    }

    private void layoutInit() {
        btnRew = (ImageButton) findViewById(R.id.btnRew);
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnFf = (ImageButton) findViewById(R.id.btnFf);

        btnRew.setOnClickListener(clickListener);
        btnPlay.setOnClickListener(clickListener);
        btnFf.setOnClickListener(clickListener);

        seekBar= (SeekBar)findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        txtDuration = (TextView)findViewById(R.id.txtDuration);
        txtCurrent = (TextView)findViewById(R.id.txtCurrent);
    }

    private void plyaerInit() {
        Uri musicUri = datas.get(position).uri;
        //시스템자원, 음원파일Uri
        player = MediaPlayer.create(this, musicUri);
        player.setLooping(false);// 반복여부
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

    //컨트롤러 정보 초기화
    private void init() {
        // 뷰페이저로 이동할 경우 플레이어 세팅된 값을 해제한 후 로직을 실행한다
        if(player != null){
            // 플레이어 현상태를 STOP으로 변경
            playStatus = STOP;
            // 아이콘을 플레이 버튼으로 변경
            btnPlay.setImageResource(android.R.drawable.ic_media_play);
            player.release();
        }
        // 플레이어 세팅
        plyaerInit();
        // 컨트롤러 세팅
        controllerInit();

        //미디어 플레이어에 완료 체크 리스너를 등록한다
        player.setOnCompletionListener(MediaPlayerListener);

        play();
    }

    private void controllerInit() {
        //SeekBar 길이
        seekBar.setMax(player.getDuration());
        // SeekBar 현재값을 0으로
        seekBar.setProgress(0);
        txtDuration.setText(convertMiliToTime(player.getDuration()) + "");
        //현재 실행시간을 0으로 설정
        txtCurrent.setText("0");
    }
    MediaPlayer.OnCompletionListener MediaPlayerListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            next();
        }
    };

    private void play() {
        switch (playStatus){
            case STOP :
                playStop();
                break;
            case PLAY :
                playPlay();
                break;
            case PAUSE :
                playPause();
                break;
        }
    }

    private void playStop() {
        // 플레이중이 아니면 음악 실행
        player.start();
        // subThread를 생성해서 미디어player의 현재 포지션 값을 seekbar를 변경해준다 매 1초마다
        playStatus = PLAY;
        //클릭시 버튼이미지 변경
        btnPlay.setImageResource(android.R.drawable.ic_media_pause);

        Thread thread = new TimerThread();
        // 새로운 쓰레드로 스타트
        thread.start();
    }

    class TimerThread extends Thread {
        // sub thread 를 생성해서 mediaplayer 의 현재 포지션 값으로 seekbar 를 변경해준다. 매 1초마다
        // sub thread 에서 동작할 로직 정의

        @Override
        public void run() {
        while (playStatus < STOP) {
            if(player != null) {
            // 이 부분은 메인쓰레드에서 동작하도록 Runnable 객체를 메인쓰레드에 던져준다
               runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                seekBar.setProgress(player.getCurrentPosition());
                                txtCurrent.setText(convertMiliToTime(player.getCurrentPosition()) + "");
                            } catch (Exception e){ }
                        }
                    });
                }
                try { Thread.sleep(1000); } catch (InterruptedException e) {}
            }
        }
    }

    private void playPlay() {
        // 플레이중이면 멈춤
        player.pause();
        playStatus = PAUSE;
        btnPlay.setImageResource(android.R.drawable.ic_media_play);
    }

    private void playPause() {
        // pause 상태이면 그자리부터 다시시작
        player.start();
        playStatus = PLAY;
        btnPlay.setImageResource(android.R.drawable.ic_media_pause);
    }

    private void prev() {
        if(position > 0)
            viewPager.setCurrentItem(position-1);
    }

    private void next() {
        if(position < datas.size())
            viewPager.setCurrentItem(position+1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(player != null){
            player.release(); // 사용이 끝나면 해제해야만 한다.
        }
        playStatus = STOP;
    }

    private String convertMiliToTime(long mili){
        long min = mili / 1000 / 60;
        long sec = mili / 1000 % 60;

        return String.format("%02d", min) + ":" + String.format("%02d",sec);
    }
    //viewpage 체인지 리스너
    ViewPager.OnPageChangeListener viewPagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
        @Override
        public void onPageSelected(int position) {
            PlayerActivity.this.position = position;
            init();
        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
           //만약 음원이 움직일때의 소리가 싫다면 onStopTrackingTouch로 이동
            if(player != null  && fromUser)
                player.seekTo(progress);
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }
    };

    // 페이지 트렌스포머
    ViewPager.PageTransformer pageTransformer = new ViewPager.PageTransformer() {
        @Override
        public void transformPage(View page, float position) {
            //현재 Page의 위치가 조금이라도 바뀔때마다 호출되는 메소드
            //첫번째 파라미터 : 현재 존재하는 View 객체들 중에서 위치가 변경되고 있는 View들
            //두번째 파라미터 : 각 View 들의 상대적 위치( 0.0 ~ 1.0 : 화면 하나의 백분율)

            //           1.현재 보여지는 Page의 위치가 0.0
            //           Page가 왼쪽으로 이동하면 값이 -됨. (완전 왼쪽으로 빠지면 -1.0)
            //           Page가 오른쪽으로 이동하면 값이 +됨. (완전 오른쪽으로 빠지면 1.0)

            //주의할 것은 현재 Page가 이동하면 동시에 옆에 있는 Page(View)도 이동함.
            //첫번째와 마지막 Page 일때는 총 2개의 View가 메모리에 만들어져 잇음.
            //나머지 Page가 보여질 때는 앞뒤로 2개의 View가 메모리에 만들어져 총 3개의 View가 instance 되어 있음.
            //ViewPager 한번에 1장의 Page를 보여준다면 최대 View는 3개까지만 만들어지며
            //나머지는 메모리에서 삭제됨.-리소스관리 차원.

            //position 값이 왼쪽, 오른쪽 이동방향에 따라 음수와 양수가 나오므로 절대값 Math.abs()으로 계산
            //position의 변동폭이 (-2.0 ~ +2.0) 사이이기에 부호 상관없이 (0.0~1.0)으로 변경폭 조절
            //주석으로 수학적 연산을 설명하기에는 한계가 있으니 코드를 보고 잘 생각해 보시기 바랍니다.
            float normalizedposition = Math.abs( 1 - Math.abs(position) );

            page.setAlpha(normalizedposition);  //View의 투명도 조절
            page.setScaleX(normalizedposition/2 + 0.5f); //View의 x축 크기조절
            page.setScaleY(normalizedposition/2 + 0.5f); //View의 y축 크기조절
            page.setRotationY(position * 80); //View의 Y축(세로축) 회전 각도
        }
    };
}
