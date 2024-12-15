package com.verve.assignment.uniquerequests.repositories;

public interface VerveDataRepository {

    void upsert(int key);

    long getUniqueCount();

    long fetchAndReset();
}
