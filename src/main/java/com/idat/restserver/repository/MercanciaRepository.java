package com.idat.restserver.repository;

import com.idat.restserver.entity.Mercancia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MercanciaRepository extends JpaRepository<Mercancia, Long> {
    Mercancia findByNombre(String nombre);
}
