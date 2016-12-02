
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

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
    
}
