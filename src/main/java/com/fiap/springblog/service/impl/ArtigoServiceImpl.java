package com.fiap.springblog.service.impl;

import com.fiap.springblog.model.Artigo;
import com.fiap.springblog.model.Autor;
import com.fiap.springblog.repository.ArtigoRepository;
import com.fiap.springblog.repository.AutorRepository;
import com.fiap.springblog.service.ArtigoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArtigoServiceImpl implements ArtigoService {

    private final MongoTemplate mongoTemplate;

    public ArtigoServiceImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    private ArtigoRepository artigoRepository;

    @Autowired
    private AutorRepository autorRepository;


    @Override
    public List<Artigo> obterTodos() {
        return this.artigoRepository.findAll();
    }

    @Override
    public Artigo obterPorCodigo(String codigo) {
        return this.artigoRepository.findById(codigo).orElseThrow(()-> new IllegalArgumentException("Artigo não encontrado"));
    }

    @Override
    public Artigo criar(Artigo artigo) {

        //se o autor existe
        if(artigo.getAutor().getCodigo() != null){

            //recuperar o autor
            Autor autor = this.autorRepository
                    .findById(artigo.getAutor().getCodigo())
                    .orElseThrow(() -> new IllegalArgumentException("Autor inexistente"));

            //define o autor no artigo
            artigo.setAutor(autor);
        }else{
            //caso contrário, gravar o artigo sem autor
            artigo.setAutor(null);

        }
        //salvo o artigo com o autor já cadastrado
        return this.artigoRepository.save(artigo);

    }

    @Override
    public List<Artigo> findByDataGreaterThan(LocalDateTime data) {
        Query query = new Query(Criteria.where("data").gt(data));
        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public List<Artigo> findByDataAndStatus(LocalDateTime data, Integer status) {
        Query query = new Query(Criteria.where("data").is(data).and("status").is(status));
        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public void atualizar(Artigo artigo) {
        this.artigoRepository.save(artigo);
    }

    @Override
    public void atualizarArtigo(String id, String novaURL) {
        //criterio de busca pelo ID
        Query query = new Query(Criteria.where("codigo").is(id));
        //definindo campos que serão atualizados
        Update update = new Update().set("url", novaURL);
        //exexutando atualização
        this.mongoTemplate.updateFirst(query, update, Artigo.class);
    }

    @Override//implementação pelo JPA
    public void deleteById(String id){
        this.artigoRepository.deleteById(id);
    }

    @Override//implementação pelo MongoTemplate
    public void deleteArtigoById(String id) {
        Query query = new Query(Criteria.where("codigo").is(id));
        mongoTemplate.remove(query, Artigo.class);
    }
}
