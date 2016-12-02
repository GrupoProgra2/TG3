
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Calendar;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Justm
 */
public class Steam {
    private RandomAccessFile codes, games, players;

    public Steam() {
         try{
            File f = new File("steam");
            f.mkdir();
            File f2= new File("steam/downloads");
            f2.mkdir();
            codes = new RandomAccessFile("steam/codes.stm", "rw");
            games = new RandomAccessFile("steam/games.stm", "rw");
            players=new RandomAccessFile("steam/players.stm", "rw");
            //3- Inicializar el archivo de codigos si es nuevo
            initCodes();
        }
        catch(IOException e){
            System.out.println("No deberia de pasar esto");
        }
    }
    
     private void initCodes() throws IOException {
        if(codes.length() == 0){
            codes.writeInt(1);
            codes.writeInt(1);
            codes.writeInt(1);
        }
    }
    
     private int getCode(char t)throws IOException{
         int code;
         if ( t=='G'){         
            codes.seek(0);
         } else if (t=='U'){
             codes.seek(4);
         } else {
             codes.seek(8);
         }
        code = codes.readInt();
        codes.seek(codes.getFilePointer()-4);
        codes.writeInt(code+1);
        return code;
    }
     
    public void addGame(String titulo, char SO, int edadMin, double precio) throws IOException{
         games.seek( games.length());
         int code= getCode('G');
         games.writeInt(code);
         games.writeUTF(titulo);
         games.writeChar(SO);
         games.writeInt(edadMin);
         games.writeDouble(precio);
         games.writeInt(0);
    }
    
    public void addPlayer(String nombre,  Calendar nacimiento) throws IOException {
        players.seek( players.length());
         int code= getCode('U');
         players.writeInt(code);
         players.writeUTF(nombre);
         players.writeLong(nacimiento.getTimeInMillis());
         players.writeInt(0);
    }
    
   
    
    public boolean downloadGame(int cvg, int ccli, char SO) throws IOException {
        int edad= this.lookClient(ccli);
        if (this.lookGame(cvg, SO, edad)){
            int code= this.getCode('D');
            FileWriter fl=new FileWriter("steam/downloads/download_"+code+".stm", true);
            fl.write(Calendar.getInstance().getTime().toString());
            fl.write("Download #"+code);
            String titulo= games.readUTF();
            games.skipBytes(5);
            double precio= games.readDouble();
            fl.write(players.readUTF()+" has bajado "+titulo+" a un precio de "+precio);
            fl.write("\n");
            fl.close();
            return true;
        }
                
        return false;
    }
    
    public int lookClient(int code)throws IOException {
        players.seek(0);
        Calendar t= Calendar.getInstance();
        int year= t.get(Calendar.YEAR);
          while(players.getFilePointer() < players.length()){
              if (players.readInt()==code)
                  players.readUTF();
                  t.setTimeInMillis(players.readLong());
                  int edad= year-t.get(Calendar.YEAR);
                  players.seek(4);
                  return edad ;
          }
          return -1;
    }
    
     public boolean lookGame(int code, char SO, int edadM)throws IOException {
         games.seek(0);
          while(games.getFilePointer() < games.length()){
              int c= games.readInt();
              games.readUTF();
              char s=games.readChar();
              int edad= games.readInt();
              if (c==code && c==SO && edad<=edadM){
                  games.seek(4);
                  return true;
              }
          } return false;
     }
}
