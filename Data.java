package com.ClipByJava;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class Data {
    public static class NullShape {
        public int shapeType;

        public NullShape(byte[] nullShapeBytes) {
            ByteBuffer buffer = ByteBuffer.wrap(nullShapeBytes);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            this.shapeType = buffer.getInt();
        }
    }
    public static class Point {
        public int shapeType;
        public double x;
        public double y;

        public Point(byte[] pointBytes) {
            ByteBuffer buffer = ByteBuffer.wrap(pointBytes);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            this.shapeType = 1;
            this.x = buffer.getDouble();
            this.y = buffer.getDouble();
        }

        @Override
        public String toString() {
            return "Point{" +
                    "shapeType=" + shapeType +
                    ", x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
    public static class MultiPoint {
        public int shapeType;
        public double boxMinX;
        public double boxMinY;
        public double boxMaxX;
        public double boxMaxY;
        public int numPoints;
        public List<Point> points;

        public MultiPoint(byte[] multiPointBytes) {
            ByteBuffer buffer = ByteBuffer.wrap(multiPointBytes);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            this.shapeType = 8;
            this.boxMinX = buffer.getDouble();
            this.boxMinY = buffer.getDouble();
            this.boxMaxX = buffer.getDouble();
            this.boxMaxY = buffer.getDouble();
            this.numPoints = buffer.getInt();

            // Read Points
            this.points = new ArrayList<>();
            for (int i = 0; i < numPoints; i++) {
                byte[] pointBytes = new byte[16]; // Assuming each Point is 16 bytes
                buffer.get(pointBytes);
                this.points.add(new Point(pointBytes));
            }
        }
        @Override
        public String toString() {
            return "MultiPoint{" +
                    "shapeType=" + shapeType +
                    ", boxMinX=" + boxMinX +
                    ", boxMinY=" + boxMinY +
                    ", boxMaxX=" + boxMaxX +
                    ", boxMaxY=" + boxMaxY +
                    ", numPoints=" + numPoints +
                    ", points=" + points +
                    '}';
        }
    }
    public static class PolyLine {
        public int shapeType;
        public double boxMinX;
        public double boxMinY;
        public double boxMaxX;
        public double boxMaxY;
        public int numParts;
        public int numPoints;
        public List<Integer> parts;
        public List<Point> points;

        public PolyLine(byte[] polyLineBytes) {
            ByteBuffer buffer = ByteBuffer.wrap(polyLineBytes);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            this.shapeType = 3;
            this.boxMinX = buffer.getDouble();
            this.boxMinY = buffer.getDouble();
            this.boxMaxX = buffer.getDouble();
            this.boxMaxY = buffer.getDouble();
            this.numParts = buffer.getInt();
            this.numPoints = buffer.getInt();

            // Read Parts
            this.parts = new ArrayList<>();
            for (int i = 0; i < numParts; i++) {
                this.parts.add(buffer.getInt());
            }

            // Read Points
            this.points = new ArrayList<>();
            for (int i = 0; i < numPoints; i++) {
                byte[] pointBytes = new byte[16]; // Assuming each Point is 16 bytes
                buffer.get(pointBytes);
                this.points.add(new Point(pointBytes));
            }
        }
        @Override
        public String toString() {
            return "PolyLine{" +
                    "shapeType=" + shapeType +
                    ", boxMinX=" + boxMinX +
                    ", boxMinY=" + boxMinY +
                    ", boxMaxX=" + boxMaxX +
                    ", boxMaxY=" + boxMaxY +
                    ", numParts=" + numParts +
                    ", numPoints=" + numPoints +
                    ", parts=" + parts +
                    ", points=" + points +
                    '}';
        }

    }

    public static class Polygon {
        public int shapeType;
        public double boxMinX;
        public double boxMinY;
        public double boxMaxX;
        public double boxMaxY;
        public int numParts;
        public int numPoints;
        public List<Integer> parts;
        public List<Point> points;

        public Polygon(byte[] polygonBytes) {
            ByteBuffer buffer = ByteBuffer.wrap(polygonBytes);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            this.shapeType = 5;
            this.boxMinX = buffer.getDouble();
            this.boxMinY = buffer.getDouble();
            this.boxMaxX = buffer.getDouble();
            this.boxMaxY = buffer.getDouble();
            this.numParts = buffer.getInt();
            this.numPoints = buffer.getInt();

            // Read Parts
            this.parts = new ArrayList<>();
            for (int i = 0; i < numParts; i++) {
                this.parts.add(buffer.getInt());
            }

            // Read Points
            this.points = new ArrayList<>();
            for (int i = 0; i < numPoints; i++) {
                byte[] pointBytes = new byte[16]; // Assuming each Point is 16 bytes
                buffer.get(pointBytes);
                this.points.add(new Point(pointBytes));
            }
        }
        @Override
        public String toString() {
            return "Polygon{" +
                    "shapeType=" + shapeType +
                    ", boxMinX=" + boxMinX +
                    ", boxMinY=" + boxMinY +
                    ", boxMaxX=" + boxMaxX +
                    ", boxMaxY=" + boxMaxY +
                    ", numParts=" + numParts +
                    ", numPoints=" + numPoints +
                    ", parts=" + parts +
                    ", points=" + points +
                    '}';
        }
    }

    public static class MultiPatch {
        public int shapeType;
        public double boxMinX;
        public double boxMinY;
        public double boxMaxX;
        public double boxMaxY;
        public int numParts;
        public int numPoints;
        public List<Integer> parts;
        public List<Integer> partTypes;
        public List<Point> points;
        public double zMin;
        public double zMax;
        public List<Double> zArray;
        public double mMin;
        public double mMax;
        public List<Double> mArray;

        public MultiPatch(byte[] multiPatchBytes) {
            ByteBuffer buffer = ByteBuffer.wrap(multiPatchBytes);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            this.shapeType = 31;
            this.boxMinX = buffer.getDouble();
            this.boxMinY = buffer.getDouble();
            this.boxMaxX = buffer.getDouble();
            this.boxMaxY = buffer.getDouble();
            this.numParts = buffer.getInt();
            this.numPoints = buffer.getInt();

            // Read Parts
            this.parts = new ArrayList<>();
            for (int i = 0; i < numParts; i++) {
                this.parts.add(buffer.getInt());
            }

            // Read PartTypes
            this.partTypes = new ArrayList<>();
            for (int i = 0; i < numParts; i++) {
                this.partTypes.add(buffer.getInt());
            }

            // Read Points
            this.points = new ArrayList<>();
            for (int i = 0; i < numPoints; i++) {
                byte[] pointBytes = new byte[16]; // Assuming each Point is 16 bytes
                buffer.get(pointBytes);
                this.points.add(new Point(pointBytes));
            }

            this.zMin = buffer.getDouble();
            this.zMax = buffer.getDouble();

            // Read ZArray
            this.zArray = new ArrayList<>();
            for (int i = 0; i < numPoints; i++) {
                this.zArray.add(buffer.getDouble());
            }

            this.mMin = buffer.getDouble();
            this.mMax = buffer.getDouble();

            // Read MArray
            this.mArray = new ArrayList<>();
            for (int i = 0; i < numPoints; i++) {
                this.mArray.add(buffer.getDouble());
            }
        }
    }
    static class FileHeader{
        public int fileCode;
        private int[] unused;
        public int fileLength;
        public int version;
        public int shapeType;
        public double boundingBoxXmin;
        public double boundingBoxYmin;
        public double boundingBoxXmax;
        public double boundingBoxYmax;
        public double boundingBoxZmin;
        public double boundingBoxZmax;
        public double boundingBoxMmin;
        public double boundingBoxMmax;

        public FileHeader(byte[] headerBytes) {
            ByteBuffer buffer = ByteBuffer.wrap(headerBytes);
            buffer.order(ByteOrder.BIG_ENDIAN);
            this.fileCode = buffer.getInt();
            if(fileCode!=9994){
                System.out.println("非SHP文件");
                System.exit(0);
            }
            this.unused = new int[5];
            for (int i = 0; i < 5; i++) {
                this.unused[i] = buffer.getInt();
            }
            //buffer.order(ByteOrder.LITTLE_ENDIAN);
            this.fileLength = buffer.getInt();
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            this.version = buffer.getInt();
            this.shapeType = buffer.getInt();
            this.boundingBoxXmin = buffer.getDouble();
            this.boundingBoxYmin = buffer.getDouble();
            this.boundingBoxXmax = buffer.getDouble();
            this.boundingBoxYmax = buffer.getDouble();
            this.boundingBoxZmin = buffer.getDouble();
            this.boundingBoxZmax = buffer.getDouble();
            this.boundingBoxMmin = buffer.getDouble();
            this.boundingBoxMmax = buffer.getDouble();
        }
    }

    static class Shapefile {
        List<PolyLine> polylines;
        List<Polygon> polygons;
        List<Point> points;
        List<MultiPoint>multiPoints;
        FileHeader header;

        public Shapefile(FileHeader header,List<Point> points,List<PolyLine> polylines, List<Polygon> polygons,List<MultiPoint>multiPoints) {
            this.polylines = polylines;
            this.multiPoints = multiPoints;
            this.polygons = polygons;
            this.points = points;
            this.header = header;
        }
    }
/*
    class DBFFile {

    }
    class SHXFile{

    }
    class GISData {
        Shapefile shapefile;
        DBFFile dbfFile;
        SHXFile shxFile;
        public GISData(Shapefile shapefile, DBFFile dbfFile,SHXFile shxFile) {
            this.shapefile = shapefile;
            this.dbfFile = dbfFile;
            this.shxFile = shxFile;
        }
    }
*/
}
