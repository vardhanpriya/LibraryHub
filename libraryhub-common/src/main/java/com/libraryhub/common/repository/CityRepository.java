package com.libraryhub.common.repository;

import com.libraryhub.common.entity.City;
import com.libraryhub.common.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
    Optional<City> findByNameAndState(String name, State state);
}