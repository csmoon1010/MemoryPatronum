package techtown.org.memorypatronum;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.w3c.dom.Text;

import java.util.Locale;

import static java.lang.Thread.sleep;

public class ExVideoActivity extends AppCompatActivity {
    public final static String[] urls = new String[]{
        "http://docs.google.com/uc?export=download&id=1YLHjrTna4vx1xg_lDUm8rXhZ1uJwfyIz",
                "http://docs.google.com/uc?export=download&id=1tyfSDGHcgdn5bTVCgNDsfKCdDdHNvzOh",
                "http://docs.google.com/uc?export=download&id=1LD5RGz6boGbVuXMK-C7AJ1_-72FcJ8eq",
                "http://docs.google.com/uc?export=download&id=1-LGccPn6f9cLhpPxK_Sro3X9Ckw2SlMZ",
                "http://docs.google.com/uc?export=download&id=1006BzGOcNAI5x5SXOHdAXK23f2D92VO5",
                "http://docs.google.com/uc?export=download&id=1Ut1yZ33CHBS7FVj-_jD2DeRRHAqRhVhh",
                "http://docs.google.com/uc?export=download&id=1QsJOsibx0tmn7WfqKb5V7ciVzil48_EG",
                "http://docs.google.com/uc?export=download&id=1p-jdk6oomRtWu9QjqQE1F977mhIG6Bfm"};
    public final static String[] titles = new String[]{
            "온몸 자극하기", "손 운동(박수)", "손 운동(쥐기)", "팔운동(두팔로 하기)",
            "팔운동(한팔로 하기)", "기 만들기", "기 펼치기", "온몸 가다듬기"
    };
    public final static String[] explains = new String[]{
        "1. 머리박수\n2. 어깨박수\n3. 엉덩이박수\n4. 세로박수\n순서대로 2회 실시합니다. 어깨 회전범위를 확대하고 상체 혈액순환 촉진 및 뇌자극에 도움이 됩니다.",
            "1. 주먹박수 4회와 세로박수 4회\n2. 손끝박수 4회와 세로박수 4회\n3. 손바닥박수 4회와 세로박수 4회\n4. 손목박수 4회와 세로박수 4회\n" +
                    "순서대로 2회 실시합니다. 말초신경을 자극, 혈액순환 촉진 및 인지기능 향상에 도움이 됩니다.",
            "1. 세로박수\n2. 가로박수\n3. 가로쥐기\n4. 깍지끼기\n순서대로 2회 실시합니다. 인지기능 향상 및 운동능력 향상에 도움이 됩니다.",
            "1. 양팔 앞으로 밀기\n2. 양팔 위로 밀기\n3. 양팔 옆으로 밀기\n4. 양팔 교차하여 밀기\n순서대로 2회 실시합니다. 상체 혈액순환 촉진, 인지기능 향상 및 운동능력 향상에 도움이 됩니다.",
            "1. 한 팔씩 번갈아 밀기. 오른손을 시작으로 앞, 위, 옆, 사선, 위, 옆, 사선, 앞의 순서대로 진행합니다.\n2회 실시합니다. 상체 혈액순환 촉진, 인지기능 향상 및 운동능력 향상에 도움이 됩니다.",
            "1. 기운모으기\n2. 기운 키우기\n3. 기운 크게 키우기\n4. 기운 펼치기\n순서대로 2회 실시합니다. 후두엽, 두정엽 및 전두엽을 활성화시킵니다.",
            "1. 밑면 동그라미 그리기\n2. 앞면 동그라미 그리기\n3. 앞과 옆면에 동그라미 그리기\n순서대로 2회 실시합니다.후두엽, 두정엽 및 전두엽을 활성화시킵니다.",
            "1. 크게 숨들여 마시기\n2. 크게 숨내쉬기\n3. 숨들여 마시기\n4. 숨 내쉬기\n순서대로 2회 실시합니다. 어깨 및 가슴근육을 이완시킵니다."
    };
    public final static int URL = 1;
    public final static int SDCARD = 2;
    int num;
    VideoView videoView;
    ImageButton btnStart, btnStop;
    TextView title, explain;

    TextToSpeech tts;

    Toolbar myToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_video);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_18dp);
        /**
         * 영상을 출력하기 위한 비디오뷰
         * SurfaceView를 상속받아 만든 클래스
         * 웬만하면 VideoView는 그때 그때 생성해서 추가 후 사용
         * 화면 전환 시 여러 UI가 있을 때 화면에 제일 먼저 그려져서 보기에 좋지 않을 때가 있다
         * 예제에서 xml에 추가해서 해봄
         */
        //레이아웃 위젯 findViewById
        videoView = (VideoView) findViewById(R.id.view);
        btnStart = (ImageButton) findViewById(R.id.btnStart);
        btnStop = (ImageButton) findViewById(R.id.btnStop);
        title = (TextView)findViewById(R.id.exerciseTitle);
        explain = (TextView)findViewById(R.id.exerciseExplain);

        //미디어컨트롤러 추가하는 부분
        MediaController controller = new MediaController(ExVideoActivity.this);
        videoView.setMediaController(controller);

        //비디오뷰 포커스를 요청함
        videoView.requestFocus();

        Intent intent = getIntent();
        num = intent.getIntExtra("LEVEL", 0);
        title.setText(titles[num]);
        explain.setText(explains[num]);
        int type = URL;
        switch (type) {
            case URL:
                //동영상 경로가 URL일 경우
                videoView.setVideoURI(Uri.parse(urls[num]));
                break;

            case SDCARD:
                //동영상 경로가 SDCARD일 경우
                String path = Environment.getExternalStorageDirectory()
                        + "/TestVideo.mp4";
                videoView.setVideoPath(path);
                break;
        }


        //동영상이 재생준비가 완료되었을 때를 알 수 있는 리스너 (실제 웹에서 영상을 다운받아 출력할 때 많이 사용됨)
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Toast.makeText(ExVideoActivity.this,
                        "동영상이 준비되었습니다. \n'시작' 버튼을 누르세요", Toast.LENGTH_SHORT).show();
            }
        });

        //동영상 재생이 완료된 걸 알 수 있는 리스너
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //동영상 재생이 완료된 후 호출되는 메소드
                Toast.makeText(ExVideoActivity.this,
                        "동영상 재생이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR){
                    int language = tts.setLanguage(Locale.KOREAN);
                    if(language == TextToSpeech.LANG_NOT_SUPPORTED){
                        Toast.makeText(getApplicationContext(), "지원하지 않는 언어", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "TTS작업 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //toolbar에 main_toolbar.xml 인플레이트
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sub_toolbar2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //toolbar에 추가된 항목의 select 이벤트 처리
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                ExVideoActivity.this.finish();
                break;
            case R.id.sound:
                Toast.makeText(getApplicationContext(), "음성 출력", Toast.LENGTH_SHORT).show();
                //음성출력
                String speech1 = title.getText().toString();
                String speech2 = explain.getText().toString();
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) { //권한허용 안함 --> 권한요청
                    ActivityCompat.requestPermissions(ExVideoActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                }
                else{ //권한 허용함
                    try{
                        //Log.i("speech", speech3);
                        tts.setPitch(1.0f);
                        tts.setSpeechRate(1.0f);
                        tts.speak(speech1, TextToSpeech.QUEUE_FLUSH, null, null);
                        try{
                            sleep(3000);
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                        tts.speak(speech2, TextToSpeech.QUEUE_FLUSH, null, null);
                    }catch(SecurityException e){
                        e.printStackTrace();
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onDestroy(){
        if(tts != null){
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    //시작 버튼 onClick Method
    public void StartButton(View v) {
        playVideo();
    }

    //정지 버튼 onClick Method
    public void StopButton(View v) {
        stopVideo();
    }

    //동영상 재생 Method
    private void playVideo() {
        //비디오를 처음부터 재생할 때 0으로 시작(파라메터 sec)
        //videoView.seekTo(0);
        videoView.start();
    }

    //동영상 정지 Method
    private void stopVideo() {
        //비디오 재생 잠시 멈춤
        if(videoView.canPause()){
            int currentPosition = videoView.getCurrentPosition();
            videoView.pause();
        }
        //videoView.pause();
        //비디오 재생 완전 멈춤
        //videoView.stopPlayback();
        //videoView를 null로 반환 시 동영상의 반복 재생이 불가능
//        videoView = null;
    }

}
