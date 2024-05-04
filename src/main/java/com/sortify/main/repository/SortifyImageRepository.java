package com.sortify.main.repository;

import com.sortify.main.model.SortifyImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SortifyImageRepository extends JpaRepository<SortifyImage, String> {
}
