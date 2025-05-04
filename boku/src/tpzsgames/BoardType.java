package tpzsgames;

public abstract interface BoardType
{
  public abstract void set(PieceType paramPieceType, FieldType paramFieldType);
  
  public abstract void clear(FieldType paramFieldType);
  
  public abstract PieceType getPiece(FieldType paramFieldType);
  
  public abstract void clear();
  
  public abstract int getWidth();
  
  public abstract int getHeight();
  
  public abstract boolean isPartOfBoard(FieldType paramFieldType);
}


/* Location:              /home/alexandre/Boku1.21.jar!/tpzsgames/BoardType.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */