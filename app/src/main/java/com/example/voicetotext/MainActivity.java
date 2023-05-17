package com.example.voicetotext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn;
    TextView tv;
    Intent intent;
    SpeechRecognizer mRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn);
        tv = findViewById(R.id.tv);
        btn.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) //권한요청
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, 1);

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(reconitionListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(),"이 앱은 RECORD_AUDIO 권한이 필요함",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private RecognitionListener reconitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) { // 준비
            btn.setText("음성인식 대기중...");
            tv.setText("");
        }

        @Override
        public void onBeginningOfSpeech() { // 시작될때

        }

        @Override
        public void onRmsChanged(float v) {

        }

        @Override
        public void onBufferReceived(byte[] bytes) {

        }

        @Override
        public void onEndOfSpeech() {   // 끝난 후
            btn.setText("버튼을 누른 후 말하세요");
        }

        @Override
        public void onError(int i) {    //에러 발생 시
            tv.setText("말이 없네요...");
            btn.setText("버튼을 누른 후 말하세요");
        }

        @Override
        public void onResults(Bundle bundle) {
            ArrayList<String> mResult = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            tv.setText(rs[0]);
        }

        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }
    };

    @Override
    public void onClick(View view) {
        btn.setText("음성 인식중...");
        mRecognizer.startListening(intent);
    }
}