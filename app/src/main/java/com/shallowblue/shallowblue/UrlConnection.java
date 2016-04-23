package com.shallowblue.shallowblue;

/**
 * Created by kofe on 4/7/16.
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;


public class UrlConnection {
    static String URLPath="http://104.154.22.179/MyFirstServlet";
    static URL url;
    static HttpURLConnection connection;
    String res;

    public UrlConnection(String... args)
    {
        new Connection().execute(args);
    }
    public String UrlRequest(String... args)
    {
        //new Request().execute(args);
        try {
            return new Request().execute(args).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return "lalalalalalallalalalalalalalal";
    }

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
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("connection", "Keep-Alive");

                    connection.connect();

                    /*
                    System.out.println("start connecting");
                    if (connection.getResponseCode() != 200)
                    {
                        throw new RuntimeException("failed to request url");
                    }
                    if(connection.getResponseCode() == 200)
                    {
                        System.out.println("connected");
                    }
                    */

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

                if (connection.getResponseCode() == 200)
                {
                    throw new RuntimeException("failed to request url");
                }
                else
                {
                    System.out.println("requesting");
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
