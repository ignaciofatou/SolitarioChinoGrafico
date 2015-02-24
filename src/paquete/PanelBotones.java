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

        gestionaCirculos(evt.getX(), evt.getY());
        this.repaint();        
    }//GEN-LAST:event_formMouseClicked
    private void gestionaCirculos(int coordenadaX, int coordenadaY){

        int espacioOcupado = (RADIO_BTN * 2) + MARGIN_BTN;
        
        int posX = (coordenadaX - (MARGIN_BTN / 2)) / espacioOcupado;
        int posY = (coordenadaY - (MARGIN_BTN / 2)) / espacioOcupado;
        
        if (estaDentroTablero(posX, posY))
            marcaDesmarcaSeleccionado(posX, posY);
    }
    private void marcaDesmarcaSeleccionado(int posX, int posY){
        
        boolean hayUno = false;
        
        //Si ya hay uno Seleccionado
        if (hayUnoSeleccionado()){
            //Obtenemos la Posicion del Circulo Seleccionado
            int[] pos = getPosicionSeleccionado();
            
            //Si hemos Marcado una que ya esta Seleccionada -> Deseleccionamos
            if ((posX == pos[0]) && (posY == pos[1]))
                tablero[posY][posX] = NORMAL;
        }
        //Si no hay ninguna Seleccionado -> Seleccionamos
        else
            tablero[posY][posX] = SELECC;
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
                    pos[0]=x;
                    pos[1]=y;
                }                    
        
        return pos;
    }
    
    
    private boolean estaDentroTablero(int posX, int posY){
        if ((posX<2) || (posX>4))
            if ((posY<2) || (posY>4))
                return false;

        //Esta dentro del Panel
        return true;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
