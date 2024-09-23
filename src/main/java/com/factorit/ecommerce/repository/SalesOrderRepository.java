package com.factorit.ecommerce.repository;

import com.factorit.ecommerce.model.SalesOrder;
import com.factorit.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {

    List<SalesOrder> findByUser(User user);
}
