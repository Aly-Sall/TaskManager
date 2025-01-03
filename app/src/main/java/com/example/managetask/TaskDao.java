package com.example.managetask;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM task_table")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM task_table WHERE id = :taskId")
    Task getTaskById(int taskId);

    @Query("SELECT * FROM task_table WHERE task_complete_status = 0")
    LiveData<List<Task>> getIncompleteTasks();

    @Query("SELECT * FROM task_table WHERE task_complete_status = 1")
    LiveData<List<Task>> getCompleteTasks();

    default void deleteTask(Object task) {
        delete((Task) task);
    }


}
