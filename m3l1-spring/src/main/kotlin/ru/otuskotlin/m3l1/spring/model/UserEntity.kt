package ru.otuskotlin.m3l1.spring.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
data class UserEntity(
    @Id
    @GeneratedValue
    val id: Int,
    val name: String
)
