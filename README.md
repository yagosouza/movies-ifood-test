# Movies App

App de filmes que consome a [TMDB API](https://developer.themoviedb.org/) para listar, buscar e salvar filmes favoritos.

## Sobre

Desenvolvi esse projeto focando em boas práticas de arquitetura e organização de código. A ideia foi montar algo que refletisse como eu estruturo um app no dia a dia: camadas bem separadas, testes cobrindo as partes importantes e uma UI simples mas funcional.

## Screenshots

> TODO: adicionar screenshots do app

## Funcionalidades

- Listagem de filmes com filtros por categoria (Populares, Em Cartaz, Mais Votados, Em Breve)
- Busca de filmes pelo teclado (sem debounce — busca ao clicar "Buscar" no teclado)
- Favoritos salvos localmente com Room (persistem offline)
- Tela de detalhes com sinopse, gêneros, duração e nota
- Paginação infinita na listagem e na busca
- Tratamento de erros com mensagens amigáveis (sem internet, timeout, erros do servidor)
- Navegação por abas (Início, Buscar, Favoritos)

## Arquitetura

Projeto multi-módulo com Clean Architecture em 3 camadas:

```
:domain  → Modelos, interfaces de repositório, use cases (Kotlin puro, sem dependência Android)
:data    → Retrofit, Room, DTOs, implementação do repositório
:app     → Compose UI, ViewModels, Hilt, navegação
```

Cada módulo tem sua responsabilidade bem definida. O `:domain` é Kotlin puro — não conhece Android, Retrofit nem Room. Isso facilita testar e trocar implementações sem impacto nas regras de negócio.

### Fluxo de dados

Segui o padrão MVVM com Unidirectional Data Flow. O ViewModel expõe um `StateFlow<UiState>` imutável e a UI só observa:

```
Compose UI → ViewModel → UseCase → Repository → API / Room
     ↑                                              |
     └──────────── StateFlow<UiState> ←─────────────┘
```

Separei cada tela em `Screen` (com ViewModel) e `Content` (stateless, recebe estado por parâmetro). Isso deixa os previews e testes instrumentados mais simples, já que o `Content` não depende de injeção.

## Stack

| Categoria | Tecnologia |
|-----------|-----------|
| Linguagem | Kotlin |
| UI | Jetpack Compose + Material Design 3 |
| Async | Coroutines + Flow |
| DI | Hilt |
| Rede | Retrofit + OkHttp + Kotlinx Serialization |
| Banco local | Room |
| Imagens | Coil 3 |
| Navegação | Compose Navigation |
| Testes | JUnit + MockK + Turbine |
| CI | GitHub Actions |

## Testes

O projeto tem testes unitários e instrumentados:

- **Unitários** — ViewModels, Use Cases, Repository e ErrorMapper, usando MockK pra mockar dependências e Turbine pra testar Flows
- **Instrumentados** — Telas do Compose (listagem, busca, favoritos, detalhes), validando estados visuais com `createComposeRule`

Para rodar:

```bash
# Unitários
./gradlew testDebugUnitTest

# Instrumentados (precisa de emulador/device)
./gradlew connectedDebugAndroidTest
```

## CI

O projeto tem um workflow no GitHub Actions que roda automaticamente a cada push na `master`:

1. Roda todos os testes unitários
2. Builda o APK de debug

A API key do TMDB fica como Secret no GitHub, então o build do CI funciona sem expor credenciais.

## Setup local

1. Clone o repositório
2. Pegue uma API key em [themoviedb.org](https://www.themoviedb.org/settings/api)
3. Crie/edite o `local.properties` na raiz:
   ```
   TMDB_API_KEY=sua_key_aqui
   ```
4. Rode:
   ```bash
   ./gradlew assembleDebug
   ```

## Estrutura de módulos

| Módulo | Tipo | O que tem |
|--------|------|-----------|
| `:domain` | Kotlin (JVM) | Models, Repository interface, Use Cases, ErrorMapper |
| `:data` | Android Library | Retrofit API, Room (Entity/DAO/Database), DTOs, Mappers, Repository impl |
| `:app` | Android App | Compose Screens, ViewModels, Hilt modules, Navigation, Theme |
