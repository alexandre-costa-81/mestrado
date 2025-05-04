  package boku;
  
  import java.awt.event.ActionEvent;
  import java.awt.event.ActionListener;
  import java.awt.event.ItemEvent;
  import java.awt.event.ItemListener;
  import javax.swing.JCheckBoxMenuItem;
  import javax.swing.JMenu;
  import javax.swing.JMenuItem;
  
  
  
  
  
  
  
  
  
  
  
  
  
  public class Options
  {
    private static boolean showHistoryPanel;
    private static boolean showFlyOver;
    private static boolean showCaptureMessage;
    private static OptionsHandler optionsHandler;
    private static JCheckBoxMenuItem moveTextItem;
    private static JCheckBoxMenuItem captureMessageItem;
    private static JCheckBoxMenuItem flyOverItem;
    private Boku GUI;
    
    public Options(Boku GUI)
    {
      showHistoryPanel = true;
      showCaptureMessage = false;
      showFlyOver = false;
      optionsHandler = new OptionsHandler();
      this.GUI = GUI;
    }
    
  
  
  
  
  
    public boolean showHistoryPanel()
    {
      return showHistoryPanel;
    }
    
  
  
  
  
  
    public boolean showCaptureMessage()
    {
      return showCaptureMessage;
    }
    
  
  
  
  
  
  
    public boolean showFlyOver()
    {
      return showFlyOver;
    }
    
  
  
  
  
    JMenu createOptionsMenu()
    {
      JMenu optionsMenu = new JMenu("Options");
      optionsMenu.setMnemonic('O');
      optionsMenu.setEnabled(true);
      
      moveTextItem = new JCheckBoxMenuItem(
        "Show moves in text area", showHistoryPanel);
      moveTextItem.setMnemonic('M');
      moveTextItem.setEnabled(true);
      moveTextItem.addItemListener(optionsHandler);
      optionsMenu.add(moveTextItem);
      
      captureMessageItem = new JCheckBoxMenuItem(
        "Show sandwich message", showCaptureMessage);
      captureMessageItem.setMnemonic('S');
      captureMessageItem.setEnabled(true);
      captureMessageItem.addItemListener(optionsHandler);
      optionsMenu.add(captureMessageItem);
      
      flyOverItem = new JCheckBoxMenuItem(
        "Show field number fly-overs", showFlyOver);
      flyOverItem.setMnemonic('F');
      flyOverItem.setEnabled(true);
      flyOverItem.addItemListener(optionsHandler);
      optionsMenu.add(flyOverItem);
      
      JMenuItem logFileItem = new JMenuItem("Log file");
      optionsMenu.addSeparator();
      logFileItem.setEnabled(!this.GUI.isApplet());
      optionsMenu.add(logFileItem);
      logFileItem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          FileOptionPane.getInstance().showOptionPane();
        }
        
      });
      return optionsMenu;
    }
    
    class OptionsHandler
      implements ItemListener
    {
      OptionsHandler() {}
      
      public void itemStateChanged(ItemEvent ie)
      {
        if (Options.moveTextItem.isSelected()) {
          Options.showHistoryPanel = true;
          Options.this.GUI.showHistoryPanel(true);
        } else {
          Options.showHistoryPanel = false;
          Options.this.GUI.showHistoryPanel(false);
        }
        
        Options.showCaptureMessage = Options.captureMessageItem.isSelected();
        
        if (Options.flyOverItem.isSelected()) {
          Options.showFlyOver = true;
          Options.this.GUI.enableFlyOver(true);
        } else {
          Options.showFlyOver = false;
          Options.this.GUI.enableFlyOver(false);
        }
      }
    }
  }


/* Location:              /home/alexandre/Boku1.21.jar!/boku/Options.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */