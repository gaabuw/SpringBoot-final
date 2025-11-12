package com.utn.productos.controller;


import com.utn.productos.dto.ActualizarStockDTO;
import com.utn.productos.dto.ProductoDTO;
import com.utn.productos.dto.ProductoResponseDTO;
import com.utn.productos.exception.ProductoNotFoundException;
import com.utn.productos.model.Categoria;
import com.utn.productos.model.Producto;
import com.utn.productos.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.List;

import java.util.Optional;

import java.util.stream.Collectors;



@RestController
@RequestMapping("/api/productos")
@Tag(name = "Gestion de Productos", description = "Endpoints para crear, leer, actualizar y eliminar")
public class ProductoController {
    private ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // 1. GET /api/productos - Listar todos
    @Operation(summary = "Obtener lista de todos los productos") //
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> obtenerTodos() {
        List<Producto> productos = productoService.obtenerTodos();
        List<ProductoResponseDTO> dtos = productos.stream()
                .map(productoService::convertirEntidadADTO) // Usando el método del service
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    // 2. GET /api/productos/{id} - Obtener por ID
    @Operation(summary = "Obtener un producto por su ID") //
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = com.utn.productos.exception.ErrorResponse.class))) //
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerPorId(@PathVariable Long id) {
        Producto producto = productoService.obtenerPorId(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con ID: " + id));
        ProductoResponseDTO dto = productoService.convertirEntidadADTO(producto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    // 3. GET /api/productos/categoria/{categoria} - Filtrar por categoría
    @Operation(summary = "Obtener productos filtrados por categoría") //
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos filtrada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoResponseDTO.class)))
    })
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProductoResponseDTO>> obtenerPorCategoria(@PathVariable Categoria categoria) {
        List<Producto> productos = productoService.obtenerPorCategoria(categoria);
        List<ProductoResponseDTO> dtos = productos.stream()
                .map(productoService::convertirEntidadADTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    // 4. POST /api/productos - Crear producto
    @Operation(summary = "Crear un nuevo producto") //
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente", //
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (error de validación)", //
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = com.utn.productos.exception.ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crearProducto(@Valid @RequestBody ProductoDTO productoDTO) { //
        Producto producto = productoService.convertirDTOAEntidad(productoDTO);
        Producto productoGuardado = productoService.crearProducto(producto);
        ProductoResponseDTO dtoRespuesta = productoService.convertirEntidadADTO(productoGuardado);

        // Retornamos 201 Created
        return new ResponseEntity<>(dtoRespuesta, HttpStatus.CREATED);
    }


    // 5. PUT /api/productos/{id} - Actualizar producto completo
    @Operation(summary = "Actualizar un producto completo por su ID") //
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (error de validación)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = com.utn.productos.exception.ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = com.utn.productos.exception.ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(@PathVariable Long id, @Valid @RequestBody ProductoDTO productoDTO) {
        Producto productoActualizado = productoService.convertirDTOAEntidad(productoDTO);
        Producto producto = productoService.actualizarProducto(id, productoActualizado); // Esto lanzará la excepción si no lo encuentra
        ProductoResponseDTO dtoRespuesta = productoService.convertirEntidadADTO(producto);
        return new ResponseEntity<>(dtoRespuesta, HttpStatus.OK);
    }

    // 6. PATCH /api/productos/{id}/stock - Actualizar solo stock
    @Operation(summary = "Actualizar solo el stock de un producto por su ID") //
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (stock nulo o negativo)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = com.utn.productos.exception.ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = com.utn.productos.exception.ErrorResponse.class)))
    })
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductoResponseDTO> actualizarStock(@PathVariable Long id, @Valid @RequestBody ActualizarStockDTO stockDTO) {

        // Llama al servicio. Si el ID no existe, el servicio lanza la excepción.
        Producto producto = productoService.actualizarStock(id, stockDTO.getStock());

        // Si todo sale bien, convierte la entidad a DTO y responde OK (200)
        ProductoResponseDTO dtoRespuesta = productoService.convertirEntidadADTO(producto);
        return new ResponseEntity<>(dtoRespuesta, HttpStatus.OK);
    }

    // 7. DELETE /api/productos/{id} - Eliminar producto
    @Operation(summary = "Eliminar un producto por su ID") //
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente", //
                    content = @Content), // Sin contenido
            @ApiResponse(responseCode = "404", description = "Producto no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = com.utn.productos.exception.ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {

        // Llama al servicio para eliminar. Si el ID no existe, lanza la excepción.
        productoService.eliminarProducto(id);

        // Retorna un código 204 No Content (respuesta exitosa sin cuerpo)
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}