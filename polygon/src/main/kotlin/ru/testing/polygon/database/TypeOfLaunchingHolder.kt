package ru.testing.polygon.database

import interfaces.AbstractTypeOfLaunching
import interfaces.AbstractTypeOfLaunchingHolder
import ru.testing.polygon.submission.OlympiadCpp
import ru.testing.polygon.submission.OlympiadJava

class TypeOfLaunchingHolder : AbstractTypeOfLaunchingHolder {
    override fun getTypesOfLaunch(): List<AbstractTypeOfLaunching> = listOf(OlympiadCpp, OlympiadJava)
}
