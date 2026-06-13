package fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.util;

import java.sql.SQLTransientException;
import java.util.concurrent.Semaphore;

public class SuspendResumeLock {
   public static final SuspendResumeLock FAUX_LOCK = new SuspendResumeLock(false) {
      public void acquire() {
      }

      public void release() {
      }

      public void suspend() {
      }

      public void resume() {
      }
   };
   private static final int MAX_PERMITS = 10000;
   private final Semaphore acquisitionSemaphore;

   public SuspendResumeLock() {
      this(true);
   }

   private SuspendResumeLock(boolean var1) {
      this.acquisitionSemaphore = var1 ? new Semaphore(10000, true) : null;
   }

   public void acquire() {
      if (!this.acquisitionSemaphore.tryAcquire()) {
         if (Boolean.getBoolean("fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari.throwIfSuspended")) {
            throw new SQLTransientException("The pool is currently suspended and configured to throw exceptions upon acquisition");
         } else {
            this.acquisitionSemaphore.acquireUninterruptibly();
         }
      }
   }

   public void release() {
      this.acquisitionSemaphore.release();
   }

   public void suspend() {
      this.acquisitionSemaphore.acquireUninterruptibly(10000);
   }

   public void resume() {
      this.acquisitionSemaphore.release(10000);
   }
}
