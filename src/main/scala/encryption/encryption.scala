package main.scala.encryption

import javax.crypto._
import java.security.{SecureRandom, InvalidKeyException, NoSuchAlgorithmException}
import java.io.{FileInputStream, FileOutputStream}

import scala.util.Try
import scala.util.control.Exception._

// import scala.pickling._
 
trait BaseEncryption {
    type TryToConvert = Try[Array[Byte]]
    def encrypt(toEncrypt: Array[Byte]): TryToConvert
    def decrypt(toDecrypt: Array[Byte]): TryToConvert
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

case class KeyValue(key: Array[Byte], value: Array[Byte])

class MostlySafeStorage {

    

    def put(kv: KeyValue): Unit = {
        val keyFileName = kv.key.toString
        val out = new FileOutputStream(keyFileName)
        out.write(kv.value)
        out.close()
    }
    /*
    def get(key: Array[Byte]): KeyValue = {
        case key
    }*/
}
