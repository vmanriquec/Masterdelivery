package com.mazinger.masterdelivery.modelo;
public class cambiomesa {
        private int mesaantes;
        private int mesadespues;
        private  int idalmacen;

        public cambiomesa(int mesaantes,int mesadespues,int idalmacen)
        {
            super();
            this.mesaantes=mesaantes;
            this.mesadespues=mesadespues;
            this.idalmacen=idalmacen;
             }
        public int getMesaantes() {
            return mesaantes;
        }
        public void setMesaantes(int mesaantes) {
            this.mesaantes = mesaantes;
        }
        public int getMesadespues() {
            return mesadespues;
        }
        public void setMesadespues(int mesadespues) {
            this.mesadespues = mesadespues;
        }

    public int getIdalmacen() {
        return idalmacen;
    }
    public void setIdalmacen(int idalmacen) {
        this.idalmacen = idalmacen;
    }
        @Override
        public String toString(){
            return String.valueOf(mesaantes)+" cambio a #-"+ String.valueOf(mesadespues);
        }
}