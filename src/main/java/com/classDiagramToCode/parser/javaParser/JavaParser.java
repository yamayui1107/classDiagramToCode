package com.classDiagramToCode.parser.javaParser;

import com.classDiagramToCode.creator.javaCreator.*;
import com.classDiagramToCode.parser.Parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaParser implements Parser {
    private File inputUmlFile;
    private final Logger logger = Logger.getLogger("createLog");

    public JavaParser(File inputUmlFile){
        this.inputUmlFile = inputUmlFile;
    }

    public List<PackageCreator> parse() {
        if (inputUmlFile == null){
            return null;
        }
        logger.log(Level.INFO, "インプットファイル名 : " + inputUmlFile.toString() );

        String fileContents = readAll(inputUmlFile);

        List<String> packageContents = searchCreator(fileContents,"package");
        List<PackageCreator> packageCreatorList = new ArrayList<>();

        for(String packageContent : packageContents){
            packageCreatorList.addAll(parsePackageCreator(packageContent));
        }

        return packageCreatorList;
    }

    /**
     * 引数で受け取ったパッケージの文字列を受け取ってパッケージクリエイターに変換して返す
     * @param packageContent
     * @return
     */
    private List<PackageCreator> parsePackageCreator(String packageContent){
        //パッケージに存在するクラスの種類を特定
        Set<String> classList = new HashSet<>();//セットを使用している理由はクラスタイプを渡してあげても重複してしまうことがあるからです
        for (ClassType classType : ClassType.values()) {
            //ENUM型は対象外とする
            if (classType == ClassType.ENUM) continue;

            classList.addAll(searchCreator(packageContent,classType.getText()));
        }

        //クラスの数だけパッケージクリエイターを作成
        List<PackageCreator> packageCreatorList = new ArrayList<>();
        for(String classContent : classList){
            //パッケージビルダーを取得
            PackageCreator.PackageCreatorBuilder pcBuilder = PackageCreator.builder();

            //一行目の内容を取得
            String firstLine = packageContent.substring(0, packageContent.indexOf(System.lineSeparator()));
            //一行目の内容からパッケージ名を設定
            pcBuilder.packageName(firstLine.replace(" ","").replace("package","").replace("{",""));

            //クラスクリエイターを設定
            pcBuilder.classCreator(parseClassCreator(classContent));

            //ビルドしパッケージクリエイターリストに追加
            packageCreatorList.add(pcBuilder.build());
        }

        return packageCreatorList;
    }

    private ClassCreator parseClassCreator(String classContent){
        ClassCreator.ClassCreatorBuilder ccBuilder = ClassCreator.builder();

        //一行目の内容を取得
        String firstLine = classContent.substring(0, classContent.indexOf(System.lineSeparator())).trim();

        //一行目の一文字目の内容からアクセス修飾子
        ccBuilder.accessModifier(checkAccessModifier(firstLine));

        //ENUM型は対象外とする
        if (checkClassType(firstLine) == ClassType.ENUM) return null;
        // 一行目の内容からクラスタイプを設定
        ccBuilder.classType(checkClassType(firstLine));

        //一行目の内容からクラス名を設定
        ccBuilder.className(checkClassName(firstLine));

        //一行目の内容から継承しているクラス名を取得
        ccBuilder.extendsClassName(checkExtendsClassName(firstLine));

        //一行目の内容から実装しているインターフェースを取得
        ccBuilder.implementsInterfaceNameList(checkImplementsClassName(firstLine));

        //クラスコンテントの内容からフィールドクリエイターを設定
        ccBuilder.fieldCreatorList(parseFieldCreator(classContent));

        //クラスコンテントの内容からメソッドクリエイターを設定
        ccBuilder.methodCreatorList(parseMethodCreator(classContent));

        return ccBuilder.build();
    }

    private List<FieldCreator> parseFieldCreator(String classContent) {
        //クラスに存在するフィールドのリストを取得
        List<String> fieldList = searchField(classContent);
        //フィールドリストの回数分繰り返し
        List<FieldCreator> fieldCreatorList = new ArrayList<>();
        for (String fieldContent : fieldList){
            //フィールドビルダーを取得
            FieldCreator.FieldCreatorBuilder fcBuilder = FieldCreator.builder();

            //アスセス修飾子を設定
            fcBuilder.accessModifier(checkAccessModifier(fieldContent));

            //変数の型を設定
            fcBuilder.typeName(checkTypeName(fieldContent));

            //変数名を設定
            fcBuilder.memberName(checkMemberName(fieldContent));

            //実装の内容を設定
            fcBuilder.implementedTypeName(checkImplementedTypeName(fieldContent));

            fieldCreatorList.add(fcBuilder.build());
        }

        return fieldCreatorList;
    }

    private List<MethodCreator> parseMethodCreator(String classContent) {
        //クラスに存在するメソッドのリストを取得
        List<String> methodList = searchMethod(classContent);

        //メソッドリストの回数分繰り返し
        List<MethodCreator> methodCreatorList = new ArrayList<>();
        for (String fieldContent : methodList){
            //フィールドビルダーを取得
            MethodCreator.MethodCreatorBuilder mcBuilder = MethodCreator.builder();

            //アスセス修飾子を設定
            mcBuilder.accessModifier(checkAccessModifier(fieldContent));

            //返り値の型を設定
            mcBuilder.returnTypeName(checkReturnTypeName(fieldContent));

            //メソッド名を設定
            mcBuilder.methodName(checkMethodName(fieldContent));

            //引数を設定
            mcBuilder.argumentList(checkArgumentList(fieldContent));

            methodCreatorList.add(mcBuilder.build());
        }

        return methodCreatorList;
    }

    private List<String> checkArgumentList(String fieldContent) {
        List<String> argumentList = new ArrayList<>();

        int argumentPosAt = fieldContent.indexOf("(") + 1;
        int argumentPosEnd = 0;

        //"," で区切られた引数リストが複数あるため繰り返し処理
        while(fieldContent.indexOf(",",argumentPosAt) != -1 || fieldContent.indexOf(")",argumentPosAt) != -1){
            argumentPosEnd = fieldContent.indexOf(",", argumentPosAt);
            if(argumentPosEnd == -1) argumentPosEnd = fieldContent.indexOf(")", argumentPosAt);

            String argumentName = fieldContent.substring(argumentPosAt,argumentPosEnd).trim();
            argumentList.add(argumentName);
            argumentPosAt = argumentPosEnd + 1;
        }

        return argumentList;
    }


    private String checkImplementedTypeName(String fieldContent) {
        int implementedTypeNamePosAt = fieldContent.indexOf("=");
        if (implementedTypeNamePosAt == -1) return null;
        else implementedTypeNamePosAt++;

        int implementedTypeNamePosEnd = fieldContent.length();

        String implementedTypeName = fieldContent.substring(implementedTypeNamePosAt,implementedTypeNamePosEnd).trim();

        return implementedTypeName;
    }


    private List<String> searchCreator(String searchTarget,String creatorKeyword){
        List<String> creatorList = new ArrayList<>();

        //キーワードで渡されたものが作成の中かをフラグで判断
        boolean keywordFlag = false;

        //"{ }"での区切りを確認するためのカウンター
        //キーワードフラグがtrueかつカウンターが0であれば"{ }"の区切りとする
        int parenthesesCount = 0;

        //”{ }”で区切られた文字列を保持する
        StringBuilder parenthesesString = new StringBuilder();

        for(String line : searchTarget.split(System.lineSeparator())) {
            if (line.contains(creatorKeyword) && !keywordFlag) {
                keywordFlag = true;
            }
            if (line.contains("{") && keywordFlag) {
                parenthesesCount++;
            }
            if (line.contains("}") && keywordFlag) {
                parenthesesCount--;
            }
            if (keywordFlag) {
                parenthesesString.append(line);
                parenthesesString.append(System.lineSeparator());
            }
            if (parenthesesCount == 0 && keywordFlag) {
                keywordFlag = false;
                creatorList.add(parenthesesString.toString());
                parenthesesString = new StringBuilder();
            }
        }

        return creatorList;
    }


    /**
     *  受け取ったファイルのパスの内容を文字列で返す
     *
     * @param path
     * @return 読み込んだファイルの内容
     */
    private static String readAll(File path){
        StringBuilder sb = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line = reader.readLine();
            while (line != null){
                sb.append(line + System.getProperty("line.separator"));
                line = reader.readLine();
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return sb.toString();
    }

    private AccessModifier checkAccessModifier(String str) {
        switch (str.substring(0,1)){
            case "+" : return AccessModifier.PUBLIC;
            case "-" : return AccessModifier.PRIVATE;
            case "~" : return AccessModifier.PROTECTED;
            default: return AccessModifier.DEFAULT;
        }
    }

    private ClassType checkClassType(String firstLine) {
        if(firstLine.contains(ClassType.ABSTRACT_CLASS.getText())) return ClassType.ABSTRACT_CLASS;
        if (firstLine.contains(ClassType.ENUM.getText())) return ClassType.ENUM;
        if (firstLine.contains(ClassType.INTERFACE.getText())) return ClassType.INTERFACE;
        if (firstLine.contains(ClassType.CLASS.getText())) return ClassType.CLASS;

        return ClassType.CLASS;
    }

    private String checkClassName(String firstLine) {
        //クラスネームがどこから始まるかを取得する
        //クラスタイプの始まるポジション + クラスタイプの文字数 + 1(空白分)
        int classNamePosAt = firstLine.indexOf(checkClassType(firstLine).getText()) + checkClassType(firstLine).getText().length() + 1;

        //クラスネームがどこで終わるかを取得する
        //始まるポジションから" "までを切り取り
        // 存在しない場合 "{" までの文字列を切り取り
        int classNamePosEnd = firstLine.indexOf(" ", classNamePosAt);
        if(classNamePosEnd == -1) classNamePosEnd = firstLine.indexOf("{", classNamePosAt);

        String className = firstLine.substring(classNamePosAt,classNamePosEnd);

        return className;
    }

    private String checkExtendsClassName(String firstLine) {
        //引数の中に extends という文字列を探す
        int extendsClassnamePosAt = firstLine.indexOf("extends");
        if(extendsClassnamePosAt == -1) return null;

        extendsClassnamePosAt += "extends".length() + 1;

        int extendsClassNamePosEnd = firstLine.indexOf(" ", extendsClassnamePosAt);
        if(extendsClassNamePosEnd == -1) extendsClassNamePosEnd = firstLine.indexOf("{", extendsClassnamePosAt);

        String extendsClassName = firstLine.substring(extendsClassnamePosAt,extendsClassNamePosEnd);

        return extendsClassName;
    }

    private List<String> checkImplementsClassName(String firstLine) {
        List<String> implementsClassNameList = new ArrayList<>();

        //引数の中に implements という文字列を探す
        int implementsClassnamePosAt = firstLine.indexOf("implements");
        if(implementsClassnamePosAt == -1) return null;

        implementsClassnamePosAt += "implements".length() + 1;
        int implementsClassNamePosEnd = 0;

        //"," で区切られた実装リストが複数あるため繰り返し処理
        while(firstLine.indexOf(",",implementsClassnamePosAt) != -1 || firstLine.indexOf("{",implementsClassnamePosAt) != -1){
            implementsClassNamePosEnd = firstLine.indexOf(",", implementsClassnamePosAt);
            if(implementsClassNamePosEnd == -1) implementsClassNamePosEnd = firstLine.indexOf("{", implementsClassnamePosAt);

            String implementsClassName = firstLine.substring(implementsClassnamePosAt,implementsClassNamePosEnd).trim();
            implementsClassNameList.add(implementsClassName);
            implementsClassnamePosAt = implementsClassNamePosEnd + 1;
        }
        return implementsClassNameList;
    }

    private List<String> searchField(String classContent) {
        List<String> fieldList = new ArrayList<>();
        for(String line : classContent.split(System.lineSeparator())) {

            //フィールドの項目でなければコンテニュー
            if (line.trim().equals("")) continue;//空行はフィールド項目でない
            if(line.contains("{")) continue;//クラスコンテントの始まりを表す { はフィールド項目でない
            if(line.contains("}")) continue;//クラスコンテントの終わりを表す } はフィールド項目でない
            if(line.contains("(") && !line.contains("=")) continue;//メソッドを表す()がつき、実装を表す = がついていない物はフィールド項目でない

            //static項目は対象外とする
            if (line.contains("static")) continue;


            //フィールドの項目として登録
            fieldList.add(line.trim());
        }
        return fieldList;
    }

    private String checkTypeName(String fieldContent) {
        //アクセス修飾子がデフォルトでなければスタート位置は 1
        int typeNamePosAt = 0;
        if (checkAccessModifier(fieldContent) != AccessModifier.DEFAULT) typeNamePosAt++;

        //終了位置は開始から次の空白まで
        int typeNamePosEnd = fieldContent.indexOf(" ",typeNamePosAt);

        String typeName = fieldContent.substring(typeNamePosAt, typeNamePosEnd);
        return typeName;
    }

    private String checkMemberName(String fieldContent) {
        int memberNamePosAt = fieldContent.indexOf(checkTypeName(fieldContent)) + checkTypeName(fieldContent).length() + 1;
        int memberNamePosEnd = fieldContent.indexOf(" ",memberNamePosAt);
        if (memberNamePosEnd == -1) memberNamePosEnd = fieldContent.indexOf(";");
        if (memberNamePosEnd == -1) memberNamePosEnd = fieldContent.length();

        String memberName = fieldContent.substring(memberNamePosAt,memberNamePosEnd);

        return memberName;
    }

    private List<String> searchMethod(String classContent) {
        List<String> methodList = new ArrayList<>();
        for(String line : classContent.split(System.lineSeparator())) {

            //フィールドの項目でなければコンテニュー
            if (line.equals("")) continue;//空行はメソッドでない
            if(line.contains("{")) continue;//クラスコンテントの始まりを表す { はメソッドでない
            if(line.contains("}")) continue;//クラスコンテントの終わりを表す } はメソッドでない
            if(!line.contains("(")) continue;//メソッドを表す () がつかない物はメソッドでない
            if(line.contains("(") && line.contains("=")) continue;//メソッドを表す()がつき、実装を表す = がついている物はメソッドでない

            //staticメソッドは対象外とする
            if (line.contains("static")) continue;

            //フィールドの項目として登録
            methodList.add(line.trim());
        }
        return methodList;
    }

    private String checkReturnTypeName(String fieldContent) {
        //アクセス修飾子がデフォルトでなければスタート位置は 1
        int returnTypeNamePosAt = 0;
        if (checkAccessModifier(fieldContent) != AccessModifier.DEFAULT) returnTypeNamePosAt++;

        //終了位置は開始から次の空白まで
        int returnTypeNamePosEnd = fieldContent.indexOf(" ",returnTypeNamePosAt);

        String returnTypeName = fieldContent.substring(returnTypeNamePosAt, returnTypeNamePosEnd);
        return returnTypeName;
    }

    private String checkMethodName(String fieldContent) {
        int methodNamePosAt = fieldContent.indexOf(checkReturnTypeName(fieldContent)) + checkReturnTypeName(fieldContent).length() + 1;
        int methodNamePosEnd = fieldContent.indexOf("(",methodNamePosAt);

        String methodName = fieldContent.substring(methodNamePosAt,methodNamePosEnd);

        return methodName;
    }

}
