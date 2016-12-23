package com.jason.loaddatademo.util;

/**
 * Created by Administrator on 2016/10/13.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.jason.loaddatademo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

/**
 * Created by csonezp on 16-1-12.
 */
public class SaveImageTask extends AsyncTask<String, Void, File> {
    private
    final Context context;

    public SaveImageTask(Context context) {
        this.context = context;
    }

    @Override
    protected File doInBackground(String... params) {
        String url = params[0]; // should be easy to extend to share multiple images at once
        try {
            return Glide
                    .with(context)
                    .load(url)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get() // needs to be called on background thread
                    ;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(File result) {
        if (result == null) {
            return;
        }
        String path = result.getPath();
//        File file = new File(Environment.getExternalStorageDirectory(),filename);
//        FileOutputStream outStream = new FileOutputStream(file);
//        outStream.write(filecontent.getBytes());
//        outStream.close();
//        FileUtil.copyFile(path, FileUtil.getPubAlbumDir().getPath() + UUID.randomUUID().toString() + ".gif");
//        GlobalUtil.shortToast(context, context.getString(R.string.save_success));
    }
}
