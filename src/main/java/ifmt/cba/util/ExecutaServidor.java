package ifmt.cba.util;

import java.io.IOException;

import org.glassfish.grizzly.http.server.HttpServer;

public class ExecutaServidor {
    
    public static void main(String[] args) throws IOException {
        HttpServer servidor = ServidorHTTP.getServerHTTP();
        System.out.println("-------------------------------------------------------------------r");
        System.out.println("Web Services Restaurante - Presseione qualquer tecla para encerrar");
        // espera uma tecla se pressionada
        System.in.read();
        servidor.shutdownNow();
    }
}
