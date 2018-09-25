package crypto

import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import java.security.spec.X509EncodedKeySpec
import java.security.KeyFactory
import java.util.Arrays


class RSA {

    fun generateKeys(): KeyPair{
        val keySize = 512
        val generator = KeyPairGenerator.getInstance("RSA")
        generator.initialize(keySize)
        return generator.genKeyPair()
    }

    fun privateKeyToString(key: PrivateKey): String{
        val factory = KeyFactory.getInstance("RSA")
        val spec = factory.getKeySpec(key,
                PKCS8EncodedKeySpec::class.java)
        val key64 = Base64.getEncoder().encodeToString(spec.encoded)
        Arrays.fill(spec.encoded, 0.toByte())
        return key64
    }

    fun publicKeyToString(key: PublicKey): String{
        val factory = KeyFactory.getInstance("RSA")
        val spec = factory.getKeySpec(key,
                X509EncodedKeySpec::class.java)
        return Base64.getEncoder().encodeToString(spec.encoded)
    }

    fun stringToPrivateKey(key: String): PrivateKey{
        val data = Base64.getDecoder().decode(key)
        val keySpec = PKCS8EncodedKeySpec(data)
        val factory = KeyFactory.getInstance("RSA")
        val private = factory.generatePrivate(keySpec)
        Arrays.fill(data, 0.toByte())
        return private
    }
    fun stringToPublicKey(key: String): PublicKey {
        val data = Base64.getDecoder().decode(key)
        val spec = X509EncodedKeySpec(data)
        val factory = KeyFactory.getInstance("RSA")
        return factory.generatePublic(spec)
    }


}