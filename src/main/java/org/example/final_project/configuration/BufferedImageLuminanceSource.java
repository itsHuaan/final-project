package org.example.final_project.configuration;

import com.google.zxing.LuminanceSource;

import java.awt.image.BufferedImage;

public class BufferedImageLuminanceSource extends LuminanceSource {

    private final BufferedImage image;

    public BufferedImageLuminanceSource(BufferedImage image) {
        super(image.getWidth(), image.getHeight());
        this.image = image;
    }

    @Override
    public byte[] getRow(int y, byte[] row) {
        if (row == null || row.length < getWidth()) {
            row = new byte[getWidth()];
        }
        for (int x = 0; x < getWidth(); x++) {
            int pixel = image.getRGB(x, y);
            int gray = (pixel >> 16) & 0xFF;
            row[x] = (byte) gray;
        }
        return row;
    }

    @Override
    public byte[] getMatrix() {
        byte[] matrix = new byte[getWidth() * getHeight()];
        int index = 0;
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                int gray = (pixel >> 16) & 0xFF;
                matrix[index++] = (byte) gray;
            }
        }
        return matrix;
    }
}
