package com.jxub.storage

import java.io.{File, FileInputStream, FileOutputStream}
import java.nio.file.{Path, Files, FileSystem, FileSystems}

// interface to be implemented also for
// better storage ideas than mine
 // key and value traits are the essential schema of the data
// keys are filenames and values are the encrypted content
trait BaseStorage {
    type key = Array[Byte]
    type value = Array[Byte]
    def put(k: key, v: value): Unit
    def get(k: key): value
}

class MostlySafeStorage extends BaseStorage {
    
    // we assume the key and value are never empty,
    // failures handled in pattern matching in main
    def put(k: key, v: value): Unit = {
        val keyPath = k.toString
        val file = new File(keyPath)
        file.getParentFile().mkdirs() // to check that the dir exists
        file.createNewFile()
        val out = new FileOutputStream(file)
        out.write(v, 0, v.length)
        out.flush()
        out.close()
    }

    // key should specify the filepath, we should append the relative
    // path to the filename in main to the user-facing key
    def get(k: key): value = {
        val path = FileSystems.getDefault().getPath("", k.toString)
        val in = Files.readAllBytes(path)
        in  // yep! it returns the value of in! yay!
    }
}