package quevedo.servidorLiga.utils;

import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

import java.util.HashMap;
import java.util.Map;

public class CreateHash {
    private Pbkdf2PasswordHash passwordHash;

    @Inject
    public CreateHash(Pbkdf2PasswordHash passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Map<String, String> parametros(){
        Map<String, String> parameters = new HashMap<>();
        parameters.put("Pbkdf2PasswordHash.Iterations", "3072");
        parameters.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512");
        parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", "32");
        parameters.put("Pbkdf2PasswordHash.KeySizeBytes", "32");

        return parameters;
    }


    public String hashearPass(String pass){

        passwordHash.initialize(parametros());

        return passwordHash.generate(pass.toCharArray());
    }

    public boolean verificarPass(String passCliente,String pass){
        passwordHash.initialize(parametros());
        return passwordHash.verify(passCliente.toCharArray(),pass);
    }
}
