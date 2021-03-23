package com.example.receiptas.model.util;

import androidx.lifecycle.MutableLiveData;

public class DataState<T> {
    private T data;
    private Throwable error;
    private final MutableLiveData<State> state = new MutableLiveData<>();

    public T getData() {
        return data;
    }

    public Throwable getError() {
        return error;
    }

    public MutableLiveData<State> getState() {
        return this.state;
    }

    public void setSuccess(T data) {
        this.data = data;
        this.state.setValue(State.SUCCESS);
    }

    public void setError(Throwable error) {
        this.error = error;
        this.state.setValue(State.ERROR);
    }

    public void setLoading() {
        this.state.setValue(State.LOADING);
    }

    public enum State {
        SUCCESS,
        ERROR,
        LOADING
    }
}
