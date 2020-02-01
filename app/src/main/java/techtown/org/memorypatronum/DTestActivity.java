package techtown.org.memorypatronum;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DTestActivity extends AppCompatActivity {
    TextView problemText;
    RandomService mService;
    boolean mBound = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_test1);
        problemText = (TextView)findViewById(R.id.problemText);
    }

    protected void onStart(){
        super.onStart();
        Intent intent = new Intent(this, RandomService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    protected void onStop() {
        super.onStop();
        if(mBound){
            unbindService(mConnection);
            mBound = false;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            RandomService.LocalBinder binder = (RandomService.LocalBinder)service;
            mService = binder.getService();
            mBound = true;
            int num = mService.getRandomNumber();
            problemText.setText(num+"");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    public void onStartClick(View view){

    }
}

