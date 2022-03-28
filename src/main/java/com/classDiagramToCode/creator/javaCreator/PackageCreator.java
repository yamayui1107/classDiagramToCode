package com.classDiagramToCode.creator.javaCreator;

import com.classDiagramToCode.creator.Creator;
import lombok.Builder;

@Builder
public class PackageCreator implements Creator {
    private String packageName;
    private ClassCreator classCreator;

    public static class PackageCreatorBuilder {
        public PackageCreator build(){
            if(packageName == null || packageName.equals("")){
                throw new IllegalArgumentException("アクセス修飾子が指定されていません");
            }
            return new PackageCreator(packageName,classCreator);
        }
    }

    @Override
    public String create() {
        StringBuilder sb = new StringBuilder();

        sb.append("package");
        sb.append(" ");
        sb.append(packageName);
        sb.append(";");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        if (classCreator != null){
            sb.append(classCreator.create());
        }

        return sb.toString();
    }

    public String getPackageName() {
        return packageName;
    }
    public String getClassFileName(){
        return classCreator.getClassName();
    }
}
