package com.cauadev.hateoas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cauadev.hateoas.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Integer>{

}
