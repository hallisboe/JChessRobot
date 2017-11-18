package GUI;

import java.io.*;

public class SaveAndLoad {

    //Loads a string from a text file
    public static String loadFromTextFile(String filePath){
        try{
            FileReader fr = new FileReader(new File(filePath));
            BufferedReader br = new BufferedReader(fr);

            String output = "";
            String line = br.readLine();
            while(line != null){
                output += line;
                line = br.readLine();
            }
            return output;
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    //Returns a byte[][] (Chess board) read from a text file
    public static byte[][] loadBoard (String filePath){
        byte[][] board = new byte[8][8];
        String input = loadFromTextFile(filePath);
        if(input == null){return null;}
        String[] values = input.split(",");
        for(int i = 0; i < values.length; i++){
            try{
                board[i/8][i%8] = Byte.parseByte(values[i]);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        return board;
    }
}
