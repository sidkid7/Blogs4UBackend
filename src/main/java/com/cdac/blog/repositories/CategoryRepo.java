package com.cdac.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cdac.blog.entities.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer> {

}
