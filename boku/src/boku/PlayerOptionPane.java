  package boku;
  
  import java.awt.Container;
  import java.awt.Dimension;
  import java.awt.event.ActionEvent;
  import java.awt.event.ActionListener;
  import javax.swing.Box;
  import javax.swing.ButtonGroup;
  import javax.swing.JButton;
  import javax.swing.JFrame;
  import javax.swing.JLabel;
  import javax.swing.JRadioButton;
  
  
  public final class PlayerOptionPane
  {
    private static PlayerOptionPane instance = null;
    
    private static Player player;
    
    private static final Dimension BASE_SIZE = new Dimension(130, 20);
    
    private static JFrame frame;
    
    private static Box optionPanel;
    
    private static Box buttonPanel;
    
    private static Box humanPanel;
    
    private static Box computerPanel;
    
    private static Box enginePanel;
    private static Box tablePanel;
    private static Box okPanel;
    private static JLabel selectionHeading;
    
    private PlayerOptionPane(Player newPlayer)
    {
      player = newPlayer;
      frame = new JFrame("Player options for " + (
        player.getColour() == 1 ? "Black" : "White"));
      optionPanel = new Box(0);
      buttonPanel = new Box(1);
      humanPanel = new Box(0);
      computerPanel = new Box(0);
      
      enginePanel = new Box(0);
      engineLabel = new JLabel("Alpha-Beta engine");
      engineProperties = new JButton("Properties");
      
      tablePanel = new Box(0);
      tableLabel = new JLabel("Lookup table");
      tableProperties = new JButton("Properties");
      
      okPanel = new Box(0);
      selectionHeading = new JLabel("This player is a");
      selectionGroup = new ButtonGroup();
      humanButton = new JRadioButton(" human");
      computerButton = new JRadioButton(" computer");
      blank = new JLabel();
      okButton = new JButton("OK");
      
      selectionGroup.add(humanButton);
      selectionGroup.add(computerButton);
      buttonPanel.add(Box.createVerticalGlue());
      selectionHeading.setPreferredSize(BASE_SIZE);
      humanPanel.add(selectionHeading);
      humanPanel.add(humanButton);
      humanPanel.add(Box.createHorizontalGlue());
      buttonPanel.add(humanPanel);
      blank.setPreferredSize(BASE_SIZE);
      computerPanel.add(blank);
      computerPanel.add(computerButton);
      computerPanel.add(Box.createHorizontalGlue());
      buttonPanel.add(computerPanel);
      buttonPanel.add(Box.createVerticalGlue());
      buttonPanel.add(Box.createVerticalGlue());
      
      engineLabel.setPreferredSize(BASE_SIZE);
      enginePanel.add(engineLabel);
      enginePanel.add(Box.createHorizontalGlue());
      enginePanel.add(engineProperties);
      enginePanel.add(Box.createHorizontalGlue());
      buttonPanel.add(enginePanel);
      buttonPanel.add(Box.createVerticalGlue());
      
      tableLabel.setPreferredSize(BASE_SIZE);
      tablePanel.add(tableLabel);
      tablePanel.add(Box.createHorizontalGlue());
      tablePanel.add(tableProperties);
      tablePanel.add(Box.createHorizontalGlue());
      buttonPanel.add(tablePanel);
      buttonPanel.add(Box.createVerticalGlue());
      
      buttonPanel.add(Box.createVerticalGlue());
      okPanel.add(okButton);
      buttonPanel.add(okPanel);
      buttonPanel.add(Box.createVerticalGlue());
      optionPanel.add(Box.createHorizontalGlue());
      optionPanel.add(buttonPanel);
      optionPanel.add(Box.createHorizontalGlue());
      
  
      humanButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          PlayerOptionPane.player.set(true);
          PlayerOptionPane.this.updateButtonSettings();
        }
      });
      computerButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          PlayerOptionPane.player.set(false);
          PlayerOptionPane.this.updateButtonSettings();
        }
      });
      okButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          PlayerOptionPane.frame.dispose();
        }
      });
      engineProperties.addActionListener(
        new ActionListener()
        {
          public void actionPerformed(ActionEvent e) {
            EngineOptionPane.getInstance(PlayerOptionPane.player.getEngine()).showOptionPane();
          }
        });
      tableProperties.addActionListener(
        new ActionListener()
        {
          public void actionPerformed(ActionEvent e) {
            TableOptionPane.getInstance(PlayerOptionPane.player.getLookupTable()).showOptionPane();
          }
        });
    }
    
    public static synchronized PlayerOptionPane getInstance(Player newPlayer) {
      if (instance == null) {
        instance = new PlayerOptionPane(newPlayer);
      }
      else {
        player = newPlayer;
        frame.dispose();
        frame = new JFrame("Player options for " + (
          player.getColour() == 1 ? "Black" : "White"));
      }
      return instance;
    }
    
    private static JLabel engineLabel;
    private static JLabel tableLabel;
    private static JLabel blank;
    private static ButtonGroup selectionGroup;
    
    public void showOptionPane() { frame.getContentPane().add(optionPanel);
      frame.setSize(350, 300);
      frame.setLocation(400, 300);
      frame.setVisible(true);
      updateButtonSettings();
    }
    
    private static JRadioButton humanButton;
    private static JRadioButton computerButton;
    private static JButton engineProperties;
    private static JButton tableProperties;
    private static JButton okButton;
    private void updateButtonSettings() { humanButton.setSelected(player.isHuman());
      computerButton.setSelected(player.isComputer());
      engineLabel.setEnabled(player.isComputer());
      engineProperties.setEnabled(player.isComputer());
      tableLabel.setEnabled(player.isComputer());
      tableProperties.setEnabled(player.isComputer());
    }
  }


/* Location:              /home/alexandre/Boku1.21.jar!/boku/PlayerOptionPane.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */