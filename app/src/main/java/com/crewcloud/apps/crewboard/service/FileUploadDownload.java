package com.crewcloud.apps.crewboard.service;

import com.crewcloud.apps.crewboard.util.CrewBoardApplication;
import com.crewcloud.apps.crewboard.util.TimeUtils;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

/**
 * Created by dazone on 3/6/2017.
 */

public class FileUploadDownload {
    public static String rootFile =CrewBoardApplication.getInstance().getmPrefs().getServerSite()+"/UI/MobileMail3/MobileFileUpload.ashx?";

    public static String uploadFileToServer(String filePath, String fileName) {
        String url = rootFile +
                "sessionId=" + CrewBoardApplication.getInstance().getPreferenceUtilities().getCurrentMobileSessionId() + "&languageCode="
                + Locale.getDefault().getLanguage().toUpperCase() + "&timeZoneOffset="
                + TimeUtils.getTimezoneOffsetInMinutes();

        String boundary = "7dfe519300448";
        String delimiter = "\r\n--" + boundary + "\r\n";

        try {
            String postDataBuilder = delimiter +
                    "Content-Disposition: form-data; name=\"UploadFile\"; filename=\"" + fileName + "\"\r\n" +
                    "Content-Type: application/octet-stream" + "\r\n" +
                    "\r\n";

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            FileInputStream in = new FileInputStream(filePath);
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(conn.getOutputStream()));

            out.writeUTF(postDataBuilder);

            byte[] buffer = new byte[1024];
            int readLength;

            while ((readLength = in.read(buffer)) != -1) {
                out.write(buffer, 0, readLength);
            }

            out.writeBytes(delimiter);
            out.flush();
            out.close();
            in.close();

            conn.getInputStream();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return "success";


    }
}

