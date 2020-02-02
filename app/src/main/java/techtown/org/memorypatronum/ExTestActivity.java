package techtown.org.memorypatronum;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraDevice;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.ArrayList;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;

public class ExTestActivity extends AppCompatActivity implements SurfaceHolder.Callback{
    boolean isRecording;
    MediaRecorder mediaRecorder;
    SurfaceHolder surfaceHolder;
    Button recordButton;
    TextView explainText;
    Integer level;
    Camera camera;
    private CameraDevice mCameraDevice;
    static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isRecording = false;
        mediaRecorder = new MediaRecorder();
        //camera = new Camera();
        /*if(ContextCompat.checkSelfPermission(ExTestActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            CameraManager cameraManager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
            Log.v("camera", "open try");
            String cameraId = getBackFacingCameraId(cameraManager);
            try {
                cameraManager.openCamera(cameraId, mStateCallback, null);
                Log.v("camera", "open success");
            }catch(CameraAccessException e){
                e.printStackTrace();
            }
        }*/

        setContentView(R.layout.exercise_test);

        //level 넘긴 것 받아오기
        Intent intent = getIntent();
        explainText = (TextView)findViewById(R.id.explainText);
        level = intent.getIntExtra("LEVEL",0);
        explainText.setText("level :" + level);

        //videoview 추가
        SurfaceView videoView = (SurfaceView)findViewById(R.id.videoView);
        surfaceHolder = videoView.getHolder();
        surfaceHolder.addCallback(this);

        //recordButton에 listener 부여
        recordButton = (Button)findViewById(R.id.recordB);
        recordButton.setOnClickListener(rButtonOnClickListener);
    }


    private Button.OnClickListener rButtonOnClickListener = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {
            if(ContextCompat.checkSelfPermission(ExTestActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(ExTestActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(ExTestActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            ){ //권한 허용
                startMediaRecorder();
            }
            else{ //권한 허용 안됨 --> 권한 확인 메시지
                String[] permissionTypes= new String[3];
                ArrayList<String> permissions = new ArrayList<String>();
                permissionTypes[0] = Manifest.permission.RECORD_AUDIO;
                permissionTypes[1] = Manifest.permission.CAMERA;
                permissionTypes[2] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                for(int i = 0; i < 3; i++){
                    if(ContextCompat.checkSelfPermission(ExTestActivity.this, permissionTypes[i]) //오디오 권한 확인
                            != PackageManager.PERMISSION_GRANTED) { //권한허용 안함 --> 권한요청
                        permissions.add(permissionTypes[i]);
                        //ActivityCompat.requestPermissions(ExTestActivity.this, new String[]{requestString[i]}, REQUEST_CODE);
                    }
                }
                String[] requests = new String[permissions.size()];
                requests = permissions.toArray(requests);
                ActivityCompat.requestPermissions(ExTestActivity.this, requests, REQUEST_CODE);
            }
        }
    };

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3){
    }
    public void surfaceCreated(SurfaceHolder arg0){
        //prepareMediaRecorder();
    }
    public void surfaceDestroyed(SurfaceHolder arg0){
    }

    private void startMediaRecorder(){
        try{
            if(isRecording){ //녹화중일때
                mediaRecorder.stop();
                mediaRecorder.release();
                finish(); //데이터 처리 후 결과 알려주는 화면으로!
            }
            else{
                InitializeCamera();
                InitializeMediaRecorder();
                prepareMediaRecorder();
                mediaRecorder.start();
                isRecording = true;
                recordButton.setText("중지");
            }
        } catch(IllegalStateException e){
            e.printStackTrace();
        }
    }

    /*private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            mCameraDevice = cameraDevice;
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {

        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {

        }
    };

    private String getBackFacingCameraId(CameraManager cManager) {
        try {
            for (final String cameraId : cManager.getCameraIdList()) {
                CameraCharacteristics characteristics = cManager.getCameraCharacteristics(cameraId);
                int cOrientation = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (cOrientation == CameraCharacteristics.LENS_FACING_FRONT) return cameraId;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    private void InitializeCamera(){
        camera = android.hardware.Camera.open(CameraInfo.CAMERA_FACING_FRONT);
        camera.setDisplayOrientation(90);
        /*if(ContextCompat.checkSelfPermission(ExTestActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            CameraManager cameraManager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
            Log.v("camera", "open try");
            String cameraId = getBackFacingCameraId(cameraManager);
            try {
                cameraManager.openCamera(cameraId, mStateCallback, null);
                Log.v("camera", "open success");
            }catch(CameraAccessException e){
                e.printStackTrace();
            }
        }*/
    }

    private void InitializeMediaRecorder(){
        camera.unlock();
        mediaRecorder.setCamera(camera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        CamcorderProfile camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        //mediaRecorder.setProfile(CamcorderProfile.get(CameraInfo.CAMERA_FACING_FRONT, CamcorderProfile.QUALITY_HIGH));
        mediaRecorder.setProfile(camcorderProfile);
        mediaRecorder.setOrientationHint(270);
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

