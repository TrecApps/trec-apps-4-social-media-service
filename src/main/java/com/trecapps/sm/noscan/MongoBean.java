package com.trecapps.sm.noscan;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories("${trecapps.sm.mongo.repos}")
public class MongoBean {
}
