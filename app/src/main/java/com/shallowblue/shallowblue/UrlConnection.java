package com.shallowblue.shallowblue;

/**
 * Created by kofe on 4/7/16.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.os.AsyncTask;
import android.widget.TextView;


public class UrlConnection {
    //static String URLPath="http://104.154.22.179/MyFirstServlet";
    static String URLPath="http://104.154.22.179/FirstServlet/ShowDataServlet";
    getRemoteAI remote;

    public UrlConnection(String... args)
    {
        //new Connection().execute(args);
        remote=new getRemoteAI();
    }

    public String UrlRequest(String... args)
    {
        //new Request().execute(args);
        try
        {
            //String url = URLPath+"?str=80980980980980";//+args[0];
            String url = URLPath+"?str="+args[0]+"&str2="+args[1]+"&str3="+args[2];
            //url = url.replaceAll("(\r\n|\n)", "<lol>");
            //System.out.println(url);
            String res="";
            LinkedBlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<Runnable>();
            ExecutorService exec = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, blockingQueue);
            res=new getRemoteAI().executeOnExecutor(exec,new String[] { url }).get(5, TimeUnit.SECONDS);
            //res= new getRemoteAI().execute(new String[] { url }).get(5, TimeUnit.SECONDS);
            //remote.execute(new String[] { url });
            return res;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
        /*
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        catch (TimeoutException e)
        {
            e.printStackTrace();
        }
        */


        //return "failed to retrieve return string";
    }




    private class getRemoteAI extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {

            String output = "";
            for (String url : urls) {
                //System.out.println("doInBackground");
                output = getOutputFromUrl(url);
            }
            System.out.println(output);
            return output;
        }


        private String getOutputFromUrl(String... urls)
        {
            StringBuffer output = new StringBuffer("");
            try
            {
                String url=urls[0];
                //System.out.println(url);
                InputStream stream = getHttpConnection(url);
                //System.out.println("getOutputFromUrl");
                BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));
                String s = "";
                while ((s = buffer.readLine()) != null)
                {
                    //System.out.println(s);
                    output.append(s);
                }
                //System.out.println(output.toString());
                //return output.toString();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            catch(NullPointerException e2)
            {
                e2.printStackTrace();
            }
            return output.toString();
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString) throws IOException
        {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            try
            {
                //System.out.println("getHttpConnection");
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setConnectTimeout(3000);
                httpConnection.setReadTimeout(3000);
                //connection.setDoOutput(true);
                //connection.setDoInput(true);
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();
                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    stream = httpConnection.getInputStream();
                }
                //System.out.println("getHttpConnection");
            }
            catch (Exception ex)
            {
                System.out.println("request failed");
                ex.printStackTrace();
            }
            return stream;
        }

        @Override
        protected void onPostExecute(String output) {
            super.onPostExecute(output);
            //outputText.setText(output);
            //help_text.setText(output);

            //result=output;
        }
    }
}
