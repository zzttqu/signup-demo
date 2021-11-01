package com.zty.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long>
{
    List<Student> findAllByLeaveTimeIsNull();
    List<Student> findAllByNameAndLeaveTimeIsNull(String name);
    List<Student> findAllByName(String name);
//    AddStudent findById(int id);
}
