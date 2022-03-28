package com.classDiagramToCode.creator.javaCreator;

import com.classDiagramToCode.creator.Creator;
import lombok.Builder;

import java.util.List;

@Builder
public class ClassCreator implements Creator {
    private AccessModifier accessModifier;
    private ClassType classType;
    private String className;
    private String extendsClassName;
    private List<String> implementsInterfaceNameList;
    private List<FieldCreator> fieldCreatorList;
    private List<MethodCreator> methodCreatorList;

    public static class ClassCreatorBuilder {
        public ClassCreator build(){
            if(accessModifier == null){
                throw new IllegalArgumentException("アクセス修飾子が指定されていません");
            }
            if(classType == null) {
                throw new IllegalArgumentException("クラスタイプが指定されていません");
            }
            if(className == null || className.equals("")){
                throw new IllegalArgumentException("クラス名が不明です");
            }

            return new ClassCreator(accessModifier,classType,className,extendsClassName,implementsInterfaceNameList,fieldCreatorList,methodCreatorList);
        }
    }

    @Override
    public String create() {

        StringBuilder sb = new StringBuilder();

        if(accessModifier != AccessModifier.DEFAULT){
            sb.append(accessModifier.getText());
            sb.append(" ");
        }
        sb.append(classType.getText());
        sb.append(" ");
        sb.append(className);
        if(extendsClassName != null && !extendsClassName.equals("")){
            sb.append(" ");
            sb.append("extends");
            sb.append(" ");
            sb.append(extendsClassName);
            sb.append(" ");
        }
        if(implementsInterfaceNameList != null && !implementsInterfaceNameList.isEmpty()){
            sb.append(" ");
            sb.append("implements");
            sb.append(" ");
            sb.append(String.join(", ",implementsInterfaceNameList));
            sb.append(" ");
        }
        sb.append("{");
        sb.append(System.lineSeparator());
        if(fieldCreatorList != null){
            for(FieldCreator fieldCreator : fieldCreatorList){
                if (fieldCreator == null) continue;
                sb.append(fieldCreator.create());
                sb.append(System.lineSeparator());
            }
            sb.append(System.lineSeparator());
        }
        if (methodCreatorList != null){
            for (MethodCreator methodCreator : methodCreatorList){
                if (methodCreator == null) continue;
                sb.append(methodCreator.create(classType == ClassType.INTERFACE));
                sb.append(System.lineSeparator());
            }
        }
        sb.append("}");

        return sb.toString();
    }

    String getClassName(){
        return className;
    }
}
















