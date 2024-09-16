package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognizerIntent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechRecognitionHelper {
    protected static final int REQUEST_CODE_SPEECH_INPUT = 1;

    public static void startSpeechRecognition(Activity activity) {
        PackageManager packageManager = activity.getPackageManager();
        if (packageManager.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0).size() > 0) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");
            activity.startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } else {
            Toast.makeText(activity, "Speech recognition is not available", Toast.LENGTH_SHORT).show();
        }
    }

    public static ArrayList<String> getSpeechResults(Intent data) {
        return data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
    }
}