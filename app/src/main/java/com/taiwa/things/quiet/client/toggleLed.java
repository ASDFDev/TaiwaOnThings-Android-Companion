package com.taiwa.things.quiet.client;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import org.quietmodem.Quiet.FrameTransmitter;
import org.quietmodem.Quiet.FrameTransmitterConfig;
import org.quietmodem.Quiet.ModemException;

import java.io.IOException;

class toggleLed {

    private Context globalContext;

    public toggleLed(Context context){
        globalContext = context;
    }

    void toggleRedLed(){
        try{
            String payload = "toggleRedLed";
            FrameTransmitterConfig transmitterConfig;
            FrameTransmitter transmitter;
            transmitterConfig = new FrameTransmitterConfig(globalContext,
                    globalContext.getResources().getString(R.string.quiet_profile));
            transmitter = new FrameTransmitter(transmitterConfig);
            new Thread(() -> {
                    /* Hack: Loop for 10 frames
                       TODO: Implement mechanism to kill runnable
                    */
                for (int i = 0; i < 10; i++) {
                    try {
                        transmitter.send(payload.getBytes());
                    } catch (IOException e) {
                        System.out.println("our message might be too long or the transmit queue full");
                    }
                    timeDelay(1000);
                }
            }).start();
        }catch (ModemException me) {
            modemExceptionDialog();
        } catch (IOException ioe) {
            System.out.println("IOException thrown");
        }
    }

    private void timeDelay(long t){
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            System.out.println("got interrupted!");
        }
    }

    private void modemExceptionDialog(){
        new AlertDialog.Builder(globalContext)
                .setTitle("Error")
                .setMessage("Your device does not support modulation")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> ((Activity)globalContext).finish())
                .create()
                .show();
    }


}
