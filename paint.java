/*package com.ClipByJava;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class paint extends JFrame {

    private static final long serialVersionUID = 1L;
    private  static final int CANVAS_SIZE =1000;
    private static Data.Shapefile SHP;
    // 计算缩放比例
    private static double scale;

    public paint(Data.Shapefile shp) {
        this.SHP = shp;
        double sX = getWidth() / (SHP.header.boundingBoxXmax - SHP.header.boundingBoxXmin);
        double sY = getHeight() / (SHP.header.boundingBoxYmax - SHP.header.boundingBoxYmin);
        this.scale = Math.min(sX, sY);
        setTitle("SHP超级无敌绘制器");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
        add(new DrawingPanel());
    }
    static class DrawingPanel extends JPanel {

        private static final long serialVersionUID = 1L;
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Point[] awtPoints = convertPoints(SHP.points);
            drawMultiplePoints(g,awtPoints);
            for(Data.PolyLine polyLine: SHP.polylines){
                Point[] awtPolylinePoints = convertPoints(polyLine.points);
                drawMultiplePolylines(g,awtPolylinePoints);
            }
            for (Data.Polygon polygon:SHP.polygons){
                Point[] awtPolygonPoints = convertPoints(polygon.points);
                drawMultiplePolygons(g,awtPolygonPoints);
            }
            for(Data.MultiPoint multiPoint:SHP.multiPoints){
                Point[] awtMultiPoints = convertPoints(multiPoint.points);
                drawMultiplePoints(g,awtMultiPoints);
            }
        }
        private void drawMultiplePoints(Graphics g, Point... points) {
            g.setColor(Color.RED);
            for (Point point : points) {
                g.fillOval(point.x, point.y, 6, 6);
            }
        }
        private void drawMultiplePolylines(Graphics g, Point... polylinePoints) {
            g.setColor(Color.BLUE);
            for (int i = 1; i < polylinePoints.length; i++) {
                g.drawLine(polylinePoints[i - 1].x, polylinePoints[i - 1].y, polylinePoints[i].x, polylinePoints[i].y);
            }
        }
        private void drawMultiplePolygons(Graphics g,Point... polygonPoints) {
            if (polygonPoints == null || polygonPoints.length < 3) {
                return; // 至少需要三个点来构成一个多边形
            }
            g.setColor(Color.BLACK);
            int[] xPoints = Arrays.stream(polygonPoints).mapToInt(p -> p.x).toArray();
            int[] yPoints = Arrays.stream(polygonPoints).mapToInt(p -> p.y).toArray();
            g.drawPolygon(xPoints, yPoints, polygonPoints.length);
            g.drawLine(xPoints[polygonPoints.length - 1], yPoints[polygonPoints.length - 1], xPoints[0], yPoints[0]);
        }
        public static Point[] convertPoints(List<Data.Point> pointList) {
            Point[] points = new Point[pointList.size()];
            for (int i = 0; i < pointList.size(); i++) {
                Point POINT = new Point(
                        (int)((pointList.get(i).x-SHP.header.boundingBoxXmin) *scale),
                        (int)((pointList.get(i).y-SHP.header.boundingBoxYmin) *-scale+CANVAS_SIZE)
                );
                points[i] = POINT;
            }
            return points;
        }

    }
    public static void Drawer(Data.Shapefile shp) {
        SwingUtilities.invokeLater(() -> {
            paint paint = new paint(shp);
            paint.setSize(800, 800);
            paint.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            paint.setVisible(true);
        });
    }
}*/
package com.ClipByJava;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Arrays;
import javax.swing.*;

public class paint extends JPanel {

    private  Data.Shapefile SHP;
    //画面缩放
    private  double scale = 1.0;
    //坐标映射缩放
    private  double Pscale = 1.0;
    public  int xOffset = 0, yOffset = 0;
    private Point origin;
    //鼠标比例和所在位置
    public double pointer_ratio_x, pointer_ratio_y;
    public int pointer_x, pointer_y;

    public paint(Data.Shapefile shp) {
        this.SHP = shp;
        setPreferredSize(new Dimension(1600, 900));
        // 添加鼠标滚轮监听器
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                // 保存当前的鼠标位置及比例
                save(e.getX(), e.getY());
                int notches = e.getWheelRotation();
                if (notches < 0) {
                    // 滚轮向上，放大画布
                    scale *= 1.1;
                } else {
                    // 滚轮向下，缩小画布
                    scale /= 1.1;
                }
                // 基于鼠标位置和比例, 计算最新的偏移
                restore();
                repaint();  // 重新绘制画布
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                origin = new Point(e.getPoint());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                origin = null;
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (origin != null) {
                    int deltaX = e.getX() - origin.x;
                    int deltaY = e.getY() - origin.y;
                    xOffset += deltaX;
                    yOffset += deltaY;
                    repaint();
                    origin = new Point(e.getPoint());
                }
            }
        });

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gd = (Graphics2D) g;
        Pscale = Math.min((double) getWidth() / (SHP.header.boundingBoxXmax - SHP.header.boundingBoxXmin),
                (double) getHeight() / (SHP.header.boundingBoxYmax - SHP.header.boundingBoxYmin));
        // 缩放画布
        gd.scale(scale, scale);
        // 拖动画布
        gd.translate(xOffset, yOffset);
        //绘制几何图形
        Point[] awtPoints = convertPoints(SHP.points);
        drawMultiplePoints(gd, awtPoints);
        for (Data.PolyLine polyLine : SHP.polylines) {
            Point[] awtPolylinePoints = convertPoints(polyLine.points);
            drawMultiplePolylines(gd, awtPolylinePoints);
        }
        for (Data.Polygon polygon : SHP.polygons) {
            Point[] awtPolygonPoints = convertPoints(polygon.points);
            drawMultiplePolygons(gd, awtPolygonPoints);
        }
        for (Data.MultiPoint multiPoint : SHP.multiPoints) {
            Point[] awtMultiPoints = convertPoints(multiPoint.points);
            drawMultiplePoints(gd, awtMultiPoints);
        }
    }
    private void drawMultiplePoints(Graphics2D g, Point... points) {
        g.setColor(Color.RED);
        for (Point point : points) {
            g.fillOval(point.x, point.y, 6, 6);
        }
    }
    private void drawMultiplePolylines(Graphics2D g, Point... polylinePoints) {
        g.setColor(Color.BLUE);
        for (int i = 1; i < polylinePoints.length; i++) {
            g.drawLine(polylinePoints[i - 1].x, polylinePoints[i - 1].y, polylinePoints[i].x, polylinePoints[i].y);
        }
    }
    private void drawMultiplePolygons(Graphics2D g,Point... polygonPoints) {
        g.setColor(Color.BLACK);
        for (int i = 0; i+2 < polygonPoints.length; i += 3) {
            int[] xPoints = {polygonPoints[i].x, polygonPoints[i+1].x, polygonPoints[i+2].x};
            int[] yPoints = {polygonPoints[i].y, polygonPoints[i+1].y, polygonPoints[i+2].y};
            // 绘制多边形边框
            g.drawPolygon(xPoints, yPoints, 3);
        }
    }
    public Point[] convertPoints(java.util.List<Data.Point> pointList) {
        Point[] points = new Point[pointList.size()];
        for (int i = 0; i < pointList.size(); i++) {
            Point POINT = new Point(
                    (int) ((pointList.get(i).x - SHP.header.boundingBoxXmin) * Pscale),
                    (int) ((pointList.get(i).y - SHP.header.boundingBoxYmin) * -Pscale+getHeight())
            );
            points[i] = POINT;
        }
        return points;
    }

    public void save(int x, int y){
        // 记录鼠标坐标
        pointer_x = x;
        pointer_y = y;

        // 计算画布
        double canvasX = x - xOffset;
        double canvasY = y - yOffset;

        // 计算图片大小
        double imageWidth = getWidth() * scale;  // 缩放后的图像宽度
        double imageHeight = getHeight() * scale;  // 缩放后的图像高度

        // 计算比例
        pointer_ratio_x = canvasX / imageWidth ;
        pointer_ratio_y = canvasY / imageHeight ;
    }
    /**
     * 计算新的比例
     */
    public void restore(){
        // 缩放后的尺寸
        double imageWidth = getWidth() * scale;  // 缩放后的图像宽度
        double imageHeight = getHeight() * scale;  // 缩放后的图像高度

        // 计算整张画布宽度
        double canvasX = imageWidth * pointer_ratio_x;
        double canvasY = imageHeight * pointer_ratio_y;

        // 计算画布偏移
        xOffset = (int) (pointer_x - canvasX);
        yOffset = (int) (pointer_y - canvasY);
    }

    public static void Drawer(Data.Shapefile shp) {
            JFrame jFrame = new JFrame("超级SHP绘制器");
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            paint paint = new paint(shp);
            JScrollPane scrollPane = new JScrollPane(paint);
            jFrame.getContentPane().add(scrollPane);
            jFrame.pack();
            jFrame.setVisible(true);
    }
}

