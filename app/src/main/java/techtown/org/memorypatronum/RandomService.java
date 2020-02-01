package techtown.org.memorypatronum;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Random;

public class RandomService extends Service {
    private final IBinder mBinder = new LocalBinder();
    private final Random mGenerator = new Random();

    public class LocalBinder extends Binder {
        RandomService getService(){
            return RandomService.this;
        }
    }

    public IBinder onBind(Intent intent){
        return mBinder;
    }
    public int getRandomNumber(){
        //DB에서 데이터 개수 받아오기.
        return mGenerator.nextInt(6)+1;
    }
}

