package com.sortify.main.repository;

import com.sortify.main.model.SortifyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SortifyUserRepository extends JpaRepository<SortifyUser, String> {
}
