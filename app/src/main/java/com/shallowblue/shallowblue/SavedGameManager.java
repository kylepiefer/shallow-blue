package com.shallowblue.shallowblue;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Kyle on 4/14/2016.
 */
public class SavedGameManager {

    public SavedGameManager() {}

    private byte[] getBytesFromString(String data) {
        if (data == null || data.isEmpty()) return null;

        byte[] bytes = null;
        Charset charset = Charset.forName("US-ASCII");

        try {
            bytes = data.getBytes(charset);
        } catch (Exception exception) {
            Log.e("SavedGameManager", exception.toString());
            bytes = null;
        }

        return bytes;
    }

    private boolean writeBytesToFile(byte[] bytes, File file) {
        boolean returnValue = false;
        if (bytes == null) return returnValue;

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            returnValue = true;
        } catch (Exception exception) {
            Log.e("SavedGameManager", exception.toString());
        } finally {
            if (fileOutputStream != null) {
                try { fileOutputStream.close(); } catch (Exception e) { }
            }
        }

        return returnValue;
    }

    private byte[] readBytesFromFile(File file) {
        if (file == null) return null;

        int fileSize = (int) file.length();
        byte[] bytes = new byte[fileSize];

        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            bufferedInputStream.read(bytes, 0, fileSize);
        } catch (Exception exception) {
            Log.e("SavedGameManager", exception.toString());
            bytes = null;
        } finally {
            if (fileInputStream != null) {
                try { fileInputStream.close(); } catch (Exception e) {}
            }

            if (bufferedInputStream != null) {
                try { bufferedInputStream.close(); } catch (Exception e) {}
            }
        }

        return bytes;
    }

    private File getFile(String filename, Context context) {
        File file = null;
        try {
            file = new File(context.getFilesDir(), filename);
        } catch (Exception exception) {
            Log.e("SavedGameManager", exception.toString());
            file = null;
        }
        return file;
    }

    public boolean saveGame(Context context, GameBoard game) {
        if (context == null || game == null) return false;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String filename = sdf.format(calendar.getTime());

        return saveGame(context, filename, game);
    }

    private String getStringFromBytes(byte[] bytes) {
        if (bytes == null) return null;

        String string = null;
        Charset charset = Charset.forName("US-ASCII");

        try {
            string = new String(bytes, charset);
        } catch (Exception exception) {
            Log.e("SavedGameManager", exception.toString());
            string = null;
        }

        return string;
    }

    public boolean loadGame(Context context, String filename) {
        if (context == null || filename == null || filename.isEmpty()) return false;

        File file = getFile(filename, context);
        if (file == null) return false;

        byte[] bytes = readBytesFromFile(file);
        if (bytes == null) return false;

        String gameData = getStringFromBytes(bytes);
        if (gameData == null) return false;

        GameBoard gameBoard = null;
        try {
            gameBoard = GameBoard.unpack(gameData);
        } catch (Exception exception) {
            return false;
        }

        GameBoard.activeGameBoard = gameBoard;
        return true;
    }

    public boolean saveGame(Context context, String filename, GameBoard game) {
        if (context == null || game == null || filename == null || filename.isEmpty()) return false;

        String gameData = game.pack();
        if (gameData == null || gameData.isEmpty()) return false;

        byte[] bytes = getBytesFromString(gameData);
        if (bytes == null) return false;

        File file = getFile(filename, context);
        if (file == null) return false;

        boolean success = writeBytesToFile(bytes, file);
        if (!success) return false;

        return true;
    }

    public List<String> getSavedGameFileNames(Context context) {
        if (context == null) return null;

        File[] savedGameFiles = context.getFilesDir().listFiles();
        if (savedGameFiles == null) return null;

        List<String> savedGameFileNames = new ArrayList<String>();
        for (File savedGameFile : savedGameFiles) {
            if (savedGameFile.getName().equalsIgnoreCase("instant-run")) continue;
            savedGameFileNames.add(savedGameFile.getName());
        }

        return savedGameFileNames;
    }

    public boolean removeSavedGame(Context context, String filename) {
        if (context == null || filename == null) return false;

        try {
            String directory = context.getFilesDir().getAbsolutePath();
            File savedGame = new File(directory, filename);
            boolean result = savedGame.delete();
            if (!result) return false;
        } catch (Exception exception) {
            return false;
        }

        return true;
    }
}
