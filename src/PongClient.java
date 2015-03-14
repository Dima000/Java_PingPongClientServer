import processing.core.*;
import processing.net.Client;


public class PongClient extends PApplet {

    ClientConnection con;
    Thread conThread;

    private int base=20;
    private int gameLevel;
    private float x=1,y=1,gameScore=0, xAux,yAux;
    private int gameOver,gameOverAux;
    private int[] coord;
    int boardDimension=200;
    private float p2X=1, p2XAux=0;

    private Client c;

    public void setup()
    {
        size(600, 480);
        x=(int)random(width);
        y=height-base;
        frameRate(30);
        c= new Client(this, "127.0.0.1", 12345);
        con= new ClientConnection(c);
        conThread=new Thread(con);
        conThread.start();
        gameOverAux=0;
    }

    public void draw()
    {
        coord = con.readServerCoord();
        c.write((int)mouseX + " " +"\n");
        if(coord[3]>=0)
            gameOverAux=coord[3];
        gameOver=gameOverAux;
        if(gameOver==0)
        {

            p2XAux=coord[0];
            xAux=coord[1];
            yAux=coord[2];

            if(p2XAux>0) p2X=width-boardDimension-p2XAux;
            if(xAux>0) x=width-xAux;
            if(yAux>0) y=height-yAux;

            background(209,157,44);
            //text("LEVEL " + gameLevel, width / 2, height / 2 - 50);
            //text("SCORE "+gameScore,width/2,height/2);
            stroke(51, 149, 24);
            fill(51, 149, 24);
            rect(mouseX, height - base, 200, base);
            rect(p2X,0,200,base);
            ellipse(x, y, 10, 10);
            stroke(0);
            fill(0);

        }
        else  {
            background(100,100,200);
            text("Game Over!",width/2-150,height/2);
            text("Wait server to restart",width/2-150,height/2+20);
        }
    }

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[] { "PongClient" };
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}
