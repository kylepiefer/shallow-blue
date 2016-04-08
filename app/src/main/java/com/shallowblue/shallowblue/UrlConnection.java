package com.shallowblue.shallowblue;

/**
 * Created by kofe on 4/7/16.
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.AsyncTask;


public class UrlConnection {
    static String URLPath="http://www.google.com";
    static URL url;
    static HttpURLConnection connection;

    class Connection extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strs)
        {
            if(strs[0].equals("disconnect"))
            {
                System.out.println("disconnecting");
                try
                {
                    connection.disconnect();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("disconnection failed");
                }
            }
            else
            {
                System.out.println("connecting to the server");
                try
                {
                    url = new URL(URLPath);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setConnectTimeout(30000);
                    connection.setReadTimeout(100000);
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setUseCaches(false);
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("connection", "Keep-Alive");
                    //connection.connect();
                    if (connection.getResponseCode() != 200)
                    {
                        throw new RuntimeException("failed to request url");
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("connection failed");
                }
            }

            return null;
        }
        @Override
        protected void onPostExecute(String unused) {
            super.onPostExecute(unused);
            //access InputStream is
        }

    }
    class Request extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strs)
        {
            try
            {

                if (connection.getResponseCode() != 200)
                {
                    throw new RuntimeException("failed to request url");
                }
                else
                {
                    OutputStream out = connection.getOutputStream();
                    String entry=strs[0];
                    out.write(entry.getBytes());
                    out.flush();
                    out.close();
                    if(connection.getResponseCode()==200)
                    {
                        InputStream is = connection.getInputStream();
                        //convert Stream to String
                        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
                        return s.hasNext() ? s.next() : "";
                    }
                }

            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("connection failed");
            }

            return null;
        }
        @Override
        protected void onPostExecute(String unused) {
            super.onPostExecute(unused);
            //access InputStream is
        }

    }
}
