package com.classDiagramToCode.classOutput;

import com.classDiagramToCode.creator.javaCreator.PackageCreator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JavaOutput {

    private File outputFolder;
    private List<PackageCreator> packageCreatorList;

    public JavaOutput(File outputFile, List<PackageCreator> packageCreatorList){
        this.outputFolder = outputFile;
        this.packageCreatorList = packageCreatorList;
    }

    public void output(){
        for (PackageCreator packageCreator : packageCreatorList){

            File packageFile = new File(outputFolder + File.separator + packageCreator.getPackageName());

            //パッケージファイル作成
            if (!packageFile.exists()) {
                packageFile.mkdirs();
            }
            String classFileName = packageCreator.getClassFileName();
            if (classFileName == null) return;

            try (FileWriter fileWriter = new FileWriter(packageFile + File.separator + classFileName + ".java")) {
                fileWriter.write(packageCreator.create());
                fileWriter.flush();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
