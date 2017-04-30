package server;

import data.Records;
import org.json.simple.JSONObject;
import tools.Log;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class MedicalRecordsOCRHandler extends Thread {

    Socket connectedClient = null;
    DataInputStream inFromClient = null;
    DataOutputStream outToClient = null;

    public MedicalRecordsOCRHandler(Socket client) {
        connectedClient = client;
    }

    private void initStreams() {
        try {
            inFromClient = new DataInputStream(connectedClient.getInputStream());
            outToClient = new DataOutputStream(connectedClient.getOutputStream());

            Log.detail("Request from " +
                    connectedClient.getInetAddress() + ":" + connectedClient.getPort());
        } catch (IOException e) {
            Log.error("Error initiating streams for " +
                    connectedClient.getInetAddress() + ":" + connectedClient.getPort());
            e.printStackTrace();
        }
    }

    void closeStreams() {
        try {
            inFromClient.close();
            outToClient.close();
            connectedClient.close();
        } catch (IOException e) {
            Log.error("Error closing streams for " +
                    connectedClient.getInetAddress() + ":" + connectedClient.getPort());
            e.printStackTrace();
        }
    }

    public void run() {
        initStreams();

        String filePath = readRequest();

        MedicalRecordsOCR recordsOCR = new MedicalRecordsOCR(filePath);
        writeResponse(recordsOCR.process());

        closeStreams();
    }

    private String readRequest() {
        String filePath = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(inFromClient));
        try {
            String line;
            while ((line = br.readLine()) != null) {
                filePath += line + "\n";
            }
        } catch (IOException e) {
            Log.error("Error reading request from " +
                    connectedClient.getInetAddress() + ":" + connectedClient.getPort());
            e.printStackTrace();
        }

        return filePath;
    }

    public void writeResponse(Records records) {
        try {
            OutputStreamWriter out = new OutputStreamWriter(outToClient, StandardCharsets.UTF_8);
            out.write(records.toJSON().toString());
        } catch (IOException e) {
            Log.error("Error sending response to " +
                    connectedClient.getInetAddress() + ":" + connectedClient.getPort());
            e.printStackTrace();
        }
    }
}