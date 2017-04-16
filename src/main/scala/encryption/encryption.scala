package main.scala.encryption

import javax.crypto._
import java.security.{SecureRandom, InvalidKeyException, NoSuchAlgorithmException}

import scala.util.Try
import scala.util.control.Exception._

import scala.pickling._

class Encryption(message: Array[Byte]) {
    val randomSeed = new SecureRandom()
    val keygenerator = KeyGenerator.getInstance("DES")
    val myDesKey = keygenerator.generateKey()
    val desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding")

    type TryToEncrypt = Try[Array[Byte]]

    def encrypt(toEncrypt: Array[Byte]): TryToEncrypt = {
        desCipher.init(Cipher.ENCRYPT_MODE, myDesKey, randomSeed)
        val textEncrypted: TryToEncrypt = catching(
            classOf[IllegalBlockSizeException],
            classOf[BadPaddingException]) withTry desCipher.doFinal(toEncrypt)
        textEncrypted
    }

    def decrypt(toDecrypt: Array[Byte]): TryToEncrypt = {
        desCipher.init(Cipher.DECRYPT_MODE, myDesKey, randomSeed)
        val textDecrypted: TryToEncrypt = catching(
            classOf[IllegalBlockSizeException],
            classOf[BadPaddingException]) withTry desCipher.doFinal(toDecrypt)
        textDecrypted
    }
}
