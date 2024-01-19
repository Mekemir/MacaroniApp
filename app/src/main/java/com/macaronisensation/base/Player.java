package com.macaronisensation.base;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

import com.macaronisensation.R;

import java.io.IOException;

public class Player {

    public MediaPlayer play(int index, Context ctx) {
        AssetManager am;
        MediaPlayer player = new MediaPlayer();
        try {
            am = ctx.getAssets();
            AssetFileDescriptor afd;
            if (index == 1) {
                afd = ctx.getResources().openRawResourceFd(R.raw.soneone);
            } else if (index == 2) {
                afd = ctx.getResources().openRawResourceFd(R.raw.songtwo);
            } else if (index == 3) {
                afd = ctx.getResources().openRawResourceFd(R.raw.songthree);
            } else {
                return null;
            }
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
                    afd.getLength());
            player.prepare();
            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    //mp.release();
                }

            });
            player.setLooping(true);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return player;
    }
}
