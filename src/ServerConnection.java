import processing.core.PApplet;
import processing.net.Client;
import processing.net.Server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Integer.*;

/**
 * Created by Dumitru on 09.01.2015.
 */
public class ServerConnection implements Runnable {

    private Client c;

    private Server s;
    private String input;
    private List serverCoorList = Collections.synchronizedList(new ArrayList(100));
    private List clientCoorList = Collections.synchronizedList(new ArrayList(100));


    ServerConnection(Server serv)  {
        s=serv;
    }

    //write coordinates of the server to the syncronized list as a string
    public void writeServerCoord(float xServer, float xEl, float yEl, int gameState) {

        String data;
        String data2="Ion";
        data=((int)xServer + " " + (int)xEl + " " + (int)yEl + " " + gameState + "\n");

        synchronized (serverCoorList) {
            serverCoorList.add(data);
            //data2= (String) serverCoorList.get(0);
            //System.out.println(data2);
        }
    }

    //return the x coordonate of client or -1 if no data found
    public int readClientCoord(){
        int rData= -1;
        String data;
        synchronized (clientCoorList) {
            if (!clientCoorList.isEmpty()) {
                data = (String) clientCoorList.get(0);
                System.out.println(data);
                try{
                    data = data.substring(0, data.indexOf("\n"));  // Only up to the newline
                    String[] strArray = data.split(" ");
                    rData=parseInt(strArray[0]);
                }
                catch (StringIndexOutOfBoundsException e){}
                catch (java.lang.NumberFormatException e){}
                clientCoorList.remove(0);
            }
        }
        return rData;
    }

    @Override
    public void run() {
        while(true){
       /* //Send data to client  (String from xServer,ElipseX,ElipseY,gameState)
        synchronized (serverCoorList) {
            if(!serverCoorList.isEmpty()) {
                input = (String) serverCoorList.get(0);
                s.write(input);
                //System.out.println("Am trimis prikoali");
                serverCoorList.remove(0);
            }
        }
        */
        // Receive data from client (String with xClient
        c = s.available();
        if (c != null) {
            input = c.readString();
            synchronized (clientCoorList) {
                clientCoorList.add(input);
            }
        }

        //sleep the thread
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        }
    }
}

