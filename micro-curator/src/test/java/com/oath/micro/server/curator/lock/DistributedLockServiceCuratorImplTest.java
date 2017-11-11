package com.oath.micro.server.curator.lock;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CreateBuilder;
import org.apache.curator.framework.api.ExistsBuilder;
import org.apache.curator.framework.api.ProtectACLCreateModeStatPathAndBytesable;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;


public class DistributedLockServiceCuratorImplTest {
	@Test
	public void createNonExisting() throws Exception {
		CuratorFramework client = mock(CuratorFramework.class);
		ExistsBuilder builder = mock(ExistsBuilder.class);
		CreateBuilder createBuilder = mock(CreateBuilder.class);
		
		
		ProtectACLCreateModeStatPathAndBytesable<String> protector = mock(ProtectACLCreateModeStatPathAndBytesable.class);
		when(builder.forPath(anyString())).thenReturn(null);
		when(client.checkExists()).thenReturn(builder);
		when(client.create()).thenReturn(createBuilder);
		when(createBuilder.creatingParentContainersIfNeeded()).thenReturn((ProtectACLCreateModeStatPathAndBytesable<String>)protector);
		new DistributedLockServiceCuratorImpl(client, "/", 0);
		verify(protector).forPath(anyString(), anyObject());
	}
	
	@Test
	public void createNonExisting2() throws Exception {
		CuratorFramework client = mock(CuratorFramework.class);
		ExistsBuilder builder = mock(ExistsBuilder.class);
		CreateBuilder createBuilder = mock(CreateBuilder.class);
		
		ProtectACLCreateModeStatPathAndBytesable<String> protector = mock(ProtectACLCreateModeStatPathAndBytesable.class);
		when(builder.forPath(anyString())).thenReturn(new Stat());
		when(client.checkExists()).thenReturn(builder);
		when(client.create()).thenReturn(createBuilder);
		when(createBuilder.creatingParentContainersIfNeeded()).thenReturn(protector);
		new DistributedLockServiceCuratorImpl(client, "/", 0);
		verify(protector, times(0)).forPath(anyString(), anyObject());
	}
	
}
