package com.rongke.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
public class User {
    private String username;
    private String password;
    private int statue;
    private int attempt;
    private long time;
}
