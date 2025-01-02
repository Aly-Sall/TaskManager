package com.example.managetask;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditTaskActivity extends AppCompatActivity {

    private EditText titleEditText, descriptionEditText, priorityEditText, dueDateEditText;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        // Récupérer l'ID de la tâche à modifier
        int taskId = getIntent().getIntExtra("taskId", -1);

        // Initialisation des champs de saisie
        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        priorityEditText = findViewById(R.id.priorityEditText);
        dueDateEditText = findViewById(R.id.dueDateEditText);

        Button saveTaskButton = findViewById(R.id.saveTaskButton);

        // Charger la tâche à modifier
        new Thread(() -> {
            TaskDatabase taskDatabase = TaskDatabase.getInstance(getApplicationContext());
            task = taskDatabase.taskDao().getTaskById(taskId);

            runOnUiThread(() -> {
                if (task != null) {
                    titleEditText.setText(task.getTitle());
                    descriptionEditText.setText(task.getDescription());
                    priorityEditText.setText(task.getPriority());
                    dueDateEditText.setText(task.getDueDate());
                }
            });
        }).start();

        // Lorsque l'utilisateur enregistre les modifications
        saveTaskButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String priority = priorityEditText.getText().toString();
            String dueDate = dueDateEditText.getText().toString();

            // Mettre à jour la tâche dans la base de données
            task.setTitle(title);
            task.setDescription(description);
            task.setPriority(priority);
            task.setDueDate(dueDate);

            new Thread(() -> {
                TaskDatabase taskDatabase = TaskDatabase.getInstance(getApplicationContext());
                taskDatabase.taskDao().update(task);
                finish(); // Retour à l'écran principal après modification
            }).start();
        });
    }
}
