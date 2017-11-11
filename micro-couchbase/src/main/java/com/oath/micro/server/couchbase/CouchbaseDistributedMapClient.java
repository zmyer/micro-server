package com.oath.micro.server.couchbase;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.oath.cyclops.util.ExceptionSoftener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oath.micro.server.distributed.DistributedMap;
import com.couchbase.client.CouchbaseClient;

public class CouchbaseDistributedMapClient<V> implements DistributedMap<V> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Optional<CouchbaseClient> couchbaseClient;

    public CouchbaseDistributedMapClient(CouchbaseClient couchbaseClient) {

        this.couchbaseClient = Optional.ofNullable(couchbaseClient);
    }

    @Override
    public boolean put(final String key, final V value) {
        logger.debug("put '{}', value:{}", key, value);
        return couchbaseClient.map(c -> putInternal(c, key, value))
                              .orElse(false);

    }

    private boolean putInternal(final CouchbaseClient client, final String key, final V value) {

        try {
            return client.set(key, value)
                         .get();
        } catch (InterruptedException | ExecutionException e) {
            throw ExceptionSoftener.throwSoftenedException(e);

        }
    }

    @Override
    public Optional<V> get(String key) {
        return couchbaseClient.map(c -> (V) c.get(key));

    }

    @Override
    public void delete(String key) {
        couchbaseClient.map(c -> c.delete(key));
    }
}
