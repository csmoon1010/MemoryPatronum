package techtown.org.memorypatronum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DiaryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
    }

    public void onWriteClick(View view){
        Intent intent = new Intent(getApplicationContext(), DWriteActivity.class);
        startActivity(intent);
    }

    public void onMemoryClick(View view){
        Intent intent = new Intent(getApplicationContext(), DMemoryActivity.class);
        startActivity(intent);
    }

    public void onTestClick(View view){
        Intent intent = new Intent(getApplicationContext(), DTestActivity.class);
        startActivity(intent);
    }
}

