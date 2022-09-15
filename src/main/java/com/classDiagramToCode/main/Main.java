package com.classDiagramToCode.main;

import com.classDiagramToCode.classOutput.JavaOutput;
import com.classDiagramToCode.creator.javaCreator.PackageCreator;
import com.classDiagramToCode.parser.javaParser.JavaParser;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if(args.length != 2) throw new IllegalArgumentException();

        //コマンドライン引数から出力対象のプラントUMLのファイルと出力先のフォルダーを受け取る
        File inputFile = new File(args[0]);
        File outputFolder = new File(args[1]);

        //クラス図の内容を解析し、出力する内容を取得する
        JavaParser javaParser = new JavaParser(inputFile);
        List<PackageCreator> packageCreatorList = javaParser.parse();

        //出力先のフォルダーに出力
        JavaOutput javaOutput = new JavaOutput(outputFolder, packageCreatorList);
        javaOutput.output();
    }
}
