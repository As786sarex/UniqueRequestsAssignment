package com.verve.assignment.uniquerequests.repositories;

public interface VerveDataRepository {

    boolean upsert(int key);

    long getUniqueCount();

    long fetchAndReset();
}
