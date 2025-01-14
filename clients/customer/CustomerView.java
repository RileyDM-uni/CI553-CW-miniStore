package clients.customer;

import catalogue.Basket;


import catalogue.BetterBasket;
import clients.Picture;
import middle.MiddleFactory;
import middle.StockReader;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.sound.sampled.*;

/**
 * Implements the Customer view.
 */

public class CustomerView implements Observer
{
  class Name                              // Names of buttons
  {
    public static final String CHECK  = "Check";
    public static final String CLEAR  = "Clear";
  }

  private static final int H = 300;       // Height of window pixels
  private static final int W = 400;       // Width  of window pixels

  private final JLabel      pageTitle  = new JLabel();
  private final JLabel      theAction  = new JLabel();
  private final JTextField  theInput   = new JTextField();
  private final JTextArea   theOutput  = new JTextArea();
  private final JScrollPane theSP      = new JScrollPane();
  private final JButton     theBtCheck = new JButton( Name.CHECK );
  private final JButton     theBtClear = new JButton( Name.CLEAR );
  
  private final JButton		darkMode   = new JButton();

  private Picture thePicture = new Picture(80,80);
  private StockReader theStock   = null;
  private CustomerController cont= null;

  /**
   * Construct the view
   * @param rpc   Window in which to construct
   * @param mf    Factor to deliver order and stock objects
   * @param x     x-cordinate of position of window on screen 
   * @param y     y-cordinate of position of window on screen  
   */
  
  public CustomerView( RootPaneContainer rpc, MiddleFactory mf, int x, int y )
  {
    try                                             // 
    {      
      theStock  = mf.makeStockReader();             // Database Access
    } catch ( Exception e )
    {
      System.out.println("Exception: " + e.getMessage() );
    }
    Container cp         = rpc.getContentPane();    // Content Pane
    Container rootWindow = (Container) rpc;         // Root Window
    cp.setLayout(null);                             // No layout manager
    rootWindow.setSize( W, H );                     // Size of Window
    rootWindow.setLocation( x, y );
    
    Font f = new Font("Courier",Font.PLAIN,14);  // Font f is
    
    
   // pageTitle.setBounds( 110, 0 , 270, 20 );       
    //pageTitle.setText( "Search products" );                        
    //cp.add( pageTitle );
    //pageTitle.setForeground(Color.WHITE);

    theBtCheck.setBounds( 135, 15+60*0, 70, 40 );    // Check button
	theBtCheck.setBackground(Color.GRAY);
	theBtCheck.setForeground(Color.WHITE);
    theBtCheck.addActionListener(                   // Call back code
      e -> {
    	  cont.playSound();
    	  cont.doCheck( theInput.getText());
      	}
      );
    cp.add( theBtCheck );                           //  Add to canvas

    theBtClear.setBounds( 212, 15+60*0, 70, 40 );    // Clear button
    theBtClear.setBackground(Color.GRAY);
	theBtClear.setForeground(Color.WHITE);
    theBtClear.addActionListener(                   // Call back code
      e -> {
    	 cont.playSound();
    	 cont.doClear() ;
      }
     );
      
    cp.add( theBtClear );                           //  Add to canvas

   theAction.setBounds( 16, 55 , 270, 20 );       // Message area
   theAction.setForeground(Color.WHITE);
   theAction.setText( " " );                       // blank
   cp.add( theAction );                            //  Add to canvas

    theInput.setBounds( 16, 15+60*0, 110, 40 );         // Product no area
    theInput.setBackground(Color.GRAY);
	theInput.setForeground(Color.WHITE);
    theInput.setText("");                           // Blank
    cp.add( theInput );                             //  Add to canvas
    
    theSP.setBounds( 16, 75, 270, 160 );          // Scrolling pane
    theOutput.setBackground(Color.GRAY);
	theOutput.setForeground(Color.WHITE);
    theOutput.setText( "" );                        //  Blank
    theOutput.setFont( f );                         //  Uses font  
    cp.add( theSP );                                //  Add to canvas
    theSP.getViewport().add( theOutput );           //  In TextArea

    thePicture.setBounds( 295, 15, 80,80 );   // Picture area
    cp.add( thePicture );                           //  Add to canvas
    thePicture.clear();
    
    darkMode.setBounds(295, 175, 83, 40 );
    darkMode.setBackground(Color.GRAY);
	darkMode.setForeground(Color.WHITE);
    darkMode.setText("Light");
    darkMode.addActionListener(
    	e ->{
    		Color bgcolor = cp.getBackground();
    		if (bgcolor.equals(Color.decode("#35ddff"))) {
    			cp.setBackground(Color.decode("#000080"));
    			theInput.setBackground(Color.GRAY);
    			theInput.setForeground(Color.WHITE);
    			theOutput.setBackground(Color.GRAY);
    			theOutput.setForeground(Color.WHITE);
    			theBtCheck.setBackground(Color.GRAY);
    			theBtCheck.setForeground(Color.WHITE);
    			theBtClear.setBackground(Color.GRAY);
    			theBtClear.setForeground(Color.WHITE);
    			darkMode.setBackground(Color.GRAY);
    			darkMode.setForeground(Color.WHITE);
    			darkMode.setText("Light");
    		}else {
    		cp.setBackground(Color.decode("#35ddff"));
			theInput.setBackground(Color.WHITE);
			theInput.setForeground(Color.BLACK);
			theOutput.setBackground(Color.WHITE);
			theOutput.setForeground(Color.BLACK);
			theBtCheck.setBackground(Color.WHITE);
			theBtCheck.setForeground(Color.BLACK);
			theBtClear.setBackground(Color.WHITE);
			theBtClear.setForeground(Color.BLACK);
			darkMode.setBackground(Color.WHITE);
			darkMode.setForeground(Color.BLACK);
			darkMode.setText("Dark");
    		}
    		cont.playSound();
    	
    	}
    		);
    cp.add( darkMode );
    
    rootWindow.setVisible( true );                  // Make visible);
    theInput.requestFocus();                        // Focus is here
  }

   /**
   * The controller object, used so that an interaction can be passed to the controller
   * @param c   The controller
   */

  public void setController( CustomerController c )
  {
    cont = c;
  }

  /**
   * Update the view
   * @param modelC   The observed model
   * @param arg      Specific args 
   */
   
  public void update( Observable modelC, Object arg )
  {
    CustomerModel model  = (CustomerModel) modelC;
    String        message = (String) arg;
    theAction.setText( message );
    ImageIcon image = model.getPicture();  // Image of product
    if ( image == null )
    {
      thePicture.clear();                  // Clear picture
    } else {
      thePicture.set( image );             // Display picture
    }
    theOutput.setText( model.getBasket().getDetails() );
    theInput.requestFocus();               // Focus is here
  }

}
