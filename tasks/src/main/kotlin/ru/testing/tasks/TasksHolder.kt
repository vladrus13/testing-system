package ru.testing.tasks

import org.w3c.dom.Node as DNode
import ru.testing.testlib.task.Task
import javax.xml.XMLConstants
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.reflect.KClass

/**
 * Tasks holder
 *
 */
class TasksHolder(xmlPath: String = "/tasks.xml"): HashMap<Long, Task>() {

    /**
     * Tasks
     */
    init {
        val url = TasksHolder::class.java.getResource(xmlPath)
        if (url == null || url.file == null) {
            error("Invalid configuration: no tasks")
        }
        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true)
        val db = documentBuilderFactory.newDocumentBuilder()
        val file = url.file
        val document = db.parse(file)
        document.documentElement.normalize()
        val listTasks = document.getElementsByTagName("task")

        val converter: DNode.(String) -> String = { clazz ->
            val attributes = attributes
            val real = attributes.getNamedItem(clazz) ?: error("Node with name $clazz can't be null")
            real.normalize()
            real.nodeValue
        }

        for (i in 0 until listTasks.length) {
            val item = listTasks.item(i)
            val id = item.converter("id").toLong()
            val clazz = Class.forName(item.converter("clazz")).kotlin as KClass<Task>
            this[id] = clazz.java.getDeclaredConstructor().newInstance()
        }
    }
}