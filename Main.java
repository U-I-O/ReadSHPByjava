package com.ClipByJava;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            //多边形
            Data.Shapefile shp = manipulationfunc.ShapefileReader.readShapefile("D:\\Desktop\\时空大数据\\Shape文件资料及图形数据20220402\\Shape文件资料及图形数据20220402\\ocldnta.shp");
            //点
            Data.Shapefile shp1 = manipulationfunc.ShapefileReader.readShapefile("D:\\Desktop\\时空大数据\\Shape文件资料及图形数据20220402\\Shape文件资料及图形数据20220402\\soudpt.shp");
            //线
            //Data.Shapefile shp = manipulationfunc.ShapefileReader.readShapefile("D:\\Desktop\\时空大数据\\Shape文件资料及图形数据20220402\\Shape文件资料及图形数据20220402\\trsppl.shp");
            shp.points=shp1.points;
            paint.Drawer(shp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}