/*
 * PantallaPrincipal.java
 *
 * Created on 20 de agosto de 2008, 11:25 AM
 */

package interfaz;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import procedimientos.Util;

/**
 *
 * @author  acremex
 */
public class PantallaPrincipal extends javax.swing.JFrame{

    //objecto usado para realizar todas las operaciones relacionadas al Fingerprint-SDK
   private Util procedimientosSDK;

   //Panel para mostrar la huella digital
   private JPanel fingerprintViewPanel = null;
   
   //Imagen de la huella actual
   private BufferedImage fingerprintImage = null;
   
    /** Crea una nueva forma de  PantallaPrincipal */
    public PantallaPrincipal() {
        inicializar();
        initComponents();
        //this.setSize(370, 455);      
        procedimientosSDK.inicializarCaptura();
        setLocationRelativeTo(null);
        setVisible(true);
    }    
    
     /**
      * Se establece el estilo visual de la aplicación y se realiza la inicialización de la clase 
      * que contiene los procedimientos principales.
      **/   
    public void inicializar() {
       String grFingerNativeDirectory = new File(".").getAbsolutePath();
       Util.setFingerprintSDKNativeDirectory(grFingerNativeDirectory);
       try {
           UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
       } catch (Exception e) {
            System.err.println("No se pudo aplicar el estilo visual");
       }
       
       //Crea una instancia de Util
       this.procedimientosSDK = new Util(this);       
   }
    
    /**
    * Crea el panel que contendrá la imagen de la huella digital
    */
   private JComponent crearPanelHuella() {
       //Crea un panel nuevo para mostrar la huella
       fingerprintViewPanel = new JPanel(){
           
           //Se sobreescribe el método paintComponent para habilitar la muestra de la imagen de la huella
           public void paintComponent(Graphics g) {
               super.paintComponent(g);

               //Si hay una imagen para ser mostrada
               if (fingerprintImage!=null) {
                   //Calcula el tamaño y posición de la imagen para ser pintada
                   //el tamaño es ajustado para que ocupe todo el tamaño del panel
                   Insets insets = getInsets();
                   int transX = insets.left;
                   int transY = insets.top;
                   int width  = getWidth()  - getInsets().right  - getInsets().left;
                   int height = getHeight() - getInsets().bottom - getInsets().top;

                   //Se dibuja la imagen
                   g.drawImage(fingerprintImage, transX, transY, width, height, null);
               }

           }

       };

       //Se agrega un border alrededor del panel
       fingerprintViewPanel.setBorder(new CompoundBorder (
               new EmptyBorder (2,2,2,2),
               new BevelBorder(BevelBorder.LOWERED)));
       
       fingerprintViewPanel.setToolTipText("Imagen de la última huella capturada.");
       
       if(fingerprintViewPanel==null)
       {                      
            System.exit(1);
            return null;
       }else{
       
       return fingerprintViewPanel;
       }
       
   }
      
   /**
     * Método utilizado para mostrar la imagen de la huella
     * en el panel correspondiente.
     */
   public void showImage(BufferedImage image) {
       //Utiliza el imageProducer para crear una imagen de la huella digital
       fingerprintImage = image;       
       //Se dibuja la nueva imagen
       repaint();        
   }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        PanelContenedor = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), "Acciones", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        PanelContenedor.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Huella Digital", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        PanelContenedor.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelContenedor, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(PanelContenedor, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        PanelContenedor.getAccessibleContext().setAccessibleParent(jPanel2);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
    procedimientosSDK.guardarHuella();
}//GEN-LAST:event_btnGuardarActionPerformed

private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed

    dispose();
}//GEN-LAST:event_btnSalirActionPerformed

private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
    this.PanelContenedor.add(this.crearPanelHuella());
}//GEN-LAST:event_formWindowActivated

private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    
}//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelContenedor;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables

}
