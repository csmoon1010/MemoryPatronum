package techtown.org.memorypatronum;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {
    Handler handler = new Handler();
    Runnable r = new Runnable(){
        @Override
        public void run() {
            if(SharedPreference.getId(StartActivity.this).length() == 0){
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                MyApplication myApp = (MyApplication)getApplication();
                myApp.setLoginID(SharedPreference.getId(StartActivity.this));
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
            /*Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();*/
        }
    };
    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀바 없애기
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);
    }

    protected void onResume(){
        super.onResume();
        handler.postDelayed(r, 3000); //3초 뒤 로그인 화면으로 전환
    }

    protected void onPause(){
        super.onPause();
        handler.removeCallbacks(r);
    }
}
