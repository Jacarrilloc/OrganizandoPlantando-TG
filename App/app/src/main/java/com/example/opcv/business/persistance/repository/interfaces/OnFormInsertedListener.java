package com.example.opcv.business.persistance.repository.interfaces;

public interface OnFormInsertedListener {
    void onFormInserted(String formId);

    void onFormInsertionError(Exception e);
}

