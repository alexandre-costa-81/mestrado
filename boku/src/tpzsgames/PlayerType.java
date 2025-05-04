  package tpzsgames;
  
  
  
  
  
  public abstract class PlayerType
  {
    public static final int FIRST = 1;
    
  
  
    public static final int SECOND = -1;
    
  
  
    protected int colour;
    
  
  
    public static final boolean HUMAN = true;
    
  
  
    public static final boolean COMPUTER = false;
    
  
  
    protected boolean isHuman;
    
  
  
  
    public PlayerType(int colour, boolean isHuman)
    {
      this.colour = colour;
      this.isHuman = isHuman;
    }
    
  
  
  
  
  
    public int getColour()
    {
      return this.colour;
    }
    
  
  
  
  
  
  
    public void set(boolean isHuman)
    {
      this.isHuman = isHuman;
    }
    
  
  
  
  
  
  
    public boolean isHuman()
    {
      return this.isHuman;
    }
    
  
  
  
  
  
  
    public boolean isComputer()
    {
      return !this.isHuman;
    }
  }


/* Location:              /home/alexandre/Boku1.21.jar!/tpzsgames/PlayerType.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */