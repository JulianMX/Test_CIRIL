import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Main {
    public static String carteAccessible = " ";
    public static String carteInnacessible = "#";
    public static String retourLigne = "\r\n";
    public static String separateurCoordonnee = ",";


    public static void main(String[] args) {

        System.out.println("Commencement");
        String[][] map = ReadMap();
        ReadPath(1, map);
        ReadPath(2, map);
        //test du out of bound
        //ReadPath(3, map);
        //ReadPath(4, map);
    }


    // pris sur internet
    public static String ReadFile(String filename) throws IOException {
        String parseResult;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            System.out.println("Erreur durant la création du buffeur fichier " + filename);
            throw new RuntimeException(e);
        }
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            parseResult = sb.toString();
        } catch (IOException e) {
            System.out.println("Erreur durant la lecture du fichier " + filename);
            throw new RuntimeException(e);
        } finally {
            br.close();
        }
        return parseResult;
    }


    // donne en sortie la matrice de la carte avec en coordonnée X,Y un chemin ou une foret innacessible
    public static String[][] ReadMap() {
        try {
            String outputFile = ReadFile("carte_v2.txt");
            String[] ligneMap = outputFile.split(retourLigne); // séparation des lignes
            String[][] matriceMap = new String[ligneMap.length][0];
            for (int i = 0; i < ligneMap.length; i++) { // séparation de chaque case dans chaque ligne
                matriceMap[i] = ligneMap[i].split("");
            }
            System.out.println(matriceMap);
            return matriceMap;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //prend en entrée le choix du chemin (fichier) et la carte
    //imprime en dernier la position finale du chemin
    public static void ReadPath(Integer pathChosen, String[][] map) {
        try {
            String inputFile = switch (pathChosen) { //on peut ajouter d'autres chemins
                case 1 -> "input1.txt";
                case 2 -> "input2.txt";
                case 3 -> "input3.txt"; //test du out of bound
                case 4 -> "input4.txt"; //test du out of bound
                default -> throw new Error("Pas de chemins choisi");
            };


            String outputPath = ReadFile(inputFile);
            String[] separateCoordinates = outputPath.split(retourLigne); //On sépare le point de départ et le chemin par le retour à la ligne
            String[] startingCoordinates = separateCoordinates[0].split(separateurCoordonnee);

            int startX = Integer.parseInt(startingCoordinates[0]);
            int startY = Integer.parseInt(startingCoordinates[1]);
            String pathToFollow = separateCoordinates[1];
            System.out.println("point de départ : (" + startX + "," + startY + ")");

            if (!Objects.equals(map[startY][startX], carteAccessible)) {
                throw new Error("Mauvais point de départ pour le chemin " + pathChosen);
            }

            //System.out.println("path : " + pathToFollow);

            int pathLength = pathToFollow.length();

            int maxX = map[0].length, maxY = map.length; //ne marche qu'avec une carte rectangulaire
            int currentX = startX, currentY = startY;
            int newX, newY;
            for (int i = 0; i < pathLength; i++) {
                newX = currentX;
                newY = currentY;
                switch (pathToFollow.charAt(i)) {
                    case 'N' -> newY = currentY - 1;
                    case 'S' -> newY = currentY + 1;
                    case 'E' -> newX = currentX + 1;
                    case 'O' -> newX = currentX - 1;
                }
                if (newX >= maxX || newX < 0 || newY < 0 || newY >= maxY) {
                    //System.out.println("On ne peut pas sortir de la carte, on ne bouge pas");
                } else if (Objects.equals(map[newY][newX], carteAccessible)) { //pourrait avoir une fonction pour vérifier que la case est accessible
                    currentX = newX;
                    currentY = newY;
                    //System.out.println("nouvelle position : (" + currentX + "," + currentY + ")");
                } else {
                    //System.out.println("forêt infranchissable, on ne bouge pas");
                }
            }
            System.out.println("Position finale pour le chemin " +
                    pathChosen + " (" + currentX + "," + currentY + ")");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}