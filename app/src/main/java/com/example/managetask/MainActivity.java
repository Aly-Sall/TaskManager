package com.example.managetask;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private TaskDatabase taskDatabase;
    private LiveData<List<Task>> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        // Définir un LayoutManager pour le RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialisation de la base de données dans un thread séparé
        new Thread(() -> {
            taskDatabase = Room.databaseBuilder(
                    getApplicationContext(),
                    TaskDatabase.class, "task_database"
            ).build();

            taskList = taskDatabase.taskDao().getAllTasks();

            runOnUiThread(() -> {
                // Observer les changements dans la liste des tâches
                taskList.observe(this, tasks -> {
                    if (adapter == null) {
                        adapter = new TaskAdapter(tasks, task -> {
                            // Logique pour supprimer une tâche
                            new Thread(() -> taskDatabase.taskDao().deleteTask(task)).start();
                        }, task -> {
                            // Logique pour modifier une tâche
                            Intent intent = new Intent(MainActivity.this, EditTaskActivity.class);
                            intent.putExtra("taskId", task.getId()); // Passer l'ID de la tâche
                            startActivity(intent);
                        });
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.updateTasks(tasks); // Mettre à jour la liste des tâches
                    }
                });
            });
        }).start();

        // Lancer l'écran d'ajout de tâche
        Button addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Recharger les données après avoir ajouté une tâche
        if (taskList != null) {
            taskList.removeObservers(this); // Supprimer les anciens observateurs
        }

        new Thread(() -> {
            taskDatabase = Room.databaseBuilder(
                    getApplicationContext(),
                    TaskDatabase.class, "task_database"
            ).build();

            taskList = taskDatabase.taskDao().getAllTasks();

            runOnUiThread(() -> {
                taskList.observe(this, tasks -> {
                    if (adapter == null) {
                        adapter = new TaskAdapter(tasks, task -> {
                            // Logique pour supprimer une tâche
                            new Thread(() -> taskDatabase.taskDao().deleteTask(task)).start();
                        }, task -> {
                            // Logique pour modifier une tâche
                            Intent intent = new Intent(MainActivity.this, EditTaskActivity.class);
                            intent.putExtra("taskId", task.getId()); // Passer l'ID de la tâch
                            startActivity(intent);
                        });
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.updateTasks(tasks); // Mettre à jour la liste des tâches
                    }
                });
            });
        }).start();
    }
}
