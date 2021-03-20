package lyrically.photovideomaker.particl.ly.musicallybeat.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import lyrically.photovideomaker.particl.ly.musicallybeat.MyApplication;
import lyrically.photovideomaker.particl.ly.musicallybeat.model.ImagesFolder;
import lyrically.photovideomaker.particl.ly.musicallybeat.model.SoundFile;
import lyrically.photovideomaker.particl.ly.musicallybeat.model.SoundStorage;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class StorageUtils {

    public static ArrayList<ImagesFolder> SearchStorageAsync(Context context) {

        MyApplication.getInstance().imageFolders.clear();

        boolean boolean_folder = false;
        int position = 0;

        Cursor cursor = null;
        String[] projection;
        projection = new String[]{
                MediaStore.MediaColumns.DATA
        };
        String absolutePathOfImage = null;

        try {
            cursor = context.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    null);

            assert cursor != null;
            while (cursor.moveToNext()) {

                absolutePathOfImage = cursor.getString(0);

                File file = new File(absolutePathOfImage);
                if (!file.exists()) {
                    continue;
                }

                String fileDir = file.getParentFile().getName();

                for (int i = 0; i < MyApplication.getInstance().imageFolders.size(); i++) {
                    if (MyApplication.getInstance().imageFolders.get(i).getFolder_name().equals(fileDir)) {
                        boolean_folder = true;
                        position = i;
                        break;
                    } else {
                        boolean_folder = false;
                    }
                }

                if (boolean_folder) {
                    ArrayList<String> al_path = new ArrayList<>(MyApplication.getInstance().imageFolders.get(position).getFolder_images());
                    al_path.add(absolutePathOfImage);
                    MyApplication.getInstance().imageFolders.get(position).setFolder_images(al_path);
                } else {
                    ArrayList<String> al_path = new ArrayList<>();
                    al_path.add(absolutePathOfImage);
                    ImagesFolder obj_model = new ImagesFolder();
                    obj_model.setFolder_name(fileDir);
                    obj_model.setFolder_images(al_path);

                    MyApplication.getInstance().imageFolders.add(obj_model);

                }
            }

            Collections.reverse(MyApplication.getInstance().imageFolders);

            return MyApplication.getInstance().imageFolders;

        } catch (Exception e) {
            ConstantUtils.showErrorLog(e.toString());
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static ArrayList<SoundStorage> searchSoundAsync(Context context) {

        MyApplication.getInstance().soundArrayList.clear();

        boolean boolean_folder = false;
        int position = 0;

        Cursor cursor = null;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
                + " AND " + MediaStore.Audio.Media.MIME_TYPE + " LIKE 'audio/mpeg'";

        // + " AND " + MediaStore.Audio.Media.DURATION + " > 5000";
        String[] projection;
        projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.ALBUM_ID
        };
        String absolutePathOfImage = null;
        String absolutePathOfSound = null;

        try {
            cursor = context.getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    selection,
                    null,
                    null);

            assert cursor != null;
            while (cursor.moveToNext()) {

                absolutePathOfSound = cursor.getString(2);
                absolutePathOfImage = cursor.getString(3);

                File file = new File(absolutePathOfImage);
                if (!file.exists()) {
                    continue;
                }

                String fileDir = file.getParentFile().getName();

                for (int i = 0; i < MyApplication.getInstance().soundArrayList.size(); i++) {
                    if (MyApplication.getInstance().soundArrayList.get(i).getFolder_name().equals(fileDir)) {
                        boolean_folder = true;
                        position = i;
                        break;
                    } else {
                        boolean_folder = false;
                    }
                }

                if (boolean_folder) {
                    ArrayList<SoundFile> al_path = new ArrayList<>(MyApplication.getInstance().soundArrayList.get(position).getSound_files());
                    SoundFile soundFile = new SoundFile(absolutePathOfSound, absolutePathOfImage);
                    al_path.add(soundFile);

                    MyApplication.getInstance().soundArrayList.get(position).setSound_files(al_path);
                } else {
                    ArrayList<SoundFile> soundFiles = new ArrayList<>();
                    SoundFile soundFile = new SoundFile(absolutePathOfSound, absolutePathOfImage);
                    soundFiles.add(soundFile);

                    SoundStorage obj_model = new SoundStorage();
                    obj_model.setFolder_name(fileDir);
                    obj_model.setSound_files(soundFiles);

                    MyApplication.getInstance().soundArrayList.add(obj_model);
                }
            }

            return MyApplication.getInstance().soundArrayList;

        } catch (Exception e) {
            ConstantUtils.showErrorLog(e.toString());
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return null;
    }
}
