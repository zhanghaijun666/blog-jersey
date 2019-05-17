package com.util.word;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFStyles;

/**
 * @author zhanghaijun
 */
public class WordSplit {

    private static final String ORIGIN_FILE_PATH = "/home/zhanghaijun/00word-demo/云盘安装信息.docx";
    private static final String SPLIT_FOLDER_PATH = "/home/zhanghaijun/00word-demo/split/";

    public static void main(String[] args) throws IOException {
        File originFile = new File(ORIGIN_FILE_PATH);
        if (!originFile.exists()) {
            return;
        }
        File splitPath = new File(SPLIT_FOLDER_PATH);
        FileUtil.DeleteFile(splitPath);
        splitPath.mkdirs();

        OPCPackage pack = POIXMLDocument.openPackage(ORIGIN_FILE_PATH);
        XWPFDocument document = new XWPFDocument(pack);
        List<IBodyElement> elements = document.getBodyElements();
        XWPFStyles styles = document.getStyles();
        for (int i = 0; i < elements.size(); i++) {
            IBodyElement element = elements.get(i);
            if (element.getElementType() == BodyElementType.PARAGRAPH) {

            }
        }
    }
}
