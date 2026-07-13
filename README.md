# Collaborative Filtering — Motor de Recomendação de Produtos

Motor de recomendação de produtos baseado em **Item-Based Collaborative Filtering**, com Similaridade de cosseno (Cosine Similarity), geração de recomendações por soma ponderada, estratégia de cold start, e correções para lidar com esparsidade de dados. Exposto via API REST em Spring Boot.

Desenvolvi esse Motor para ser aplicado em um projeto que estou trabalhando, com o cuidado de validar o comportamento do algoritmo com dados sintéticos que simulam padrões de afinidade entre usuários e categorias.

## Índice

- [O problema](#o-problema)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [O algoritmo](#o-algoritmo)
- [Lidando com esparsidade de dados](#lidando-com-esparsidade-de-dados)
- [API](#api)
- [Dados de exemplo](#dados-de-exemplo)
- [Testes](#testes)
- [Como rodar](#como-rodar)

## O problema

Dado o histórico de interações de um usuário com produtos (visualizações, cliques, compras e avaliações explícitas), recomendar outros produtos que esse usuário provavelmente vai gostar — sem depender de atributos do produto (categoria, preço), apenas do **padrão de comportamento** entre usuários.

Quando o usuário não tem histórico algum (cold start), o sistema recorre a uma estratégia alternativa: recomendar os produtos mais populares da base.

## Tecnologias

- **Java 21** — records, sealed interfaces, pattern matching
- **Spring Boot** — camada REST, injeção de dependência
- **JUnit 5** — testes unitários e de integração
- **MockMvc** — testes de integração da API

## Arquitetura

```
com.collaborativefiltering
├── domain/
│   ├── model/        → Product, Category, User
│   └── data/          → SampleDataGenerator, DataInitializer (popula a aplicação dados de exemplo)
├── interaction/       → Interaction (sealed interface) -> Impression, Click, Purchase, ExplicitRating
│   └── matrix/        → InteractionMatrix (construção e inversão da matriz usuário-produto)
├── calculator/        → CosineSimilarityCalculator
├── engine/            → ProductRecommendationEngine (soma ponderada + cold start)
└── api/
    ├── dtos/          → DTOs de request e response
    ├── repository/    → Repositórios em memória (ConcurrentHashMap)
    ├── service/       → ProductService, CategoryService, RecommendationService, UserService
    ├── controller/    → RecommendationController
    └── exception/     → GlobalExceptionHandler
```

### Decisões tomadas no Desenvolvimento

- **Interações como sealed interface**: `Interaction` é implementada por quatro records (`Impression`, `Click`, `Purchase`, `ExplicitRating`), cada um sabendo calcular seu próprio `score()`. Isso evita representar interações com um campo opcional "às vezes nulo" (como seria uma nota explícita apenas para avaliações) — a própria estrutura do tipo torna essa inconsistência impossível de existir.

- **Normalização de avaliações explícitas**: uma nota de 1 a 5 é normalizada para o intervalo -5 a +5, com nota 3 (neutra) mapeando para 0. Isso permite que avaliações ruins gerem um sinal de rejeição real (score negativo), em vez de apenas um sinal fraco positivo — mais fiel ao comportamento real de um usuário insatisfeito.

- **Similaridade calculada por produtos**: a similaridade é calculada entre produtos, não entre usuários, o que é mais estável em e-commerce, onde o catálogo de produtos varia menos que a base de usuários, e menos sujeito a problemas de cold start.

- **Serialização polimórfica de `Interaction`**: via `@JsonTypeInfo`/`@JsonSubTypes`, cada interação carrega um campo `type` explícito no JSON, permitindo distinguir os quatro tipos na resposta da API (necessário já que os records "binários" têm exatamente os mesmos campos).

## O algoritmo

### 1. Matriz de interação

Toda interação registrada é agregada em uma estrutura `Map<userId, Map<productId, score>>`, somando os scores quando o mesmo usuário interage múltiplas vezes com o mesmo produto (ex: visualizou, depois comprou). Essa matriz é invertida (`Map<productId, Map<userId, score>>`) para uso no cálculo de similaridade.

### 2. Cosine Similarity | Similaridade de Cosseno

Para cada par de produtos, a similaridade é calculada como o cosseno do ângulo entre os vetores de pontuação dos usuários que interagiram com ambos:

Fórmula de similaridade:

```
similaridade(A, B) = (Σ scoreA[u] * scoreB[u]) / (√(Σ scoreA[u]²) * √(Σ scoreB[u]²))
```

Fonte: https://www.ibm.com/br-pt/think/topics/cosine-similarity

O resultado varia de -1 (padrões opostos) a 1 (padrões idênticos), passando por 0 (nenhuma relação).

### 3. Geração de recomendações com soma ponderada

Para prever a pontuação de um produto candidato `C` para um usuário, soma-se a similaridade de `C` com cada produto do histórico do usuário, ponderada pela pontuação que o usuário deu a cada um:

Fórmula

```
score_previsto(C) = Σ (similaridade(C, P) * score_usuário(P)) / Σ |similaridade(C, P)|
```

Os candidatos são ordenados por essa pontuação prevista, do maior para o menor.

### 4. Cold start

Quando o usuário não tem nenhuma interação registrada, o sistema recorre à soma total de scores recebidos por cada produto (popularidade agregada), servindo como recomendação genérica até o usuário acumular histórico próprio.

## Lidando com esparsidade de dados

Durante o desenvolvimento, testes com uma base pequena (poucos usuários e produtos) mostraram um problema: produtos com sinal de interesse genuíno e forte no histórico do usuário podiam ficar mal ranqueados, enquanto produtos com pouquíssima relação real subiam artificialmente no ranking.

**Causa**: quando poucos usuários compartilham interações com um mesmo par de produtos, o denominador da soma ponderada (`Σ|similaridade|`) fica muito pequeno. Isso amplifica desproporcionalmente qualquer numerador pequeno gerado por ruído (interações fora do padrão principal do usuário), distorcendo o ranking final de produtos recomendados.

**Correções aplicadas:**

1. **Confiança mínima por interseção** (`CosineSimilarityCalculator.MIN_COMMON_USERS`): uma similaridade só é considerada válida se houver um número mínimo de usuários em comum entre os dois produtos comparados. Evita que coincidências isoladas sejam tratadas como sinal.

2. **Regularização aditiva** (`ProductRecommendationEngine.REGULARIZATION_CONSTANT`): uma constante é somada ao denominador da soma ponderada, suavizando o efeito de denominadores pequenos e eliminando a necessidade de tratamento especial para divisão por zero.

Essas correções foram validadas gerando dados sintéticos com afinidade intencional entre usuários e categorias (viés de 80/20), e confirmando manualmente que produtos da categoria de afinidade real do usuário passaram a ocupar as primeiras posições do ranking quando havia candidato disponível.

**Nota importante**: os valores atuais de `MIN_COMMON_USERS` (1) e `REGULARIZATION_CONSTANT` (1.0) foram calibrados para uma base pequena de testes. Em produção, com uma base real de usuários, esses valores devem ser recalibrados para cima, para evitar que coincidências espúrias sejam tratadas como sinal confiável.

## API

### `POST /api/interactions`

Registra uma nova interação.

**Request body:**

```json
{
  "userId": "user-uuid",
  "productId": "product-uuid",
  "type": "PURCHASE"
}
```

Para `type: "EXPLICIT_RATING"`, é necessário incluir o campo `rating` (1.0 a 5.0):

```json
{
  "userId": "user-uuid",
  "productId": "product-uuid",
  "type": "EXPLICIT_RATING",
  "rating": 4.0
}
```

Tipos válidos: `VIEW`, `CLICK`, `PURCHASE`, `EXPLICIT_RATING`.

**Resposta:** `201 Created`

### `GET /api/recommendations/{userId}`

Devolve a lista de produtos recomendados para o usuário, ordenados pela pontuação prevista.

**Resposta (`200 OK`):**

```json
{
  "userId": "user-uuid",
  "recommendedProductsIds": ["product-uuid-1", "product-uuid-2"]
}
```

## Dados de exemplo

Ao subir a aplicação, o `DataInitializer` popula automaticamente:

- 3 categorias (Eletrônicos, Moda, Livros), com produtos nomeados
- 5 usuários, cada um com uma afinidade fixa por categoria
- ~15 interações por usuário, com 80% de viés em direção à categoria de afinidade e 20% de ruído

Isso permite validar o comportamento do algoritmo com um padrão real e verificável, em vez de dados totalmente aleatórios (que não fornecem sinal suficiente para diferenciar um algoritmo correto de um aleatório).

## Testes

- **`CosineSimilarityCalculatorTest`**: valida o cálculo contra valores computados manualmente (produtos perfeitamente proporcionais, produtos sem interseção, produto inexistente).
- **`ProductRecommendationEngineTest`**: valida a soma ponderada e a ordenação contra cenários calculados manualmente, e valida a estratégia de cold start.
- **`InteractionMatrixTest`**: valida agregação de scores e isolamento entre usuários/produtos diferentes.
- **`RecommendationControllerTest`**: testes de integração via `MockMvc`, validando status HTTP e formato da resposta.

## Como rodar o projeto

```bash
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`, já populada com os dados de exemplo.

Para rodar os testes:

```bash
./mvnw test
```

## Desenvolvido por:

Enzo Mendes Novaes Santos

LinkedIn: https://www.linkedin.com/in/enzo-mendes-49896b285/

GitHub: https://github.com/EnzoMendes34
