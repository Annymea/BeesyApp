package com.beesy.data

class LocalTaskRepository(
    private val taskDAO: TaskDAO,
) : TaskRepository {
    override suspend fun insertTask(task: Task) {
        taskDAO.insertTask(task)
    }

    override suspend fun getAllTasks(): List<Task> = taskDAO.getAllTasks()

    override suspend fun updateItem(task: Task) {
        taskDAO.updateTask(task)
    }

    override suspend fun deleteTask(task: Task) {
        taskDAO.deleteTask(task)
    }
}