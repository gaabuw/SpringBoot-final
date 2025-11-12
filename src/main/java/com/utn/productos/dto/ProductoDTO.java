package com.utn.productos.dto;

import com.utn.productos.model.Categoria;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "DTO para crear o actualizar un producto")
public class ProductoDTO {

    @Schema(description = "Nombre del producto", example = "Teclado Mecánico")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 100, message = "Debe tener entre 3 y 100 caracteres")
    private String nombre;

    @Schema(description = "Descripción detallada del producto", example = "Teclado con switches Cherry MX Red") //
    @Size(max = 500, message = "Longitud máxima de 500 caracteres")
    private String descripcion;

    @Schema(description = "Precio del producto en formato decimal", example = "150.99", requiredMode = Schema.RequiredMode.REQUIRED) //
    @NotNull(message = "El precio no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El precio debe ser como mínimo 0.01")
    private Double precio;

    @Schema(description = "Cantidad de stock disponible", example = "50", requiredMode = Schema.RequiredMode.REQUIRED) //
    @NotNull(message = "El stock no puede ser nulo")
    @DecimalMin(value = "0", message = "El stock no puede ser negativo")
    private Integer stock;

    @Schema(description = "Categoría del producto", example = "ELECTRONICA")
    @NotNull(message = "La categoria no puede ser nula")
    private Categoria categoria;
}
