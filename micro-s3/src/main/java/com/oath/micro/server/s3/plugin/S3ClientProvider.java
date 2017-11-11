package com.oath.micro.server.s3.plugin;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.oath.micro.server.s3.S3Configuration;

@Configuration
@ComponentScan(basePackages = {"com.oath.micro.server.s3"})
public class S3ClientProvider {

    @Autowired
    private S3Configuration s3Configuration;

    @Bean
    public AmazonS3Client getClient() {

        AWSCredentials credentials = getAwsCredentials();

        AmazonS3Client amazonS3Client = new AmazonS3Client(credentials, getClientConfiguration());

        if (s3Configuration.getRegion() != null) {
            Region region = Region.getRegion(Regions.fromName(s3Configuration.getRegion()));
            amazonS3Client.setRegion(region);
        }

        return amazonS3Client;
    }

    AWSCredentials getAwsCredentials() {
        AWSCredentials credentials;

        if(s3Configuration.isDefaultChainEnabled()) {
            credentials = new DefaultAWSCredentialsProviderChain().getCredentials();
        } else if (s3Configuration.getSessionToken() == null) {
            credentials = new BasicAWSCredentials(s3Configuration.getAccessKey(), s3Configuration.getSecretKey());
        } else {
            credentials = new BasicSessionCredentials(s3Configuration.getAccessKey(), s3Configuration.getSecretKey(),
                                                      s3Configuration.getSessionToken());
        }
        return credentials;
    }

    private ClientConfiguration getClientConfiguration() {
        return new ClientConfiguration().withMaxConnections(s3Configuration.getMaxConnections());
    }
}
