package vn.giabaochatapp.giabaochatappserver.services;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;

@Service
public class EnvService {
    private final Dotenv dotenv;

    public EnvService() {
        this.dotenv = Dotenv.load();
    }

    public String get(String key) {
        return dotenv.get(key);
    }
}
