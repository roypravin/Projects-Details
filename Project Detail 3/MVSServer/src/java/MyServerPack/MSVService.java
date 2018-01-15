/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MyServerPack;

import LibPack.SingleImage;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Vector;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author Shree
 */
@WebService(serviceName = "MSVService")
public class MSVService {

    /**
     * This is a sample web service operation
     */
    SingleImage si;
    int col, r, g, b, rgbMin = 0;
    int rgbMax = 0, v_hsv = 0, h_hsv = 0, s_hsv = 0, inpixel[][];
    Vector<SingleImage> all_features, input_image, return_images;

    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";


    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "receiveimagerequest")
    public String receiveimagerequest(@WebParam(name = "fromClient") String fromclient) {
        // return_images=new Vector<SingleImage>();
        //TODO write your implementation code here:
        SingleImage singleImage = new SingleImage();
        singleImage = (SingleImage) stringToObject(fromclient);
        BufferedImage bmp = new BufferedImage(singleImage.width, singleImage.height, BufferedImage.TYPE_INT_RGB);

        bmp.setRGB(0, 0, singleImage.width, singleImage.height, singleImage.img, 0, singleImage.width);
        inpixel = new int[640][480];

        BufferedImage imgResized = resizeImage(bmp, 640, 480);
        singleImage.img = new int[640 * 480];
        singleImage.img = imgResized.getRGB(0, 0, imgResized.getWidth(), imgResized.getHeight(), singleImage.img, 0, imgResized.getWidth());
        for (int i = 0; i < 480; i++) {
            for (int j = 0; j < 640; j++) {
                inpixel[j][i] = imgResized.getRGB(j, i) & 0xffffff;
            }
        }
        obtainHistogram(singleImage);
        return (objectToString(return_images));
    }

    public int[][] monoToBidi(final int[] array, final int rows, final int cols) {
        if (array.length != (rows * cols)) {
            throw new IllegalArgumentException("Invalid array length");
        }
        int[][] bidi = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(array, (i * cols), bidi[i], 0, cols);
        }
        return bidi;
    }

    public void obtainHistogram(SingleImage singleImage) {
        // inpixel = new int[640][480];
        int count = 0;
        for (int y = 0; y < 480; y++) {
            for (int x = 0; x < 640; x++) {
                col = inpixel[x][y];

                r = (col >> 16) & 0xFF;
                g = (col >> 8) & 0xFF;
                b = (col >> 0) & 0xFF;

                // hsv conversion
                rgbMin = Math.min(Math.min(r, g), b);
                rgbMax = Math.max(Math.max(r, g), b);
                v_hsv = rgbMax;
                if (v_hsv == 0) {
                    h_hsv = s_hsv = 0;
                } else {
                    s_hsv = 255 * (rgbMax - rgbMin) / v_hsv;
                    if (s_hsv == 0) {
                        h_hsv = 0;
                    } else {
                        if (rgbMax == r) {
                            h_hsv = 0 + 43 * (g - b) / (rgbMax - rgbMin);
                        } else if (rgbMax == g) {
                            h_hsv = 85 + 43 * (b - r) / (rgbMax - rgbMin);
                        } else if (rgbMax == b) {
                            h_hsv = 171 + 43 * (r - g) / (rgbMax - rgbMin);
                        }
                    }
                }

                if (h_hsv < 0) {
                    h_hsv = 255 + h_hsv;
                }

                singleImage.image_features[0][(h_hsv / 8)]++;
                singleImage.image_features[1][(s_hsv / 8)]++;
                if (count < 15) {
                    System.out.println(singleImage.image_features[0][(h_hsv / 8)]);
                    System.out.println(singleImage.image_features[0][(s_hsv / 8)]);
                    count++;
                }
            }
        }
        processRequest(singleImage);
    }

    public void read_features() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(Settings.databasepath + "\\features_database.dat"));
            all_features = (Vector<SingleImage>) in.readObject();
            in.close();
            for (int i = 0; i < all_features.size(); i++) {
                BufferedImage bi = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
                bi.setRGB(0, 0, 640, 480, all_features.get(i).img, 0, 640);
            }
        } catch (Exception e) {
            all_features = new Vector<SingleImage>();
            e.printStackTrace();
        }
    }

    BufferedImage resizeImage(BufferedImage image, int width, int height) {
        int type = image.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : image.getType();
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.setComposite(AlphaComposite.Src);

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
        //  System.out.println("RESIZEING IMAGE.....");
        return resizedImage;
    }

    public void processRequest(SingleImage singleImage) {
        Vector<Integer> template = new Vector<Integer>();
        read_features();
        int name = 0;
        String str = "";
        //  System.out.println(str);

        BufferedImage biFinal = null;
        for (int ivs = 0; ivs < all_features.size(); ivs++) {
            int singleError = 0;
            for (int imv = 0; imv < 32; imv++) {
                double h1 = all_features.get(ivs).image_features[0][imv];
                double s1 = all_features.get(ivs).image_features[1][imv];
                double h2 = singleImage.image_features[0][imv];
                double s2 = singleImage.image_features[1][imv];
                int res = (int) Math.pow((h1 - h2), 2);
                int res1 = (int) Math.pow((s1 - s2), 2);
                int sum = (int) Math.sqrt(res + res1);
                singleError += sum;
            }
            System.out.println("Error Computed for " + ivs + ": " + singleError);
            template.add(singleError);
        }
        return_images = new Vector<SingleImage>();
        for (int il = 0; il < template.size(); il++) {
            if (template.elementAt(il) < 150000.0000) {
                return_images.add(all_features.get(il));
            }
        }
        System.out.println("Size of return_images:" + return_images.size());
    }

    public String objectToString(Object obj) {
        byte[] b = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            b = bos.toByteArray();
        } catch (Exception e) {
            System.out.println("NOT SERIALIZABLE: " + e);
        }
        return Base64.encode(b);
    }

    Object stringToObject(String inp) {
        byte b[] = Base64.decode(inp);
        Object ret = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(b);
            ObjectInput in = new ObjectInputStream(bis);
            ret = (Object) in.readObject();
            bis.close();
            in.close();
        } catch (Exception e) {
            System.out.println("NOT DE-SERIALIZABLE: " + e);
        }
        return ret;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "voicesearch")
    public String voicesearch(@WebParam(name = "fromClient") String fromClient) {
        //TODO write your implementation code here:
        return_images = new Vector<SingleImage>();
        System.out.println("TAG of Input Image:" + fromClient);
        read_features();
        Stemmer ss = new Stemmer();
        String input_Query_Stem = ss.StemmerResult((fromClient + "."));
        input_Query_Stem = input_Query_Stem.substring(0, (input_Query_Stem.length() - 1));
        System.out.println("Stemmed Input Query: " + input_Query_Stem);
        for (int i = 0; i < all_features.size(); i++) {
            String compare_With_Stem = ss.StemmerResult((all_features.elementAt(i).img_tag + "."));
            compare_With_Stem = compare_With_Stem.substring(0, (compare_With_Stem.length() - 1));
            if (compare_With_Stem.contains(input_Query_Stem)) {
                return_images.add(all_features.get(i));
            }
        }
        System.out.println("Size of return images: " + return_images.size());
        return (objectToString(return_images));
    }

    @WebMethod(operationName = "textsearch")
    public String textsearch(@WebParam(name = "fromClient") String fromClient) {
        //TODO write your implementation code here:
        return_images = new Vector<SingleImage>();
        System.out.println("TAG of Input Image:" + fromClient);
        read_features();
        Stemmer ss = new Stemmer();
        String input_Query_Stem = ss.StemmerResult((fromClient + "."));
        input_Query_Stem = input_Query_Stem.substring(0, (input_Query_Stem.length() - 1));
        System.out.println("Stemmed Input Query: " + input_Query_Stem);
        for (int i = 0; i < all_features.size(); i++) {
            String compare_With_Stem = ss.StemmerResult((all_features.elementAt(i).img_tag + "."));
            compare_With_Stem = compare_With_Stem.substring(0, (compare_With_Stem.length() - 1));
            if (compare_With_Stem.contains(input_Query_Stem)) {
                return_images.add(all_features.get(i));
            }
        }
        System.out.println("Size of return images: " + return_images.size());
        return (objectToString(return_images));
    }
}
