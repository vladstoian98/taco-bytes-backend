package com.example.tacocloudapplication.repo.impl;

import com.example.tacocloudapplication.table.TacoOrder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<TacoOrder, Long> {

    List<TacoOrder> findByDeliveryZip(String deliveryZip);

    List<TacoOrder> readTacoOrdersByDeliveryZipAndPlacedAtBetween(
            String deliveryZip, Date startDate, Date endDate);

    @Query("select order from TacoOrder order where order.user.id = :userId order by order.placedAt")
    List<TacoOrder> findTacoOrdersByUserIdOrderByPlacedAtDate(Long userId, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "delete from taco_order where user_id = :id", nativeQuery = true)
    void deleteFromTacoOrderByUserId(@Param("id") Integer id);

    @Transactional
    @Query(value = "select * from taco_order where user_id = :id", nativeQuery = true)
    List<TacoOrder> selectTacoOrdersByUserId(@Param("id") Integer id);


}
