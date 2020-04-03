package techtown.org.memorypatronum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class GameMain extends AppCompatActivity {
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_18dp);
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
                finish();
                break;
            case R.id.account:
                Toast.makeText(getApplicationContext(), "account clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onLevel1Click(View view){
        Intent intent = new Intent(getApplicationContext(), GameLevel_1.class);
        startActivity(intent);
    }


    public void onLevel2Click(View view){
        Intent intent = new Intent(getApplicationContext(), GameLevel_2.class);
        startActivity(intent);
    }

    public void onLevel3Click(View view){
        Intent intent = new Intent(getApplicationContext(), GameLevel_3.class);
        startActivity(intent);
    }

    public void onLevel4Click(View view){
        Intent intent = new Intent(getApplicationContext(), GameLevel_4.class);
        startActivity(intent);
    }

    public void onLevel5Click(View view){
        Intent intent = new Intent(getApplicationContext(), GameLevel_5.class);
        startActivity(intent);
    }



}
