package com.example.trabalhofinal;

import java.util.Date;

/*
CREATE TABLE produto (
id INT AUTO_INCREMENT PRIMARY KEY,
codigorastreio VARCHAR(50) NOT NULL UNIQUE,
empresa VARCHAR(100),
servico VARCHAR(100),
estatus VARCHAR(50),
origem VARCHAR(150),
destino VARCHAR(150),
datadeprevistadeentraga DATE
);
 */
public class Produto {
    private int id;
    private String codigorastreio;
    private String empresa;
    private String servico;
    private String status;
    private String origem;
    private String destino;
    private Date dataprevisao;

    public Produto(int id, String codigorastreio, String empresa, String servico, String status, String origem, String destino, Date dataprevisao) {
        this.id = id;
        this.codigorastreio = codigorastreio;
        this.empresa = empresa;
        this.servico = servico;
        this.status = status;
        this.origem = origem;
        this.destino = destino;
        this.dataprevisao = dataprevisao;
    }

    public Produto() {
    }

    public Produto(String codigorastreio, String empresa, String servico, String status, String origem, String destino, Date dataprevisao) {
        this.codigorastreio = codigorastreio;
        this.empresa = empresa;
        this.servico = servico;
        this.status = status;
        this.origem = origem;
        this.destino = destino;
        this.dataprevisao = dataprevisao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigorastreio() {
        return codigorastreio;
    }

    public void setCodigorastreio(String codigorastreio) {
        this.codigorastreio = codigorastreio;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Date getDataprevisao() {
        return dataprevisao;
    }

    public void setDataprevisao(Date dataprevisao) {
        this.dataprevisao = dataprevisao;
    }

    @Override
    public String toString() {
        return this.codigorastreio;
    }
}
