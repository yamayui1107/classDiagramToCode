package com.classDiagramToCode.classOutput;

import com.classDiagramToCode.creator.javaCreator.PackageCreator;
import com.classDiagramToCode.parser.javaParser.JavaParser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

class JavaOutputTest {

    @Test
    void 正常にファイル出力ができる() {
        //ローカル環境のファイルを指定して実行してください
        File inputFile = new File("");
        File outputFolder = new File("");
        JavaParser javaParser = new JavaParser(inputFile);


        List<PackageCreator> packageCreatorList = javaParser.parse();

        JavaOutput.output(outputFolder,packageCreatorList);

    }
}