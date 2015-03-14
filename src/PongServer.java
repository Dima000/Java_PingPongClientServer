import processing.core.*;
import processing.net.Server;


public class PongServer extends PApplet {

    ServerConnection con;
    Thread conThread;


    int base=20;
    float x=1,y=1,gameScore=0;
    float changeX=-5;
    float changeY=-5;
    int gameOver=0;
    float Multiplier=1.1f;
    int gameLevel=1;
    int boardDimension=200;

    private int clientCoordAux, clientCoord=250;
    Server s;

    public void setup()
    {
        size(600, 480);
        x=(int)random(width);
        y=height-base;
        frameRate(30);
        s = new Server(this, 12345);
        con= new ServerConnection(s);
        conThread=new Thread(con);
        conThread.start();

    }

    public void draw()
    {
        //con.writeServerCoord(mouseX,x,y,gameOver);
        s.write((int) mouseX + " " + (int) x + " " + (int) y + " " + gameOver + "\n");
        clientCoordAux=con.readClientCoord();
        //System.out.println("Coordonate client " + clientCoord);
        if(clientCoordAux>=0)
            clientCoord=width-boardDimension-clientCoordAux;
        if(gameOver==0)
        {
            background(209,157,44);
            text("LEVEL "+gameLevel,width/2,height/2-50);
            //text("SCORE "+gameScore,width/2,height/2);
            stroke(51,149,24);
            fill(51,149,24);
            rect(mouseX,height-base,boardDimension,base); //jucator server
            rect(clientCoord,0,boardDimension,base);  //jucator client
            ellipse(x,y,10,10);
            stroke(0);
            fill(0);
            x=x+changeX;
            y=y+changeY;
            if(x<0 | x>width)
            {
                changeX=-changeX;
            }
            if(y<base)
            {
                if(x>clientCoord && x<clientCoord+boardDimension)    {
                    changeY=-changeY; //bounce back
                    gameScore++;
                    if((gameScore%3)==0)
                    {

                        changeX=Multiplier*changeX;
                        changeY=Multiplier*changeY;
                        gameLevel++;
                    }
                }else{
                    gameOverSplash();
                }
            }

            if(y>height-base){
                if(x>mouseX && x<mouseX+boardDimension)    {
                    changeY=-changeY; //bounce back
                    gameScore++;
                    if((gameScore%3)==0)
                    {
                        changeX=Multiplier*changeX;
                        changeY=Multiplier*changeY;
                        gameLevel++;
                    }
                }else{
                    gameOverSplash();
                }
            }
        }
        else  {
            background(100,100,200);
            text("Game Over!", width/2-150,height/2);
            text("Click to Restart", width/2-150,height/2+20);
        }}
    public void gameOverSplash()
    {
        gameOver=1;
    }
    public void mouseClicked()
    {
        changeY=-changeY;
        gameScore=0;
        gameLevel=1;
        gameOver=0;
        clientCoord=250;
        x=450;
        y=300;
    }
    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[] { "PongServer" };
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}
