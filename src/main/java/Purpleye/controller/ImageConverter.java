package Purpleye.controller;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;
import java.util.*;
import java.util.List;

public class ImageConverter {

    private final static int FIRST_ASCII_CHAR = 32;
    private final static int LAST_ASCII_CHAR = 126;
    private static BufferedImage outputImage;


    public BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {

            Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
            BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_BYTE_GRAY);
            outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);


            return outputImage;
        }

        //Assigns char, image and weight for each WeightedChar object
    private static List<WeightedChar> generateFontWeights(){

        List<WeightedChar> weightedChars = new ArrayList<WeightedChar>();
        Dimension commonSize = getGeneralSize(); // get the standard size

        for (int i = FIRST_ASCII_CHAR; i <= LAST_ASCII_CHAR ; i++) { // iterate through all the chars

            var forWeighting = new WeightedChar(); // new object to hold image wieght and char of new character
            char c = (char)i;   //character code

            if (!Character.isISOControl(c)){    //if not a control key

                forWeighting.setCharacter(String.valueOf(c));
                forWeighting.setWeight(calculateDarkness(c,commonSize));
                forWeighting.setCharacterImage(drawText(c,commonSize));

            }
            weightedChars.add(forWeighting);
        }

        return linearMap(weightedChars);
    }

    //adds the right amount of space on each side of a character so that each character has the same size (like a pixel)
    public static TreeMap<String,String> padCharacters(){
            TreeMap<String,String> charMapper = new TreeMap<>();

            BufferedImage bufferedImage = new BufferedImage(1,1, BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D g2d = bufferedImage.createGraphics();
            FontMetrics fm = g2d.getFontMetrics();

            for (int i = 32; i <= 126 ; i++) {

                char c = (char)i;
                String sc = String.valueOf(c);

                while (fm.stringWidth(sc) < getGeneralSize().width){
                    sc = " " + sc/* + " "*/;
                }

                charMapper.put(String.valueOf(c),sc);
            }
            return charMapper;
        }

        //is responsible for getting the right dimensions so that every char has the ssme patch size
    public static Dimension getGeneralSize(){

        Dimension generalSize = new Dimension(0, 0);

        for (int i = 32; i <= 126; i++) { // Iterate through contemplated characters calculating necessary width

            char c = (char)(i);
            // Create a dummy bitmap just to get a graphics object
            Image img = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_GRAY);
            Graphics drawing = img.getGraphics();

            // Measure the string to see its dimensions using the graphics object  all chars have same height
            Dimension textSize = new Dimension(drawing.getFontMetrics().charWidth(c),
                                        drawing.getFontMetrics().getHeight());

            // Update, if necessary, the max width and height
            if (textSize.width > generalSize.width) generalSize.width = textSize.width;

            if (textSize.height > generalSize.height) generalSize.height = textSize.height;


            // free all resources
            img.flush();
            drawing.dispose();
        }

        // and that size defines a square to maintain image proportions
        if (generalSize.width > generalSize.height) generalSize.setSize(generalSize.width,generalSize.width);
            else generalSize.setSize(generalSize.height,generalSize.height);

        return generalSize;
    }

    //calculates the ratio of darkness for each character
    private static double calculateDarkness(char c, Dimension size){

            BufferedImage charImage = drawText(c,size);

            double totalSum = 0;

        for (int i = 0; i <charImage.getWidth(); i++) {

            for (int j = 0; j < charImage.getHeight(); j++) {

                Color color = new Color(charImage.getRGB(i,j));
                totalSum += (((double)color.getRed() + (double)color.getGreen() + (double)color.getBlue()) / 3);

            }
        }

        return totalSum / (size.height * size.width);
    }

    //draws a character on a image
    private static BufferedImage drawText(char c, Dimension size){

        //create image
        BufferedImage charImage = new BufferedImage(size.width,size.height,BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphicsImage = charImage.createGraphics();

        //alter image
        graphicsImage.setBackground(Color.WHITE);
        graphicsImage.fillRect(0,0,size.width,size.height);
        graphicsImage.setColor(Color.BLACK);

        //apply changes
        graphicsImage.drawString(String.valueOf(c),(int)(size.getWidth() / 2),(int)(size.getHeight() / 2));

        return charImage;
    }

    //maps the character to the desired range
    private static List<WeightedChar> linearMap(List<WeightedChar> characters){
            Collections.sort(characters);

            double max = characters.get(characters.size()-1).weight;
            double min = characters.get(0).weight;

            double range = 255;
            double slope = range / (max-min);
            double n = -min * slope;

        for (WeightedChar weightedChar: characters) {
            weightedChar.setWeight(slope * weightedChar.weight + n);
        }
        return characters;
    }

    //Puts everything together and returns a string that represents ascii
    public static String comvertToASCII(BufferedImage image, int width, int height){

            TreeMap<String,String> mappedChars;
            StringBuilder sb = new StringBuilder();
            List<List<String>> resultText = new ArrayList<>();
            List<WeightedChar> characters = generateFontWeights();

            BufferedImage resultImage = new BufferedImage(image.getWidth() * characters.get(0).characterImage.getWidth()/width,
                    image.getHeight() * characters.get(0).characterImage.getHeight()/height, BufferedImage.TYPE_BYTE_GRAY);
            setImage(resultImage);
            Graphics drawing = resultImage.getGraphics();
            drawing.setColor(Color.white);

            for (int j = 0; j < image.getHeight(); j+=height) // ROW
            {
                List<String> RowText = new ArrayList<>();
                for (int i=0; i <image.getWidth(); i+=width) // COLUMN
                {
                    double targetvalue = 0;

                    for (int nj=j; nj<j+height; nj++)
                    {
                        for (int ni = i; ni < i+width; ni++)
                        {
                            // Sum all the pixels in neighbourhood and average
                            try
                            {
                                Color rgb = new Color(image.getRGB(ni, nj));
                                targetvalue += rgb.getRed();
                            }
                            catch (Exception ex)
                            {
                                targetvalue += 128;
                            }
                        }
                    }

                    targetvalue /= (width * height);
                    WeightedChar closestChar = null;
                    Collections.sort(characters);

                    //gets the closest character within the range of contrast/darkness
                    for (WeightedChar character: characters) {

                                if(closestChar == null || Math.abs(character.getWeight() - targetvalue) < Math.abs(closestChar.getWeight() - targetvalue)){
                                    closestChar = character;
                                }
                    }

                    RowText.add(closestChar.character);
                    drawing.drawImage(closestChar.getCharacterImage(), (i/width) * closestChar.getCharacterImage().getWidth(),
                            (j/height) * closestChar.getCharacterImage().getHeight(),null);

                }

                resultText.add(RowText);
            }

            mappedChars = padCharacters();

            for (List<String> rowLines : resultText){
                for (String c : rowLines){
                    sb.append(mappedChars.get(c));
                }
                sb.append("\n");
            }
            drawing.dispose();

            return sb.toString();
        }

        public static void setImage(BufferedImage image){
        outputImage = image;

        }

        public static Dimension getImage(){
        if (outputImage == null){
            return new Dimension(500,0);
        }else{
            int height = 0, width = 500;
            if (outputImage.getWidth() > 1000) width = 1000;
            else if (outputImage.getWidth() < 500) width = 500;
            else width = outputImage.getWidth();

            if (outputImage.getHeight() > 1000) height = 1000;
            else if (outputImage.getHeight() < 500) height = 500;
            else height = outputImage.getHeight();

            return new Dimension(width,height);
            }
        }


    public static class WeightedChar implements Comparable<WeightedChar>{
        public String character;
        public BufferedImage characterImage;
        public double weight;


        public void setCharacter(String character) {
            this.character = character;
        }

        public BufferedImage getCharacterImage() {
            return this.characterImage;
        }

        public void setCharacterImage(BufferedImage characterImage) {
            this.characterImage = characterImage;
        }

        public double getWeight() {
            return this.weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        @Override
        public String toString(){
            return "character: " + this.character + " weight " + this.weight;
        }


        @Override
        public int compareTo(WeightedChar o) {
            return Double.compare(this.weight,o.weight);
        }
    }

    }


