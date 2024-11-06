package com.example.tacocloudapplication.repo.impl;

import com.example.tacocloudapplication.table.Taco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface TacoRepository extends JpaRepository<Taco, Long> {

    List<Taco> findByTacoOrderIsNullAndCreatedByUserId(Long userId);

    Integer deleteTacoById(Long tacoId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM ingredient_tacos WHERE taco_id = :tacoId", nativeQuery = true)
    void deleteEntriesFromIngredientTaco(@Param("tacoId") Long tacoId);

    @Modifying
    @Query(value = "UPDATE taco SET taco_order_id = :orderId WHERE id = :tacoId", nativeQuery = true)
    void associateOrderToTaco(@Param("orderId") Long orderId, @Param("tacoId") Long tacoId);

    @Modifying
    @Query(value = "delete from ingredient_tacos it where it.taco_id in (select id from taco t where t.taco_order_id is null) ", nativeQuery = true)
    void deleteOrderlessTacosEntitiesFromIngredientTacosTable();

    @Modifying
    @Query(value = "delete from taco t where t.taco_order_id is null ", nativeQuery = true)
    void deleteOrderlessTacoFromTacoTable();

    @Transactional
    @Query(value = "select * from taco where taco_order_id = :orderId", nativeQuery = true)
    List<Taco> selectTacosByOrderId(@Param("orderId") long orderId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM taco WHERE taco_order_id = :orderId", nativeQuery = true)
    void deleteEntriesFromTacoByOrderId(@Param("orderId") Long orderId);

}
