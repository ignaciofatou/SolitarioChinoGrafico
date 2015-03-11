/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paquete;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Ignacio
 */
public class PanelBotones extends javax.swing.JPanel {

    private static final int LARGUE     = 7;
    private static final int RADIO_BTN  = 15;
    private static final int MARGIN_BTN = 5;
    private final     String COL_NORMAL = "FF0000";
    private final     String COL_SELECC = "0000FF";
    private final        int NORMAL     = 0;
    private final        int SELECC     = 1;
    private final        int VACIO      = 2;
    private final        int OUT_PANEL  = 3;
    private int[][] tablero;
    

    /**
     * Creates new form PanelBotones
     */
    public PanelBotones() {
        initComponents();        
        inicializaTablero();
    }
    
    private void inicializaTablero(){
        
        tablero = new int[LARGUE][LARGUE];
        
        for (int x=0; x<LARGUE; x++){
            for (int y=0; y<LARGUE; y++){
                //Si Esta dentro del Tablero
                if (estaDentroTablero(x, y))
                    tablero[x][y]=NORMAL;
                //Esta fuera del Tablero
                else
                    tablero[x][y]=OUT_PANEL;
            }
        }
        //Posicion de Enmedio
        tablero[3][3]=VACIO;
    }    
    
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        //Obtenemos el color en formato HSB
        float[] colorHSB_Normal = getColorHSB(COL_NORMAL);
        float[] colorHSB_Selecc = getColorHSB(COL_SELECC);
        int posX = MARGIN_BTN;
        int posY = MARGIN_BTN;
        
        for (int y=0; y<LARGUE; y++){
            
            for (int x=0; x<LARGUE; x++){
                
                if (tablero[y][x] == NORMAL)
                    pintaCirculo3D(g, posX, posY, RADIO_BTN, colorHSB_Normal);
                else if (tablero[y][x] == SELECC)
                    pintaCirculo3D(g, posX, posY, RADIO_BTN, colorHSB_Selecc);
                else if (tablero[y][x] == VACIO)
                    pintaCirculoBlanco(g, posX, posY, RADIO_BTN);
                
                posX = posX + (RADIO_BTN * 2) + MARGIN_BTN;
            }
            posX = MARGIN_BTN;
            posY = posY + (RADIO_BTN * 2) + MARGIN_BTN;
        }
    }
    

    
    private void pintaCirculoBlanco(Graphics g, int posX, int posY, int radio){
        int size = radio * 2;
        g.setColor(Color.WHITE);        
        g.fillOval(posX, posY, size, size);
    }
    private void pintaCirculo3D(Graphics g, int posX, int posY, int radio, float[] colorHSB){
        
        //Calculamos el Tamaño del Circulo
        int size = radio * 2;
        float resta = (float)(0.8/radio);
        float saturation = colorHSB[1];

        for (int x=size; x > 1; x=x-2){
            g.setColor(Color.getHSBColor(colorHSB[0], saturation, colorHSB[2]));
            g.fillOval(posX, posY, x, x);

            //Decrementamos Saturacion
            saturation = (float)saturation - resta;

            //Incrementamos la posicion del siguiente circulo
            if ((x % 4) == 0.0){
                posY++;
                posX++;
            }
        }
    }
    private float[] getColorHSB(String color){
        try{
            //Convertimos el Color en Formato HSB
            int red   = Integer.parseInt(color.substring(0, 2), 16);                
            int green = Integer.parseInt(color.substring(2, 4), 16);
            int blue  = Integer.parseInt(color.substring(4, 6), 16);        
            return Color.RGBtoHSB(red, green, blue, null);
        } catch (Exception ex){
            System.out.println("Error en Conversion a Hexadecimal: "+ ex.getMessage());
        }
        return  null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked

        gestionaCirculos(evt.getY(), evt.getX());
        this.repaint();        
    }//GEN-LAST:event_formMouseClicked
    private void gestionaCirculos(int coordenadaY, int coordenadaX){

        int espacioOcupado = (RADIO_BTN * 2) + MARGIN_BTN;
        
        int posY = (coordenadaY - (MARGIN_BTN / 2)) / espacioOcupado;
        int posX = (coordenadaX - (MARGIN_BTN / 2)) / espacioOcupado;
        
        if (estaDentroTablero(posY, posX))
            marcaDesmarcaSeleccionado(posY, posX);
    }
    private void marcaDesmarcaSeleccionado(int newPosY, int newPosX){
        
        boolean hayUno = false;
        
        //Si ya hay uno Seleccionado anteriormente
        if (hayUnoSeleccionado()){
            //Obtenemos la Posicion del Circulo Seleccionado
            int[] pos = getPosicionSeleccionado();
            
            //Si hemos Marcado una que ya esta Seleccionada -> Deseleccionamos
            if ((newPosY == pos[0]) && (newPosX == pos[1]))
                tablero[newPosY][newPosX] = NORMAL;
            //Si hemos Marcado uno que esta vacio -> Comprobamos si es Adyacente
            //al que se marco anteriormente
            else if (tablero[newPosY][newPosX] == VACIO){
                int difPosY = newPosY - pos[0];
                int difPosX = newPosX - pos[1];                
                
                //Si en una de las coordenadas hay 2 posiciones de diferencia
                if (((Math.abs(difPosY) == 2) && (difPosX == 0)) ||
                    ((Math.abs(difPosX) == 2) && (difPosY == 0))){
                    
                    //Inicializamos la Posicion de en medio
                    int posMedioY = newPosY;
                    int posMedioX = newPosX;
                    
                    //Calculamos la Posicion de Enmedio
                    if (difPosY > 0)
                        posMedioY--;
                    else if (difPosY < 0)
                         posMedioY++;
                    else if (difPosX > 0)
                        posMedioX--;
                    else if (difPosX < 0)
                        posMedioX++;
                    
                    //Si la Posicion de en medio contiene un circulo --> OK
                    if (tablero[posMedioY][posMedioX] == NORMAL){                    
                        tablero[newPosY][newPosX] = NORMAL;
                        tablero[pos[0]][pos[1]] = VACIO;
                        tablero[posMedioY][posMedioX] = VACIO;
                        
                        //Comprobamos si ha finalizado la Partida
                        compruebaFinPartida();
                    }
                }                
            }
        }
        //Si no hay ninguna Seleccionado y es Normal -> Seleccionamos
        else if (tablero[newPosY][newPosX] == NORMAL)
            tablero[newPosY][newPosX] = SELECC;
    }
    private void compruebaFinPartida(){

        int numNormal = 0;
        boolean perdido = true;

        for (int y=0; y<LARGUE; y++){
            for (int x=0; x<LARGUE; x++){
                //Si la Posicion contiene una bola Normal
                if (tablero[y][x] == NORMAL){
                    numNormal++;

                    //Si de momento no encuentra una Bola adyacente
                    if (perdido){
                        //Compruebo si tiene otra Normal Adyacente en Vertical
                        if ((y >= 1) && (tablero[y - 1][x] == NORMAL)){
                            //Si Delante de la Bola esta vacio o
                            //Detras de la Adyacente esta vacio
                            if ((((y + 1) < LARGUE) && (tablero[y + 1][x] == VACIO)) ||
                                (((y - 2) >= 0    ) && (tablero[y - 2][x] == VACIO)))
                                perdido = false;
                        }
                        //Compruebo si tiene otra Normal Adyacente en Horizontal
                        if ((x >= 1) && (tablero[y][x - 1] == NORMAL)){
                            //Si Arriba de la Bola esta cacio o
                            //Abajo de la Adyacente esta vacio
                            if ((((x + 1) < LARGUE) && (tablero[y][x + 1] == VACIO)) ||
                                (((x - 2) >= 0    ) && (tablero[y][x - 2] == VACIO)))
                                perdido = false;
                        }
                    }
                }
            }
        }

        //Si solo hay uno Normal -> Ha ganado la partida
        if (numNormal == 1)
            System.out.println("Ha ganado la Partida");
        //Si no ha encontrado bolas Adyacentes -> Ha perdido la partida
        else if (perdido)
            System.out.println("Ha perdido la Partida");
    }
    
    
    private boolean hayUnoSeleccionado(){
        for (int y=0; y<LARGUE; y++)
            for (int x=0; x<LARGUE; x++)
                if (tablero[y][x] == SELECC)
                    return true;
        
        return false;
    }
    private int[] getPosicionSeleccionado(){
        int[] pos = new int[2];
        for (int y=0; y<LARGUE; y++)
            for (int x=0; x<LARGUE; x++)
                if (tablero[y][x] == SELECC){
                    pos[0]=y;
                    pos[1]=x;
                }                    
        
        return pos;
    }
    
    
    private boolean estaDentroTablero(int posY, int posX){
        if ((posY<2) || (posY>4))
            if ((posX<2) || (posX>4))            
                return false;

        //Esta dentro del Panel
        return true;
    }
    


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
