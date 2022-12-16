package et.fayda;


import com.fasterxml.jackson.core.JsonProcessingException;
import et.fayda.DTO.BioMetricsDataDto;
import et.fayda.DTO.CaptureRequestDeviceDetailDto;
import et.fayda.DTO.CaptureRequestDto;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        FaceCapture fc = new FaceCapture();

        byte[] image = null;

        try {
            image = extractBytes("img.png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(image.length);

        try {
            fc.getEncryptedFaceData(image, "1231231231");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] extractBytes (String ImageName) throws IOException {
        // open image
        File imgPath = new File(ImageName);
        BufferedImage bufferedImage = ImageIO.read(imgPath);

        // get DataBufferBytes from Raster
        WritableRaster raster = bufferedImage .getRaster();
        DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();

        return ( data.getData() );
    }





}