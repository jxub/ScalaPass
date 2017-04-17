package main.scala.encryption

import javax.crypto._
import java.security.{SecureRandom, InvalidKeyException, NoSuchAlgorithmException}
import java.io.{File, FileInputStream, FileOutputStream}
import java.nio.file.{Path, Files, FileSystem, FileSystems}

import scala.util.Try
import scala.util.control.Exception._

 
// traits that represent the interfaces, java-like
// scala is just a better java, like it or not
// not same FP potential as haskell, but has OOP
// which is its bread & butter
trait BaseEncryption {
    type TryToConvert = Try[Array[Byte]]
    def encrypt(toEncrypt: Array[Byte]): TryToConvert
    def decrypt(toDecrypt: Array[Byte]): TryToConvert
}

trait BaseStorage {
    type key = Array[Byte]
    type value = Array[Byte]
    def put(k: key, v: value): Unit
    def get(k: key): value
}


class DESEncryption extends BaseEncryption {
    val randomSeed = new SecureRandom()
    val keygenerator = KeyGenerator.getInstance("DES")
    val myDesKey = keygenerator.generateKey()
    val desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding")

    def encrypt(toEncrypt: Array[Byte]): TryToConvert = {
        desCipher.init(Cipher.ENCRYPT_MODE, myDesKey, randomSeed)
        val textEncrypted: TryToConvert = catching(
            classOf[IllegalBlockSizeException],
            classOf[BadPaddingException]) withTry desCipher.doFinal(toEncrypt)
        textEncrypted
    }

    def decrypt(toDecrypt: Array[Byte]): TryToConvert = {
        desCipher.init(Cipher.DECRYPT_MODE, myDesKey, randomSeed)
        val textDecrypted: TryToConvert = catching(
            classOf[IllegalBlockSizeException],
            classOf[BadPaddingException]) withTry desCipher.doFinal(toDecrypt)
        textDecrypted
    }
}

// the essential schema of the data
// keys are filenames and values are the encrypted content
// can't e used in MostlySafeStorage.get() cause the java methods
// don't seem to work properly

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
        in
    }
}
