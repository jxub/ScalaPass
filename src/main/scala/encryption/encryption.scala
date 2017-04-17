package main.scala.encryption

import javax.crypto._
import java.security.{SecureRandom, InvalidKeyException, NoSuchAlgorithmException}

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
