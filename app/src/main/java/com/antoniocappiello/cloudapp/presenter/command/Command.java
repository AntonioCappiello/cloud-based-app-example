package com.antoniocappiello.cloudapp.presenter.command;

public interface Command {

    void execute();
    void execute(String message);
}
