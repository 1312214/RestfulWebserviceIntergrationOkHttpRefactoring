package com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.restbean;

/**
 * Created by rogerh on 7/23/2018.
 */

public class ModifiedTodoPayload {

    private ToDoItem currentToDoItem;
    private ToDoItem proposedToDoItem;

    public ModifiedTodoPayload(ToDoItem currentToDoItem, ToDoItem proposedToDoItem){
        this.currentToDoItem = currentToDoItem;
        this.proposedToDoItem = proposedToDoItem;
    }

    public ToDoItem getCurrentToDoItem() {
        return currentToDoItem;
    }

    public void setCurrentToDoItem(ToDoItem currentToDoItem) {
        this.currentToDoItem = currentToDoItem;
    }

    public ToDoItem getProposedToDoItem() {
        return proposedToDoItem;
    }

    public void setProposedToDoItem(ToDoItem proposedToDoItem) {
        this.proposedToDoItem = proposedToDoItem;
    }
}
