package techtown.org.memorypatronum;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.ImageButton;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class VoiceRecognition {
    static final int REQUEST_CODE = 1;
    SpeechRecognizer recognizer;
    Intent intent;
    Context context;
    ImageButton button;
    int mode;
    String voiceResult = "";
    public String returnString = "";

    VoiceRecognition(Intent i, SpeechRecognizer r, Context c, ImageButton btn, int m){
        intent = i;
        recognizer = r;
        context = c;
        button = btn;
        mode = m;
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, c.getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        recognizer.setRecognitionListener(voiceListener);
    }

    public void checkPermission(Activity a){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) { //권한허용 안함 --> 권한요청
            ActivityCompat.requestPermissions(a, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_CODE);
        }
        else{ //권한 허용함
            try{
                recognizer.startListening(intent);
            }catch(SecurityException e){
                e.printStackTrace();
            }
        }
    }

    public void checkVoice(String s){

    }

    private RecognitionListener voiceListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {

        }

        @Override
        public void onBeginningOfSpeech() {
            button.setImageResource(R.drawable.activemicrophone);
            //Toast.makeText(context, "말해주세요", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRmsChanged(float v) {

        }

        @Override
        public void onBufferReceived(byte[] bytes) {

        }

        @Override
        public void onEndOfSpeech() {
        }

        @Override
        public void onError(int i) {
            button.setImageResource(R.drawable.stopmicrophone);
            //Toast.makeText(context, "다시 한번 말해주세요", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle bundle) {
            String result = "";
            result = recognizer.RESULTS_RECOGNITION;
            ArrayList<String> resultArray = bundle.getStringArrayList(result);
            String[] resultString = new String[resultArray.size()];
            resultArray.toArray(resultString);
            //Toast.makeText(context, resultString[0], Toast.LENGTH_SHORT).show();
            voiceResult = resultString[0];
            Log.i("result", "speechend" + voiceResult);
            checkVoice(voiceResult);
            button.setImageResource(R.drawable.stopmicrophone);
        }

        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }
    };
}
