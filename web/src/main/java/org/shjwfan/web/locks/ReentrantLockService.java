package org.shjwfan.web.locks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.stereotype.Service;

@Service
public class ReentrantLockService implements LockService {

  private final Map<String, ReentrantLock> id2Lock = new ConcurrentHashMap<>();

  @Override
  public Lock computeIfAbsent(String id) {
    return id2Lock.computeIfAbsent(id, unused -> new ReentrantLock());
  }
}
