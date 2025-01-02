package com.example.managetask;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class AddTaskActivity extends AppCompatActivity {

    private EditText titleEditText, descriptionEditText, priorityEditText, dueDateEditText;
    private TaskDatabase taskDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // Initialisez TaskDatabase
        taskDatabase = Room.databaseBuilder(getApplicationContext(),
                TaskDatabase.class, "task_database").build();

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        priorityEditText = findViewById(R.id.priorityEditText);
        dueDateEditText = findViewById(R.id.dueDateEditText);

        Button saveTaskButton = findViewById(R.id.saveTaskButton);
        saveTaskButton.setOnClickListener(v -> saveTask());
    }

    private void saveTask() {
        String title = titleEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String priority = priorityEditText.getText().toString();
        String dueDate = dueDateEditText.getText().toString();

        if (title.isEmpty()) {
            Toast.makeText(this, "Title cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Créez une nouvelle tâche
        Task task = new Task(title, description, priority, dueDate, false);

        // Insérez la tâche dans un thread séparé
        new Thread(() -> {
            taskDatabase.taskDao().insert(task);
            runOnUiThread(() -> {
                Toast.makeText(AddTaskActivity.this, "Task added: " + task.getTitle(), Toast.LENGTH_SHORT).show();
                finish(); // Retourner à la page principale
            });
        }).start();

    }
}
