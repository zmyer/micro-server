package com.oath.micro.server.s3;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.oath.micro.server.s3.data.S3Utils;

public class S3UtilsTest {

    private boolean answer = true;

    @Test
    public void getAllSummaries() {
        answer = true;
        AmazonS3Client client = mock(AmazonS3Client.class);
        ObjectListing objectListing = mock(ObjectListing.class);
        when(client.listObjects(any(ListObjectsRequest.class))).thenReturn(objectListing);
        when(objectListing.isTruncated()).thenAnswer(__ -> {
            try {
                return answer;
            } finally {
                answer = false;
            }
        });
        S3Utils utils = new S3Utils(
                                    client, null, null, false, null);
        utils.getAllSummaries(new ListObjectsRequest());
        verify(objectListing, times(2)).getObjectSummaries();
    }

    @Test
    public void getSummariesStream() {
        answer = true;
        AmazonS3Client client = mock(AmazonS3Client.class);

        ObjectListing objectListing = mock(ObjectListing.class);

        when(objectListing.getObjectSummaries()).thenReturn(createSummaries());
        when(client.listObjects(any(ListObjectsRequest.class))).thenReturn(objectListing);
        when(objectListing.isTruncated()).thenAnswer(__ -> {
            try {
                return answer;
            } finally {
                answer = false;
            }
        });
        // when(objectListing.getObjectSummaries()).thenReturn(summaries);

        S3Utils utils = new S3Utils(
                                    client, null, null, false, null);
        verify(objectListing, times(0)).getObjectSummaries();
        Stream<String> stream = utils.getSummariesStream(new ListObjectsRequest(), s -> {
            return s.getKey();
        });

        assertEquals(500, stream.limit(500)
                                .count());

        verify(objectListing, times(1)).getObjectSummaries();

    }

    @Test
    public void getSummariesStreamFull() {
        answer = true;
        AmazonS3Client client = mock(AmazonS3Client.class);

        ObjectListing objectListing = mock(ObjectListing.class);

        when(objectListing.getObjectSummaries()).thenReturn(createSummaries());
        when(client.listObjects(any(ListObjectsRequest.class))).thenReturn(objectListing);
        when(objectListing.isTruncated()).thenAnswer(__ -> {
            try {
                return answer;
            } finally {
                answer = false;
            }
        });
        // when(objectListing.getObjectSummaries()).thenReturn(summaries);

        S3Utils utils = new S3Utils(
                                    client, null, null, false, null);
        verify(objectListing, times(0)).getObjectSummaries();
        Stream<String> stream = utils.getSummariesStream(new ListObjectsRequest(), s -> {
            return s.getKey();
        });

        assertEquals(2000, stream.limit(2000)
                                 .count());

    }

    private List<S3ObjectSummary> createSummaries() {
        List<S3ObjectSummary> summaries = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            S3ObjectSummary summary = new S3ObjectSummary();
            summary.setKey("" + i);
            summaries.add(summary);
        }

        return summaries;
    }

    @Test
    public void deleteObjects() {
        AmazonS3Client client = mock(AmazonS3Client.class);
        S3Utils utils = new S3Utils(
                                    client, null, null, false, null);
        List<KeyVersion> keys = new ArrayList<>();
        for (int i = 0; i < 2000; i++) {
            keys.add(new KeyVersion(
                                    ""));
        }

        utils.delete("", keys);

        verify(client, times(2)).deleteObjects(any());
    }

    @Test
    public void emptyInputStream() throws IOException {
        assertEquals(0, S3Utils.emptyInputStream()
                               .available());
    }

    @Test(expected = IOException.class)
    public void emptyInputStreamException() throws IOException {
        S3Utils.emptyInputStream()
               .read();
    }

}
