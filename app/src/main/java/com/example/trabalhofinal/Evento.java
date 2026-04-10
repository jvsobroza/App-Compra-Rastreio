package com.example.trabalhofinal;

import java.util.Date;

/*
CREATE TABLE evento (
id INT AUTO_INCREMENT PRIMARY KEY,
produto_id INT NOT NULL,
data DATETIME,
local VARCHAR(150),
status VARCHAR(100),
detalhes TEXT,
FOREIGN KEY (produto_id) REFERENCES produto(id)
ON DELETE CASCADE
ON UPDATE CASCADE
);
 */
public class Evento {
    private int id;
    private int produto_id;
    private Date data;
    private String local;
    private String status;
    private String detalhes;

    public Evento(int id, int produto_id, Date data, String local, String status, String detalhes) {
        this.id = id;
        this.produto_id = produto_id;
        this.data = data;
        this.local = local;
        this.status = status;
        this.detalhes = detalhes;
    }

    public Evento() {
    }

    public Evento(int produto_id, Date data, String local, String status, String detalhes) {
        this.produto_id = produto_id;
        this.data = data;
        this.local = local;
        this.status = status;
        this.detalhes = detalhes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProduto_id() {
        return produto_id;
    }

    public void setProduto_id(int produto_id) {
        this.produto_id = produto_id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    @Override
    public String toString() {
        return "Data = " + data +
                "\nLocal = " + local +
                "\nStatus = " + status +
                "\nDetalhes = " + detalhes + "\n";
    }
}
