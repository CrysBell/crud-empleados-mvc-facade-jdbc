package com.example.models;

import java.util.Set;

public record Detalle(String nombreDpto, Set<String> telefonos, Set<String> correos) {

}
