package main

import com.jxub.encryption
import com.jxub.storage

object CLIApp extends App {
    val usage = """
        ScalaPass - Your (mostly) safe password manager in Scala
    """
    if (args.length == 0)
        println(usage)
    else if (args.length == 1)
        println(s"Hello, ${args(0)}")
    else println(1)
}