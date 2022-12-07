package com.example.aditodolist.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.aditodolist.AddNewTask;
import com.example.aditodolist.MainActivity;
import com.example.aditodolist.R;
import com.example.aditodolist.Utils.DatabaseHandler;
import com.example.aditodolist.model.ToDoModel;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDoModel> todoList;
    private MainActivity activity;
    private DatabaseHandler db;

    public ToDoAdapter(DatabaseHandler db, MainActivity activity){
        this.db = db;
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false );
        return new ViewHolder(itemView);
        }

        public void onBindViewHolder(ViewHolder holder, int position){
        db.openDatabase();
        ToDoModel item = todoList.get(position);
        holder.task.setText(item.getTask());
        holder.task2.setText(item.getDescription());
        holder.date.setText(item.getDate());
 //       holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    db.updateStatus(item.getId(), 1);
                }
                else{
                    db.updateStatus(item.getId(), 0);
                }
            }
        });
        }

        public int getItemCount(){
            return todoList.size();
        }

         private boolean toBoolean(int n){
            return n!=0;
        }

        public void setTasks(List<ToDoModel> todoList){
        this.todoList = todoList;
        notifyDataSetChanged();
        }
        public Context getContext(){return activity;}

        public void deleteItem(int position){
        ToDoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
        }

        public void editItem(int position){
        ToDoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        bundle.putString("description",item.getDescription());
        bundle.putString("date", item.getDate());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
        }
         public static class ViewHolder extends RecyclerView.ViewHolder{
            CheckBox task;
            TextView task2;
            TextView date;

            ViewHolder(View view){
                super(view);
                task = view.findViewById(R.id.todoCheckBox);
                task2 = view.findViewById(R.id.todoTextView);
                date = view.findViewById((R.id.todoTextView2));

            }
    }
}
