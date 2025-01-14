package clients.cashier;

import catalogue.Basket;
import middle.MiddleFactory;
import middle.OrderProcessing;
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
 * View of the model 
 */
public class CashierView implements Observer
{
  private static final int H = 300;       // Height of window pixels
  private static final int W = 400;       // Width  of window pixels
  
  private static final String CHECK  = "Check";
  private static final String BUY    = "Add";
  private static final String BOUGHT = "Pay";

  private final JLabel      pageTitle  = new JLabel();
  private final JLabel      theAction  = new JLabel();
  private final JTextField  theInput   = new JTextField();
  private final JTextArea   theOutput  = new JTextArea();
  private final JScrollPane theSP      = new JScrollPane();
  private final JButton     theBtCheck = new JButton( CHECK );
  private final JButton     theBtBuy   = new JButton( BUY );
  private final JButton     theBtBought= new JButton( BOUGHT );
  
  private final JButton		darkMode   = new JButton();
  //private final JButton		theBtDiscount = new JButton("Discount");
  
  private StockReadWriter theStock     = null;
  private OrderProcessing theOrder     = null;
  private CashierController cont       = null;
  
  File buttonEffect = new File("sounds/buttonNoise.wav");
  
  /**
   * Construct the view
   * @param rpc   Window in which to construct
   * @param mf    Factor to deliver order and stock objects
   * @param x     x-coordinate of position of window on screen 
   * @param y     y-coordinate of position of window on screen  
   */
          
  public CashierView(  RootPaneContainer rpc,  MiddleFactory mf, int x, int y  )
  {
    try                                           // 
    {      
      theStock = mf.makeStockReadWriter();        // Database access
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

    Font f = new Font("Courier",Font.PLAIN,14);  // Font f is
    
    theBtCheck.setBounds( 135, 15+60*0, 70, 40 );    // Check button
    theBtCheck.setBackground(Color.GRAY);
    theBtCheck.setForeground(Color.WHITE);
    theBtCheck.addActionListener(                   // Call back code
      e -> {
    	  cont.playSound();
    	  cont.doCheck( theInput.getText());
      	}
      );
    cp.add( theBtCheck );                          //  Add to canvas

    theBtBuy.setBounds( 220, 15+60*0, 70, 40 );      // Buy button
    theBtBuy.setBackground(Color.GRAY);
    theBtBuy.setForeground(Color.WHITE);
    theBtBuy.addActionListener(                     // Call back code
      e -> {
    	  cont.playSound();
    	  cont.doBuy();
    	  } 
      );
    cp.add( theBtBuy );   							//  Add to canvas
 
    theBtBought.setBounds( 305, 15+60*0, 70, 40 );   // Bought Button
    theBtBought.setBackground(Color.GRAY);
    theBtBought.setForeground(Color.WHITE);
    theBtBought.addActionListener(                  // Call back code
      e -> {
    	  cont.playSound();
    	  cont.doBought(); 
      	}
      );
    cp.add( theBtBought );                          //  Add to canvas

    theAction.setBounds( 16, 55 , 270, 20 );       // Message area
    theAction.setText( "" );                        // Blank
    cp.add( theAction );                         //  Add to canvas
    theAction.setForeground(Color.WHITE);
    
    theInput.setBounds( 16, 15+60*0, 110, 40 );     // Input Area
    theInput.setBackground(Color.GRAY);
    theInput.setForeground(Color.WHITE);
    theInput.setText("");                           // Blank
    cp.add( theInput );                             //  Add to canvas
    
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
    			theBtBuy.setBackground(Color.GRAY);
    			theBtBuy.setForeground(Color.WHITE);
    			theBtBought.setBackground(Color.GRAY);
    			theBtBought.setForeground(Color.WHITE);
    			darkMode.setBackground(Color.GRAY);
    			darkMode.setForeground(Color.WHITE);
    			theAction.setForeground(Color.WHITE);
    			darkMode.setText("Light");
    		}else {
    		cp.setBackground(Color.decode("#35ddff"));
			theInput.setBackground(Color.WHITE);
			theInput.setForeground(Color.BLACK);
			theOutput.setBackground(Color.WHITE);
			theOutput.setForeground(Color.BLACK);
			theBtCheck.setBackground(Color.WHITE);
			theBtCheck.setForeground(Color.BLACK);
			theBtBuy.setBackground(Color.WHITE);
			theBtBuy.setForeground(Color.BLACK);
			theBtBought.setBackground(Color.WHITE);
			theBtBought.setForeground(Color.BLACK);
			darkMode.setBackground(Color.WHITE);
			darkMode.setForeground(Color.BLACK);
			theAction.setForeground(Color.BLACK);
			darkMode.setText("Dark");
    		}
    		cont.playSound();
    	
    	}
    		);
    cp.add( darkMode );
    /*
    theBtDiscount.setBounds(210,15+60*3, 110, 40);
    theBtDiscount.addActionListener(
    	e ->cont.applyDiscount());
    cp.add( theBtDiscount );
    */

    theSP.setBounds( 16, 75, 275, 160 );          // Scrolling pane
    theOutput.setBackground(Color.GRAY);
    theOutput.setForeground(Color.WHITE);
    theOutput.setText( "" );                        //  Blank
    theOutput.setFont( f );                         //  Uses font  
    cp.add( theSP );                                //  Add to canvas
    theSP.getViewport().add( theOutput );           //  In TextArea
    rootWindow.setVisible( true );                  // Make visible
    theInput.requestFocus();                        // Focus is here
  }

  /**
   * The controller object, used so that an interaction can be passed to the controller
   * @param c   The controller
   */

  public void setController( CashierController c )
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
    CashierModel model  = (CashierModel) modelC;
    String      message = (String) arg;
    theAction.setText( message );
    Basket basket = model.getBasket();
    if ( basket == null )
      theOutput.setText( "Customers order" );
    else
      theOutput.setText( basket.getDetails() );
    
    theInput.requestFocus();               // Focus is here
  }

}
