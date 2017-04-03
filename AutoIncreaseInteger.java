package co.fiottrendsolar.m2m.utils;

/**
 * Created by caoxuanphong on 3/31/17.
 * Use for PendingIntent in Notification
 *
 * Usesage: 
 * 		new AutoIncreaseInteger().getValue();	
 */

public class AutoIncreaseInteger {
    private static java.util.concurrent.atomic.AtomicInteger value = new java.util.concurrent.atomic.AtomicInteger(1);

    public int getValue() {
        return value.incrementAndGet();
    }
    
}
