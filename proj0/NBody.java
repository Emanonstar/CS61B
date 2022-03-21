public class NBody{
    public static double readRadius(String str){
        In in = new In(str);
        int N = in.readInt();
        return in.readDouble();
    }

    public static Planet[] readPlanets(String str){
        In in = new In(str);
        int N = in.readInt();
        double r = in.readDouble();
        Planet[] planets = new Planet[N];
        for (int i = 0; i < N; i++){
            planets[i] = new Planet(in.readDouble(), in.readDouble(), in.readDouble(),
                                    in.readDouble(), in.readDouble(), in.readString());
        }
        return planets;
    }

    public static void main(String[] args){
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        Planet[] planets = readPlanets(filename);
        double radius = readRadius(filename);
        int N = planets.length;

        StdDraw.setScale(-radius, radius);
        StdDraw.clear();
        StdDraw.picture(0, 0, "images/starfield.jpg");
        for(Planet p: planets){
            p.draw();
        }

        StdDraw.enableDoubleBuffering();

        double t = 0;
        while(t < T){
            double[] xForces = new double[N];
            double[] yForces = new double[N];
            for (int i = 0; i < N; i++){
                xForces[i] = planets[i].calcNetForceExertedByX(planets);
                yForces[i] = planets[i].calcNetForceExertedByY(planets);
            }

            for (int i = 0; i < N; i++){
                planets[i].update(dt, xForces[i], yForces[i]);
            }

            StdDraw.picture(0, 0, "images/starfield.jpg");
            for(Planet p: planets){
            p.draw();
            }
            StdDraw.show();
            StdDraw.pause(10);

            t += dt;
        }
        
        StdOut.printf("%d\n", N);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < N; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                  planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                  planets[i].yyVel, planets[i].mass, planets[i].imgFileName);   
        }
    }
}