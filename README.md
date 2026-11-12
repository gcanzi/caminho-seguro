# Caminho Seguro

Aplicação em Java que sugere a rota **mais segura** de A até B em São Paulo e o **Ponto Seguro** aberto mais próximo para quem se sente em risco no caminho. O piloto é o bairro da **Bela Vista** (região da Av. Paulista), com dados simulados.

Projeto da Global Solution FIAP 2026.2 (CitizenTech), disciplina **Domain Driven Design**, ligado às ODS **3** (Saúde e Bem-Estar), **10** (Redução das Desigualdades) e **11** (Cidades e Comunidades Sustentáveis).

## Como funciona

| Etapa | O que o app faz |
| --- | --- |
| Risco do trecho | Soma o peso das ocorrências e a penalidade da iluminação, ajusta pelo perfil de deslocamento e limita de 0 a 10 |
| Nível de risco | Classifica o risco em **baixo** (< 3), **médio** (< 6) ou **alto** |
| Nota da rota | Calcula o risco médio ponderado pela distância e transforma em nota: `100 - risco x 10` |
| Rota equilibrada | Escolhe a rota de menor custo: `distância x (1 + alfa x risco / 10)` |
| Motivos | Explica em linguagem simples o que aumenta o risco de cada trecho |
| Ponto Seguro | Sugere o ponto aberto e confiável mais próximo; se a distância for parecida (até 100 m), prefere o órgão público |

### Regras de risco

| Regra | Valor |
| --- | --- |
| Peso por tipo de ocorrência | assalto 4, assédio 3, acidente 3, local deserto 2, rua escura 1 |
| Report da comunidade | peso x fator de tempo (até 24 h: 1,0; até 72 h: 0,5; depois: 0,2); metade do peso se tiver menos de 2 confirmações |
| Ocorrência oficial | peso x gravidade (1 a 5) / 3 |
| Iluminação | boa 0, regular 1, ruim 3; x1,5 à noite (das 20h às 5h) |
| Perfil a pé | à noite, +30% com iluminação ruim e +10% com regular |
| Perfil bicicleta | +1,5 por acidente no trecho |
| Perfil transporte público | x0,8 (caminha menos), +1,5 por local deserto à noite |

### Regras do Ponto Seguro

- **Órgão público** (delegacia, base da GCM, hospital, UBS): prioridade 1, sempre pode ser sugerido.
- **Estabelecimento parceiro** (comércio): prioridade 2. Só é sugerido se for **verificado** e não estiver **suspenso**. Ele fica suspenso com 3 ou mais avaliações de acolhimento e média abaixo de 3.
- Pontos fechados no horário do pedido ficam de fora, e o app explica o motivo de cada descarte.

## Estrutura do projeto

```
src/br/com/fiap/caminhoseguro
├── enums        tipos fixos do domínio
├── dominio      entidades, regras de negócio e utilitários
├── repositorio  listas em memória com busca
├── servico      cálculo de risco, comparação de rotas e busca de Ponto Seguro
└── app          cenário simulado e execução
```

### Classes

**enums**
- `TipoOcorrencia`: assédio, assalto, rua escura, acidente e local deserto, cada um com seu peso.
- `Iluminacao`: boa, regular e ruim, cada uma com sua penalidade no risco.
- `TipoPontoApoio`: delegacia, base da GCM, hospital, UBS e comércio.
- `NivelRisco`: baixo, médio e alto, com `classificar(risco)`.

**dominio**
- `Bairro`, `Trecho` e `Usuario`: o bairro, os trechos de rua e as pessoas, identificadas só por apelido.
- `Ocorrencia` (abstrata), `ReportComunitario` e `OcorrenciaOficial`: cada tipo calcula o próprio peso e explica o próprio motivo.
- `PerfilDeslocamento` (interface), `APe`, `Bicicleta` e `TransportePublico`: cada perfil ajusta o risco do seu jeito.
- `PontoApoio` (abstrata), `OrgaoPublico` e `EstabelecimentoParceiro`: cada tipo define a prioridade e se pode ser sugerido.
- `AvaliacaoAcolhimento`: nota anônima de 1 a 5 dada a um estabelecimento parceiro.
- `RotaConsultada`: rota candidata formada por trechos, com a nota de segurança e os motivos.
- `Horario`: valida a hora e diz se é noite.
- `Validacao`: validações reutilizadas pelos setters (obrigatório, texto obrigatório e faixa de valores).
- `Formatacao`: números com vírgula decimal e a barra de nota `[#######---]`.

**repositorio**
- `TrechoRepositorio`, `OcorrenciaRepositorio` e `PontoApoioRepositorio`: guardam os objetos em `ArrayList`, recusam id repetido e fazem buscas por id, trecho e bairro.

**servico**
- `CalculadoraRisco`: calcula o risco de 0 a 10 de um trecho e explica os motivos.
- `Roteirizador`: avalia as rotas candidatas e escolhe a mais rápida, a mais segura e a equilibrada.
- `BuscadorPontoApoio`: encontra o Ponto Seguro mais próximo e explica descartes e desempates.

**app**
- `CenarioBelaVista`: monta os dados simulados, com 8 trechos, 3 usuários, 9 ocorrências, 3 rotas e 8 pontos de apoio.
- `Main`: imprime cada etapa do uso do app, com um método por seção, e termina com o teste de validação.


## Pilares de Orientação a Objetos

| Pilar | Onde aparece |
| --- | --- |
| Encapsulamento | Atributos privados, validação nos setters com `IllegalArgumentException`, ids `final`, listas devolvidas como cópia e setters críticos privados (`setTrecho`, `setAutor`, `setTipo`) |
| Herança | `ReportComunitario` e `OcorrenciaOficial` herdam de `Ocorrencia`; `OrgaoPublico` e `EstabelecimentoParceiro` herdam de `PontoApoio`, reaproveitando `super.descrever()` e `super.toString()` |
| Abstração | Classes abstratas `Ocorrencia` e `PontoApoio` e a interface `PerfilDeslocamento` |
| Polimorfismo | `calcularPeso`, `explicarMotivo`, `prioridade`, `podeSerSugerido`, `descreverSituacao` e `ajustarRisco` são chamados pela classe mãe ou pela interface, sem `instanceof` |

## Como executar

Requer **Java 17** ou superior.

**Na IDE:** abra a pasta do projeto e execute `br.com.fiap.caminhoseguro.app.Main`.

## Exemplo de saída

```
===== RECOMENDACAO PARA LUA (A PE) AS 23H =====
Siga pela rota: Pela Alameda Santos e Rua Pamplona
[#######---] nota 70,0/100 | 150 m a mais que a mais rapida, com 19,0 pontos a mais de seguranca
Evite os trechos de risco alto: Rua Augusta (8,3), Rua Frei Caneca (6,5)
Se sentir perigo no caminho, use o botao Me sinto insegura para achar um Ponto Seguro

===== BOTAO ME SINTO INSEGURA (23H) =====
Lua pediu ajuda na Rua Frei Caneca (x = 600 m, y = 400 m)
Pontos que nao entram na busca:
   - UBS da regiao (simulada): fechado as 23h (das 7h as 19h)
   - Padaria Bom Dia: fechado as 23h (das 6h as 20h)
   - Bar Esquina Amiga: suspenso (media 1,7 em 3 avaliacoes)
   - Cafe Aconchego: nao verificado
Ponto Seguro sugerido: Base da GCM Paulista (simulada) a 158,1 m (aberto 24 horas)
Farmacia Vida 24h fica a 128,1 m: distancia parecida, entao o ponto de maior prioridade (orgao publico) tem preferencia
Em emergencia ligue 190 (Policia Militar) ou 180 (Central de Atendimento a Mulher)
```

## Dados

Todos os dados são **simulados**: os usuários são identificados apenas por apelido, os CNPJs são fictícios (`00.000.000/0001-0X`) e as ocorrências oficiais não vêm da base real da SSP-SP.

## Autor

Gustavo Canzi
