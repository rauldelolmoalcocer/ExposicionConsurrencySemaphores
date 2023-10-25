package Ejercicio1;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;

public class Exposicion {

    int aforo;
    ListaThreads colaEspera, dentro;
    Semaphore semaforo;
    Semaphore semSignalPause;
    Semaphore semAbiertoAlPublico = new Semaphore(1);
    boolean cerrar = false;

    public Exposicion(int aforo, JTextField tfEsperan, JTextField tfDentro) {
        this.aforo = aforo;
        semaforo = new Semaphore(aforo, true);
        semSignalPause = new Semaphore(1, true);
        colaEspera = new ListaThreads(tfEsperan);
        dentro = new ListaThreads(tfDentro);
    }

    public void entrar(Visitante v) {

        try {
            semSignalPause.acquire(); // Bloquear si se ha llamado a DetenerThreads
            semSignalPause.release();
            
            
            
            //semAbiertoAlPublico.acquire(); //Si se llama desde bloquear acceso Public se bloquea
            //semAbiertoAlPublico.release();

            colaEspera.meter(v);
            semaforo.acquire();
            colaEspera.sacar(v);
            dentro.meter(v);
            
            
            
            
        } catch (Exception e) {
        }

    }

    public void salir(Visitante v) {
        try {
            semSignalPause.acquire(); // Bloquear si se ha llamado a DetenerThreads
            semSignalPause.release();
            
            
            
            dentro.sacar(v);
            if(!cerrar){
            semaforo.release();
            }
        } catch (Exception ex) {
            Logger.getLogger(Exposicion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mirar(Visitante v) {
        try {
            Thread.sleep(2000 + (int) (3000 * Math.random()));
        } catch (InterruptedException e) {
        }
    }

    public void DetenerThreads() {

         try {
            semSignalPause.acquire(); // Tomamos el permiso, lo que bloquea el flujo normal de hilos.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void AbrirThreads() {

        semSignalPause.release(); // Liberamos el permiso, lo que permite que el flujo de hilos continúe.

    }
    
    
    public void CerrarAlPublico(){
        try {
            cerrar = true;
        } catch (Exception ex) {
            Logger.getLogger(Exposicion.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    public void abrirAlPublico(){
        
        
        try {
            cerrar = false;
            semaforo.release(aforo - semaforo.availablePermits());
            
        } catch (Exception ex) {
            Logger.getLogger(Exposicion.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
}
