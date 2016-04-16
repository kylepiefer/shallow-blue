package com.shallowblue.shallowblue;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by Kyle on 4/14/2016.
 */
public class SavedGameManager {

    private static byte[] getBytes(String data) {
        if (data == null || data.isEmpty()) return null;

        byte[] bytes = null;
        ByteArrayOutputStream byteArrayOutputStream = null;

        try {
            byteArrayOutputStream = new ByteArrayOutputStream(data.length());
            bytes = byteArrayOutputStream.toByteArray();
        } catch (Exception exception) {
            Log.e("SavedGameManager", exception.toString());
        } finally {
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (Exception e) {
                }
            }
        }

        return bytes;
    }

    private static boolean writeBytesToFile(byte[] bytes, File file) {
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

    private static byte[] readBytesFromFile(File file) {

    }

    private static File getFile(String filename, Context context) {
        File file = null;
        try {
            file = new File(context.getFilesDir(), filename);
        } catch (Exception exception) {
            file = null;
            Log.e("SavedGameManager", exception.toString());
        }
        return file;
    }

    public static boolean saveGame(Context context, GameBoard game) {
        if (context == null || game == null) return false;

        String gameData = game.pack();
        if (gameData == null || gameData.isEmpty()) return false;

        byte[] bytes = getBytes(gameData);
        if (bytes == null) return false;

        /*Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String filename = sdf.format(calendar.getTime());*/
        String filename = "savedGame";

        File file = getFile(filename, context);
        if (file == null) return false;

        boolean success = writeBytesToFile(bytes, file);
        if (!success) return false;

        return true;
    }

    public static boolean loadGame(Context context, String filename) {
        if (context == null || filename == null || filename.isEmpty()) return false;
        filename = "savedGame"; // TODO: Remove this.

        File file = getFile(filename, context);
        if (file == null) return false;

        try {
            int bufferSize = (int) file.length();
            byte[] compressedGameData = new byte[bufferSize];
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            bufferedInputStream.read(compressedGameData, 0, compressedGameData.length);

            byteArrayInputStream = new ByteArrayInputStream(compressedGameData);
            gzipInputStream = new GZIPInputStream(byteArrayInputStream, bufferSize);
            StringBuilder stringBuilder = new StringBuilder();

            byte[] gameDataBuffer = new byte[bufferSize];
            int totalBytesRead = 0;
            int bytesRead = 0;
            while ((bytesRead = gzipInputStream.read(gameDataBuffer, totalBytesRead, bufferSize)) != -1) {
                totalBytesRead = totalBytesRead + bytesRead;
                stringBuilder.append(new String(gameDataBuffer, 0, bytesRead));
            }
            String gameData = stringBuilder.toString();
            Log.d("SavedGameManager", gameData);

            GameBoard gameBoard = new GameBoard();
            gameBoard.unpack(gameData);
            GameBoard.activeGameBoard = gameBoard;

            returnValue = true;
        } catch (Exception exception) {
            Log.e("SavedGameManager", exception.toString());
        } finally {
            if (bufferedInputStream != null) {
                try { bufferedInputStream.close(); } catch (Exception e) { }
            }

            if (byteArrayInputStream != null) {
                try { byteArrayInputStream.close(); } catch (Exception e) { }
            }

            if (gzipInputStream != null) {
                try { gzipInputStream.close(); } catch (Exception e) { }
            }
        }

        return returnValue;
    }
}
