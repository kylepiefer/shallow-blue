package com.shallowblue.shallowblue;
import android.os.AsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

/**
 * Created by kofe on 4/6/16.
 */
public class ApacheClient {
    protected class httpStore extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strs) {
            String reply = null;
            String temp1="";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost storeVal = new HttpPost("http://shallowBlue");
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("tag", "shallowBlue"));
            nameValuePairs.add(new BasicNameValuePair("GameBoard", strs[0]));

            try {
                UrlEncodedFormEntity httpEntity = new UrlEncodedFormEntity(nameValuePairs);
                storeVal.setEntity(httpEntity);
                HttpResponse response = httpclient.execute(storeVal);
                temp1 = EntityUtils.toString(response.getEntity());
            }
            catch (Exception e) {
                System.out.println("HTTP IO Exception.");
                e.printStackTrace();
            }

            try {
                JSONArray jsonArray = new JSONArray(temp1);
                reply = jsonArray.getString(0);
            } catch (JSONException e) {
                System.out.println("Error in JSON decoding");
                e.printStackTrace();
            }
            return reply;
        }

        @Override
        protected void onPostExecute(String res) {
            System.out.println("gameBoard Passed");
        }
    }

    protected class httpQuery extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strs)
        {
            HttpClient client=new DefaultHttpClient();
            HttpPost val=new HttpPost("http://shallowBlue");
            String returnVal="";
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("tag","shallowBlue"));
            try
            {
                UrlEncodedFormEntity httpEntity = new UrlEncodedFormEntity(nameValuePairs);
                val.setEntity(httpEntity);
                HttpResponse response = client.execute(val);
                String temp = EntityUtils.toString(response.getEntity());
                JSONArray jsonArray = new JSONArray(temp);
                returnVal=jsonArray.getString(2);

            }catch(Exception e){
                e.printStackTrace();
            }
            return returnVal;
        }
        @Override
        protected void onPostExecute(String res)
        {
            System.out.println("new move got");
        }
    }
}
