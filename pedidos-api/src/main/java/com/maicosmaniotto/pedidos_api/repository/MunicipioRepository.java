package com.maicosmaniotto.pedidos_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maicosmaniotto.pedidos_api.model.Municipio;

@Repository
public interface MunicipioRepository extends JpaRepository<Municipio, Long>  {

}
