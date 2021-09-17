package com.example.projekt1.activities.plugins.ToDo;

public class ToDoModel {

    //id = row in Database
    //status 0 = not checked
    //Status 1 = checked
    private int id, status;

    //String task is the actual name of the task
    //each task has id and status
    private String task;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
