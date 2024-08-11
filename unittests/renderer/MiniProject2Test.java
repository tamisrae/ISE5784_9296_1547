/**
 *
 */

package renderer;


import org.junit.jupiter.api.Test;
import lighting.PointLight;

import primitives.*;
import renderer.*;

import geometries.*;
import scene.Scene;

import static java.awt.Color.*;

/**
 * Tests for BVH 
 *
 *
 */
public class MiniProject2Test {
    @Test
    public void test() {
        final Scene          scene         = new Scene("minip2");
        final Camera.Builder cameraBuilder = Camera.getBuilder()
                .setDirection(new Vector(0,0,-1), new Vector(0,1,0))
                .setRayTracer(new SimpleRayTracer(scene))
                .setadaptive(true);
        //.setMultithreading(3)


        scene.geometries.add(
                //train pass-Rails

                new Triangle(new Point(-150, -70, -130), new Point(150, -70, -130), new Point(150, -75, -130)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0.24)).setEmission(new Color(150, 75, 0)),
                new Triangle(new Point(-150, -70, -130), new Point(-150, -75, -130), new Point(150, -75, -130)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0.24)).setEmission(new Color(150, 75, 0)),
                new Triangle(new Point(-150, -50, -100), new Point(150, -50, -100), new Point(150, -45, -100)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0.24)).setEmission(new Color(150, 75, 0)),
                new Triangle(new Point(-150, -50, -100), new Point(-150, -45, -100), new Point(150, -45, -100)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0.24)).setEmission(new Color(150, 75, 0)),
                new Triangle(new Point(-90, -70, -130), new Point(-85, -70, -130), new Point(-75, -50, -100)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0.24)).setEmission(new Color(150, 75, 0)),
                new Triangle(new Point(-90, -70, -130), new Point(-80, -50, -100), new Point(-75, -50, -100)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0.24)).setEmission(new Color(150, 75, 0)),
                new Triangle(new Point(-40, -70, -130), new Point(-35, -70, -130), new Point(-26, -50, -100)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0.24)).setEmission(new Color(150, 75, 0)),
                new Triangle(new Point(-40, -70, -130), new Point(-31, -50, -100), new Point(-26, -50, -100)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0.24)).setEmission(new Color(150, 75, 0)),
                new Triangle(new Point(10, -70, -130), new Point(15, -70, -130), new Point(21, -50, -100)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0.24)).setEmission(new Color(150, 75, 0)),
                new Triangle(new Point(10, -70, -130), new Point(16, -50, -100), new Point(21, -50, -100)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0.24)).setEmission(new Color(150, 75, 0)),
                new Triangle(new Point(60, -70, -130), new Point(65, -70, -130), new Point(71, -50, -100)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0.24)).setEmission(new Color(150, 75, 0)),
                new Triangle(new Point(60, -70, -130), new Point(66, -50, -100), new Point(71, -50, -100)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0.24)).setEmission(new Color(150, 75, 0)),
                //Wheels
                new Sphere(20, new Point(-60, -44, -80)) //
                        .setEmission(new Color(GRAY)) //
                        .setMaterial(new Material().setkD(0.2).setkS(0.2).setnShininess(50).setkT(0)),
                new Sphere(10,new Point(-59, -44, -60)) //
                        .setEmission(new Color(BLACK)) //
                        .setMaterial(new Material().setkD(0.2).setkS(0.2).setnShininess(50).setkT(0)),
                new Sphere(25, new Point(40, -40, -80)) //
                        .setEmission(new Color(GRAY)) //
                        .setMaterial(new Material().setkD(0.2).setkS(0.2).setnShininess(50).setkT(0)),
                new Sphere(12.5, new Point(39, -40, -60)) //
                        .setEmission(new Color(BLACK)) //
                        .setMaterial(new Material().setkD(0.2).setkS(0.2).setnShininess(50).setkT(0)),


//red box
        new Triangle(new Point(-90, -44, -90), new Point(70, -44, -90), new Point(70, 10, -90)) //
                .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0)).setEmission(new Color(RED)),
                new Triangle(new Point(-90, -44, -90), new Point(-90, 10, -90), new Point(70, 10, -90)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0)).setEmission(new Color(RED)),
                new Triangle(new Point(70, -44, -90), new Point(80, -34, -110), new Point(70, 10, -100)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0)).setEmission(new Color(RED)),
                new Triangle(new Point(70, 10, -100), new Point(80, -34, -110), new Point(80, 20, -110)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0)).setEmission(new Color(RED)),
                new Triangle(new Point(-90, 10, -90), new Point(70, 10, -100), new Point(80, 20, -110)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0)).setEmission(new Color(RED)),
                new Triangle(new Point(-90, 10, -90), new Point(-80, 20, -110), new Point(80, 20, -110)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0)).setEmission(new Color(RED)),

                // blue box
                new Triangle(new Point(70, 10, -100), new Point(80, 20, -110), new Point(70, 50, -100)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0)).setEmission(new Color(50, 0, 100)),
                new Triangle(new Point(80, 60, -110), new Point(80, 20, -110), new Point(70, 50, -100)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0)).setEmission(new Color(50, 0, 100)),
                new Triangle(new Point(80, 60, -110), new Point(0 - 20, 50, -100), new Point(70, 50, -100)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0)).setEmission(new Color(50, 0, 100)),
                new Triangle(new Point(80, 60, -110), new Point(-20, 50, -100), new Point(-10, 60, -110)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0)).setEmission(new Color(50, 0, 100)),
                new Triangle(new Point(70, 10, -100), new Point(70, 50, -100), new Point(-20, 10, -90)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0)).setEmission(new Color(50, 0, 100)),
                new Triangle(new Point(-20, 10, -90), new Point(70, 50, -100), new Point(-20, 50, -90)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0)).setEmission(new Color(50, 0, 100)),

                //steam
                new Triangle(new Point(-70, -5, -100), new Point(-50, -5, -100), new Point(-70, 50, -100)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0)).setEmission(new Color(PINK)),
                new Triangle(new Point(-70, 50, -100), new Point(-50, -5, -100), new Point(-50, 50, -100)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0)).setEmission(new Color(PINK)),
                new Triangle(new Point(-50, 0, -100), new Point(-50, 50, -100), new Point(-45, 55, -105)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0)).setEmission(new Color(PINK)),
                new Triangle(new Point(-45, 55, -105), new Point(-50, 0, -100), new Point(-45, 15, -110)) //


.setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0)).setEmission(new Color(PINK)),
                new Triangle(new Point(-70, 50, -100), new Point(-50, 50, -100), new Point(-45, 55, -105)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0)).setEmission(new Color(PINK)),
                new Triangle(new Point(-70, 50, -100), new Point(-65, 55, -105), new Point(-45, 55, -105)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0)).setEmission(new Color(PINK)),
                //Smoke cloud
                new Sphere(9,new Point(-55, 57, -106)) //
                        .setEmission(new Color(GRAY)) //
                        .setMaterial(new Material().setnShininess(50).setkT(0.4)),
                new Sphere(8,new Point(-45, 65, -110)) //
                        .setEmission(new Color(GRAY)) //
                        .setMaterial(new Material().setkD(0.2).setkS(0.2).setnShininess(50).setkT(0.4)),
                new Sphere(13, new Point(-33, 73, -110)) //
                        .setEmission(new Color(GRAY)) //
                        .setMaterial(new Material().setkD(0.2).setkS(0.2).setnShininess(50).setkT(0.4)),


                //windows
                new Triangle(new Point(-5, 40, -80), new Point(17.5, 40, -80), new Point(17.5, 20, -80)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0.24)).setEmission(new Color(0, 200, 200)),
                new Triangle(new Point(-5, 40, -80), new Point(-5, 20, -80), new Point(17.5, 20, -80)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0.24)).setEmission(new Color(0, 200, 200)),
                new Triangle(new Point(32.5, 40, -80), new Point(55, 40, -80), new Point(55, 20, -80)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0.24)).setEmission(new Color(0, 200, 200)),
                new Triangle(new Point(32.5, 40, -80), new Point(32.5, 20, -80), new Point(55, 20, -80)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0.24)).setEmission(new Color(0, 200, 200)),


                //Stick
                new Triangle(new Point(-59, -45, -50), new Point(-59, -39, -50), new Point(45, -42, -50)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0)).setEmission(new Color(YELLOW)),
                new Triangle(new Point(-59, -39, -50), new Point(45, -36, -50), new Point(45, -42, -50)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0)).setEmission(new Color(YELLOW)),


                //background
                new Plane(new Vector(0,0,1),new Point(-150, -150, -250))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(20).setkT(0.5)).setEmission(new Color(gray)),
                new Plane( new Vector(0,1,0),new Point(0, -75, 0))
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(20).setkR(0.1)).setEmission(new Color(BLACK))


        );

        scene.lights.add(new PointLight(new Color(WHITE), new Point(-91, 0, -80)) //
                .setKl(0.001).setKq(0.0005));
        scene.lights.add(new PointLight(new Color(100, 40, 80), new Point(-100, 150, 0)) //
                .setKl(4E-5).setKq(2E-8));

        //scene.lights.add( //
        //  new SpotLight(new Color(700, 400, 400), new Point(30, 30, 115), new Vector(-1, -1, -4)) //
        //         .setKl(4E-4).setKq(2E-5).setRadius(17));

        cameraBuilder.setLocation(new Point(0, 0, 1000)).setVpDistance(1000)
                .setVpSize(200, 200)
                .setNumOfRays(1000)
                .setImageWriter(new ImageWriter("newPic", 600, 600))
                .build()
                .renderImage()
                .writeToImage();




    }
}
