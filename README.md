# 📦 Compra e Veja — App de Compra e Rastreio

Aplicativo mobile Android para **cadastro e rastreamento de pedidos/encomendas**. Permite ao usuário registrar compras, acompanhar o status de entrega e visualizar o histórico completo de movimentações, tudo integrado a um servidor remoto via **Web Services em PHP**.

---

## 📱 Telas do Aplicativo

O app possui dois perfis de acesso na tela inicial:

- **Cliente** — pode cadastrar e consultar seus pedidos
- **Administrador** — pode selecionar um pedido e inserir novas atualizações de rastreio

---

## ⚙️ Funcionalidades

### Cliente
- **Cadastrar Pedido** — registra um novo pedido com código de rastreio, transportadora, método de entrega, origem, destino e previsão de entrega
- **Consultar Pedido** — seleciona o pedido pelo código e visualiza o histórico completo de eventos (data, local, status e detalhes)

### Administrador
- **Módulo Admin** — seleciona um pedido existente e cadastra novas atualizações de rastreio informando data, local, status e detalhes

---

## 🛠️ Tecnologias Utilizadas

| Camada | Tecnologia |
|---|---|
| App Mobile | Java (Android nativo) |
| Build | Gradle / Android Studio |
| Web Service | PHP |
| Banco de Dados | MySQL / MariaDB |
| Comunicação | HTTP + JSON |

---

## 🗄️ Banco de Dados

### Nome do banco: `correios_mobile`

O banco possui duas tabelas principais com relacionamento 1:N — um produto pode ter vários eventos de rastreio.

---

### Tabela `produto`

Armazena os pedidos cadastrados.

| Coluna | Tipo | Descrição |
|---|---|---|
| `id` | int(11) AUTO_INCREMENT | Chave primária |
| `codigorastreio` | varchar(50) | Código único de rastreio (UNIQUE) |
| `empresa` | varchar(100) | Nome da transportadora |
| `servico` | varchar(100) | Tipo de serviço (ex: Normal, Expresso) |
| `status` | varchar(50) | Status atual do pedido |
| `origem` | varchar(150) | Cidade/local de origem |
| `destino` | varchar(150) | Cidade/local de destino |
| `datadeprevistadeentraga` | date | Data prevista de entrega |

```sql
CREATE TABLE `produto` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `codigorastreio` varchar(50) NOT NULL,
  `empresa` varchar(100) DEFAULT NULL,
  `servico` varchar(100) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `origem` varchar(150) DEFAULT NULL,
  `destino` varchar(150) DEFAULT NULL,
  `datadeprevistadeentraga` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `codigorastreio` (`codigorastreio`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
```

---

### Tabela `evento`

Armazena o histórico de movimentações de cada pedido.

| Coluna | Tipo | Descrição |
|---|---|---|
| `id` | int(11) AUTO_INCREMENT | Chave primária |
| `produto_id` | int(11) | Chave estrangeira referenciando `produto.id` |
| `data` | datetime | Data e hora do evento |
| `local` | varchar(150) | Local do evento |
| `status` | varchar(100) | Status nesse evento (ex: Enviado, Em trânsito) |
| `detalhes` | text | Informações adicionais sobre o evento |

```sql
CREATE TABLE `evento` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `produto_id` int(11) NOT NULL,
  `data` datetime DEFAULT NULL,
  `local` varchar(150) DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  `detalhes` text DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `produto_id` (`produto_id`),
  CONSTRAINT `evento_ibfk_1` FOREIGN KEY (`produto_id`) REFERENCES `produto` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
```

---

### Relacionamento entre tabelas

```
produto (1) ──────< evento (N)
produto.id = evento.produto_id
ON DELETE CASCADE / ON UPDATE CASCADE
```

> Ao deletar um produto, todos os seus eventos são automaticamente removidos.

---

## 🌐 Web Services (PHP)

A comunicação entre o app Android e o banco de dados é feita exclusivamente via **Web Services em PHP**, que recebem e retornam dados em formato **JSON**.

### Fluxo de comunicação

```
App Android
    │
    │  HTTP POST (JSON)
    ▼
Web Service PHP
    │
    │  Query SQL
    ▼
Banco MySQL (correios_mobile)
    │
    │  { "status": "ok" } ou { "status": "erro" }
    ▼
App Android
```

### Pasta dos Web Services

Os arquivos PHP ficam na pasta `correios/` dentro do projeto:

| Arquivo | Função |
|---|---|
| `cadastra_produto.php` | Cadastra um novo pedido no banco |
| `cadastra_evento.php` | Cadastra uma nova atualização de rastreio |
| `consulta_produtos.php` | Retorna a lista de todos os produtos |
| `consulta_eventos.php` | Retorna o histórico de eventos de um produto pelo `produto_id` |

A URL base das requisições do app deve apontar para essa pasta, ex:
```
http://192.168.x.x/correios/cadastra_produto.php
```

---


## 🚀 Como Executar o Projeto

### Pré-requisitos

- Android Studio instalado
- Servidor local com PHP e MySQL (ex: XAMPP ou WAMP)
- PHP 8.x / MariaDB 10.x

### Passo a passo

1. **Clone o repositório**
   ```bash
   git clone https://github.com/jvsobroza/App-Compra-Rastreio.git
   ```

2. **Configure o banco de dados**
   - Inicie o servidor MySQL
   - Crie o banco `correios_mobile`
   - Execute os scripts SQL das tabelas `produto` e `evento` descritos acima

3. **Configure os Web Services**
   - Copie os arquivos PHP para a pasta do servidor (ex: `htdocs` no XAMPP)
   - Verifique as credenciais de conexão nos arquivos PHP (`$servidor`, `$usuario`, `$senha`, `$banco`)

4. **Configure o endereço do servidor no App**
   - Abra o projeto no Android Studio
   - Localize as classes responsáveis pelas chamadas HTTP
   - Atualize a URL base para o IP do seu servidor (ex: `http://192.168.x.x/webservice/`)

5. **Execute o app**
   - Conecte um dispositivo Android ou inicie um emulador
   - Clique em **Run** no Android Studio

---

## 📁 Estrutura do Repositório

```
App-Compra-Rastreio/
├── .idea/              → Configurações do Android Studio
├── app/                → Código-fonte principal
│   └── src/
│       └── main/
│           ├── java/   → Activities, Adapters, chamadas ao Web Service
│           └── res/    → Layouts XML, strings, drawables
├── gradle/wrapper/     → Wrapper do Gradle
├── build.gradle        → Configuração de build do projeto
├── settings.gradle     → Configurações do projeto
└── README.md
```

---

## 👨‍💻 Autor

Desenvolvido por **João Victor Sobroza Dal Ross**  
🔗 [github.com/jvsobroza](https://github.com/jvsobroza)
