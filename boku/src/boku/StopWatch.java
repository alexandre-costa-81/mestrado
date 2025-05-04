  package boku;
  
  import java.util.Date;
  
  
  
  
  
  
  
  
  
  
  
  
  
  public class StopWatch
  {
    private long startTime;
    private long lastTimeMark;
    private long stopTime;
    private boolean watchRunning;
    
    public StopWatch()
    {
      clear();
    }
    
  
  
    public void start()
    {
      this.startTime = new Date().getTime();
      this.lastTimeMark = this.startTime;
      this.watchRunning = true;
    }
    
  
  
    public void stop()
    {
      if (this.watchRunning) {
        this.stopTime += new Date().getTime() - this.startTime;
        this.watchRunning = false;
      }
    }
    
  
  
    public void clear()
    {
      this.startTime = (this.lastTimeMark = this.stopTime = 0L);
      this.watchRunning = false;
    }
    
  
  
  
  
  
  
    public long getIntermediateTime()
    {
      long now = this.watchRunning ? new Date().getTime() : 0L;
      return now - this.startTime;
    }
    
  
  
  
  
  
  
  
    public long getInterval()
    {
      long now = this.watchRunning ? new Date().getTime() : 0L;
      long interval = now - this.lastTimeMark;
      this.lastTimeMark = now;
      return interval;
    }
    
  
  
  
  
  
  
    public long getStopTime()
    {
      return this.stopTime;
    }
    
  
  
  
  
  
  
    public String intermediateTimeToString()
    {
      return Converter.timeToString(getIntermediateTime());
    }
    
  
  
  
  
  
    public String stopTimeToString()
    {
      return Converter.timeToString(this.stopTime);
    }
  }


/* Location:              /home/alexandre/Boku1.21.jar!/boku/StopWatch.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */