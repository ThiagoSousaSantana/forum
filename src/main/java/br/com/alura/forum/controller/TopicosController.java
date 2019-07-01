package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.TopicoBigDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.dto.TopicoForm;
import br.com.alura.forum.controller.dto.TopicoUpdateForm;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/topicos")
public class TopicosController {

    @Autowired
    private TopicoRepository repository;

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    public List list(String nomeCurso){
        List topicos;
        System.out.println(nomeCurso);
        if (nomeCurso != null) topicos = repository.findByCursoNome(nomeCurso);
        else topicos = repository.findAll();
        return TopicoDto.convert(topicos);
    }

    @GetMapping("/{id}")
    public TopicoBigDto details(@PathVariable Long id){
        return new TopicoBigDto(repository.findById(id).get());
    }

    @PostMapping
    public ResponseEntity<TopicoDto> insert(@RequestBody @Valid TopicoForm form,
                                            UriComponentsBuilder uriBuilder){
        var topico = form.convert(cursoRepository);
        repository.save(topico);

        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicoDto> update(@PathVariable Long id, @RequestBody @Valid TopicoUpdateForm form){
        var topico = form.update(id, repository);

        return ResponseEntity.ok(new TopicoDto(topico));
    }
}
