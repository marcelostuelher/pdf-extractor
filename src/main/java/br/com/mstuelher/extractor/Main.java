package br.com.mstuelher.extractor;

import br.com.mstuelher.extractor.model.CommonParameter;
import br.com.mstuelher.extractor.model.MusclePerformanceComparison;
import br.com.mstuelher.extractor.model.TestType;
import br.com.mstuelher.extractor.model.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {


    private static final String EXTENSAO_IMAGEM = "png";
    private static final String OUTPUT_PATH = System.getProperty("user.home").concat("\\").concat("biodex");
    private static final int INDEX_PATH_HIB_IMG = 0;
    private static final int INDEX_PATH_KNEE_IMG = 1;
    private static final int INDEX_PATH_ANKLE_IMG = 2;


    private static final String HIP_DOC_ID = "Hip";
    private static final String KNEE_DOC_ID = "Knee";
    private static final String ANKLEE_DOC_ID = "Ankle";

    public static void main(String[] args) {
        try {
            System.out.println(System.getProperty("user.home"));
            List<String> pdfPaths = new ArrayList<>();
            pdfPaths.add(args[0]);
            pdfPaths.add(args[1]);
            pdfPaths.add(args[2]);

            String name = args.length >= 4 ? args[3].replace("%20", " ") : "";
            String dtNasc = args.length >= 5 ? args[4].replace("%20", " ") : "";
            String stature = args.length >= 6 ? args[5].replace("%20", " ") : "";
            String weight = args.length >= 7 ? args[6].replace("%20", " ") : "";
            String gender = args.length >= 8 ? args[7].replace("%20", " ") : "";

            UserInfo userInfo = new UserInfo(name, dtNasc, stature, weight, gender);

            List<PDDocument> docs = new ArrayList<>();

            List<String> imgPaths = new ArrayList<>();

            for(PDDocument doc: loadDocs(pdfPaths)) {
                if (getTextFromFile(doc).stream().anyMatch(s -> s.equalsIgnoreCase(HIP_DOC_ID))) {
                    docs.add(0, doc);
                }
                if (getTextFromFile(doc).stream().anyMatch(s -> s.equalsIgnoreCase(KNEE_DOC_ID))) {
                    docs.add(1, doc);
                };

                if (getTextFromFile(doc).stream().anyMatch(s -> s.equalsIgnoreCase(ANKLEE_DOC_ID))) {
                    docs.add(2, doc);
                };
            }


            for (PDDocument doc: docs) {
                imgPaths.add(getImagesFromPDF(doc));
            }
            List<String> filteredList = getTextFromFiles(docs);
            generateDocx(filteredList, imgPaths, userInfo);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getImagesFromPDF(PDDocument document) throws IOException {
        List<RenderedImage> images = new ArrayList<>();
        String path = null;
        for (PDPage page : document.getPages()) {
            images.addAll(getImages(page.getResources()));
        }
        for (RenderedImage image: images) {
            UUID uuid = UUID.randomUUID();
            path = OUTPUT_PATH.concat("\\")
                    .concat(uuid + ".")
                    .concat(EXTENSAO_IMAGEM);
            ImageIO.write(image, EXTENSAO_IMAGEM.toUpperCase(Locale.ROOT), new File(path));
        }
        return path;
    }


    private static void generateDocx(List<String> list,  List<String> imgPaths, UserInfo userInfo) throws Exception {
        MusclePerformanceComparison dto = new MusclePerformanceComparison();
        dto.setHipAbductors(new CommonParameter(list, TestType.HIP_ABDUCTORS));
        dto.setHipAdductors(new CommonParameter(list, TestType.HIP_ADDUCTORS));
        dto.setKneeFlexors(new CommonParameter(list, TestType.KNEE_FLEXORS));
        dto.setKneeExtenders(new CommonParameter(list, TestType.KNEE_EXTENDERS));
        dto.setAnkleEversors(new CommonParameter(list, TestType.ANKLE_EVERSORS));
        dto.setAnkleInverters(new CommonParameter(list, TestType.ANKLE_INVERTERS));

        XWPFDocument doc = new XWPFDocument(OPCPackage.open(OUTPUT_PATH.concat("\\biodex.docx")));
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String strDate = dateFormat.format(date);
        for (XWPFParagraph p : doc.getParagraphs()) {
            List<XWPFRun> runs = p.getRuns();
            if (runs != null) {
                for (XWPFRun r : runs) {
                    String text = r.getText(0);
                    if (text != null) {
                        text = text.replace("{nome}", userInfo.getName());
                        text = text.replace("{dtNascimento}", userInfo.getDtNasc());
                        text = text.replace("{sexo}", userInfo.getGender());
                        text = text.replace("{altura}", userInfo.getStature());
                        text = text.replace("{peso}", userInfo.getWeight());
                        text = text.replace("{dtAvaliacao}", strDate);
                        r.setText(text, 0);
                    }
                }
            }
        }

        for (XWPFTable tbl : doc.getTables()) {
            for (XWPFTableRow row : tbl.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        for (XWPFRun r : p.getRuns()) {
                            String text = r.text();
                            if (text != null) {

                                text = text.replace("{1}", dto.getHipAbductors().getRightStrength());
                                text = text.replace("{2}", dto.getHipAbductors().getLeftStrength());
                                text = text.replace("{3}", dto.getHipAbductors().getDifferenceStrength());

                                text = text.replace("{4}", dto.getHipAbductors().getRightWork());
                                text = text.replace("{5}", dto.getHipAbductors().getLeftWork());
                                text = text.replace("{6}", dto.getHipAbductors().getDifferenceWork());

                                text = text.replace("{7}", dto.getHipAbductors().getRightForce());
                                text = text.replace("{8}", dto.getHipAbductors().getLeftForce());
                                text = text.replace("{9}", dto.getHipAbductors().getDifferenceForce());

                                text = text.replace("{10}", dto.getHipAdductors().getRightStrength());
                                text = text.replace("{11}", dto.getHipAdductors().getLeftStrength());
                                text = text.replace("{12}", dto.getHipAdductors().getDifferenceStrength());

                                text = text.replace("{13}", dto.getHipAdductors().getRightWork());
                                text = text.replace("{14}", dto.getHipAdductors().getLeftWork());
                                text = text.replace("{15}", dto.getHipAdductors().getDifferenceWork());

                                text = text.replace("{16}", dto.getHipAdductors().getRightForce());
                                text = text.replace("{17}", dto.getHipAdductors().getLeftForce());
                                text = text.replace("{18}", dto.getHipAdductors().getDifferenceForce());

                                text = text.replace("{19}", dto.getKneeExtenders().getRightStrength());
                                text = text.replace("{20}", dto.getKneeExtenders().getLeftStrength());
                                text = text.replace("{21}", dto.getKneeExtenders().getDifferenceStrength());

                                text = text.replace("{22}", dto.getKneeExtenders().getRightWork());
                                text = text.replace("{23}", dto.getKneeExtenders().getLeftWork());
                                text = text.replace("{24}", dto.getKneeExtenders().getDifferenceWork());

                                text = text.replace("{25}", dto.getKneeExtenders().getRightForce());
                                text = text.replace("{26}", dto.getKneeExtenders().getLeftForce());
                                text = text.replace("{27}", dto.getKneeExtenders().getDifferenceForce());

                                text = text.replace("{28}", dto.getKneeFlexors().getRightStrength());
                                text = text.replace("{29}", dto.getKneeFlexors().getLeftStrength());
                                text = text.replace("{30}", dto.getKneeFlexors().getDifferenceStrength());

                                text = text.replace("{31}", dto.getKneeFlexors().getRightWork());
                                text = text.replace("{32}", dto.getKneeFlexors().getLeftWork());
                                text = text.replace("{33}", dto.getKneeFlexors().getDifferenceWork());

                                text = text.replace("{34}", dto.getKneeFlexors().getRightForce());
                                text = text.replace("{35}", dto.getKneeFlexors().getLeftForce());
                                text = text.replace("{36}", dto.getKneeFlexors().getDifferenceForce());

                                text = text.replace("{37}", dto.getAnkleEversors().getRightStrength());
                                text = text.replace("{38}", dto.getAnkleEversors().getLeftStrength());
                                text = text.replace("{39}", dto.getAnkleEversors().getDifferenceStrength());

                                text = text.replace("{40}", dto.getAnkleEversors().getRightWork());
                                text = text.replace("{41}", dto.getAnkleEversors().getLeftWork());
                                text = text.replace("{42}", dto.getAnkleEversors().getDifferenceWork());

                                text = text.replace("{43}", dto.getAnkleEversors().getRightForce());
                                text = text.replace("{44}", dto.getAnkleEversors().getLeftForce());
                                text = text.replace("{45}", dto.getAnkleEversors().getDifferenceForce());

                                text = text.replace("{46}", dto.getAnkleInverters().getRightStrength());
                                text = text.replace("{47}", dto.getAnkleInverters().getLeftStrength());
                                text = text.replace("{48}", dto.getAnkleEversors().getDifferenceStrength());

                                text = text.replace("{49}", dto.getAnkleInverters().getRightWork());
                                text = text.replace("{50}", dto.getAnkleInverters().getLeftWork());
                                text = text.replace("{51}", dto.getAnkleInverters().getDifferenceWork());

                                text = text.replace("{52}", dto.getAnkleInverters().getRightForce());
                                text = text.replace("{53}", dto.getAnkleInverters().getLeftForce());
                                text = text.replace("{54}", dto.getAnkleInverters().getDifferenceForce());
                                if (text.contains("+")) {
                                    text = text.replace("+", "");
                                    r.setColor("F62E0F");
                                }

                                if (text.contains("-")) {
                                    text = text.replace("-", "");
                                    r.setColor("0FF61E");
                                }
                                r.setText(text,0);
                            }
                        }
                    }
                }
            }
        }

        insertImages(doc, imgPaths.get(INDEX_PATH_HIB_IMG), imgPaths.get(INDEX_PATH_KNEE_IMG), imgPaths.get(INDEX_PATH_ANKLE_IMG));
        UUID uuid = UUID.randomUUID();
        String fileName = uuid.toString().concat(".docx");
        doc.write(new FileOutputStream(OUTPUT_PATH.concat("\\").concat(fileName)));
        Runtime.getRuntime().exec("cmd /c start ".concat(OUTPUT_PATH).concat("\\").concat(fileName));
    }


    private static void insertImages(XWPFDocument doc, String hipGraphicsPath, String kneeGraphicsPath, String ankleGraphicsPath) throws IOException, InvalidFormatException {
        List<XWPFParagraph> paragraphs = doc.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            String text = paragraph.getText();
            if(text.contains("&")){
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    String hipGraphics = "&{{hipGraphics}}";
                    String kneeGraphics = "&{{kneesGraphics}}";
                    String ankleGraphics = "&{{ankleGraphics}}";
                    String item=run.toString();

                    if (item.contains(hipGraphics)) {
                        run.setText("",0);
                        FileInputStream in = new FileInputStream(hipGraphicsPath);
                        File file = new File(hipGraphicsPath);
                        run.addPicture(in, XWPFDocument.PICTURE_TYPE_PNG, file.getName(), Units.toEMU(510), Units.toEMU(190));
                        in.close();
                    }
                    if (item.contains(kneeGraphics)) {
                        run.setText("",0);
                        FileInputStream in = new FileInputStream(kneeGraphicsPath);
                        File file = new File(kneeGraphicsPath);
                        run.addPicture(in, XWPFDocument.PICTURE_TYPE_PNG, file.getName(), Units.toEMU(510), Units.toEMU(190));
                        in.close();
                    }
                    if (item.contains(ankleGraphics)) {
                        run.setText("",0);
                        FileInputStream in = new FileInputStream(ankleGraphicsPath);
                        File file = new File(ankleGraphicsPath);
                        run.addPicture(in, XWPFDocument.PICTURE_TYPE_PNG, file.getName(), Units.toEMU(510), Units.toEMU(190));
                        in.close();
                    }
                }
            }
        }
    }


    private static List<RenderedImage> getImages(PDResources resources) throws IOException {
        List<RenderedImage> imgs = new ArrayList<>();

        for (COSName xObjectName : resources.getXObjectNames()) {
            PDXObject xObject = resources.getXObject(xObjectName);

            if (xObject instanceof PDFormXObject) {
                imgs.addAll(getImages(((PDFormXObject) xObject).getResources()));
            } else if (xObject instanceof PDImageXObject) {
                imgs.add(((PDImageXObject) xObject).getImage());
            }
        }
        return imgs;
    }


    private static List<PDDocument> loadDocs(List<String> paths) {
        List<PDDocument> docs = new ArrayList<>();
        paths.forEach(p -> {
            try {
                docs.add(PDDocument.load(new File(p)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return docs;
    }

    private static List<String> getTextFromFiles(List<PDDocument> docs) {
        List<String> list = new ArrayList<>();
        docs.forEach(d -> {
            try {
                list.addAll(Arrays.asList(new PDFTextStripper()
                        .getText(d)
                        .split("\\r|\\n|\\s+")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        List<String> filteredList = new ArrayList<>();

        list.stream().forEach(s ->  {
            if (NumberUtils.isNumber(s)) {
                filteredList.add(s);
            }
        });

        return filteredList;
    }


    private static List<String> getTextFromFile(PDDocument doc) {
        List<String> list = new ArrayList<>();
        try {
            list.addAll(Arrays.asList(new PDFTextStripper()
                    .getText(doc)
                    .split("\\r|\\n|\\s+")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}
