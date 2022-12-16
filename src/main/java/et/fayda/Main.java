package et.fayda;


import et.fayda.DTO.BioMetricsDataDto;
import et.fayda.DTO.CaptureRequestDeviceDetailDto;
import et.fayda.DTO.CaptureRequestDto;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        FaceCapture fc = new FaceCapture();

        byte[] image = null; //byte[] with image data

        fc.getEncryptedFaceData(image, "1231231231");
    }




}