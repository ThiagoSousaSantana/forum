package br.com.alura.forum.controller.dto;

import br.com.alura.forum.model.Topico;
import br.com.alura.forum.repository.TopicoRepository;

public class TopicoUpdateForm {

    private String titulo;
    private String mensagem;

    public TopicoUpdateForm(String titulo, String mensagem) {
        this.titulo = titulo;
        this.mensagem = mensagem;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public Topico update(Long id, TopicoRepository repository){
        var topico = repository.findById(id).get();
        topico.setMensagem(this.mensagem);
        topico.setTitulo(this.titulo);
        return topico;
    }
}
