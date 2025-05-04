  package tpzsgames;
  
  import bitboard.LookupTable;
  import treesearch.AlphaBeta;
  import treesearch.BokuEngine;
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  public class Player
    extends PlayerType
  {
    public static final int BLACK = 1;
    public static final int WHITE = -1;
    private BokuEngine searchEngine;
    private LookupTable lookupTable;
    
    public Player(GameType game, int colour, boolean isHuman)
    {
      super(colour, isHuman);
      this.searchEngine = new AlphaBeta(game);
      this.lookupTable = new LookupTable();
    }
    
  
  
  
  
    public synchronized BokuEngine getEngine()
    {
      return this.searchEngine;
    }
    
  
  
  
  
    public synchronized LookupTable getLookupTable()
    {
      return this.lookupTable;
    }
    
    public void setEngine(BokuEngine engine) {
      this.searchEngine = engine;
    }
    
    public void setLookupTable(LookupTable table) {
      this.lookupTable = table;
    }
    
  
  
    public String toString()
    {
      return 
        (this.colour == 1 ? "Black: " : "White: ") + (this.isHuman ? "Human" : new StringBuffer(String.valueOf(getEngine().toString())).append(", ")
        .append(getLookupTable().toString()).toString());
    }
  }


/* Location:              /home/alexandre/Boku1.21.jar!/tpzsgames/Player.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */