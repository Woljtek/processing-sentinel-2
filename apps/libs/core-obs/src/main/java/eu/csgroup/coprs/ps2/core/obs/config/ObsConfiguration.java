package eu.csgroup.coprs.ps2.core.obs.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.transfer.s3.S3ClientConfiguration;
import software.amazon.awssdk.transfer.s3.S3TransferManager;

import java.net.URI;

import static software.amazon.awssdk.transfer.s3.SizeConstant.MB;

@Slf4j
@Configuration
public class ObsConfiguration {

    private final ObsProperties obsProperties;

    public ObsConfiguration(ObsProperties obsProperties) {
        this.obsProperties = obsProperties;
    }

    @Bean
    public S3Client s3Client() {
        log.debug("OBS Client enabled !");
        return S3Client.builder()
                .credentialsProvider(staticCredentialsProvider())
                .endpointOverride(URI.create(obsProperties.getEndpoint()))
                .region(Region.of(obsProperties.getRegion()))
                .serviceConfiguration(s3Configuration())
                .build();
    }

    @Bean
    public S3TransferManager s3TransferManager() {
        return S3TransferManager.builder()
                .s3ClientConfiguration(s3ClientConfiguration())
                .build();
    }

    public StaticCredentialsProvider staticCredentialsProvider() {
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(obsProperties.getAccessKey(), obsProperties.getSecretKey()));
    }

    private S3Configuration s3Configuration() {
        return S3Configuration.builder()
                .pathStyleAccessEnabled(true)
                .build();
    }

    private S3ClientConfiguration s3ClientConfiguration() {
        return S3ClientConfiguration.builder()
                .credentialsProvider(staticCredentialsProvider())
                .region(Region.of(obsProperties.getRegion()))
                .endpointOverride(URI.create(obsProperties.getEndpoint()))
                .minimumPartSizeInBytes(obsProperties.getMinimumPartSize() * MB)
                .maxConcurrency(obsProperties.getMaxConcurrency())
                .targetThroughputInGbps(obsProperties.getMaxThroughput())
                .build();
    }

}
