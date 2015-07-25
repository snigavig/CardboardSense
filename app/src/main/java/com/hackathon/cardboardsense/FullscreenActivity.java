package com.hackathon.cardboardsense;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;


import com.unity3d.player.UnityPlayer;


public class FullscreenActivity
        extends UnityPlayerActivity {
    public static final int FLOATS_PER_FRAME = 63;
    private static final boolean RIGHT = true;
    private static final boolean LEFT = false;

//    private TextView serverStatus;

    // DEFAULT IP
    public static String SERVERIP = "192.168.137.1";

    // DESIGNATE A PORT
//    public static final int SERVERPORT = 9999;

    private Handler handler = new Handler();

    private ServerSocket serverSocketLeft;
    private ServerSocket serverSocketRight;


    private void sendStringRepresentationOfHandFrameToUnity(String s, boolean leftOrRight) {

        UnityPlayer.UnitySendMessage((leftOrRight == RIGHT) ? "hand_right" : "hand_left", "SetCoordinates", s);

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            // MAKE SURE YOU CLOSE THE SOCKET UPON EXITING
            serverSocketRight.close();
            serverSocketLeft.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    UnityPlayer mUnityPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(this, "PROCECCING BIG DATA...", Toast.LENGTH_LONG).show();


        SERVERIP = getLocalIpAddress();

        Thread rightThread = new Thread(new ServerThread(RIGHT, 8887));
        rightThread.start();

        Thread leftThread = new Thread(new ServerThread(LEFT, 8888));
        leftThread.start();
    }


    // GETS THE IP ADDRESS OF YOUR PHONE'S NETWORK
    private String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("ServerActivity", ex.toString());
        }
        return null;
    }



    public class ServerThread implements Runnable {
        // DESIGNATE A PORT
        private int port;
        // WHICH HAND
        private boolean leftOrRight;
        private ServerSocket serverSocket;

        public ServerThread(boolean leftOrRight, int port) {
            this.leftOrRight = leftOrRight;
            this.port = port;
            this.serverSocket = (leftOrRight==RIGHT)?serverSocketRight:serverSocketLeft;
        }


        public void run() {
            try {
                if (SERVERIP != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
//                            serverStatus.setText("Listening on IP: " + SERVERIP);
                        }
                    });
                    serverSocket = new ServerSocket(port);
                    while (true) {
                        // LISTEN FOR INCOMING CLIENTS
                        Socket client = serverSocket.accept();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
//                                serverStatus.setText("Connected.");
                            }
                        });

                        try {
                            DataInputStream dis = new DataInputStream(client.getInputStream());
                            final List<Float> floatList = new ArrayList<>();
                            float f = dis.readFloat();
//                            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
//                            float float = null;
                            int checker = 0;
                            while (f != 0 || true) {

                                Log.d("ServerActivity", "" + f);
                                f = dis.readFloat();
                                floatList.add(f);
                                if (floatList.size() == FLOATS_PER_FRAME) {
                                    final List<Float> handFrame = new ArrayList<>(floatList);
                                    floatList.clear();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            StringBuilder sb = new StringBuilder();
                                            for (float f : handFrame) {
                                                sb.append(f).append(" ");
                                            }
                                            sendStringRepresentationOfHandFrameToUnity(sb.toString(), leftOrRight);

                                        }
                                    }).start();
                                }

                            }
                            break;
                        } catch (Exception e) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
//                                    serverStatus.setText("Oops. Connection interrupted. Please reconnect your phones.");
                                }
                            });
                            e.printStackTrace();
                        }
                    }
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
//                            serverStatus.setText("Couldn't detect internet connection.");
                        }
                    });
                }
            } catch (Exception e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
//                        serverStatus.setText("Error");
                    }
                });
                e.printStackTrace();
            }
        }
    }

}