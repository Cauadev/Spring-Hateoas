package com.cauadev.hateoas.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cauadev.hateoas.model.Produto;
import com.cauadev.hateoas.repository.ProdutoRepository;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
	
	@Autowired
	private ProdutoRepository repository;
	
	@GetMapping
	public ResponseEntity<List<Produto>> getAllProdutos(){
		List<Produto> listProdutos = repository.findAll();
		
		if(listProdutos.isEmpty()) {
			return ResponseEntity.notFound().build();
		}else {
			listProdutos.forEach(produto ->{
				produto.add(WebMvcLinkBuilder
						.linkTo(WebMvcLinkBuilder
						.methodOn(ProdutoController.class)
						.getOneProduto(produto.getId()))
						.withSelfRel());
			});
		}
		return ResponseEntity.ok(listProdutos);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Produto> getOneProduto(@PathVariable Integer id){
		Optional<Produto> produto = repository.findById(id);
		if(!produto.isPresent()) {
			return ResponseEntity.notFound().build();
		}else {
			produto.get().add(WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(ProdutoController.class)
					.getAllProdutos())
					.withRel("Lista de produtos"));
			return ResponseEntity.ok().body(produto.get());
		}
	}
	
	@PostMapping
	public ResponseEntity<Produto> saveProduto(@RequestBody Produto produto){
		return ResponseEntity.ok(repository.save(produto));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Produto> update(@PathVariable Integer id, @RequestBody Produto produto) {
		Integer CONTACT_ID = repository.findById(id)
		.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"id inválido.")).getId();
		produto.setId(CONTACT_ID);
		return ResponseEntity.ok(repository.save(produto));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		repository.deleteById(id);
		return ResponseEntity.ok("Deletado com sucesso.");
	}
	

}
