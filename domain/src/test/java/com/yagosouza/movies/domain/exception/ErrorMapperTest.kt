package com.yagosouza.movies.domain.exception

import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ErrorMapperTest {

    @Test
    fun `maps UnknownHostException to no internet message`() {
        val result = ErrorMapper.map(UnknownHostException())
        assertEquals("Sem conexão com a internet. Verifique sua rede e tente novamente.", result)
    }

    @Test
    fun `maps SocketTimeoutException to timeout message`() {
        val result = ErrorMapper.map(SocketTimeoutException())
        assertEquals("A conexão demorou muito. Tente novamente.", result)
    }

    @Test
    fun `maps unknown exception to generic message`() {
        val result = ErrorMapper.map(IllegalStateException("something"))
        assertEquals("Ocorreu um erro inesperado. Tente novamente.", result)
    }
}
