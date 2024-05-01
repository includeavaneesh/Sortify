package com.sortify.main.repository;

import com.sortify.main.model.SortifySubFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SortifySubFolderRepository extends JpaRepository<SortifySubFolder, String> {
}