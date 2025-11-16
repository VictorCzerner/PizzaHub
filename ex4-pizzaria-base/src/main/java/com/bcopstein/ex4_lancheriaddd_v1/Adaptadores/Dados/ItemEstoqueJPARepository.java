package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemEstoqueJPARepository extends JpaRepository<ItemEstoqueBD, Long> {

    @Query("SELECT i.quantidade FROM ItemEstoqueBD i WHERE i.ingredienteId = :id")
    Integer getQuantidade(@Param("id") Long ingredienteId);
}
