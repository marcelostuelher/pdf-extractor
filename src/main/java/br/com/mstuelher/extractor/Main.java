package br.com.mstuelher.extractor;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Main {


    private static final String EXTENSAO_IMAGEM = "png";

    public static void main(String[] args) {
        try {
            String pdfPath = args[0];
            String outputFolder = args[1];
            PDDocument doc = PDDocument.load(new File(pdfPath));
            getImagesFromPDF(doc, outputFolder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getImagesFromPDF(PDDocument document, String outputPath) throws IOException {
        List<RenderedImage> images = new ArrayList<>();
        for (PDPage page : document.getPages()) {
            images.addAll(getImagesFromResources(page.getResources()));
        }
        for (RenderedImage image: images) {
            UUID uuid = UUID.randomUUID();
            String path = outputPath.concat("/")
                    .concat(uuid + ".")
                    .concat(EXTENSAO_IMAGEM);
            ImageIO.write(image, EXTENSAO_IMAGEM.toUpperCase(Locale.ROOT), new File(path));
        }
    }


    private static List<RenderedImage> getImagesFromResources(PDResources resources) throws IOException {
        List<RenderedImage> images = new ArrayList<>();

        for (COSName xObjectName : resources.getXObjectNames()) {
            PDXObject xObject = resources.getXObject(xObjectName);

            if (xObject instanceof PDFormXObject) {
                images.addAll(getImagesFromResources(((PDFormXObject) xObject).getResources()));
            } else if (xObject instanceof PDImageXObject) {
                images.add(((PDImageXObject) xObject).getImage());
            }
        }

        return images;
    }
}
