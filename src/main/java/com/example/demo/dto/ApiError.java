package com.example.demo.dto;

import java.time.LocalDateTime;

public record ApiError(LocalDateTime fecha, int estado, String error, String mensaje) { }
