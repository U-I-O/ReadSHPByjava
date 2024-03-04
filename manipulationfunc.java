package com.ClipByJava;

import java.io.*;
import java.util.*;

public class manipulationfunc {
    public static class ShapefileReader {
        //--shp文件内容--
        static List<Data.Point> points = new ArrayList<>();
        static List<Data.PolyLine> polylines = new ArrayList<>();
        static List<Data.Polygon> polygons = new ArrayList<>();
        static List<Data.MultiPoint> multiPoints = new ArrayList<>();
        static List<Data.NullShape> nullShapes = new ArrayList<>();
//        static List<Data.MultiPatch> multiPatches = new ArrayList<>();

        public static Data.Shapefile readShapefile(String filePath) throws IOException {
            try (RandomAccessFile raf = new RandomAccessFile(filePath, "r")) {
                // Read header
                byte[] headerBytes = new byte[100];
                int bytesRead = raf.read(headerBytes);
                //--shp文件头--
                Data.FileHeader fileHeader = new Data.FileHeader(headerBytes);
                // Set headerBytes and bytesRead to null
                headerBytes = null;
                // Read shapes
                raf.seek(raf.getFilePointer());
                int graNum = 1;
                while (raf.getFilePointer() < fileHeader.fileLength*2) {
                    if(graNum==1465){
                        long local = raf.getFilePointer();
                        raf.seek(raf.getFilePointer()+4);
                        local = raf.getFilePointer();
                        int i =2;
                    }
                    // Read record number (big-endian)
                    int recordNumber = raf.readInt();
                    // Read content length (big-endian)
                    int contentLength = raf.readInt();
                    // Process geometry based on shape type
                    processGeometry(raf, contentLength);
                    // Move to the next record
                    raf.seek(raf.getFilePointer());
                    graNum++;
                }
                return new Data.Shapefile(fileHeader, points, polylines, polygons,multiPoints);
            }
        }

        private static void processGeometry(RandomAccessFile raf, int contentLength) throws IOException {
            // Read shape type
            int shapeType = Integer.reverseBytes(raf.readInt());
            byte[] contentBytes = new byte[contentLength * 2-4];
            int bytesRead = raf.read(contentBytes);
            switch (shapeType) {
                case 0:
                    Data.NullShape nullShape = new Data.NullShape(contentBytes);
                    nullShapes.add(nullShape);
                    break;
                case 1:  // Point
                    Data.Point point = new Data.Point(contentBytes);
                    points.add(point);
                   // System.out.println(point.toString());
                    break;

                case 3:  // Polyline
                    // Read and process polyline coordinates
                    Data.PolyLine polyline = new Data.PolyLine(contentBytes);
                    polylines.add(polyline);
                   // System.out.println(polyline.toString());
                    break;

                case 5:  // Polygon
                    Data.Polygon polygon = new Data.Polygon(contentBytes);
                    polygons.add(polygon);
                    //System.out.println(polygon.toString());
                    break;

                case 8:
                    Data.MultiPoint multiPoint = new Data.MultiPoint(contentBytes);
                    multiPoints.add(multiPoint);
                   // System.out.println(multiPoint.toString());
                    break;
//                case 31:
//                    Data.MultiPatch multiPatch = new Data.MultiPatch(contentBytes);
//                    multiPatches.add(multiPatch);
//                    break;
                default:
                    // Unsupported shape type
                    System.out.println("Unsupported Shape Type: " + shapeType);
                    break;
            }
        }
    }
}
