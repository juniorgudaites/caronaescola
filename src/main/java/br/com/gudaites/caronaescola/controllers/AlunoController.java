package br.com.gudaites.caronaescola.controllers;

import br.com.gudaites.caronaescola.models.Aluno;
import br.com.gudaites.caronaescola.repositorys.AlunoRepository;
import br.com.gudaites.caronaescola.service.GeolocalizacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Class Controller do Aluno
 *
 * @author Jair Gudaites Junior
 */

@Controller
public class AlunoController {

    @Autowired
    private AlunoRepository repository;

    @Autowired
    private GeolocalizacaoService geolocalizacaoService;

    @GetMapping("/aluno/cadastrar")
    public String cadastrar(Model model) {
        model.addAttribute("aluno", new Aluno());
        return "aluno/cadastrar";
    }

    @PostMapping("/aluno/salvar")
    public String salvar(@ModelAttribute Aluno aluno) {
        try {
            List<Double> latELong = geolocalizacaoService.obterLateLongPor(aluno.getContato());
            aluno.getContato().setCoordinates(latELong);
            repository.salvar(aluno);
        } catch (Exception e) {
            System.out.println("Endereco nao localizado");
            e.printStackTrace();
        }

        System.out.println(aluno);
        return "redirect:/";
    }

    @GetMapping("/aluno/listar")
    public String listar(Model model) {
        List<Aluno> alunos = repository.obterTodosAlunos();
        model.addAttribute("alunos", alunos);
        return "aluno/listar";
    }

    @GetMapping("/aluno/visualizar/{id}")
    public String visualizar(@PathVariable String id, Model model) {
        Aluno aluno = repository.obterAlunoPor(id);
        model.addAttribute("aluno", aluno);
        return "aluno/visualizar";
    }

    @GetMapping("/aluno/pesquisarnome")
    public String pesquisarNome() {
        return "aluno/pesquisarnome";
    }

    @GetMapping("/aluno/pesquisar")
    public String pesquisar(@RequestParam("nome") String nome, Model model) {
        List<Aluno> alunos = repository.pesquisarPor(nome);

        model.addAttribute("alunos", alunos);

        return "aluno/pesquisarnome";
    }

}



