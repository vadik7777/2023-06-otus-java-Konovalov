package ru.otus.hw09.legacy.crm.service;

import java.util.List;
import java.util.Optional;
import ru.otus.hw09.legacy.crm.model.Manager;

public interface DBServiceManager {

    Manager saveManager(Manager client);

    Optional<Manager> getManager(long no);

    List<Manager> findAll();
}
