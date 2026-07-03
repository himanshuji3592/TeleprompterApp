package com.teleprompter.app.services;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.speech.RecognizerIntent;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class VoiceControlService extends Service implements RecognitionListener {

    private SpeechRecognizer speechRecognizer;
    private boolean isListening = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isListening) {
            startVoiceRecognition();
        }
        return START_STICKY;
    }

    private void startVoiceRecognition() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            speechRecognizer.setRecognitionListener(this);

            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

            speechRecognizer.startListening(intent);
            isListening = true;
        }
    }

    @Override
    public void onReadyForSpeech(android.os.Bundle params) {}

    @Override
    public void onBeginningOfSpeech() {}

    @Override
    public void onRmsChanged(float rmsdB) {}

    @Override
    public void onBufferReceived(byte[] buffer) {}

    @Override
    public void onEndOfSpeech() {}

    @Override
    public void onError(int error) {
        if (isListening) {
            startVoiceRecognition();
        }
    }

    @Override
    public void onResults(android.os.Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (matches != null && matches.size() > 0) {
            String command = matches.get(0).toLowerCase();
            processVoiceCommand(command);
        }

        if (isListening) {
            startVoiceRecognition();
        }
    }

    @Override
    public void onPartialResults(android.os.Bundle partialResults) {}

    @Override
    public void onEvent(int eventType, android.os.Bundle params) {}

    private void processVoiceCommand(String command) {
        // Process voice commands
        if (command.contains("start") || command.contains("play")) {
            sendBroadcast(new Intent("com.teleprompter.VOICE_COMMAND").putExtra("action", "start"));
        } else if (command.contains("stop") || command.contains("pause")) {
            sendBroadcast(new Intent("com.teleprompter.VOICE_COMMAND").putExtra("action", "stop"));
        } else if (command.contains("faster")) {
            sendBroadcast(new Intent("com.teleprompter.VOICE_COMMAND").putExtra("action", "faster"));
        } else if (command.contains("slower")) {
            sendBroadcast(new Intent("com.teleprompter.VOICE_COMMAND").putExtra("action", "slower"));
        } else if (command.contains("up")) {
            sendBroadcast(new Intent("com.teleprompter.VOICE_COMMAND").putExtra("action", "up"));
        } else if (command.contains("down")) {
            sendBroadcast(new Intent("com.teleprompter.VOICE_COMMAND").putExtra("action", "down"));
        } else if (command.contains("repeat")) {
            sendBroadcast(new Intent("com.teleprompter.VOICE_COMMAND").putExtra("action", "repeat"));
        } else if (command.contains("next")) {
            sendBroadcast(new Intent("com.teleprompter.VOICE_COMMAND").putExtra("action", "next"));
        }
    }

    @Override
    public void onDestroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
            isListening = false;
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
