package com.example.managetask;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> tasks;
    private OnTaskClickListener onTaskClickListener;

    public TaskAdapter(List<Task> tasks, OnTaskClickListener onTaskClickListener) {
        this.tasks = tasks;
        this.onTaskClickListener = onTaskClickListener;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = tasks.get(position);

        // Configurer les données de la tâche
        holder.titleTextView.setText(task.getTitle());
        holder.descriptionTextView.setText(task.getDescription());

        // Gérer les clics sur les boutons de suppression et modification
        holder.deleteButton.setOnClickListener(v -> {
            tasks.remove(position); // Suppression de l'élément de la liste
            notifyItemRemoved(position); // Notifie l'adaptateur de la suppression
            onTaskClickListener.onDeleteClick(task); // Action après suppression
        });

        holder.editButton.setOnClickListener(v -> onTaskClickListener.onEditClick(task)); // Modification
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    // Méthode pour mettre à jour les tâches
    public void updateTasks(List<Task> tasks) {
        this.tasks = tasks != null ? tasks : new ArrayList<>();
        notifyDataSetChanged();
    }

    // Interface pour les clics sur les tâches
    public interface OnTaskClickListener {
        void onDeleteClick(Task task);
        void onEditClick(Task task);
    }

    // ViewHolder pour chaque élément de la liste
    public class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView;
        Button deleteButton, editButton;

        public TaskViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }
}
