package ifmt.cba.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bairro")
public class Bairro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int codigo;

    @Column(name = "nome")
    private String nome;

    @Column(name = "custo_entrega")
    private float custoEntrega;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getCustoEntrega() {
        return custoEntrega;
    }

    public void setCustoEntrega(float custoEntrega) {
        this.custoEntrega = custoEntrega;
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
        Bairro other = (Bairro) obj;
        if (codigo != other.codigo)
            return false;
        return true;
    }

    public String validar() {
        String retorno = "";

        if (this.nome == null || this.nome.length() < 3) {
            retorno += "Nome invalido";
        }

        if (this.custoEntrega <= 0) {
            retorno += "Custo entrega deve ser maior que zero";
        }

        return retorno;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
