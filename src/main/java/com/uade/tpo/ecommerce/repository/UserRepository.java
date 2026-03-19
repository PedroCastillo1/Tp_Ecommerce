package com.uade.tpo.ecommerce.repository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    String getUsername(long id);
}
