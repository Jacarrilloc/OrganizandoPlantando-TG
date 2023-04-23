package com.example.opcv.repository.interfaces;

public interface OnFormInsertedListener {
    void onFormInserted(String formId);

    void onFormInsertionError(Exception e);
}

