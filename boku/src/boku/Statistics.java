  package boku;
  
  
  public class Statistics
  {
    int gameCt;
    
    Collector blackTime;
    
    Collector whiteTime;
    
    Collector movesPerGame;
    
    Collector timePerGame;
    
    int blackMoveCt;
    
    int whiteMoveCt;
    
    int winsBlack;
    
    int winsWhite;
    
    int draws;
    
    int blackCaptureCt;
    
    int whiteCaptureCt;
    
    int blackCaptureCtTotal;
    int whiteCaptureCtTotal;
    StopWatch stopWatch;
    boolean collectingStats;
    String header;
    boolean ignoreFirstGame;
    boolean firstGame;
    
    public Statistics()
    {
      this.header = "";
      this.blackTime = new Collector();
      this.whiteTime = new Collector();
      this.movesPerGame = new Collector();
      this.timePerGame = new Collector();
      this.stopWatch = new StopWatch();
      reset();
      this.ignoreFirstGame = false;
      this.firstGame = true;
    }
    
  
  
    public void reset()
    {
      this.header = "";
      this.gameCt = 0;
      this.winsBlack = (this.winsWhite = this.draws = 0);
      this.blackTime.reset();
      this.whiteTime.reset();
      this.movesPerGame.reset();
      this.timePerGame.reset();
      this.blackCaptureCtTotal = (this.whiteCaptureCtTotal = 0);
      this.collectingStats = false;
    }
    
  
  
  
    public void newGame()
    {
      if ((!this.collectingStats) && ((!this.firstGame) || (!this.ignoreFirstGame))) {
        this.gameCt += 1;
        this.blackMoveCt = (this.whiteMoveCt = 0);
        this.blackCaptureCt = (this.whiteCaptureCt = 0);
        this.stopWatch.start();
        this.collectingStats = true;
      } else if ((this.firstGame) && (this.ignoreFirstGame)) {
        this.firstGame = false;
      }
    }
    
  
  
  
  
  
    public void finaliseGame(int winner)
    {
      if (this.collectingStats) {
        this.stopWatch.stop();
        if (winner == 1) {
          this.winsBlack += 1;
        } else if (winner == -1) {
          this.winsWhite += 1;
        } else
          this.draws += 1;
        this.movesPerGame.add(this.blackMoveCt + this.whiteMoveCt);
        this.timePerGame.add(this.blackTime.getTotal() + this.whiteTime.getTotal() - 
          this.timePerGame.getTotal());
        this.blackCaptureCtTotal += this.blackCaptureCt;
        this.whiteCaptureCtTotal += this.whiteCaptureCt;
        this.collectingStats = false;
      }
    }
    
  
  
  
  
  
  
    public void add(Move move)
    {
      if ((this.collectingStats) && (move != null)) {
        long timeInterval = this.stopWatch.getInterval();
        if (move.getMarble().getColour() == 1) {
          this.blackMoveCt += 1;
          this.blackTime.add(timeInterval);
          if (move.getCaptureField() != null)
            this.blackCaptureCt += 1;
        } else {
          this.whiteMoveCt += 1;
          this.whiteTime.add(timeInterval);
          if (move.getCaptureField() != null) {
            this.whiteCaptureCt += 1;
          }
        }
      }
    }
    
  
  
  
    public int getGameCounter()
    {
      return this.gameCt;
    }
    
  
  
  
  
  
    public void setHeader(String header)
    {
      this.header = header;
    }
    
  
  
  
  
  
  
  
  
    public String toString()
    {
      if ((this.collectingStats) || (this.gameCt == 0))
        return "";
      return 
      
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
        this.header + "\n" + "Games played:\t" + this.gameCt + "\n" + "Results (Black wins/White wins/Draws):\t(" + this.winsBlack + "/" + this.winsWhite + "/" + this.draws + ")\n" + "Half moves per game (min/max/average):\t" + this.movesPerGame.minMaxAverage(false) + "\n" + "Time per game (min/max/average):\t" + this.timePerGame.minMaxAverage(true) + "\n" + "Black time per move (min/max/average):\t" + this.blackTime.minMaxAverage(true) + "\n" + "White time per move (min/max/average):\t" + this.whiteTime.minMaxAverage(true) + "\n" + "Average capture moves:\t" + Converter.toString((this.blackCaptureCtTotal + this.whiteCaptureCtTotal) / this.gameCt, 1) + "\n" + "Average Black capture moves:\t" + Converter.toString(this.blackCaptureCtTotal / this.gameCt, 1) + "\n" + "Average White capture moves:\t" + Converter.toString(this.whiteCaptureCtTotal / this.gameCt, 1) + "\n";
    }
    
  
  
    class Collector
    {
      private int count;
      
  
      private long total;
      
      private long min;
      
      private long max;
      
      static final boolean TIME_FORMAT = true;
      
      static final boolean PLAIN_FORMAT = false;
      
  
      public Collector()
      {
        reset();
      }
      
  
  
  
      public void reset()
      {
        this.count = 0;
        this.total = 0L;
        this.min = (this.max = 0L);
      }
      
  
  
  
  
      public void add(long number)
      {
        this.total += number;
        if ((number < this.min) || (this.count == 0))
          this.min = number;
        if ((number > this.max) || (this.count == 0))
          this.max = number;
        this.count += 1;
      }
      
  
  
  
      public long getMin()
      {
        return this.min;
      }
      
  
  
  
      public long getMax()
      {
        return this.max;
      }
      
  
  
  
      public long getTotal()
      {
        return this.total;
      }
      
  
  
      public int getCount()
      {
        return this.count;
      }
      
  
  
  
  
      public double getAverageDouble()
      {
        return this.total / this.count;
      }
      
  
  
  
  
      public long getAverageLong()
      {
        return this.total / this.count;
      }
      
  
  
  
  
  
  
  
      public String minMaxAverage(boolean timeConversion)
      {
        if (timeConversion)
          return 
          
            "(" + Converter.timeToString(getMin()) + "/" + Converter.timeToString(getMax()) + "/" + Converter.timeToString(getAverageLong()) + ")";
        return 
          "(" + getMin() + "/" + getMax() + "/" + Converter.toString(getAverageDouble(), 1) + ")";
      }
      
  
  
  
  
  
  
      public String getLine()
      {
        return 
        
  
  
          "min: " + getMin() + " " + "max: " + getMax() + " " + "total: " + getTotal() + " " + "average: " + getAverageLong() + " " + "count: " + getCount();
      }
    }
  }


/* Location:              /home/alexandre/Boku1.21.jar!/boku/Statistics.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */