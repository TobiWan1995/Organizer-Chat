package com.example.projekt1.models.plugins;

import com.example.projekt1.models.plugins.pluginData.ToDo;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PluginToDo extends Plugin<ToDo> {

    public PluginToDo(String id, String beschreibung, String typ, String chatRef, ArrayList<ToDo> tasks) {
        super(id, beschreibung, typ, chatRef, tasks);
    }

    public PluginToDo(){
        super();
    }

    @Override
    public void doPluginStuff() {
    }

    public void updateToDoTask(ToDo todo){
        this.pluginData = this.pluginData.stream().map(val -> {
            if(val.getId() == todo.getId()) val = todo;
            return val;
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    public void updateToDoTaskText(String id, String text){
        this.pluginData = this.pluginData.stream().map(val -> {
            if(id.equals(val.getId())) val.setTask(text);
            return val;
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    public void addToDoTask(ToDo todo){
        this.pluginData.add(todo);
    }
}
