package com.verve.assignment.uniquerequests.repositories.impl;

import com.verve.assignment.uniquerequests.repositories.VerveDataRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

@Repository
@Profile("basic")
public class InMemoryVerveDataRepository implements VerveDataRepository {

    private final ConcurrentHashMap<Integer, Boolean> uniqueIds;

    public InMemoryVerveDataRepository() {
        this.uniqueIds = new ConcurrentHashMap<>();
    }

    @Override
    public void upsert(final int id) {
        Boolean putIfAbsent = uniqueIds.putIfAbsent(id, true);
    }

    @Override
    public long getUniqueCount() {
        return uniqueIds.size();
    }

    @Override
    public long fetchAndReset() {
        long count = this.uniqueIds.size();
        this.uniqueIds.clear();
        return count;
    }
}
