package quevedo.servidorLiga.utils;

import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

import java.util.HashMap;
import java.util.Map;

public class CreateHash {
    private final Pbkdf2PasswordHash passwordHash;

    @Inject
    public CreateHash(Pbkdf2PasswordHash passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Map<String, String> parametros() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(ConstantesUtils.ITERATIONSKEY, ConstantesUtils.ITERATIONS_VALUE);
        parameters.put(ConstantesUtils.ALGORITMO_KEY, ConstantesUtils.ALGORITMO_VALUE);
        parameters.put(ConstantesUtils.SALT_SIZE_KEY, ConstantesUtils.SALT_VALUE);
        parameters.put(ConstantesUtils.KEY_SIZE_BYTES_KEY, ConstantesUtils.SALT_VALUE);

        return parameters;
    }


    public String hashearPass(String pass) {

        passwordHash.initialize(parametros());

        return passwordHash.generate(pass.toCharArray());
    }

    public boolean verificarPass(String passCliente, String pass) {
        passwordHash.initialize(parametros());
        return passwordHash.verify(passCliente.toCharArray(), pass);
    }
}
