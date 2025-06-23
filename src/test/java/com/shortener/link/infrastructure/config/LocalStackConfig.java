package com.shortener.link.infrastructure.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

@TestConfiguration(proxyBeanMethods = false)
public class LocalStackConfig {

    @Container
    private static final DockerImageName LOCALSTACK_IMAGE_NAME = DockerImageName.parse("localstack/localstack:latest");

    @Bean
    public LocalStackContainer localStackContainer() {
        LocalStackContainer localstack = new LocalStackContainer(LOCALSTACK_IMAGE_NAME).withServices(LocalStackContainer.Service.DYNAMODB);
        localstack.start();
        return localstack;
    }

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider(LocalStackContainer localStackContainer) {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(localStackContainer.getAccessKey(), localStackContainer.getSecretKey());
        return StaticCredentialsProvider.create(credentials);
    }
}
