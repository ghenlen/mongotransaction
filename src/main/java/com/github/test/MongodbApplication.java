package com.github.test;

import com.github.test.mongodb.common.repository.CustomSimpleMongodbRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.github.test.**.dao",repositoryBaseClass = CustomSimpleMongodbRepository.class)
public class MongodbApplication
{
    public static void main( String[] args )
    {

        SpringApplication.run(MongodbApplication.class,args);
    }
}
