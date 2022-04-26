package com.cctv.redis;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author by 封心
 * @classname RedisLockService
 * @description TODO
 * @date 2022/4/26 15:34
 */
@Service
public class RedisLockService {


  @Autowired
  private RedissonClient redisson;


  public void lock(String name) throws InterruptedException {
    //获得name的锁
    RLock lock = redisson.getLock(name);
    //对name进行加锁 线程会一直等待 直到拿到该锁
    lock.lock();
    //尝试对name进行加锁,线程会一直等待 直到拿到该锁 然后10秒后自动解锁
    lock.lock(10L, TimeUnit.SECONDS);
    //对name进行解锁,如果锁不是该线程持有则会抛出异常
    lock.unlock();
    //强制对name进行解锁,即此锁不论是那个线程持有都会进行解锁
    lock.forceUnlock();

    //尝试对name进行加锁,如果该锁被其他线程持有,会等待10秒,然后返回是否成功,如果成功 会在20秒后自动解锁
    boolean b = lock.tryLock(10L, 20L, TimeUnit.SECONDS);

    //尝试对name进行加锁 立即返回加锁状态 如果加锁成功会在20秒后自动解锁
    boolean b1 = lock.tryLock(20L, TimeUnit.SECONDS);
    //检查该锁是否被任何线程所持有
    boolean locked = lock.isLocked();
    //检查该锁是否当前线程持有
    boolean heldByCurrentThread = lock.isHeldByCurrentThread();
    //当前线程对该锁的保持次数
    int holdCount = lock.getHoldCount();
    //该锁的剩余时间
    long l = lock.remainTimeToLive();
  }


  public void testLock() {
    //创建实例对象
    RLock lock = redisson.getLock("newlock");

    //当前是否有锁
    boolean locked = lock.isLocked();
    if (locked) {
      //有锁直接return
      return;
    }
    //上锁
    lock.lock();
    //TODO 执行逻辑
    System.out.println("111111");
    //执行完毕释放锁
    lock.unlock();
  }
}
