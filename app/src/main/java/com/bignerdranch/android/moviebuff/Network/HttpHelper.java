package com.bignerdranch.android.moviebuff.Network;

import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Vaisakh on 28-10-2016.
 * Helps establish Http connection and retrieve data from url
 */
class HttpHelper {

    // Private Members

    /**
     * The data stream obtained from the url
     */
    private byte[] dataStream;
    /**
     * The URLConnection object for the given URL
     */
    private HttpURLConnection urlConnection;

    // Constants

    private static final String LOG_TAG = HttpHelper.class.getSimpleName();

    // Constructor(s)

    /**
     * Initializes the helper with the url to fetch data from.
     *
     * @param url The URL where the data needs to be received from
     */
    private HttpHelper(URL url) throws IOException {
        urlConnection = (HttpURLConnection) url.openConnection();
        connect();
        fetchData();
    }

    /**
     * Initializes the helper with the url string to fetch data from.
     *
     * @param urlString The URL string where the data needs to be received from
     */
    private HttpHelper(String urlString) throws IOException {
        this(new URL(urlString));
    }

    /**
     * Initializes the helper with the uri to fetch data from.
     *
     * @param uri The Uri where the data needs to be received from
     */
    HttpHelper(Uri uri) throws IOException {
        this(uri.toString());
    }

    // Overridden Methods

    /**
     * Directly converts the data stream to string format
     *
     * @return the string representation of the data if it exists. Else returns the default.
     */
    @Override
    public String toString() {
        if (getDataStream() != null)
            return new String(getDataStream());
        return super.toString();
    }

    // Getter Methods

    /**
     * @return Returns the data stream byte array that was obtained
     */
    private byte[] getDataStream() {
        return dataStream;
    }

    // Private Methods

    /**
     * Initializes the connection with default settings
     * Request Method: GET
     * Connection Timeout: 15s
     * Read Timeout: 10s
     */
    private void connect() throws IOException {
        urlConnection.setRequestMethod("GET");
        urlConnection.setConnectTimeout(15000);
        urlConnection.setReadTimeout(10000);
        urlConnection.connect();

        int responseCode = urlConnection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_ACCEPTED) {
            Log.e(LOG_TAG, "Bad connection gateway.\nResponse code: " + responseCode);
        }
    }

    /**
     * Helper method to fetch data from the URL.
     * Updates the data stream
     *
     * @throws IOException
     */
    private void fetchData() throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = null;

        try {
            inputStream = urlConnection.getInputStream();

            while ((bytesRead = inputStream.read(buffer)) != - 1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            dataStream = outputStream.toByteArray();
        } finally {
            if (inputStream != null) inputStream.close();
            outputStream.close();
        }
    }

    // Package Private Methods

    /**
     * Closes all connections
     */
    void disconnect() {
        urlConnection.disconnect();
    }

}
