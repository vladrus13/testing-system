package ru.testing.testlib.domain

// TODO: We need a domain module, probably.
// Placing it into polygon will result in cyclic dependency between configuration and polygon.
data class User(
    val id: Int,
    val name: String,
    val passwordHash: String
)