package server;

import org.opencv.core.Core;
import tools.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MedicalRecordsOCRServer {

    // Compulsory
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    static ServerSocket server = null;

    public static void main(String[] args) {
        if(args.length != 2){
            Log.error("Please specify the address and port number.");
            return;
        }

        if(!initServer(args[0], args[1])){
            Log.error("Please specify the address and port number.");
            return;
        }

        while(true) {
            try {
                Socket requestConnection = server.accept();
                (new MedicalRecordsOCRHandler(requestConnection)).start();
            } catch (IOException e) {
                Log.error("An error occurred.");
                e.printStackTrace();
                return;
            }
        }
    }

    private static boolean initServer(String address, String port) {
        try {
            server = new ServerSocket (Integer.parseInt(port), 10, InetAddress.getByName(address));
            Log.info("Server waiting requests on " + address + ":" + port);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
