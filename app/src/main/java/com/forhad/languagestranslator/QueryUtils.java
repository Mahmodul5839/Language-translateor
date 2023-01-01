package com.forhad.languagestranslator;

import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

public class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils() {
    }

    private static URL createUrl(String stringUrl) {
        try {
            return new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x005b, code lost:
        if (r2 != null) goto L_0x005d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x005d, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0071, code lost:
        if (r2 != null) goto L_0x005d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0074, code lost:
        return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static String makeHttpRequest(URL r6) throws IOException {
        /*
            java.lang.String r0 = ""
            if (r6 != 0) goto L_0x0005
            return r0
        L_0x0005:
            r1 = 0
            r2 = 0
            java.net.URLConnection r3 = r6.openConnection()     // Catch:{ IOException -> 0x0063 }
            java.net.HttpURLConnection r3 = (java.net.HttpURLConnection) r3     // Catch:{ IOException -> 0x0063 }
            r1 = r3
            r3 = 10000(0x2710, float:1.4013E-41)
            r1.setReadTimeout(r3)     // Catch:{ IOException -> 0x0063 }
            r3 = 15000(0x3a98, float:2.102E-41)
            r1.setConnectTimeout(r3)     // Catch:{ IOException -> 0x0063 }
            java.lang.String r3 = "GET"
            r1.setRequestMethod(r3)     // Catch:{ IOException -> 0x0063 }
            r1.connect()     // Catch:{ IOException -> 0x0063 }
            int r3 = r1.getResponseCode()     // Catch:{ IOException -> 0x0063 }
            r4 = 200(0xc8, float:2.8E-43)
            if (r3 != r4) goto L_0x0033
            java.io.InputStream r3 = r1.getInputStream()     // Catch:{ IOException -> 0x0063 }
            r2 = r3
            java.lang.String r3 = readFromStream(r2)     // Catch:{ IOException -> 0x0063 }
            r0 = r3
            goto L_0x0056
        L_0x0033:
            java.lang.String r3 = LOG_TAG     // Catch:{ IOException -> 0x0063 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0063 }
            r4.<init>()     // Catch:{ IOException -> 0x0063 }
            java.lang.String r5 = "Error response code: "
            r4.append(r5)     // Catch:{ IOException -> 0x0063 }
            int r5 = r1.getResponseCode()     // Catch:{ IOException -> 0x0063 }
            r4.append(r5)     // Catch:{ IOException -> 0x0063 }
            java.lang.String r4 = r4.toString()     // Catch:{ IOException -> 0x0063 }
            android.util.Log.e(r3, r4)     // Catch:{ IOException -> 0x0063 }
            java.lang.String r3 = LOG_TAG     // Catch:{ IOException -> 0x0063 }
            java.lang.String r4 = r6.toString()     // Catch:{ IOException -> 0x0063 }
            android.util.Log.e(r3, r4)     // Catch:{ IOException -> 0x0063 }
        L_0x0056:
            if (r1 == 0) goto L_0x005b
            r1.disconnect()
        L_0x005b:
            if (r2 == 0) goto L_0x0074
        L_0x005d:
            r2.close()
            goto L_0x0074
        L_0x0061:
            r3 = move-exception
            goto L_0x0075
        L_0x0063:
            r3 = move-exception
            java.lang.String r4 = LOG_TAG     // Catch:{ all -> 0x0061 }
            java.lang.String r5 = "Problem retrieving the JSON results."
            android.util.Log.e(r4, r5, r3)     // Catch:{ all -> 0x0061 }
            if (r1 == 0) goto L_0x0071
            r1.disconnect()
        L_0x0071:
            if (r2 == 0) goto L_0x0074
            goto L_0x005d
        L_0x0074:
            return r0
        L_0x0075:
            if (r1 == 0) goto L_0x007a
            r1.disconnect()
        L_0x007a:
            if (r2 == 0) goto L_0x007f
            r2.close()
        L_0x007f:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: greenage.languages.translator.QueryUtils.makeHttpRequest(java.net.URL):java.lang.String");
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                output.append(line);
            }
        }
        return output.toString();
    }

    private static String extractFromJsonTranslation(String stringJSON) {
        if (TextUtils.isEmpty(stringJSON)) {
            return null;
        }
        try {
            return new JSONObject(stringJSON).getJSONArray("text").getString(0);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the JSON results", e);
            return "";
        }
    }

    private static ArrayList<String> extractFromJsonLanguages(String stringJSON) {
        ArrayList<String> languagesList = new ArrayList<>();
        if (TextUtils.isEmpty(stringJSON)) {
            return null;
        }
        try {
            JSONObject baseJsonResponseLangs = new JSONObject(stringJSON).optJSONObject("langs");
            Iterator<String> iter = baseJsonResponseLangs.keys();
            GlobalVars.LANGUAGE_CODES.clear();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    languagesList.add(baseJsonResponseLangs.get(key).toString());
                    GlobalVars.LANGUAGE_CODES.add(key);
                } catch (JSONException e) {
                    Log.e("QueryUtils", "Problem parsing the JSON results", e);
                }
            }
        } catch (JSONException e2) {
            Log.e("QueryUtils", "Problem parsing the JSON results", e2);
        }
        return languagesList;
    }

    public static String fetchTranslation(String requestUrl) {
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(createUrl(requestUrl));
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        return extractFromJsonTranslation(jsonResponse);
    }

    public static ArrayList<String> fetchLanguages(String requestUrl) {
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(createUrl(requestUrl));
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        return extractFromJsonLanguages(jsonResponse);
    }
}
