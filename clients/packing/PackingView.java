package clients.packing;

import catalogue.Basket;
import middle.MiddleFactory;
import middle.OrderProcessing;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Packing view.

 */

public class PackingView implements Observer
{
  private static final String PACKED = "Packed";

  private static final int H = 300;       // Height of window pixels
  private static final int W = 400;       // Width  of window pixels

  private final JLabel      pageTitle  = new JLabel();
  private final JLabel      theAction  = new JLabel();
  private final JTextArea   theOutput  = new JTextArea();
  private final JScrollPane theSP      = new JScrollPane();
  private final JButton     theBtPack= new JButton( PACKED );
  
  private final JButton		darkMode   = new JButton();
  private OrderProcessing theOrder     = null;
  
  private PackingController cont= null;

  /**
   * Construct the view
   * @param rpc   Window in which to construct
   * @param mf    Factor to deliver order and stock objects
   * @param x     x-cordinate of position of window on screen 
   * @param y     y-cordinate of position of window on screen  
   */
  public PackingView(  RootPaneContainer rpc, MiddleFactory mf, int x, int y )
  {
    try                                           // 
    {      
      theOrder = mf.makeOrderProcessing();        // Process order
    } catch ( Exception e )
    {
      System.out.println("Exception: " + e.getMessage() );
    }
    Container cp         = rpc.getContentPane();    // Content Pane
    Container rootWindow = (Container) rpc;         // Root Window
    cp.setLayout(null);                             // No layout manager
    rootWindow.setSize( W, H );                     // Size of Window
    rootWindow.setLocation( x, y );
    
    Font f = new Font("Monospaced",Font.PLAIN,12);  // Font f is
    
    pageTitle.setBounds( 110, 0 , 270, 20 );
    pageTitle.setForeground(Color.WHITE);
    pageTitle.setText( "Packing Bought Order" );                        
    cp.add( pageTitle );

    theBtPack.setBounds( 16, 25+60*3, 80, 40 );   // Check Button
    theBtPack.setBackground(Color.GRAY);
    theBtPack.setForeground(Color.WHITE);
    theBtPack.addActionListener(                   // Call back code
      e -> {
    	  cont.playSound();
    	  cont.doPacked();
      } );
    cp.add( theBtPack );                          //  Add to canvas

    theAction.setBounds( 110, 25 , 270, 20 );       // Message area
    theAction.setText( "" );                        // Blank
    cp.add( theAction );                            //  Add to canvas
    
    darkMode.setBounds(280, 25+60*3, 83, 40 );
    darkMode.setBackground(Color.GRAY);
	darkMode.setForeground(Color.WHITE);
    darkMode.setText("Light");
    darkMode.addActionListener(
    	e ->{
    		Color bgcolor = cp.getBackground();
    		if (bgcolor.equals(Color.decode("#35ddff"))) {
    			cp.setBackground(Color.decode("#000080"));
    			theOutput.setBackground(Color.GRAY);
    			theOutput.setForeground(Color.WHITE);
    			theBtPack.setBackground(Color.GRAY);
    			theBtPack.setForeground(Color.WHITE);
    			darkMode.setBackground(Color.GRAY);
    			darkMode.setForeground(Color.WHITE);
    			theAction.setForeground(Color.WHITE);
    			pageTitle.setForeground(Color.WHITE);
    			darkMode.setText("Light");
    		}else {
    		cp.setBackground(Color.decode("#35ddff"));
			theOutput.setBackground(Color.WHITE);
			theOutput.setForeground(Color.BLACK);
			theBtPack.setBackground(Color.WHITE);
			theBtPack.setForeground(Color.BLACK);
			darkMode.setBackground(Color.WHITE);
			darkMode.setForeground(Color.BLACK);
			theAction.setForeground(Color.BLACK);
			pageTitle.setForeground(Color.BLACK);
			darkMode.setText("Dark");
    		}
    		cont.playSound();
    	
    	}
    		);
    cp.add( darkMode );

    theSP.setBounds( 16, 25, 350, 170 );           // Scrolling pane
    theOutput.setBackground(Color.GRAY);
    theOutput.setForeground(Color.WHITE);
    theOutput.setText( "" );                        //  Blank
    theOutput.setFont( f );                         //  Uses font  
    cp.add( theSP );                                //  Add to canvas
    theSP.getViewport().add( theOutput );           //  In TextArea
    rootWindow.setVisible( true );                  // Make visible
  
  }
  
  public void setController( PackingController c )
  {
    cont = c;
  }

  /**
   * Update the view
   * @param modelC   The observed model
   * @param arg      Specific args 
   */
  @Override
  public void update( Observable modelC, Object arg )
  {
	  PackingModel model  = (PackingModel) modelC;
    String        message = (String) arg;
    theAction.setText( message );
    
    Basket basket =  model.getBasket();
    if ( basket != null )
    {
      theOutput.setText( basket.getDetails() );
    } else {
      theOutput.setText("");
    }
  }

}

