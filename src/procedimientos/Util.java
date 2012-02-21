package procedimientos;

import com.griaule.grfingerjava.FingerprintImage;
import com.griaule.grfingerjava.GrFingerJava;
import com.griaule.grfingerjava.GrFingerJavaException;
import com.griaule.grfingerjava.IFingerEventListener;
import com.griaule.grfingerjava.IImageEventListener;
import com.griaule.grfingerjava.IStatusEventListener;
import com.griaule.grfingerjava.MatchingContext;
import com.griaule.grfingerjava.Template;
import interfaz.PantallaPrincipal;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 * @author Monillo007
 * :: Visita http://monillo007.blogspot.com ::
 */
public class Util implements IStatusEventListener, IImageEventListener, IFingerEventListener{

    /**Contexto utilizado para la captura, extracción y coincidencia de huellas digitales */
   private MatchingContext fingerprintSDK;

   /**Interfaz de usuario donde se muestra la imagena de la huella*/
   private PantallaPrincipal ui;

   /** Indica si la plantilla o template debe ser extraída automáticamente*/
   private boolean autoExtract = true;

   /** Arreglo que contiene localmente los datos de las huellas capturadas */
   private ByteArrayInputStream fingerprintData;
   
   /**Arreglo que contiene la longitud del dato de la huella*/
   private int fingerprintDataLength;
   
   /** La imagen de la última huella digital capturada. */
   private FingerprintImage fingerprint;
   
   /** La plantilla de la última imagen de huella capturada */
   public com.griaule.grfingerjava.Template template;
   
   public Util(PantallaPrincipal ui) {
       this.ui = ui;
       //Inicializa la conexión a la BD.
       initDB();
       
   }
   
   /**
    * Inicializa el Fingerprint SDK y habilita la captura de huellas.
    */
   public void inicializarCaptura() {
       try {
           fingerprintSDK = new MatchingContext();          
           //Inicializa la captura de huella digital.
           GrFingerJava.initializeCapture(this);         
       } catch (Exception e) {
           //Si ocurre un error durante la inicialización se indica con un mensaje y se cierra la aplicación.
           e.printStackTrace();
           System.exit(1);
       }
   }
   
   /**Conexión a la base de datos*/
   private Connection dbConnection;
   
   /**Consultas preparadas**/
   private PreparedStatement guardarStmt;
   private PreparedStatement identificarStmt;
   private PreparedStatement verificarStmt;
   
   /**
    * Inicializa la conexión a la base de datos y prepara las operaciones para insertar, 
    * eliminar y modificar los datos
    */
   private void initDB() {
       try {
           //Carga el driver ODBC
           Class.forName("com.mysql.jdbc.Driver");

           //Se conecta a la bd
           dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/huellas","huella", "digital");

           Statement stm = dbConnection.createStatement();
           
           //Prepara las consultas/sentencias que se utilizarán
           guardarStmt     = dbConnection.prepareStatement("INSERT INTO somhue(id, huenombre, huehuella) values(?,?,?)");
           identificarStmt   = dbConnection.prepareStatement("SELECT * FROM somhue");
           verificarStmt     = dbConnection.prepareStatement("SELECT huehuella FROM somhue WHERE huenombre=?");
                   
       } catch (Exception e) {
           e.printStackTrace();
       }
       
   }
   
   /**
    * Cierra la conexión a la base de datos y libera los recursos empleados.
    */
   private void destroyDB() {
       try {
           //Se cierran todas las sentencias
           guardarStmt.close();           
           identificarStmt.close();
           //Cierra la conexión a la  base
           dbConnection.close();
           
       } catch (Exception e) {
           e.printStackTrace();
       }        
   }
   
   /**
    * Establece el directorio donde se ubican las librerías nativas del SDK
    */
   public static void setFingerprintSDKNativeDirectory(String directorio) {
       File directory = new File(directorio);
       
       try {
           GrFingerJava.setNativeLibrariesDirectory(directory);
           GrFingerJava.setLicenseDirectory(directory);
       } catch (GrFingerJavaException e) {
           e.printStackTrace(); 
       }
   }
   
    public void onSensorPlug(String idSensor) {       
       try {
           //Comienza la captura con el lector conectado.
           GrFingerJava.startCapture(idSensor, this, this);
       } catch (GrFingerJavaException e) {
           //Indica si ha ocurrido un error con el lector.
           e.printStackTrace();
       }
    }

    public void onSensorUnplug(String idSensor) {
        try {
           GrFingerJava.stopCapture(idSensor);
       } catch (GrFingerJavaException e) {
           //Indica si ha ocurrido un error con el lector.
           e.printStackTrace();           
       }
    }

    public void onImageAcquired(String idSensor, FingerprintImage huellaDigital) {
        //Almacena la imagen de la huella
       this.fingerprint=huellaDigital;

       //Muestra la imagen obtenida
       ui.showImage(huellaDigital);       
       
       //Muestra la plantilla en la imagen actual
       extract();
    }
    
    
   /**
    * Extract a fingerprint template from current image.
    */  
   public void extract() {
       
       try {
           //Extracts a template from the current fingerprint image. 
           template = fingerprintSDK.extract(fingerprint);
           //display minutiae/segments/directions into image
           ui.showImage(GrFingerJava.getBiometricImage(template,fingerprint));            
       } catch (GrFingerJavaException e) {
           e.printStackTrace();
       }
       
   }
   
   /*
    * Guarda los datos de la huella digital actual en la base de datos  
    * */
   public void guardarHuella(){

           fingerprintData = new ByteArrayInputStream(template.getData());
           fingerprintDataLength = template.getData().length;
           
           String nombre = JOptionPane.showInputDialog("Nombre:");
           
        try {
            guardarStmt.setInt(1,1);
            guardarStmt.setString(2,nombre);
            guardarStmt.setBinaryStream(3, fingerprintData, fingerprintDataLength);
            
            guardarStmt.execute();
        } catch (SQLException ex) {
            System.err.println("Error al guardar los datos de la huella.");
            ex.printStackTrace();
        }
   }
   
    public void onFingerDown(String idSensor) {
        
    }

    public void onFingerUp(String arg0) {
        
    }
    
    /**
    * verifica la huella digital actual contra otra en la base de datos
    */
   public void identificarPersona() {
       try {
           //Obtiene todas las huellas de la bd
           ResultSet rsIdentificar = identificarStmt.executeQuery();
           
           //Si se encuentra el nombre en la base de datos
           while(rsIdentificar.next()){
               //Lee la plantilla de la base de datos
               byte templateBuffer[] = rsIdentificar.getBytes("huehuella");
               //Crea una nueva plantilla
               Template referenceTemplate = new Template(templateBuffer);
               
               //compara las plantilas (actual vs bd)
               boolean coinciden = fingerprintSDK.verify(template,referenceTemplate);
               
               //Si encuentra correspondencia dibuja el mapa 
               //e indica el nombre de la persona que coincidió.
               if (coinciden){                   
                   ui.showImage(GrFingerJava.getBiometricImage(template, fingerprint, fingerprintSDK));                                  
                   JOptionPane.showMessageDialog(ui, "La huella es de "+rsIdentificar.getString("huenombre"));
                   return;
               }
               
           } 
           
           //Si no encuentra alguna huella que coincida lo indica con un mensaje
           JOptionPane.showMessageDialog(ui, "No existe ningún registro que coincida con la huella.");
               
       } catch (SQLException e) {
           e.printStackTrace();           
       } catch (GrFingerJavaException e) {
           e.printStackTrace();           
       }
   }
   
   /**
    * Identifica a una persona registrada por medio de su huella digital
    */
   public void verify(String nom){
       try {
           //Obtiene la plantilla correspondiente a la persona indicada
           verificarStmt.setString(1,nom);
           ResultSet rs = verificarStmt.executeQuery();
           
           //Si se encuentra el nombre en la base de datos
           if (rs.next()){
               //Lee la plantilla de la base de datos
               byte templateBuffer[] = rs.getBytes("huehuella");
               //Crea una nueva plantilla
               Template referenceTemplate = new Template(templateBuffer);
               
               //compara las plantilas (actual vs bd)
               boolean coinciden = fingerprintSDK.verify(template,referenceTemplate);
               
               //Si corresponden, dibuja el mapa de correspondencia y lo indica con un mensaje
               if (coinciden){                   
                   ui.showImage(GrFingerJava.getBiometricImage(template, fingerprint, fingerprintSDK));                                  
                   JOptionPane.showMessageDialog(ui, "Las huellas sí coinciden");
               } else {
                   //De no ser así lo indica con un mensaje
                   JOptionPane.showMessageDialog(ui, "No corresponde la huella con "+nom, "Error", JOptionPane.ERROR_MESSAGE);
               }
               
               //Si no encuentra alguna huella correspondiente al nombre lo indica con un mensaje
           } else {
               JOptionPane.showMessageDialog(ui, "No existe el registro de "+nom, "Error", JOptionPane.ERROR_MESSAGE);
           }
       } catch (SQLException e) {
           e.printStackTrace();           
       } catch (GrFingerJavaException e) {
           e.printStackTrace();           
       }
   } 

}
