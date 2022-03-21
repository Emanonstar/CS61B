public class Planet{
    public static final double G = 6.67e-11;

    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;

    public Planet(double xP, double yP, double xV,
              double yV, double m, String img){
                  xxPos = xP;
                  yyPos = yP;
                  xxVel = xV;
                  yyVel = yV;
                  mass = m;
                  imgFileName = img;
              }
    
    public Planet(Planet p){
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }

    public double calcDistance(Planet p){
        double dx = p.xxPos - xxPos;
        double dy = p.yyPos - yyPos;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double calcForceExertedBy(Planet p){
        double r = calcDistance(p);
        return Planet.G * mass * p.mass / (r * r); 
    }

    public double calcForceExertedByX(Planet p){
        double dx = p.xxPos - xxPos;
        double r = calcDistance(p);
        return calcForceExertedBy(p) * dx / r; 
    }

    public double calcForceExertedByY(Planet p){
        double dy = p.yyPos - yyPos;
        double r = calcDistance(p);
        return calcForceExertedBy(p) * dy / r; 
    }

    public double calcNetForceExertedByX(Planet[] all_p){
        double FX = 0;
        for (int i = 0, l = all_p.length; i < l; i++){
            if (!this.equals(all_p[i])){
                FX += calcForceExertedByX(all_p[i]);
            }
        }
        return FX;
    }

    public double calcNetForceExertedByY(Planet[] all_p){
        double FY = 0;
        for (int i = 0, l = all_p.length; i < l; i++){
            if (!this.equals(all_p[i])){
                FY += calcForceExertedByY(all_p[i]);
            }
        }
        return FY;
    }

    public void update(double dt, double fX, double fY){
        double ax = fX / mass;
        double ay = fY / mass;
        xxVel += ax * dt;
        yyVel += ay * dt;
        xxPos += xxVel * dt;
        yyPos += yyVel * dt;
    }
}