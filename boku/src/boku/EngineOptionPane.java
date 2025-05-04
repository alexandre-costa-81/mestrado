  package boku;
  
  import java.awt.Color;
  import java.awt.Container;
  import java.awt.Dimension;
  import java.awt.event.ActionEvent;
  import java.awt.event.ActionListener;
  import java.awt.event.ItemEvent;
  import java.awt.event.ItemListener;
  import javax.swing.Box;
  import javax.swing.JButton;
  import javax.swing.JCheckBox;
  import javax.swing.JFrame;
  import javax.swing.JLabel;
  import treesearch.BokuEngine;
  
  public final class EngineOptionPane
  {
    private static EngineOptionPane instance = null;
    
    private static int maxPly;
    
    private static BokuEngine engine;
    
    private static JFrame frame;
    
    private static JLabel plyText;
    
    private static JLabel plyLabel;
    
    private static JLabel randomHeading;
    private static JButton downButton;
    private static JButton upButton;
    
    private EngineOptionPane(BokuEngine newEngine)
    {
      engine = newEngine;
      frame = new JFrame(engine.getID() + " options");
      plyText = new JLabel();
      downButton = new JButton(Boku.arrowLIcon);
      upButton = new JButton(Boku.arrowRIcon);
      optionPanel = new Box(0);
      buttonPanel = new Box(1);
      plyPanel = new Box(0);
      randomHeading = new JLabel("If several moves have same evaluation:");
      randomButton = new JCheckBox("Choose move randomly");
      randomPanelV = new Box(1);
      randomPanelH = new Box(0);
      plyLabel = new JLabel("Maximum search depth (ply):");
      okPanel = new Box(0);
      okButton = new JButton("OK");
      cancelButton = new JButton("Cancel");
      maxPly = engine.getMaxPly();
      plyText.setBackground(Color.white);
      downButton.setDisabledIcon(Boku.arrowLDisabledIcon);
      upButton.setDisabledIcon(Boku.arrowRDisabledIcon);
      downButton.setPreferredSize(new Dimension(24, 28));
      upButton.setPreferredSize(new Dimension(24, 28));
      okButton.setPreferredSize(new Dimension(80, 28));
      cancelButton.setPreferredSize(new Dimension(80, 28));
      plyPanel.add(Box.createHorizontalGlue());
      plyPanel.add(plyLabel);
      plyPanel.add(plyText);
      plyPanel.add(downButton);
      plyPanel.add(upButton);
      plyPanel.add(Box.createHorizontalGlue());
      randomHeading.setPreferredSize(new Dimension(250, 30));
      randomPanelV.add(randomHeading);
      randomButton.setSelected(engine.randomChoiceAllowed());
      randomPanelV.add(randomButton);
      randomPanelV.add(Box.createVerticalGlue());
      randomPanelH.add(Box.createHorizontalGlue());
      randomPanelH.add(randomPanelV);
      randomPanelH.add(Box.createHorizontalGlue());
      okPanel.add(Box.createHorizontalGlue());
      okPanel.add(okButton);
      okPanel.add(cancelButton);
      okPanel.add(Box.createHorizontalGlue());
      buttonPanel.add(Box.createVerticalGlue());
      buttonPanel.add(plyPanel);
      buttonPanel.add(Box.createVerticalGlue());
      buttonPanel.add(randomPanelH);
      buttonPanel.add(Box.createVerticalGlue());
      buttonPanel.add(okPanel);
      buttonPanel.add(Box.createVerticalGlue());
      optionPanel.add(Box.createHorizontalGlue());
      optionPanel.add(buttonPanel);
      optionPanel.add(Box.createHorizontalGlue());
      downButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          EngineOptionPane.maxPly -= 1;
          EngineOptionPane.this.updateButtonSettings();
        }
      });
      upButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          EngineOptionPane.maxPly += 1;
          EngineOptionPane.this.updateButtonSettings();
        }
      });
      okButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          EngineOptionPane.engine.setMaxPly(EngineOptionPane.maxPly);
          EngineOptionPane.frame.dispose();
        }
      });
      cancelButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          EngineOptionPane.frame.dispose();
        }
      });
      randomButton.addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          EngineOptionPane.engine.setRandomChoice(
            e.getStateChange() == 1);
        }
      });
    }
    
    public static synchronized EngineOptionPane getInstance(BokuEngine newEngine) {
      if (instance == null) {
        instance = new EngineOptionPane(newEngine);
      } else {
        engine = newEngine;
        maxPly = engine.getMaxPly();
        frame.dispose();
        frame = new JFrame(engine.getID() + " options");
      }
      instance.updateButtonSettings();
      return instance;
    }
    
    private static JButton okButton;
    private static JButton cancelButton;
    private static JCheckBox randomButton;
    private static Box optionPanel;
    
    public void showOptionPane() { initialiseWindow();
      updateButtonSettings();
      frame.setVisible(true);
    }
    
  
  
    private void initialiseWindow()
    {
      frame.getContentPane().add(optionPanel);
      frame.setSize(400, 300);
      frame.setLocation(440, 340);
    }
    
    private static Box buttonPanel;
    private static Box plyPanel;
    private static Box randomPanelV;
    private static Box randomPanelH;
    private static Box okPanel;
    private void updateButtonSettings() { plyText.setText("  " + maxPly + "   ");
      downButton.setEnabled(maxPly > 0);
      upButton.setEnabled(maxPly < engine.getAllowedMaxPly());
    }
  }


/* Location:              /home/alexandre/Boku1.21.jar!/boku/EngineOptionPane.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */