package app.singleton.com.oath.micro.server.copy;

import org.springframework.stereotype.Component;

import com.oath.micro.server.dist.lock.DistributedLockService;

@Component
public class DummyLock implements DistributedLockService {

	@Override
	public boolean tryLock(String key) {
		return false;
	}

	@Override
	public boolean tryReleaseLock(String key) {
		return false;
	}

}
