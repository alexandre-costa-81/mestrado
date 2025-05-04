  package boku;
  
  import java.awt.event.ActionEvent;
  import java.awt.event.ActionListener;
  import javax.swing.Box;
  import javax.swing.ButtonGroup;
  import javax.swing.JButton;
  import javax.swing.JCheckBox;
  import javax.swing.JFileChooser;
  import javax.swing.JFrame;
  import javax.swing.JLabel;
  import javax.swing.JRadioButton;
  
  public final class FileOptionPane
  {
    private static FileOptionPane instance = null;
    
  
    private static final java.awt.Dimension BASE_SIZE = new java.awt.Dimension(130, 30);
    
    private static JFrame frame;
    
    private static Box optionPanel;
    
    private static Box buttonPanel;
    
    private static Box checkboxPanel;
    
    private static JButton okButton;
    private static JButton selectFileButton;
    private static JLabel movesHeading;
    private static JLabel fileNameLabel;

    private static void access$0() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static void access$1() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private FileOptionPane()
    {
      frame = new JFrame("Log file options");
      buttonPanel = new Box(0);
      optionPanel = new Box(0);
      checkboxPanel = new Box(1);
      outputGroup = new ButtonGroup();
      noOutputButton = new JRadioButton(" No log");
      stdOutButton = new JRadioButton(" Write log to standard output");
      fileOutButton = new JRadioButton(" Write log to file");
      outputGroup.add(noOutputButton);
      outputGroup.add(stdOutButton);
      outputGroup.add(fileOutButton);
      movesHeading = new JLabel("Write ");
      movesOnlyButton = new JCheckBox(" moves only, no evaluation");
      movesEvalButton = new JCheckBox(" moves with evaluation");
      bestLinesButton = new JCheckBox(" moves and best lines of thought");
      allLinesButton = new JCheckBox(" moves and all lines of thought");
      statisticsButton = new JCheckBox(" Write statistics");
      
      fileNameLabel = new JLabel();
      selectFileButton = new JButton(" Select File");
      selectFileButton.setPreferredSize(BASE_SIZE);
      okButton = new JButton("OK");
      okButton.setPreferredSize(BASE_SIZE);
    }
    
    public static synchronized FileOptionPane getInstance() {
      if (instance == null) {
        instance = new FileOptionPane();
      }
      return instance;
    }
    
  
  
  
    public void showOptionPane()
    {
      buttonPanel.add(Box.createHorizontalGlue());
      buttonPanel.add(selectFileButton);
      buttonPanel.add(Box.createHorizontalGlue());
      buttonPanel.add(okButton);
      buttonPanel.add(Box.createHorizontalGlue());
      checkboxPanel.add(Box.createVerticalGlue());
      checkboxPanel.add(noOutputButton);
      checkboxPanel.add(stdOutButton);
      checkboxPanel.add(fileOutButton);
      checkboxPanel.add(Box.createVerticalGlue());
      checkboxPanel.add(movesHeading);
      checkboxPanel.add(movesOnlyButton);
      checkboxPanel.add(movesEvalButton);
      checkboxPanel.add(bestLinesButton);
      checkboxPanel.add(allLinesButton);
      checkboxPanel.add(Box.createVerticalGlue());
      checkboxPanel.add(statisticsButton);
      
      checkboxPanel.add(Box.createVerticalGlue());
      checkboxPanel.add(fileNameLabel);
      checkboxPanel.add(Box.createVerticalGlue());
      checkboxPanel.add(buttonPanel);
      checkboxPanel.add(Box.createVerticalGlue());
      optionPanel.add(Box.createHorizontalGlue());
      optionPanel.add(checkboxPanel);
      optionPanel.add(Box.createHorizontalGlue());
      
  
      movesOnlyButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          LogWriter.enablePrintModus(1);
          LogWriter.disablePrintModus(2);
          LogWriter.disablePrintModus(4);
          LogWriter.disablePrintModus(8);
          FileOptionPane.access$0();
        }
        
      });
      movesEvalButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          LogWriter.switchPrintModus(2);
          if (LogWriter.isPrintModus(2))
            LogWriter.disablePrintModus(1);
          FileOptionPane.access$0();
        }
        
      });
      bestLinesButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          LogWriter.switchPrintModus(4);
          if (LogWriter.isPrintModus(4))
            LogWriter.disablePrintModus(1);
          FileOptionPane.access$0();
        }
        
      });
      allLinesButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          LogWriter.switchPrintModus(8);
          if (LogWriter.isPrintModus(8))
            LogWriter.disablePrintModus(1);
          FileOptionPane.access$0();
        }
        
      });
      selectFileButton.addActionListener(new ActionListener()
      {
  
        public void actionPerformed(ActionEvent e) {}
  
      });
      okButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          FileOptionPane.frame.dispose();
        }
        
      });
      noOutputButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          LogWriter.switchTo(0);
        }
        
      });
      stdOutButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          LogWriter.switchTo(1);
          FileOptionPane.access$0();
        }
        
      });
      statisticsButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          LogWriter.switchPrintModus(256);
          FileOptionPane.access$0();
        }
        
      });
      fileOutButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if ((LogWriter.getFileName() == null) || 
            (LogWriter.getFileName() == ""))
            FileOptionPane.access$1();
          LogWriter.switchTo(2);
          FileOptionPane.access$0();
        }
        
      });
      updateButtons();
      
  
      frame.getContentPane().add(optionPanel);
      frame.setSize(350, 400);
      frame.setLocation(400, 300);
      frame.setVisible(true);
    }
    
  
  
  
    private static void updateButtons()
    {
      if (LogWriter.fileAvailable()) {
        fileNameLabel.setText("Log file: " + LogWriter.getFileName());
      } else
        fileNameLabel.setText("[no file selected]");
      fileNameLabel.setEnabled(LogWriter.isSetTo(2));
      movesOnlyButton.setSelected(LogWriter.isPrintModus(1));
      movesEvalButton.setSelected(LogWriter.isPrintModus(2));
      bestLinesButton.setSelected(LogWriter.isPrintModus(4));
      allLinesButton.setSelected(LogWriter.isPrintModus(8));
      noOutputButton.setSelected(LogWriter.isSetTo(0));
      stdOutButton.setSelected(LogWriter.isSetTo(1));
      fileOutButton.setSelected(LogWriter.isSetTo(2));
      statisticsButton.setSelected(LogWriter.isPrintModus(256));
    }
    
    private static ButtonGroup outputGroup;
    private static JRadioButton noOutputButton;
    private static JRadioButton stdOutButton;
    private static JRadioButton fileOutButton;
    
    private static void openFileChooserDialog() {
      JFileChooser fileChooser = new JFileChooser();
      int result = fileChooser.showDialog(frame, "Select file");
      if (result == 1)
        return;
      LogWriter.setFile(fileChooser.getSelectedFile());
      updateButtons();
    }
    
    private static JCheckBox movesOnlyButton;
    private static JCheckBox movesEvalButton;
    private static JCheckBox bestLinesButton;
    private static JCheckBox allLinesButton;
    private static JCheckBox statisticsButton;
  }


/* Location:              /home/alexandre/Boku1.21.jar!/boku/FileOptionPane.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */