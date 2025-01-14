package clients.backDoor;

import middle.MiddleFactory;
import middle.StockReadWriter;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Customer view.
 */

public class BackDoorView implements Observer
{
  private static final String RESTOCK  = "Add";
  private static final String CLEAR    = "Clear";
  private static final String QUERY    = "Query";
 
  private static final int H = 300;       // Height of window pixels
  private static final int W = 400;       // Width  of window pixels

  private final JLabel      pageTitle  = new JLabel();
  private final JLabel      theAction  = new JLabel();
  private final JTextField  theInput   = new JTextField();
  private final JTextField  theInputNo = new JTextField();
  private final JTextArea   theOutput  = new JTextArea();
  private final JScrollPane theSP      = new JScrollPane();
  private final JButton     theBtClear = new JButton( CLEAR );
  private final JButton     theBtRStock = new JButton( RESTOCK );
  private final JButton     theBtQuery = new JButton( QUERY );
  
  private final JButton		darkMode   = new JButton();
  
  private StockReadWriter theStock     = null;
  private BackDoorController cont= null;

  /**
   * Construct the view
   * @param rpc   Window in which to construct
   * @param mf    Factor to deliver order and stock objects
   * @param x     x-cordinate of position of window on screen 
   * @param y     y-cordinate of position of window on screen  
   */
  public BackDoorView(  RootPaneContainer rpc, MiddleFactory mf, int x, int y )
  {
    try                                             // 
    {      
      theStock = mf.makeStockReadWriter();          // Database access
    } catch ( Exception e )
    {
      System.out.println("Exception: " + e.getMessage() );
    }
    Container cp         = rpc.getContentPane();    // Content Pane
    Container rootWindow = (Container) rpc;         // Root Window
    cp.setLayout(null);                             // No layout manager
    rootWindow.setSize( W, H );                     // Size of Window
    rootWindow.setLocation( x, y );
    
    Font f = new Font("Courier",Font.PLAIN,12);  // Font f is
    
    theBtQuery.setBounds( 125, 15+60*0, 80, 40 );    // Buy button 
    theBtQuery.setBackground(Color.GRAY);
    theBtQuery.setForeground(Color.WHITE);
    theBtQuery.addActionListener(                   // Call back code
      e -> {
    	  cont.playSound();
    	  cont.doQuery( theInput.getText() );
    	  } );
    cp.add( theBtQuery );                           //  Add to canvas
    
    theBtRStock.setBounds( 300, 15+60*0, 80, 40 );   // Check Button
    theBtRStock.setBackground(Color.GRAY);
    theBtRStock.setForeground(Color.WHITE);
    theBtRStock.addActionListener(                  // Call back code
      e ->{
    	  cont.playSound();
    	  cont.doRStock( theInput.getText(),
                          theInputNo.getText() );
      	}
      );
    cp.add( theBtRStock );                          //  Add to canvas

    theBtClear.setBounds( 250, 60*1, 90, 40 );
    theBtClear.setBackground(Color.GRAY);
    theBtClear.setForeground(Color.WHITE);
    theBtClear.addActionListener(                   // Call back code
      e -> {
    	  cont.playSound();
    	  cont.doClear();
      	}
      );
    cp.add( theBtClear );                           //  Add to canvas

 
    theAction.setBounds( 10, 60 , 270, 20 );       // Message area
    theAction.setForeground(Color.WHITE);
    theAction.setText( "" );                        // Blank
    cp.add( theAction );                            //  Add to canvas

    theInput.setBounds( 10, 15+60*0, 110, 40 );         // Input Area
    theInput.setBackground(Color.GRAY);
    theInput.setForeground(Color.WHITE);
    theInput.setText("");                           // Blank
    cp.add( theInput );                             //  Add to canvas

    theInputNo.setBounds( 215, 15+60*0, 80, 40 );       // Input Area
    theInputNo.setBackground(Color.GRAY);
    theInputNo.setForeground(Color.WHITE);
    theInputNo.setText("0");                        // 0
    cp.add( theInputNo );                           //  Add to canvas
    
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
    			theInputNo.setBackground(Color.GRAY);
    			theInputNo.setForeground(Color.WHITE);
    			theOutput.setBackground(Color.GRAY);
    			theOutput.setForeground(Color.WHITE);
    			theBtQuery.setBackground(Color.GRAY);
    			theBtQuery.setForeground(Color.WHITE);
    			theBtRStock.setBackground(Color.GRAY);
    			theBtRStock.setForeground(Color.WHITE);
    			theBtClear.setBackground(Color.GRAY);
    			theBtClear.setForeground(Color.WHITE);
    			darkMode.setBackground(Color.GRAY);
    			darkMode.setForeground(Color.WHITE);
    			theAction.setForeground(Color.WHITE);
    			darkMode.setText("Light");
    		}else {
    		cp.setBackground(Color.decode("#35ddff"));
			theInput.setBackground(Color.WHITE);
			theInput.setForeground(Color.BLACK);
			theInputNo.setBackground(Color.WHITE);
			theInputNo.setForeground(Color.BLACK);
			theOutput.setBackground(Color.WHITE);
			theOutput.setForeground(Color.BLACK);
			theBtQuery.setBackground(Color.WHITE);
			theBtQuery.setForeground(Color.BLACK);
			theBtRStock.setBackground(Color.WHITE);
			theBtRStock.setForeground(Color.BLACK);
			theBtClear.setBackground(Color.WHITE);
			theBtClear.setForeground(Color.BLACK);
			darkMode.setBackground(Color.WHITE);
			darkMode.setForeground(Color.BLACK);
			theAction.setForeground(Color.BLACK);
			darkMode.setText("Dark");
    		}
    		cont.playSound();
    	
    	}
    		);
    cp.add( darkMode );
    

    theSP.setBounds( 10, 105, 270, 150 );          // Scrolling pane
    theOutput.setBackground(Color.GRAY);
    theOutput.setForeground(Color.WHITE);
    theOutput.setText( "" );                        //  Blank
    theOutput.setFont( f );                         //  Uses font  
    cp.add( theSP );                                //  Add to canvas
    theSP.getViewport().add( theOutput );           //  In TextArea
    rootWindow.setVisible( true );                  // Make visible
    theInput.requestFocus();                        // Focus is here
 
  }
  
  public void setController( BackDoorController c )
  {
    cont = c;
  }

  /**
   * Update the view, called by notifyObservers(theAction) in model,
   * @param modelC   The observed model
   * @param arg      Specific args 
   */
  @Override
  public void update( Observable modelC, Object arg )  
  {
    BackDoorModel model  = (BackDoorModel) modelC;
    String        message = (String) arg;
    theAction.setText( message );
    
    theOutput.setText( model.getBasket().getDetails() );
    theInput.requestFocus();
  }

}