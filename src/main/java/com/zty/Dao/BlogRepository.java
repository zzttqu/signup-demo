package com.zty.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog,Long>
{
    List<Blog> findAllByUsername(String username);
    Blog findById(int id);
}
