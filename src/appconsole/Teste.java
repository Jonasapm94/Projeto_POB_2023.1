package appconsole;

import regras_negocio.Fachada;

import java.util.List;

import modelo.Time;

public class Teste {
    public static void main(String[] args) {
        Fachada.inicializar();
        try{
            List<Time> times = Fachada.timesJogandoPorData("27/04/2023");
            for(Time time  : times){
                System.out.println(time);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        Fachada.finalizar();
    }
}
