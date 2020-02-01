package techtown.org.memorypatronum;

import android.content.Intent;
import android.graphics.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class ExTestActivity extends AppCompatActivity implements SurfaceHolder.Callback{
    boolean isRecording;
    MediaRecorder mediaRecorder;
    SurfaceHolder surfaceHolder;
    Button recordButton;
    TextView explainText;
    Integer level;
    Camera camera;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isRecording = false;
        mediaRecorder = new MediaRecorder();
        startMediaRecorder();

        setContentView(R.layout.exercise_test);

        Intent intent = getIntent();
        explainText = (TextView)findViewById(R.id.explainText);
        level = intent.getIntExtra("LEVEL",0);
        explainText.setText("level :" + level);


        SurfaceView videoView = (SurfaceView)findViewById(R.id.videoView);
        surfaceHolder = videoView.getHolder();
        surfaceHolder.addCallback(this);

        recordButton = (Button)findViewById(R.id.recordB);
        recordButton.setOnClickListener(rButtonOnClickListener);
    }

    private Button.OnClickListener rButtonOnClickListener = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {
            if(isRecording){ //녹화중
                mediaRecorder.stop();
                mediaRecorder.release();
                finish();
            }else{ //중지 상태
                mediaRecorder.start();
                isRecording=true;
                recordButton.setText("중지");
            }
        }
    };

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3){
    }
    public void surfaceCreated(SurfaceHolder arg0){
        prepareMediaRecorder();
    }
    public void surfaceDestroyed(SurfaceHolder arg0){
    }

    private void startMediaRecorder(){
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        CamcorderProfile camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);

        mediaRecorder.setProfile(camcorderProfile);
        mediaRecorder.setOrientationHint(90);
        mediaRecorder.setOutputFile("/sdcard/video.mp4"); //DB로 변경 OR  openCV 처리로
        mediaRecorder.setMaxDuration(60000); //파일 길이
        mediaRecorder.setMaxFileSize(5000000); //파일사이즈
    }

    private void prepareMediaRecorder(){
        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
        try{
            mediaRecorder.prepare();
        }catch(IllegalStateException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

