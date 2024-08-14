package ifmt.cba.entity;

import java.time.LocalDate;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import ifmt.cba.dto.MovimentoEstoqueDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "registro_estoque")
public class RegistroEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int codigo;

    @ManyToOne
    @JoinColumn(name = "id_produto")
    private Produto produto;

    @Enumerated(EnumType.STRING)
    private MovimentoEstoqueDTO movimento;

    @Temporal(TemporalType.DATE)
    private LocalDate data;

    @Column(name = "quantidade")
    private int quantidade;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public MovimentoEstoqueDTO getMovimento() {
        return movimento;
    }

    public void setMovimento(MovimentoEstoqueDTO movimento) {
        this.movimento = movimento;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + codigo;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RegistroEstoque other = (RegistroEstoque) obj;
        if (codigo != other.codigo)
            return false;
        return true;
    }

    public String validar() {
        String retorno = "";

        if (this.produto == null) {
            retorno += "Produto invalido";
        }

        if (this.quantidade <= 0) {
            retorno += "Custo por unidade invalido";
        }

        if (this.movimento == null) {
            retorno += "Motivo invalido";
        }

        if (this.data == null) {
            retorno += "Data invalida";
        }

        return retorno;
    }
}
