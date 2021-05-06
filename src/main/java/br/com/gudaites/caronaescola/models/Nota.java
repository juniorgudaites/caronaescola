package br.com.gudaites.caronaescola.models;

/**
 * Class Model da Nota
 *
 * @author Jair Gudaites Junior
 */

public class Nota {
    private Double valor;

    public Nota(){}

    public Nota(Double valor) {
        this.valor = valor;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}