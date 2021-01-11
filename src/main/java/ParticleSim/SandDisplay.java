package ParticleSim;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class SandDisplay extends JComponent implements MouseListener,
  MouseMotionListener, ActionListener, ChangeListener
{
	private static final long serialVersionUID = 1L;
  private BufferedImage image;
  private int cellSize;
  private JFrame frame;
  private int numRows;
  private int numCols;
  private int[] mouseLoc;

  private HashMap<String, JButton> cellButtons = new HashMap<String, JButton>();
  private JButton selectedCellButton;
  
  private JSlider sizeSlider;
  
  public SandDisplay(String title, int numRows, int numCols, Class<? extends Cell>[] cellTypes)
  {
    this.numRows = numRows;
    this.numCols = numCols;
    mouseLoc = null;
    
    try { 
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {}

    //determine cell size
    cellSize = Math.max(1, 600 / Math.max(numRows, numCols));
    image = new BufferedImage(numCols * cellSize, numRows * cellSize, BufferedImage.TYPE_INT_RGB);
    
    frame = new JFrame(title);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
    
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
    frame.getContentPane().add(topPanel);
    
    setPreferredSize(new Dimension(numCols * cellSize, numRows * cellSize));
    addMouseListener(this);
    addMouseMotionListener(this);
    topPanel.add(this);
    
    
    JPanel buttonPanel = new JPanel();

    JButton resetButton = new JButton("Reset");
    buttonPanel.add(resetButton);

    resetButton.addActionListener(new AbstractAction() {
      private static final long serialVersionUID = 1L;
      
		  @Override
      public void actionPerformed(ActionEvent e) {
        SandLab.grid = new Cell[SandLab.numRows][SandLab.numCols];
      }
    });

    frame.getContentPane().add(buttonPanel);

    JPanel cellButtonPanel = new JPanel();

    for (int i = 0; i < cellTypes.length; i++) {
      JButton btn;
      if (cellTypes[i] != null) {
        btn = new JButton(cellTypes[i].getSimpleName());
        cellButtons.put(cellTypes[i].getName(), btn);
        btn.setActionCommand(cellTypes[i].getName());
      } else {
        btn = new JButton("Empty");
        cellButtons.put("Empty", btn);
      }
      btn.addActionListener(this);
      cellButtonPanel.add(btn);
    }

    buttonPanel.add(cellButtonPanel);
    
    JButton selected = (JButton)cellButtons.values().toArray()[0];
    selected.setSelected(true);
   
    sizeSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
    sizeSlider.addChangeListener(this);
    sizeSlider.setMajorTickSpacing(1);
    sizeSlider.setPaintTicks(true);
    Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
    labelTable.put(Integer.valueOf(0), new JLabel("1"));
    labelTable.put(Integer.valueOf(100), new JLabel("10"));
    sizeSlider.setLabelTable(labelTable);
    sizeSlider.setPaintLabels(true);

    frame.getContentPane().add(sizeSlider);

    frame.pack();
    frame.setVisible(true);
  }

  public int sizeSliderValue() {
    return sizeSlider.getValue();
  }
  
  public void paintComponent(Graphics g)
  {
    g.drawImage(image, 0, 0, null);
  }
  
  public void pause(int milliseconds)
  {
    try
    {
      Thread.sleep(milliseconds);
    }
    catch(InterruptedException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public int[] getMouseLocation()
  {
    return mouseLoc;
  }
  
  @SuppressWarnings("unchecked")
  public Class<? extends Cell> getSelected()
  {
    try {
      return (Class<? extends Cell>) Class.forName(selectedCellButton.getActionCommand());
    } catch (Exception e) {
      return null;
    }
  }

  public void clear(Color color) {
    Graphics g = image.getGraphics();
    g.setColor(color);
    g.fillRect(0, 0, image.getWidth(), image.getHeight());
  }
  
  public void setColor(int row, int col, Color color)
  {
    Graphics g = image.getGraphics();
    g.setColor(color);
    g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
  }
  
  public void mouseClicked(MouseEvent e)
  {
  }
  
  public void mousePressed(MouseEvent e)
  {
    mouseLoc = toLocation(e);
  }
  
  public void mouseReleased(MouseEvent e)
  {
    mouseLoc = null;
  }
  
  public void mouseEntered(MouseEvent e)
  {
  }
  
  public void mouseExited(MouseEvent e)
  {
  }
  
  public void mouseMoved(MouseEvent e)
  {
  }
  
  public void mouseDragged(MouseEvent e)
  {
    mouseLoc = toLocation(e);
  }
  
  private int[] toLocation(MouseEvent e)
  {
    int row = e.getY() / cellSize;
    int col = e.getX() / cellSize;
    if (row < 0 || row >= numRows || col < 0 || col >= numCols)
      return null;
    int[] loc = new int[2];
    loc[0] = row;
    loc[1] = col;
    return loc;
  }
  
  public void actionPerformed(ActionEvent e)
  {
    selectedCellButton = cellButtons.get(e.getActionCommand());
    for (JButton button : (JButton[])cellButtons.values().toArray(new JButton[cellButtons.size()]))
      button.setSelected(false);
    ((JButton)e.getSource()).setSelected(true);
  }
  
  public void stateChanged(ChangeEvent e)
  {
    //speed = computeSpeed(sizeSlider.getValue());
  }
  
  //returns number of times to step between repainting and processing mouse input 
  // public int getSpeed()
  // {
  //   return speed;
  // }
  
  //returns speed based on sliderValue
  //speed of 0 returns 10^3
  //speed of 100 returns 10^6
  // private int computeSpeed(int sliderValue)
  // {
  //   return (int)Math.pow(10, 0.03 * sliderValue + 3);
  // }

  // private void enterFullScreen() {
  //   GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
  //   GraphicsDevice device = graphicsEnvironment.getDefaultScreenDevice();
  //   if (device.isFullScreenSupported()) {
  //       device.setFullScreenWindow(frame);
  //       frame.validate();
  //   }
  // }
}