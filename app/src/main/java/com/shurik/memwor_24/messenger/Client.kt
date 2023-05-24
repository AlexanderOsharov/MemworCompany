package com.shurik.memwor_24.messenger

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

object Client {
    private const val SERVER_ADDRESS = "localhost"
    private const val SERVER_PORT = 1234

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            Socket(SERVER_ADDRESS, SERVER_PORT).use { socket ->
                println("Connected to the server")

                val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
                val writer = PrintWriter(socket.getOutputStream(), true)

                val userInput = BufferedReader(InputStreamReader(System.`in`))
                var text: String?

                while (userInput.readLine().also { text = it } != null) {
                    writer.println(text)
                    val serverResponse = reader.readLine()
                    println("Server says: $serverResponse")

                    if (text.equals("bye", ignoreCase = true)) {
                        break
                    }
                }
            }
        } catch (e: IOException) {
            println("Client error: ${e.message}")
        }
    }
}
