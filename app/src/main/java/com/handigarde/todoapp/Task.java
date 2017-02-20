package com.handigarde.todoapp;

/**
 * Created by ryanhandy on 2/19/17.
 */

public class Task {

    private String task;
    private String priority;

    public Task(String task, String priority) {
        this.task = task;
        this.priority = priority;
    }

    public String getTask() {
        return task;
    }

    public String getPriority() {
        return priority;
    }

    public void setTask(String newTask) {
        task = newTask;
    }

    public void setPriority(String newPriority) {
        priority = newPriority;
    }

}
