package com.bjedrasik.elcontacto;


import android.os.AsyncTask;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class HttpHelper extends AsyncTask<ContactModel, Void, String> {

    @Override
    protected String doInBackground(ContactModel... params) {
        ContactModel contact = params[0];
        //TODO: rewrite body to json
        String body = "";// "<contact><id>" + contact.Id + "</id><dateofbirth>"+ contact.DateOfBirth +"</dateofbirth><name>"+ contact.Name +"</name><phone>" + contact.Phone + "</phone><photo>" + contact.PhotoURL + "</photo><skype>" + contact.Skype + "</skype><surname>" + contact.Surname + "</surname></contact>";
        String response = "No response";

        try {
            URL url = new URL("http", WebConfig.host, WebConfig.port, WebConfig.apiDirectory + "contacts");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setChunkedStreamingMode(0);
                urlConnection.setRequestProperty("content-type", "application/xml; charset=ISO-8859-1"); // TODO change content type

                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                out.write(body.getBytes());
                out.flush();

//                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                response = urlConnection.getResponseCode() + " " + urlConnection.getResponseMessage();
            }
            catch (Exception e) {
                System.out.println(e);
            }
            finally {
                urlConnection.disconnect();
            }
        }
        catch (MalformedURLException e) {
            System.out.println(e);
        }
        catch (IOException e) {
            System.out.println(e);
        }

        return response;
    }

    protected void onPostExecute(String response) {
        onResponse(response);
    }

    public abstract void onResponse(String response);
}
