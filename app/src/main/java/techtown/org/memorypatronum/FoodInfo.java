package techtown.org.memorypatronum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class FoodInfo extends AppCompatActivity {

    private ImageButton wheat;
    private ImageButton vegetable;
    private ImageButton peanut;
    private ImageButton bean;
    private ImageButton hen;
    private ImageButton berry;
    private ImageButton fish;
    private ImageButton oliveoil;
    private ImageButton wine;
    public TextView tv2;

    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_info);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_18dp);

        wheat = (ImageButton) findViewById(R.id.wheat);
        wheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodInfo.this);
                builder.setMessage(Html.fromHtml("<strong><font face=\"hangeulnurib\">" + "통곡물의 '아베난쓰라마이드' 물질이 치매 예방에 효과가 있다. 특히 통곡물 중 귀리에 이 물질이 많이 들어있다."
                        + "</font></strong><br>"))
                        .setNegativeButton("취소", null);

                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        vegetable = (ImageButton) findViewById(R.id.vegetable);
        vegetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodInfo.this);
                builder.setMessage(Html.fromHtml("<strong><font face=\"hangeulnurib\">"
                        + "채소, 특히 잎채소에는 비타민 K와 엽산 같은 영양소가 풍부해 인지력 감퇴를 늦춰준다. 특히 시금치에는 배추의 2배, 당근의 3배가 되는 단백질이 있어 치매 예방 효과가 더욱 좋다."
                        + "</font></strong><br>"))
                        .setNegativeButton("취소", null);

                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        peanut = (ImageButton) findViewById(R.id.peanut);
        peanut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodInfo.this);
                builder.setMessage(Html.fromHtml("<strong><font face=\"hangeulnurib\">"
                        + "견과류는 뇌세포에 쌓이는 노폐물을 제거하고, 뇌 기능을 활성화 해 기억력과 집중력을 높여준다. 노화를 막는 항산화제도 많아 뇌세포 노화 방지에 도움이 된다."
                        + "</font></strong><br>"))
                        .setNegativeButton("취소", null);

                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        bean = (ImageButton) findViewById(R.id.bean);
        bean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodInfo.this);
                builder.setMessage(Html.fromHtml("<strong><font face=\"hangeulnurib\">"
                        + "콩류 설명"
                        + "</font></strong><br>"))
                        .setNegativeButton("취소", null);

                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        hen = (ImageButton) findViewById(R.id.hen);
        hen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodInfo.this);
                builder.setMessage("hoho")
                        .setNegativeButton("취소", null);

                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        berry = (ImageButton) findViewById(R.id.berry);
        berry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodInfo.this);
                builder.setMessage(Html.fromHtml("<strong><font face=\"hangeulnurib\">"
                        + "베리류가 지닌 안토시아닌과 폴리페롤 성분은 뇌세포 노화 방지를 도와준다. 특히 블루베리는 미국 신시내티 의대에서 진행한 임상실험에서 치매 위험이 높은 노인들의 기억력을 개선하고 뇌 기능을 활성화 하는 효과가 있다고 발표했다."
                        + "</font></strong><br>"))
                        .setNegativeButton("취소", null);

                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        fish = (ImageButton) findViewById(R.id.fish);
        fish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodInfo.this);
                builder.setMessage(Html.fromHtml("<strong><font face=\"hangeulnurib\">"
                        + "생선에는 오메가-3가 풍부하게 들어있는데, 이 오메가-3는 혈액 순환을 잘 되게 하고, 두뇌에 영양을 공급해준다."
                        + "</font></strong><br>"))
                        .setNegativeButton("취소", null);

                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        oliveoil = (ImageButton) findViewById(R.id.oliveoil);
        oliveoil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodInfo.this);
                builder.setMessage(Html.fromHtml("<strong><font face=\"hangeulnurib\">"
                        + "치매 예방을 위해서는 불포화 지방산이 많이 들어있는 음식을 섭취해야 한다. 올리브유는 불포화 지방산이 많이 들어있는 식물성 기름이다."
                        + "</font></strong><br>"))
                        .setNegativeButton("취소", null);

                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        wine = (ImageButton) findViewById(R.id.wine);
        wine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodInfo.this);
                builder.setMessage(Html.fromHtml("<strong><font face=\"hangeulnurib\">"
                        + "베를린 샤리테 의대에서는 레드와인의 레스베라트롤이 기억력과 인지능력을 높인다는 연구 결과를 발표했다. 하지만 반 잔 이상 마시는 것은 효과가 없다고 한다."
                        + "</font></strong><br>"))
                        .setNegativeButton("취소", null);

                AlertDialog alert = builder.create();
                alert.show();

            }
        });






    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            /*case R.id.mic:
                on = getResources().getDrawable(R.drawable.ic_mic_black_18dp, null);
                off = getResources().getDrawable(R.drawable.ic_mic_off_black_18dp, null);
                micItem = (ActionMenuItemView) findViewById(R.id.mic);
                speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                mRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                vRecognizer = new vRecog(speechIntent, mRecognizer, getApplicationContext(), micItem, 1, on, off);

                vRecognizer.checkPermission(DWriteActivity.this);
                Toast.makeText(getApplicationContext(), "mic clicked", Toast.LENGTH_SHORT).show();
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }


}
