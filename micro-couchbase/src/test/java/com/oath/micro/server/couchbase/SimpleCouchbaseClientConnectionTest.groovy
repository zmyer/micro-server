package com.oath.micro.server.couchbase;

import static org.hamcrest.Matchers.is
import static org.junit.Assert.*
import static org.mockito.Mockito.*

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

import com.oath.micro.server.distributed.DistributedMap;
import com.couchbase.client.CouchbaseClient

class SimpleCouchbaseClientConnectionTest {

	CouchbaseClient  client
	DistributedMap con

	@Before
	public void setup() {
		client = Mockito.mock(CouchbaseClient)
		con  = new CouchbaseDistributedMapClient(client)
	}
	@Test
	public void testDelete() {
		con.delete("key")
		Mockito.verify(client).delete(any(String))
	}

	@Test
	public void testGet() {
		con.get("key")
		Mockito.verify(client).get(any(String))
	}

	@Test
	public void testGetDistributedCacheDisabled() {
		con  = new CouchbaseDistributedMapClient(null)
		Optional result = con.get("key")
		assertThat(result, is(Optional.empty()))
	}
}
