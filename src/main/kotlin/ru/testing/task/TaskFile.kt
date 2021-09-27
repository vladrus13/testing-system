package ru.testing.task

class TaskFile(val title : String?, val listing : String, val fileType : TaskFileType) {
    enum class TaskFileType {
        JAVA, KOTLIN, CPP
    }
}