/*
 * mMm_Loader.java
 *
 * Created on June 30, 2006, 11:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mMm_game.SRC;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import mMm_game.SRC.Graphices.mMm_Animation_Manager;
import mMm_game.SRC.Input.myinput;
import mMm_game.SRC.Input.myinputManager;
import mMm_game.SRC.Screen.StartMenu;
import mMm_game.SRC.Screen.myScreen;
import mMm_game.SRC.engine2D.Enemy;
import mMm_game.SRC.engine2D.Meat;
import mMm_game.SRC.engine2D.Player;
import mMm_game.SRC.engine2D.myMap_Loader;
 
 
/**
 *
 * @author sami
 */
public class GameLoop  
   {
    
    /** Creates a new instance of mMm_Loader */
    public GameLoop() 
    {
        /**
         *  Init the Game Paramters 
         *  load maps,new players and enemys  
         *
         **/
        newGame();    
        
        GameScreen=new myScreen();
        myMenu=new StartMenu(GameScreen,this); 
        inputManager=new myinputManager(myPlayer ,myMenu);
          GameScreen.addKeyListener(inputManager);
            
        screen=GameScreen.getToolkit().getDefaultToolkit().getScreenSize();

        start_Animation();
 
    }
    public void newGame()
    {
        mapl=new myMap_Loader("map.txt");//Load the Images
        myPlayer=mapl.getPlayer();// get The Player
        myEnemys=mapl.getEnemy();// get The Enemy
        myMeat=mapl.geMeat();// get The Meat
        Height=mapl.getHeight();// get Height of Map
        Width=mapl.getWidth();//get Width of Map 
        myPlayer.SetEnemy(myEnemys);//Point the Enemies to Player
        if(inputManager!=null)//Not Yet Created
        inputManager.setPlayer(myPlayer);//The Input Manager point to the new Player
        
    }
    public void start_Animation()
    {
        BufferStrategy myDrawBuffer;
         
        myDrawBuffer=GameScreen.getBufferStrategy();
        
        long StartTime;//Time start in Loop
        long TimeAfter;//Diff in Time
    
        while(true)
        {
        StartTime=System.currentTimeMillis();
         TimeAfter=0;
        
         
        
        while(TimeAfter<=650)
        {
           
        Graphics  g=myDrawBuffer.getDrawGraphics();// Get the graphices object of Buffer Startagy
         
        if(myinput.status==0)// In Start Menu Status
        {
            myMenu.draw(g);// Draw start menu in the Double Buffers
             g.dispose();// Free Memory 
        
        if(!myDrawBuffer.contentsLost())
        myDrawBuffer.show();// Show the Buffers on the Screen
        }
        else if(myinput.status==1)// In Game Status
        {
        draw(g);//Draw all objects , Map, Player, Enemies,Meat in Double Buffers
     
        g.dispose();//Free Memory
        
        if(!myDrawBuffer.contentsLost())
        myDrawBuffer.show();// Show the Buffers on the Screen
        
        if(myPlayer.IsAlive())// check the player live
         myPlayer.Up_Date_Animation(TimeAfter);// Update the Animation of Player 
         for(int i=0;i<myEnemys.size();i++)
         {
             myEnemy=((Enemy)(myEnemys.get(i)));
             if(myEnemy.IsAlive())// check the Enemy live
        myEnemy.Up_Date_Animation(TimeAfter);// Update the Animation of Enemy
        }
        }
         try {
                Thread.sleep(10);// Sleep for 10 MilliSeconds
            }
            catch (InterruptedException ex) { } 

       TimeAfter=System.currentTimeMillis()-StartTime;// Get The Difference in the Time 
       
        }   
        }
        
    }
    
    public void draw(Graphics g) 
    {
    
        Graphics2D g2d=(Graphics2D)g;
      
        
                 myPlayer.jumpUpdate();//Update Player Jump mode   
             
                for(int i=0;i<myEnemys.size();i++)
                {
                         myEnemy=((Enemy)(myEnemys.get(i))); 
                         if(myEnemy.IsAlive())
                         {
                         myEnemy.Move_turn();// Update Postions of enemies
                         myEnemy.check_for_death(myPlayer);// Check if it killed by player
                         }
                }
     
        
        
                int offsetX=(screen.width/2)-(myPlayer.getX());// Start X to draw Maps
         
                offsetX=Math.min(offsetX,0);
                offsetX=Math.max(offsetX,screen.width-(mapl.getWidth())*100);
                int offsetY=screen.height-(mapl.getHeight()*22);//Start Y to draw Maps
                
               
                
                int firstCubX=(-offsetX)/60;//First Cub to draw from X
                int lastCubX=firstCubX+(screen.height/60)+1;//Last Cub to draw  
                
             int startY=0;
             int additionY=0;
                if(myPlayer.getY()>=screen.height)// This is Changes in Y-Postions
                {
                 int X=myPlayer.getY()/screen.height;
                 
                offsetY=-(myPlayer.getY()%(screen.height))-450;
                
                if(X>1)
                offsetY=(offsetY*X)-30;
                
                
                startY=9*X;
     
                additionY=8;
                }
        
               // Draw the Map
     
              g2d.drawImage(img,0,0,screen.width,screen.height,null);// Background Images
         
            Image myCub;
              for(int x=firstCubX;x<=lastCubX+4&&x<Width;x++)
                  for(int y=(startY);y<14+startY+additionY&&y<Height;y++)
                  {
                  myCub=mapl.getImage(x,y);// get Each  Cube  Image on the Map
                  
                  if(myCub!=null)
              g2d.drawImage(myCub,x*60+offsetX,y*60+offsetY,60,60,null);          
                  }
            
               
            // Draw the Enemies
               for(int i=0;i<myEnemys.size();i++)
               {
              myEnemy=((Enemy)(myEnemys.get(i)));
                  if((!(myEnemy.getY()<myPlayer.getY()-screen.height/2)||!(myEnemy.getY()>myPlayer.getY()+screen.height/2)))
                      if(!(myEnemy.getX()<myPlayer.getX()-screen.width/2)||!(myEnemy.getX()>myPlayer.getX()+screen.width/2))
                      {
                       if(myEnemy.face_to_right())     
                       {
                           if(myEnemy.IsAlive())
                       g2d.drawImage(myEnemy.getImage(),(myEnemy.getX())+offsetX,(myEnemy.getY())+offsetY,null);
                           else 
                           {
                                
                               g2d.drawImage(deadimg,(myEnemy.getX())+offsetX,(myEnemy.getY())+offsetY,null);
                                
                           }
                       
                       }
                       else 
                       {// transform the Image 
                           transform.setToScale(-1,1);
                           transform.translate(-((myEnemy.getX())+offsetX)-60,(myEnemy.getY())+offsetY);
                           if(myEnemy.IsAlive())
                           g2d.drawImage(myEnemy.getImage(),transform,null);
                           else
                           {
                            
                               g2d.drawImage(deadimg,myEnemy.getX()+offsetX,myEnemy.getY()+offsetY,null);
                           }
                               
                       }
                       
                      }
               }
                 
                
        // Draw the Meat 
         for(int i=0;i<myMeat.size();i++)
         {
                
                myfood=((Meat)myMeat.get(i));
                if(myfood.IsAlive())
                myfood.EatMe(myPlayer);
                          if(myfood.IsAlive())// is not eaten
                   g2d.drawImage(myfood.getImage(),myfood.getX()+offsetX,myfood.getY()+offsetY,null);
                
         }
            g2d.drawImage(Exit_img,1026/2,680,null);
               if(myPlayer.IsAlive())
               if(myPlayer.face_to_right())//drawing the player
               {
                
                 g2d.drawImage(myPlayer.getImage(),(myPlayer.getX())+offsetX,(myPlayer.getY())+offsetY,null);
               }
            
              else
              {  
                transform.setToScale(-1,1);
                 transform.translate(-((myPlayer.getX())+offsetX)-80,(myPlayer.getY())+offsetY);
               
                 g2d.drawImage(myPlayer.getImage(),transform,null);        
               }
                  else
                           g2d.drawImage(deadimg,myPlayer.getX()+offsetX,myPlayer.getY()+offsetY+20,null);
            
           
           
 
    }    

 
    
    
    private myScreen GameScreen;
    private Image img=new ImageIcon("Images/background.jpg").getImage();
    private Image deadimg=new ImageIcon("Images/dead_sign.png").getImage();
    private myMap_Loader mapl;
    private StartMenu myMenu;
    private Player myPlayer;
    private myinputManager inputManager;
    private ArrayList myEnemys;
    private Enemy myEnemy;
    private int Height;
    private int Width;
    private Meat myfood;
    private ArrayList myMeat;
    private Image load_img=new ImageIcon("Images/Loading.jpg").getImage();
    private Image Exit_img=new ImageIcon("Images/Exit.png").getImage();
    private AffineTransform transform = new AffineTransform();
    private  Dimension screen;
    
     
}