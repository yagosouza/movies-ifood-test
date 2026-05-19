# Movies App

App Android para listagem e detalhes de filmes populares usando a [TMDB API](https://developer.themoviedb.org/).

## Arquitetura

Projeto multi-módulo seguindo **Clean Architecture** com 3 camadas:

```
:domain  → Modelos puros, interfaces de repositório, use cases (Kotlin puro, sem Android)
:data    → Retrofit API, DTOs, implementação do repositório (Android Library)
:app     → UI (Jetpack Compose), ViewModels, DI (Hilt), navegação
```

### Fluxo de Dados (MVVM + UDF)

```
UI (Compose) → ViewModel → UseCase → Repository → API (Retrofit)
     ↑                                                    |
     └──────────── StateFlow<UiState> ←───────────────────┘
```

## Stack

- **Kotlin** + **Coroutines/Flow**
- **Jetpack Compose** + Material Design 3
- **Hilt** (Injeção de Dependência)
- **Retrofit** + OkHttp + Kotlinx Serialization
- **Coil** (carregamento de imagens)
- **Compose Navigation**
- **MockK** + **Turbine** (testes)

## Setup

1. Obtenha uma API key em [themoviedb.org](https://www.themoviedb.org/settings/api)
2. Adicione no `local.properties`:
   ```
   TMDB_API_KEY=sua_api_key_aqui
   ```
3. Build:
   ```bash
   ./gradlew assembleDebug
   ```
4. Testes:
   ```bash
   ./gradlew test
   ```

## Estrutura de Módulos

| Módulo | Tipo | Dependências |
|--------|------|-------------|
| `:domain` | Kotlin (JVM) | Coroutines |
| `:data` | Android Library | `:domain`, Retrofit, OkHttp |
| `:app` | Android App | `:domain`, `:data`, Compose, Hilt |
