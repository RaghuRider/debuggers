/*
 * myinputManager.java
 *
 * Created on December 20, 2006, 12:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mMm_game.SRC.Input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import mMm_game.SRC.Screen.StartMenu;
import mMm_game.SRC.engine2D.Player;

/**
 *
 * @author Mohamed
 */
public class myinputManager extends myinput implements KeyListener {
    
    /** Creates a new instance of myinputManager */
    public myinputManager(Player myP,StartMenu menu) {
        myPlayer=myP;
        myMenu=menu;
        
    }

       public void keyTyped(KeyEvent e) 
    {
            
        
    }

    public void keyPressed(KeyEvent e)
    {
        if(status==1)// Game Status 
        { 
           if(myPlayer.IsAlive())
           {
          if(myPlayer.getDirection()==-1)
          {
        if(e.getKeyCode()==LEFT )
        {
         
             myPlayer.setLeft();
        }
        else 
            if(e.getKeyCode()==RIGHT)
            {
              
            myPlayer.setRight();
       
            }
          }
            if(e.getKeyCode()==UP)
            {
               
              myPlayer.setJumpMode();  
  
            }
           }
       
           
       if(e.getKeyCode()==e.VK_ESCAPE)
       {   
           status=0;//Return to Start Menu Status

            
       }
        }
        else if(status==0)// Start Menu status 
           {
        
         if(e.getKeyCode()==UP)
        {
            myMenu.increament_up();
        }
        else if(e.getKeyCode()==DOWN)
        {
            myMenu.increament_down();
        }
        else if(e.getKeyCode()==e.VK_ENTER)
        {
        	myMenu.startTheState();
        }
        else if(e.getKeyCode()==e.VK_ESCAPE)
        {
            
        }
        } 
        e.consume();
    }

    public void keyReleased(KeyEvent e) 
    {
        if(status==1)
        {
          if(e.getKeyCode()!=UP)
             myPlayer.setDo_Nothing();
          else if (e.getKeyCode()==UP)
          {
              myPlayer.UP_Released();
          }
        }
    }
 
    public void setPlayer(Player p)
    {
        myPlayer=p;
    }
    Player myPlayer;
    StartMenu myMenu;
}
