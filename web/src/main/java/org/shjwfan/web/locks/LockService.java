package org.shjwfan.web.locks;

import java.util.concurrent.locks.Lock;

public interface LockService {

  Lock computeIfAbsent(String id);
}
