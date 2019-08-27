package br.com.pcabrantes.prodmanager.util.enums;

public enum StatusPedido {

    EM_PROCESSAMENTO(0, "Em processamento"), CONCLUIDO(1, "Concluído"),
    CANCELADO(2, "Cancelado");

    private int id;
    private String descricao;

    StatusPedido(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public static StatusPedido getById(int id) {

        switch (id) {
            case 0 : return EM_PROCESSAMENTO;
            case 1 : return CONCLUIDO;
            case 2 : return CANCELADO;
            default: return null;
        }
    }
}
