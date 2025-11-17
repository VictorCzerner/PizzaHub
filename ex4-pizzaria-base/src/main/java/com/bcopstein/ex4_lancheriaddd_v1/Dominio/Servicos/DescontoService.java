package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.DescontosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Desconto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
@Service
public class DescontoService {
    private final DescontosRepository descontosRepository;
    private final Map<String, DescontoConcretoI> descontosAplicaveisPorNome;
    
    // Todos os descontos que forem Beans, serão colocados dentro dessa lista
    @Autowired
    public DescontoService(DescontosRepository descontosRepository,  List<DescontoConcretoI> descontosDiferentes){
        this.descontosRepository = descontosRepository;
        // Monta um "factory" de estratégias baseado no nome do desconto
        this.descontosAplicaveisPorNome = descontosDiferentes.stream()
                .collect(Collectors.toMap(
                        DescontoConcretoI::getNome,
                        descontoContreto -> descontoContreto
                ));
    }

    public boolean decideDescontoAtivo(long id){
        return descontosRepository.decideDescontoAtivo(id);
    }

    public List<Desconto> RecuperaTodosDescontos(){
        return descontosRepository.listaTodos();
    } 

    public double percentagemDesconto(){
        Desconto descontoAtivo = descontosRepository.retornaDescontoAtivo();
        return descontoAtivo.getDesconto();
    }

    // Procura na tabela qual que é o desconto ativo e o aplica
    public double aplicarDescontoAtivo(Cliente cliente, Pedido pedido){
        // Desconto escolhido no momento pelo Usuario MASTER
        Desconto descontoAtivo = descontosRepository.retornaDescontoAtivo();
        // Se for a resposta padrao no caso de inexistencia de desconto ativo, apenas retorna o valor cheio
        if (descontoAtivo.getId() == -1){
            return pedido.getValor();
        }
        // Caso contrario, escolhe o desconto certo dentre todos que existem -> pelo nome
        // Os nomes dos descontos tem que ser os exatamente os mesmos nomes que estão na tabela SQL
        else{
            String nomeDescontoAtivo =  descontoAtivo.getNome();
            double percentagemDesconto = descontoAtivo.getDesconto();
            DescontoConcretoI descontoEscolhido =  descontosAplicaveisPorNome.get(nomeDescontoAtivo);

            return descontoEscolhido.aplicarDesconto(cliente, pedido, percentagemDesconto);
            }
    
    }
}