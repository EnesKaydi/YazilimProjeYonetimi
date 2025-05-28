package com.eneskaydi.yazilimprojeyonetimi.Repository;

import com.eneskaydi.yazilimprojeyonetimi.Entity.AddedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddedUserRepository extends JpaRepository<AddedUser, Long> {
    Optional<AddedUser> findByEmail(String email);
} 