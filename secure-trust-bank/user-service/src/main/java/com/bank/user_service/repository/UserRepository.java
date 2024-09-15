package com.bank.user_service.repository;

import com.bank.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByEmailId(String emailId);
    User findByPanNumber(String panNumber);
    User findByPhoneNumber(String panNumber);

}
