package com.thorinhood.botFarm.configs

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration

@Profile("mongo")
@Configuration
class MongoConfig(
    @Value("\${database.name}") private val databaseName: String,
    @Value("\${database.mongo.host}") private val mongoHost: String,
    @Value("\${database.mongo.port}") private val mongoPort: Int,
    @Value("\${database.mongo.username}") private val mongoUsername: String,
    @Value("\${database.mongo.password}") private val mongoPassword: String
): AbstractMongoClientConfiguration() {
    override fun getDatabaseName(): String = databaseName

    override fun mongoClient() : MongoClient {
        val connectionString = ConnectionString("mongodb://$mongoUsername:$mongoPassword@" +
                "$mongoHost:$mongoPort/$databaseName")
        val mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build()
        return MongoClients.create(mongoClientSettings)
    }

}