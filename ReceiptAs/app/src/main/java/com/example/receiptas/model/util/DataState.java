package com.example.receiptas.model.util;

public class DataState<T> {
    private T data;
    private Throwable error;
    private State state;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public enum State {
        SUCCESS,
        ERROR,
        LOADING
    }
}
