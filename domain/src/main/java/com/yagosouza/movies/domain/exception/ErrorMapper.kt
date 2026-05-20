package com.yagosouza.movies.domain.exception

import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorMapper {

    fun map(throwable: Throwable): String = when {
        throwable is UnknownHostException ->
            "Sem conexão com a internet. Verifique sua rede e tente novamente."

        throwable is SocketTimeoutException ->
            "A conexão demorou muito. Tente novamente."

        throwable.isHttpException() ->
            mapHttpError(throwable)

        else ->
            "Ocorreu um erro inesperado. Tente novamente."
    }

    private fun Throwable.isHttpException(): Boolean =
        this::class.simpleName == "HttpException"

    private fun mapHttpError(throwable: Throwable): String {
        val code = try {
            throwable::class.java.getMethod("code").invoke(throwable) as? Int
        } catch (_: Exception) {
            null
        }

        return when (code) {
            401 -> "Erro de autenticação com o servidor."
            404 -> "Conteúdo não encontrado."
            in 500..599 -> "O servidor está com problemas. Tente novamente mais tarde."
            else -> "Erro ao se comunicar com o servidor."
        }
    }
}
