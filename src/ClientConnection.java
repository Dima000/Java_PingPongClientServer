import processing.core.PApplet;
import processing.net.Client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Dumitru on 09.01.2015.
 */
public class ClientConnection implements Runnable {


    private Client c;
    private String input;

    private List serverCoorList = Collections.synchronizedList(new ArrayList());
    private List clientCoorList = Collections.synchronizedList(new ArrayList());

    ClientConnection(Client cl){
        c=cl;
    }

    //Reads coordonates from serverCoorList as String and returns them as int[]
    public int[] readServerCoord() {
        int intData[]= new int[]{-1, -1, -1, -1};
        String data=null;
        synchronized (serverCoorList) {
            if(!serverCoorList.isEmpty()) {
                data = (String) serverCoorList.get(0);
                serverCoorList.remove(0);
            }
        }
        if(data!=null) {
            data = data.substring(0, data.indexOf("\n"));  // Only up to the newline
            String[] strArray = data.split(" ");
            intData = new int[strArray.length];
            for (int i = 0; i < strArray.length; i++) {
                intData[i] = Integer.parseInt(strArray[i]);
            }
        }
        return intData;
    }

    //Writes (int) clientCoord as a (String) in clientCoorList
    public void writeClientCoord(int clientCoord) {
        String data= clientCoord + "\n";
        synchronized (clientCoorList) {
            clientCoorList.add(data);
        }
    }


    @Override
    public void run() {
        while(true){
        // Receive data from server
        if (c.available() > 0) {
            input = c.readString();
            synchronized (serverCoorList) {
                serverCoorList.add(input);
            }
        }

       /* //Send data to Server  (String with ClientCoord)
        synchronized (clientCoorList) {
            if(!clientCoorList.isEmpty()) {
                input = (String) clientCoorList.get(0);
                c.write(input);
                //System.out.println("Am primit prikoali");
                clientCoorList.remove(0);
            }
        }
        */
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        }
    }

}
