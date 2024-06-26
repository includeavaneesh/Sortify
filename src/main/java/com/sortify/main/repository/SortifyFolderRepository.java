package com.sortify.main.repository;

import com.sortify.main.model.SortifyFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SortifyFolderRepository extends JpaRepository<SortifyFolder, String> {
}