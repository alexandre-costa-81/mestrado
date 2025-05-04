  package boku;
  
  import tpzsgames.FieldType;
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  public class Field
    implements FieldType
  {
    private int x;
    private int y;
    
    public Field(int x, int y)
    {
      this.x = x;
      this.y = y;
    }
    
  
  
  
  
    public int getX()
    {
      return this.x;
    }
    
  
  
  
  
    public int getY()
    {
      return this.y;
    }
    
  
  
  
  
  
  
    public boolean isEqual(Field thatField)
    {
      return (thatField != null) && (this.x == thatField.x) && (this.y == thatField.y);
    }
    
  
  
  
  
  
  
  
    public String toString()
    {
      return "(" + this.x + "," + this.y + ")";
    }
  }


/* Location:              /home/alexandre/Boku1.21.jar!/boku/Field.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */