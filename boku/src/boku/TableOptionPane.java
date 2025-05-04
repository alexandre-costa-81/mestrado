  package boku;
  
  import bitboard.LookupTable;
  import java.awt.Container;
  import java.awt.Dimension;
  import java.awt.Font;
  import java.awt.event.ActionEvent;
  import java.awt.event.ActionListener;
  import javax.swing.Box;
  import javax.swing.JButton;
  import javax.swing.JFrame;
  import javax.swing.JTextArea;
  
  public final class TableOptionPane
  {
    private static TableOptionPane instance = null;
    
    private static LookupTable table;
    private static LookupTable tmpTable;
    private static JFrame frame;
    private static JButton okButton;
    private static JButton cancelButton;
    private static Box optionPanel;
    private static Box okPanel;
    private static Box viewPanel;
    private static JTextArea viewNotepad;
    
    private TableOptionPane(LookupTable newTable)
    {
      table = newTable;
      frame = new JFrame(table.toString() + " values");
      okPanel = new Box(0);
      viewPanel = new Box(0);
      optionPanel = new Box(1);
      
  
      viewNotepad = new JTextArea();
      viewNotepad.setText("Test");
      viewNotepad.setFont(
        new Font("Monospaced", 0, 14));
      viewNotepad.setEditable(false);
      okButton = new JButton("OK");
      cancelButton = new JButton("Cancel");
      okButton.setPreferredSize(new Dimension(80, 28));
      cancelButton.setPreferredSize(new Dimension(80, 28));
      okPanel.add(Box.createHorizontalGlue());
      okPanel.add(okButton);
      okPanel.add(cancelButton);
      okPanel.add(Box.createHorizontalGlue());
      viewPanel.add(Box.createHorizontalGlue());
      viewPanel.add(viewNotepad);
      viewPanel.add(Box.createHorizontalGlue());
      optionPanel.add(Box.createVerticalGlue());
      optionPanel.add(viewPanel);
      optionPanel.add(Box.createVerticalGlue());
      optionPanel.add(okPanel);
      optionPanel.add(Box.createVerticalGlue());
      
      okButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          TableOptionPane.table = TableOptionPane.tmpTable;
          TableOptionPane.frame.dispose();
        }
      });
      cancelButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          TableOptionPane.frame.dispose();
        }
      });
    }
    
    public static synchronized TableOptionPane getInstance(LookupTable newTable) {
      if (instance == null) {
        instance = new TableOptionPane(newTable);
      } else {
        tmpTable = newTable;table = newTable;
        frame.dispose();
        frame = new JFrame(table.toString() + " values");
      }
      return instance;
    }
    
  
  
  
    public void showOptionPane()
    {
      initialiseWindow();
      updateButtonSettings();
      frame.setVisible(true);
    }
    
  
  
    private void initialiseWindow()
    {
      frame.getContentPane().add(optionPanel);
      frame.setSize(480, 380);
      frame.setLocation(440, 340);
    }
    
  
  
  
    private void updateButtonSettings()
    {
      System.out.println(table.showTable());
      viewNotepad.setText(table.showTable());
    }
  }


/* Location:              /home/alexandre/Boku1.21.jar!/boku/TableOptionPane.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */