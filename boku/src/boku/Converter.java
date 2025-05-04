package boku;

public class Converter {

    public static String timeToString(long t) {
        int hs = (int) (t % 1000L) / 10;
        t /= 1000L;
        int sec = (int) (t % 60L);
        t /= 60L;
        int min = (int) (t % 60L);
        t /= 60L;
        int h = (int) t;
        String tStr = Integer.toString(min);
        tStr = tStr + (sec < 10 ? ":0" : ":") + sec;
        tStr = tStr + (hs < 10 ? ".0" : ".") + hs;
        if (h > 0) {
            tStr
                    = (h < 10 ? "0" : "") + Integer.toString(h) + ":" + (min < 10 ? "0" : "") + tStr;
        }
        return tStr;
    }

    public static String toString(double d, int precision) {
        if (d < 0.0D) {
            d = -d;
        }
        int shift = (int) Math.pow(10.0D, precision);
        long pre = (long) (d * shift);
        if (d * shift - pre >= 0.5D) {
            pre += 1L;
        }
        int post = (int) (pre % shift);
        pre /= shift;
        return pre + "." + post;
    }
}


/* Location:              /home/alexandre/Boku1.21.jar!/boku/Converter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
