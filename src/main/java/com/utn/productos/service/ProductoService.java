package com.utn.productos.service;

import com.utn.productos.dto.ProductoDTO;
import com.utn.productos.dto.ProductoResponseDTO;
import com.utn.productos.model.Categoria;
import com.utn.productos.model.Producto;
import com.utn.productos.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import com.utn.productos.exception.ProductoNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    private ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public Producto crearProducto(Producto producto){
        return productoRepository.save(producto);
    }

    public List<Producto> obtenerTodos(){
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerPorId(Long id){
        return productoRepository.findById(id);
    }

    public List<Producto> obtenerPorCategoria(Categoria categoria){
        return productoRepository.findByCategoria(categoria);
    }

    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ProductoNotFoundException("No se puede eliminar. Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }

    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con ID: " + id));

        producto.setNombre(productoActualizado.getNombre());
        producto.setDescripcion(productoActualizado.getDescripcion());
        return productoRepository.save(producto);
    }

    public Producto actualizarStock(Long id, Integer nuevoStock) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con ID: " + id));

        producto.setStock(nuevoStock);
        return productoRepository.save(producto);
    }

    //Convertir DTOs a Entidades
    public Producto convertirDTOAEntidad(ProductoDTO productoDTO){
        Producto producto = new Producto();
        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setCategoria(productoDTO.getCategoria());
        return producto;
    }

    //Convertir Entidad producto a un ProductoResponseDTO
    public ProductoResponseDTO convertirEntidadADTO(Producto producto) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        dto.setCategoria(producto.getCategoria());
        return dto;
    }



}
