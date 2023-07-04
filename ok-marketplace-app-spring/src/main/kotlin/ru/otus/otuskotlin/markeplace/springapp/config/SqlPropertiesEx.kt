package ru.otus.otuskotlin.markeplace.springapp.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import ru.otus.otuskotlin.marketplace.backend.repo.sql.SqlProperties

//Annotation @ConstructorBinding is needed, it cannot be placed over the c @Bean method, one way as workaround
@ConfigurationProperties("sql")
class SqlPropertiesEx @ConstructorBinding constructor(
    url: String,
    user: String,
    password: String,
    schema: String,
    dropDatabase: Boolean
) : SqlProperties(url, user, password, schema, dropDatabase)