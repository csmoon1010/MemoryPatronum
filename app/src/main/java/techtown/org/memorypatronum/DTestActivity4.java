package techtown.org.memorypatronum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DTestActivity4 extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_test4);

        Intent intent = getIntent();
        int correctNum = intent.getIntExtra("correct", 0);
        int totalNum = intent.getIntExtra("total", 0);

        TextView scoreText = (TextView)findViewById(R.id.scoreText);
        scoreText.setText(totalNum + "개 중" + correctNum + "개를\n 맞추셨습니다!");
    }

    public void backClick(View view){
        Toast.makeText(getApplicationContext(), "회상 테스트를 종료합니다.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
