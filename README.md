# Read shp file and draw it up By Java 
My class first Task of Spatiotemporal Data Platform Technology.
Read and draw SHP file with the original JAVA. 
In this project,I use RandomAccessFile and ByteBuffer to read the different part of SHP,including its header and recorders. 
I design 6 class to store the different type content.
At last ,it is awt I used to draw up. 

# HOW to use
the three .shp file is the examples,you can try it in the Main.java,
> Data.Shapefile shp = manipulationfunc.ShapefileReader.readShapefile("ocldnta.shp");
> paint.Drwaer(shp); 

then you will see the graphic.

# description of files
the pain.java involve the setting of awt and drawing method of point\polyline\polygon

the manipulationfun.java involve the read of shp

the data.java involve the class of SHP,SHPheader,point,polyline,polygon,multipoint.And their construct function will work while reading the shp file.

the main.java is an enterance

