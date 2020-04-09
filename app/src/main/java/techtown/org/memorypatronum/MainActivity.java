package techtown.org.memorypatronum;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

//merge check
public class MainActivity extends AppCompatActivity {
    Toolbar myToolbar;
    TextView ruleText;
    String[] rules = {"일주일에 3번 이상 걷기", "생선과 채소 골고루 먹기", "부지런히 읽고 쓰기",
    "술은 적게 마시기", "담배는 피우지 말기", "머리 다치지 않도록 조심하기",
    "정기적으로 건강검진 받기", "가족, 친구들과 자주 소통하기", "매년 치매조기검진 받기"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        ruleText = (TextView) findViewById(R.id.ruleText);
        long seed = System.currentTimeMillis();
        Random rand = new Random(seed);
        int randNumber = rand.nextInt(rules.length);
        ruleText.setText(rules[randNumber]);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_18dp);
    }

    //toolbar에 main_toolbar.xml 인플레이트
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //toolbar에 추가된 항목의 select 이벤트 처리
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("앱을 종료하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
            case R.id.account:
                Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void exerciseClick(View view){
        Intent intent = new Intent(getApplicationContext(), ExerciseActivity.class);
        startActivity(intent);
    }

    public void diaryClick(View view){
        Intent intent = new Intent(getApplicationContext(), DiaryActivity.class);
        startActivity(intent);
    }

    public void foodClick(View view){
        Intent intent = new Intent(getApplicationContext(), FoodMain.class);
        startActivity(intent);
    }

    public void gameClick(View view){
        Intent intent = new Intent(getApplicationContext(), GameMain.class);
        startActivity(intent);
    }
}
