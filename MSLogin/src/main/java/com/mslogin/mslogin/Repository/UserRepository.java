package com.mslogin.mslogin.Repository;

import com.mslogin.mslogin.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("UserRepository")
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.email = ?1 and u.senha = ?2")
    public User findByUser(String email, String senha);
}
