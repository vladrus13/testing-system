package ru.testing.testlib.domain

data class User(
    val id: Long,
    val name: String,
    val passwordHash: String
)