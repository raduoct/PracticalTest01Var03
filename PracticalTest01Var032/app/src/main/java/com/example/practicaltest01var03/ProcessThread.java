package com.example.practicaltest01var03;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Date;
import java.util.Random;

public class ProcessThread extends Thread{

    private boolean isRunning = true;
    private final Context context;
    private final Random random = new Random();
    private final double sum;
    private final double difference;

    public ProcessThread(Context context, int firstNumber, int secondNumber) {
        this.context = context;
        sum = firstNumber + secondNumber;
        difference = firstNumber - secondNumber;
    }

    @Override
    public void run() {
        while (isRunning) {
            sleep();
            sendMessage();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(Constants.SLEEP_TIME);
        } catch (InterruptedException interruptedException) {
            Log.d("ProcessThread", "Thread has stopped!");
        }
    }

    private void sendMessage() {
        Intent intent = new Intent();
        intent.setAction(Constants.actionTypes[0]);
        intent.putExtra(Constants.BROADCAST_RECEIVER_EXTRA,
                new Date(System.currentTimeMillis()) + " " + sum);
        context.sendBroadcast(intent);

        Intent intent2 = new Intent();
        intent2.setAction(Constants.actionTypes[1]);
        intent2.putExtra(Constants.BROADCAST_RECEIVER_EXTRA,
                new Date(System.currentTimeMillis()) + " " + difference);
        context.sendBroadcast(intent);
    }

    public void stopThread() {
        isRunning = false;
    }
}