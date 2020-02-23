package com.github.test.mongodb.common.config;

import com.github.test.mongodb.common.page.MongoPageHelper;
import com.github.test.mongodb.common.strategy.CamelCaseFieldNamingStrategy;
import com.github.test.mongodb.converter.GenderReadConverter;
import com.github.test.mongodb.converter.GenderWriteConverter;
import java.util.Arrays;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
public class MongodbConfig {

  @Value("${spring.data.mongodb.property-naming-strategy}")
  private String fieldNamingStrategy;

  @Value("${spring.data.mongodb.transaction-enable}")
  private String transactionEnable;

  @Bean
  public MappingMongoConverter mappingMongoConverter(MongoDbFactory factory, MongoMappingContext context, BeanFactory beanFactory) {
    DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
    MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
    // Don't save _class to mongo
    mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
    mappingConverter.setCustomConversions(mongoCustomConversions());
    if("SNAKE_CASE".equalsIgnoreCase(fieldNamingStrategy)) {
      context.setFieldNamingStrategy(new CamelCaseFieldNamingStrategy());
    }

    return mappingConverter;
  }

  /**
   * spring mongodb 开启的事务的必要条件
   * 1、mongodb的版本为4.0版本以上
   * 2、mongodb必须存在副本
   * 3、事务只对已经存在的mongodb中的集合起作用，如果要进行操作的集合，在mongodb中还没有，必须得先创建该集合，否则当该集合进行插入操作时
   * 会报类似“Cannot create namespace sampledb_200.demo in multi-document transaction ”的错误，参考文档
   * https://stackoverflow.com/questions/52585715/cannot-create-namespace-in-multi-document-transactionmongodb-4-0-spring-data-2
   * @param factory
   * @return
   */
  @Bean
  @ConditionalOnProperty(name="spring.data.mongodb.transactionEnabled",havingValue = "true")
  MongoTransactionManager transactionManager(MongoDbFactory factory){

    return new MongoTransactionManager(factory);
  }

  @Bean
  public MongoPageHelper mongoPageHelper(MongoTemplate mongoTemplate){
    return new MongoPageHelper(mongoTemplate);
  }

  @Bean
  public MongoCustomConversions mongoCustomConversions() {
    return new MongoCustomConversions(Arrays.asList(new GenderWriteConverter(),new GenderReadConverter()));
  }

@PostConstruct
public void test(){
  System.out.println("transactionEnable:"+transactionEnable);
  System.out.println("fieldNamingStrategy:"+fieldNamingStrategy);
}


}
