package fr.maxlego08.zauctionhouse.libs.sarah.libs.hikari;

public interface HikariPoolMXBean {
   int getIdleConnections();

   int getActiveConnections();

   int getTotalConnections();

   int getThreadsAwaitingConnection();

   void softEvictConnections();

   void suspendPool();

   void resumePool();
}
