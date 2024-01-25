package com.fiap.springblog.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document//Em vez de Entity, quando usamos MongoDB, precisamos colocar como Docuemnt
@Data//todos os métodos GETs e SETs não ficam visíveis, mas o LOMBOK disponibilza
public class Artigo {

    @Id
    private String codigo;
    private String titulo;
    private LocalDateTime data;
    private String texto;
    private String url;
    private Integer status;

    @DBRef  //referencia de uma collection dentro da outra
    private Autor autor;
}
